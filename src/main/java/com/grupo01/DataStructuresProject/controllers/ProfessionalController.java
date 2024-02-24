package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.AppointmentDAOImp;
import com.grupo01.DataStructuresProject.dao.ProfessionalDAOImp;
import com.grupo01.DataStructuresProject.models.ProfessionalUser;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import com.grupo01.DataStructuresProject.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {
    @Autowired
    private ProfessionalDAOImp professionalDAOImp;
    private AppointmentDAOImp appointmentDAOImp;
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

    public Mono<HashMap<String, ScheduleDate>> getAvailableScheduleProfessionals(String idArea, LocalDateTime lastMonday) {
        Flux<ProfessionalUser> professionals = professionalDAOImp.findAllByIdArea(idArea);
        return professionals.flatMap(p -> {
            var app = appointmentDAOImp.findAllByIdProfessional(p.getId());
            ScheduleDate schedule = new ScheduleDate();
            return app.filter(a -> a.getStatus().equals(AppointmentStatus.PENDING))
                    .filter(a -> a.getDate().getEnd().isAfter(LocalDateTime.now()) && a.getDate().getStart().isBefore(lastMonday))
                    .doOnNext(a -> deleteLapse(p.getAvailableHours(), schedule, a.getDate().getStart().getDayOfWeek(), a.getDate()))
                    .then(Mono.just(Map.entry(p.getId(), schedule)));
        }).collect(HashMap::new, (map, mapEntry) -> map.put(mapEntry.getKey(), mapEntry.getValue()));
    }

    //Método agregado (
    public Mono<Map<DateTimeLapse, List<String>>> getAvailableTimesByProfessionals(String idArea, LocalDateTime lastMonday) {
        return getAvailableScheduleProfessionals(idArea, lastMonday)
                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                .flatMap(entry -> Flux.fromIterable(extractDateTimeLapses(entry.getValue()))
                        .map(lapse -> new AbstractMap.SimpleEntry<>(lapse, entry.getKey())))
                .groupBy(Map.Entry::getKey) // Agrupación por DateTimeLapse
                .flatMap(group -> group.collectList()
                        .map(list -> new AbstractMap.SimpleEntry<>(group.key(), list.stream()
                                .map(Map.Entry::getValue)
                                .collect(Collectors.toList()))))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
    //Método auxiliar
    private List<DateTimeLapse> extractDateTimeLapses(ScheduleDate schedule) {
        List<DateTimeLapse> allLapses = new ArrayList<>();

        allLapses.addAll(schedule.getMonday());
        allLapses.addAll(schedule.getTuesday());
        allLapses.addAll(schedule.getWednesday());
        allLapses.addAll(schedule.getThursday());
        allLapses.addAll(schedule.getFriday());
        allLapses.addAll(schedule.getSaturday());
        allLapses.addAll(schedule.getSunday());

        return allLapses;
    }

    private void deleteLapse(Schedule professionalSchedule, ScheduleDate dest, DayOfWeek Day, DateTimeLapse appLapse) {

        List<TimeLapse> dayLapses = getLapsesOfDay(professionalSchedule, Day);

        //se obtienen los lapsos de tiempo donde no interfiere una cita
        List<TimeLapse> UpdatedLapses = UpdateLapsesAfterApp(dayLapses, appLapse);

        //Se le asigna el día a esos lapsos de tiempo de acuerdo a la cita
        List<DateTimeLapse> UpdatedLapsesWithDate = DateUpdateLapse(UpdatedLapses, appLapse);

        //Ingresar los lapsos de tiempo en el cronograma (ScheduleDate) de acuerdo al día.
        AssingSchedulewithDate(dest, Day, UpdatedLapsesWithDate);

        // AssingSchedule(professionalSchedule, dia, lapsosActualizados);
    }

    private List<TimeLapse> getLapsesOfDay(Schedule cronograma, DayOfWeek dia) {
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

    private List<DateTimeLapse> DateUpdateLapse(List<TimeLapse> UpdateLapses, DateTimeLapse appLapse) {
        List<DateTimeLapse> updatedLapsesWithDate = new ArrayList<>();

        for (TimeLapse lapse : UpdateLapses) {

            LocalDate appDate = appLapse.getStart().toLocalDate();

            LocalDateTime startDateTime = LocalDateTime.of(appDate, lapse.getStart());
            LocalDateTime endDateTime = LocalDateTime.of(appDate, lapse.getEnd());

            DateTimeLapse LapseWithDate = new DateTimeLapse(startDateTime, endDateTime);

            updatedLapsesWithDate.add(LapseWithDate);
        }
        return updatedLapsesWithDate;
    }

    private List<TimeLapse> UpdateLapsesAfterApp(List<TimeLapse> daylapses, DateTimeLapse app) {
        List<TimeLapse> nuevosLapsos = new ArrayList<>();

        for (TimeLapse daylapse : daylapses) {
            if (app.getStart().toLocalTime().isBefore(daylapse.getStart()) || app.getEnd().toLocalTime().isAfter(daylapse.getEnd())) {
                //Aqui vemos si la cita no está dentro del horario, si efectivamente no lo está es porque la cita no pertenece a este horario,
                // así que salta al siguiente lapso  *del mismo día*
                //se agregar el lapso completo sin particiones ya que el lapso no contiene la cita por lo tanto no hay necesidad de particionar.
                nuevosLapsos.add(daylapse);

            } else if (app.getStart().toLocalTime().equals(daylapse.getStart()) || app.getEnd().toLocalTime().equals(daylapse.getEnd())) {
                //La cita pertenece a este intervalo del horario pero un lado coincide con el inicio o final de un lapso.
                if (app.getStart().toLocalTime().equals(daylapse.getStart()) && app.getEnd().toLocalTime().isBefore(daylapse.getEnd())) {
                    // Generar un nuevo lapso más corto antes de la cita
                    TimeLapse nuevolapso = new TimeLapse(app.getEnd().toLocalTime(), daylapse.getEnd());

                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                } else if (app.getEnd().toLocalTime().equals((daylapse.getEnd())) && app.getStart().toLocalTime().isAfter(daylapse.getStart())) {
                    TimeLapse nuevolapso = new TimeLapse(daylapse.getStart(), app.getStart().toLocalTime());
                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                }

            } else if (app.getStart().toLocalTime().isAfter(daylapse.getStart()) && app.getEnd().toLocalTime().isBefore(daylapse.getEnd())) {
                TimeLapse newLapseBefore = new TimeLapse(daylapse.getStart(), app.getStart().toLocalTime());
                if (newLapseBefore.getDuration() > 60) {
                    nuevosLapsos.add(newLapseBefore);
                }
                TimeLapse newLapseAfter = new TimeLapse(app.getEnd().toLocalTime(), daylapse.getEnd());
                if (newLapseAfter.getDuration() > 60) {
                    nuevosLapsos.add(newLapseAfter);
                }
            }
        }
        return nuevosLapsos;
    }

    private void AssingSchedulewithDate(ScheduleDate dest, DayOfWeek Day, List<DateTimeLapse> UpdatedLapsesWithDate) {
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
    /*
    private void AssingSchedule(Schedule cronograma, DayOfWeek dia, List<TimeLapse> lapsos) {
        switch (dia) {
            case MONDAY:
                cronograma.setMonday(lapsos);
                break;
            case TUESDAY:
                cronograma.setTuesday(lapsos);
                break;
            case WEDNESDAY:
                cronograma.setWednesday(lapsos);
                break;
            case THURSDAY:
                cronograma.setThursday(lapsos);
                break;
            case FRIDAY:
                cronograma.setFriday(lapsos);
                break;
            case SATURDAY:
                cronograma.setSaturday(lapsos);
                break;
            case SUNDAY:
                cronograma.setSunday(lapsos);
                break;
        }
    }*/

}
