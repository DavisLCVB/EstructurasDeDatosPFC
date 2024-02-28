package com.grupo01.DataStructuresProject.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.dao.AreaDAOImp;
import com.grupo01.DataStructuresProject.dao.ProfessionalDAOImp;
import com.grupo01.DataStructuresProject.frontformat.DateTimeLapseID;
import com.grupo01.DataStructuresProject.frontformat.ProfesionalIDFormat;
import com.grupo01.DataStructuresProject.frontformat.ProfessionalFormat;
import com.grupo01.DataStructuresProject.models.Area;
import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import com.grupo01.DataStructuresProject.service.LapseOperation;
import com.grupo01.DataStructuresProject.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/getId")
    public String getID(){
        return idGenerator.generateAppointmentID();
    }
    @GetMapping(value = "/findAll")
    public Flux<ProfessionalFormat> findAll() {
        return professionalDAOImp.findAll().map(ProfessionalFormat::new);
    }

    @GetMapping(value = "/findById/{idProfessional}")
    public Mono<ProfessionalFormat> findById(@PathVariable String idProfessional) {
        return professionalDAOImp.findById(idProfessional).map(ProfessionalFormat::new);
    }

    @PostMapping(value = "/save")
    public Mono<ProfessionalFormat> save(@RequestBody ProfessionalUser professional) {
        professional.setId(idGenerator.generateProfessionalID());
        return professionalDAOImp.save(professional).map(ProfessionalFormat::new);
    }

    @PutMapping(value = "/update/{idProfessional}")
    public Mono<ProfessionalFormat> update(@PathVariable String idProfessional, @RequestBody ProfessionalUser professional) {
        return professionalDAOImp.update(idProfessional, professional).map(ProfessionalFormat::new);
    }

    public Mono<HashMap<String, ScheduleDate>> getAvailableScheduleProfessionals(@PathVariable String idArea, @RequestBody @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime lastSunday) {
        return professionalDAOImp.findAllByIdArea(idArea)
                .flatMapSequential(p -> {
                    Mono<Area> areaMono = areaDAOImp.findById(idArea);

                    return areaMono.flatMap(area -> {
                        ScheduleDate schedule = convertToScheduleDate(p.getAvailableHours(), lastSunday);
                        return appointmentDAOImp.findAllByIdProfessional(p.getId())
                                .filter(a -> a.getStatus().equals(AppointmentStatus.PENDING))
                                .filter(a -> a.getDate().getStart().isAfter(lastSunday) && a.getDate().getStart().isBefore(lastSunday.plusDays(7)))
                                .doOnNext(a -> {
                                    var minutes = (area.getDuration().getHour() * 60) + area.getDuration().getMinute();
                                    deleteLapse(schedule, a.getDate(), minutes);
                                })
                                .then(Mono.just(Map.entry(p.getId(), schedule)));
                    });
                })
                .collect(HashMap::new, (map, mapEntry) -> map.put(mapEntry.getKey(), mapEntry.getValue()));
    }

    private ScheduleDate convertToScheduleDate(Schedule schedule, LocalDateTime sunday) {
        var returnSchedule = new ScheduleDate();
        for (int i = 0; i < 7; i++) {
            var day = sunday.plusDays(i);
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
                    if (nuevolapso.getDuration() >= minutes) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                } else if (app.getEnd().equals((daylapse.getEnd())) && app.getStart().isAfter(daylapse.getStart())) {
                    DateTimeLapse nuevolapso = new DateTimeLapse(daylapse.getStart(), app.getStart());
                    if (nuevolapso.getDuration() >= minutes) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                }
            } else if (app.getStart().isAfter(daylapse.getStart()) && app.getEnd().isBefore(daylapse.getEnd())) {
                DateTimeLapse newLapseBefore = new DateTimeLapse(daylapse.getStart(), app.getStart());
                if (newLapseBefore.getDuration() >= minutes) {
                    nuevosLapsos.add(newLapseBefore);
                }
                DateTimeLapse newLapseAfter = new DateTimeLapse(app.getEnd(), daylapse.getEnd());
                if (newLapseAfter.getDuration() >= minutes) {
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

    @GetMapping(value = "/getSchedule/{idArea}")
    public Mono<HashMap<String, List<DateTimeLapseID>>> getAvailable(@PathVariable String idArea, @RequestBody @JsonFormat(pattern = "yyyy-MM-ddTHH:mm") LocalDateTime lastSunday) {
        return getAvailableScheduleProfessionals(idArea, lastSunday)
                .flatMap(map -> {
                    var lists = new HashMap<String, List<DateTimeLapseID>>();
                    lists.put("monday", new ArrayList<>());
                    lists.put("tuesday", new ArrayList<>());
                    lists.put("wednesday", new ArrayList<>());
                    lists.put("thursday", new ArrayList<>());
                    lists.put("friday", new ArrayList<>());
                    lists.put("saturday", new ArrayList<>());
                    lists.put("sunday", new ArrayList<>());
                    LapseOperation op = new LapseOperation();
                    List<Mono<Void>> mergeOperations = new ArrayList<>();
                    map.forEach((id, schedule) -> {
                        mergeOperations.add(mergeLapses(lists.get("monday"), schedule.getMonday(), id, op).doOnNext(list -> lists.put("monday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("tuesday"), schedule.getTuesday(), id, op).doOnNext(list -> lists.put("tuesday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("wednesday"), schedule.getWednesday(), id, op).doOnNext(list -> lists.put("wednesday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("thursday"), schedule.getThursday(), id, op).doOnNext(list -> lists.put("thursday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("friday"), schedule.getFriday(), id, op).doOnNext(list -> lists.put("friday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("saturday"), schedule.getSaturday(), id, op).doOnNext(list -> lists.put("saturday", list)).then());
                        mergeOperations.add(mergeLapses(lists.get("sunday"), schedule.getSunday(), id, op).doOnNext(list -> lists.put("sunday", list)).then());
                    });
                    return Mono.when(mergeOperations).thenReturn(lists);
                });
    }

    private Mono<ArrayList<DateTimeLapseID>> mergeLapses(List<DateTimeLapseID> day, List<DateTimeLapse> lapses, String id, LapseOperation op) {
        return professionalDAOImp.findById(id)
                .map(professionalUser -> {
                    var returnList = day;
                    for (DateTimeLapse lapse : lapses) {
                        DateTimeLapseID newLapse = new DateTimeLapseID(lapse.getStart(), lapse.getEnd());
                        newLapse.getProfessionalID().add(new ProfesionalIDFormat(professionalUser));
                        returnList = op.mergeDateTimeLapses((ArrayList<DateTimeLapseID>) returnList, newLapse);
                    }
                    return (ArrayList<DateTimeLapseID>) returnList;
                });
    }
}
