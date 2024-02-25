package com.grupo01.DataStructuresProject.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.dao.AreaDAOImp;
import com.grupo01.DataStructuresProject.dao.ProfessionalDAOImp;
import com.grupo01.DataStructuresProject.frontformat.DateTimeLapseID;
import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import com.grupo01.DataStructuresProject.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {
    @Autowired
    private ProfessionalDAOImp professionalDAOImp;
    @Autowired
    private AppointmentDAOImp appointmentDAOImp;
    @Autowired
    private AreaDAOImp areaDAOImp;
    @Autowired
    private IDGenerator idGenerator;

    @GetMapping(value = "/findAll")
    public Flux<ProfessionalUser> findAll() {
        return professionalDAOImp.findAll();
    }

    @GetMapping(value = "/findById/{idProfessional}")
    public Mono<ProfessionalUser> findById(@PathVariable String idProfessional) {
        return professionalDAOImp.findById(idProfessional);
    }

    @PostMapping(value = "/save")
    public Mono<ProfessionalUser> save(@RequestBody ProfessionalUser professional) {
        professional.setId(idGenerator.generateProfessionalID());
        return professionalDAOImp.save(professional);
    }

    @PutMapping(value = "/update/{idProfessional}")
    public Mono<ProfessionalUser> update(@PathVariable String idProfessional, @RequestBody ProfessionalUser professional) {
        return professionalDAOImp.update(idProfessional, professional);
    }

    @GetMapping(value = "/getSchedule_1/{idArea}")
    public Mono<HashMap<String, ScheduleDate>> getAvailableScheduleProfessionals(@PathVariable String idArea, @RequestBody @JsonFormat(pattern = "yyyy-MM-ddTHH:mm") LocalDateTime lastMonday) {
        return professionalDAOImp.findAllByIdArea(idArea).flatMapSequential(p -> {
            ScheduleDate schedule = convertToScheduleDate(p.getAvailableHours(), lastMonday);
            return appointmentDAOImp.findAllByIdProfessional(p.getId()).filter(a -> a.getStatus().equals(AppointmentStatus.PENDING))
                    .filter(a -> a.getDate().getStart().isAfter(lastMonday))
                    .doOnNext(a -> {
                        var s = a.getDate().getStart();
                        var e = a.getDate().getEnd();
                        var minutes = e.getHour() * 60 + e.getMinute() - s.getHour() * 60 - s.getMinute();
                        deleteLapse(schedule, a.getDate(), minutes);
                    })
                    .then(Mono.just(Map.entry(p.getId(), schedule)));
        }).collect(HashMap::new, (map, mapEntry) -> map.put(mapEntry.getKey(), mapEntry.getValue()));
    }

    private ScheduleDate convertToScheduleDate(Schedule schedule, LocalDateTime monday) {
        var returnSchedule = new ScheduleDate();
        for (int i = 0; i < 7; i++) {
            var day = monday.plusDays(i);
            var dayOfWeek = day.getDayOfWeek();
            var daySchedule = new ArrayList<DateTimeLapse>();
            List<TimeLapse> dayLapses = (List<TimeLapse>) getLapsesOfDay(schedule, dayOfWeek);
            for (var lapse : dayLapses) {
                var start = LocalDateTime.of(day.toLocalDate(), lapse.getStart());
                var end = LocalDateTime.of(day.toLocalDate(), lapse.getEnd());
                daySchedule.add(new DateTimeLapse(start, end));
            }
            switch (dayOfWeek) {
                case MONDAY -> returnSchedule.setMonday(daySchedule);
                case TUESDAY -> returnSchedule.setTuesday(daySchedule);
                case WEDNESDAY -> returnSchedule.setWednesday(daySchedule);
                case THURSDAY -> returnSchedule.setThursday(daySchedule);
                case FRIDAY -> returnSchedule.setFriday(daySchedule);
                case SATURDAY -> returnSchedule.setSaturday(daySchedule);
                case SUNDAY -> returnSchedule.setSunday(daySchedule);
            }
        }
        return returnSchedule;
    }

    private void deleteLapse(ScheduleDate dest, DateTimeLapse appLapse, int minutes) {
        DayOfWeek day = appLapse.getStart().getDayOfWeek();
        List<DateTimeLapse> dayLapses = (List<DateTimeLapse>) getLapsesOfDay(dest, day);
        List<DateTimeLapse> UpdatedLapses = UpdateLapsesAfterApp(dayLapses, appLapse, minutes);
        AssingScheduleWithDate(dest, day, UpdatedLapses);
    }

    private List<? extends Lapses> getLapsesOfDay(ScheduleFunc<? extends Lapses> cronograma, DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> cronograma.getMonday();
            case TUESDAY -> cronograma.getTuesday();
            case WEDNESDAY -> cronograma.getWednesday();
            case THURSDAY -> cronograma.getThursday();
            case FRIDAY -> cronograma.getFriday();
            case SATURDAY -> cronograma.getSaturday();
            case SUNDAY -> cronograma.getSunday();
            default -> new ArrayList<>();
        };
    }

    private List<DateTimeLapse> UpdateLapsesAfterApp(List<DateTimeLapse> daylapses, DateTimeLapse app, int minutes) {
        List<DateTimeLapse> nuevosLapsos = new ArrayList<>();
        for (DateTimeLapse daylapse : daylapses) {
            if (app.getStart().isBefore(daylapse.getStart()) || app.getEnd().isAfter(daylapse.getEnd())) {
                //Aquí vemos si la cita no está dentro del horario, si efectivamente no lo está es porque la cita no pertenece a este horario,
                // así que salta al siguiente lapso *del mismo día*
                //se agrega el lapso completo sin particiones ya que el lapso no contiene la cita por lo tanto no hay necesidad de particionar.
                nuevosLapsos.add(daylapse);
            } else if (app.getStart().equals(daylapse.getStart()) || app.getEnd().equals(daylapse.getEnd())) {
                //La cita pertenece a este intervalo del horario pero un lado coincide con el inicio o final de un lapso.
                if (app.getStart().equals(daylapse.getStart()) && app.getEnd().isBefore(daylapse.getEnd())) {
                    // Generar un nuevo lapso más corto antes de la cita
                    DateTimeLapse nuevolapso = new DateTimeLapse(app.getEnd(), daylapse.getEnd());
                    if (nuevolapso.getDuration() > minutes) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                } else if (app.getEnd().equals((daylapse.getEnd())) && app.getStart().isAfter(daylapse.getStart())) {
                    DateTimeLapse nuevolapso = new DateTimeLapse(daylapse.getStart(), app.getStart());
                    if (nuevolapso.getDuration() > minutes) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                }
            } else if (app.getStart().isAfter(daylapse.getStart()) && app.getEnd().isBefore(daylapse.getEnd())) {
                DateTimeLapse newLapseBefore = new DateTimeLapse(daylapse.getStart(), app.getStart());
                if (newLapseBefore.getDuration() > minutes) {
                    nuevosLapsos.add(newLapseBefore);
                }
                DateTimeLapse newLapseAfter = new DateTimeLapse(app.getEnd(), daylapse.getEnd());
                if (newLapseAfter.getDuration() > minutes) {
                    nuevosLapsos.add(newLapseAfter);
                }
            }
        }
        return nuevosLapsos;
    }

    private void AssingScheduleWithDate(ScheduleDate dest, DayOfWeek Day, List<DateTimeLapse> UpdatedLapsesWithDate) {
        switch (Day) {
            case MONDAY:
                dest.setMonday(UpdatedLapsesWithDate);
                break;
            case TUESDAY:
                dest.setTuesday(UpdatedLapsesWithDate);
                break;
            case WEDNESDAY:
                dest.setWednesday(UpdatedLapsesWithDate);
                break;
            case THURSDAY:
                dest.setThursday(UpdatedLapsesWithDate);
                break;
            case FRIDAY:
                dest.setFriday(UpdatedLapsesWithDate);
                break;
            case SATURDAY:
                dest.setSaturday(UpdatedLapsesWithDate);
                break;
            case SUNDAY:
                dest.setSunday(UpdatedLapsesWithDate);
                break;
        }
    }

//    @GetMapping(value = "/getSchedule_2/{idArea}")
//    public Mono<Map<DateTimeLapse, List<String>>> getAvailableTimesByProfessionals(@PathVariable String idArea, @RequestBody @JsonFormat(pattern = "yyyy-MM-ddTHH:mm") LocalDateTime lastMonday) {
//        return getAvailableScheduleProfessionals(idArea, lastMonday)
//                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
//                .flatMap(entry -> Flux.fromIterable(extractDateTimeLapses(entry.getValue()))
//                        .map(lapse -> new AbstractMap.SimpleEntry<>(lapse, entry.getKey())))
//                .groupBy(Map.Entry::getKey)
//                .flatMap(group -> group.collectList()
//                        .map(list -> new AbstractMap.SimpleEntry<>(group.key(), list.stream()
//                                .map(Map.Entry::getValue)
//                                .collect(Collectors.toList()))))
//                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
//    }
//
//    @GetMapping(value = "/getSchedule_3/{idArea}")
//    public Mono<HashMap<String, List<DateTimeLapseID>>> getAvailable(@PathVariable String idArea, @RequestBody @JsonFormat(pattern = "yyyy-MM-ddTHH:mm") LocalDateTime lastMonday) {
//        List<DateTimeLapseID> monday = new ArrayList<>();
//        List<DateTimeLapseID> tuesday = new ArrayList<>();
//        List<DateTimeLapseID> wednesday = new ArrayList<>();
//        List<DateTimeLapseID> thursday = new ArrayList<>();
//        List<DateTimeLapseID> friday = new ArrayList<>();
//        List<DateTimeLapseID> saturday = new ArrayList<>();
//        List<DateTimeLapseID> sunday = new ArrayList<>();
//    }
}
