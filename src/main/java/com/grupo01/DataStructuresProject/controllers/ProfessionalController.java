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

    private void deleteLapse(ScheduleFunc<TimeLapse> professionalSchedule, ScheduleFunc<DateTimeLapse> dest, DayOfWeek dia, DateTimeLapse intervaloCita) {
        List<TimeLapse> dayLapses = obtenerListaDia(professionalSchedule, dia);

        List<TimeLapse> lapsosActualizados = actualizarIntervalosDespuesDeCita(dayLapses, intervaloCita);

        // asignarListaDia(professionalSchedule, dia, lapsosActualizados);
    }

    private List<TimeLapse> obtenerListaDia(ScheduleFunc cronograma, DayOfWeek dia) {
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

    private List<TimeLapse> actualizarIntervalosDespuesDeCita(List<TimeLapse> lapsosDia, DateTimeLapse cita) {
        List<TimeLapse> nuevosLapsos = new ArrayList<>();

        for (TimeLapse lapso : lapsosDia) {
            if (cita.getStart().toLocalTime().isBefore(lapso.getStart()) || cita.getEnd().toLocalTime().isAfter(lapso.getEnd())) {
                //Aqui vemos si la cita no está dentro del horario, si efectivamente no lo está es porque la cita no pertenece a este horario,
                // así que salta al siguiente lapso  *del mismo día*
                //se agregar el lapso completo sin particiones ya que el lapso no contiene la cita por lo tanto no hay necesidad de particionar.
                nuevosLapsos.add(lapso);

            } else if (cita.getStart().toLocalTime().equals(lapso.getStart()) || cita.getEnd().toLocalTime().equals(lapso.getEnd())) {
                //La cita pertenece a este intervalo del horario pero un lado coincide con el inicio o final de un lapso.
                if (cita.getStart().toLocalTime().equals(lapso.getStart())) {
                    // Generar un nuevo lapso más corto antes de la cita
                    TimeLapse nuevolapso = new TimeLapse(cita.getEnd().toLocalTime(), lapso.getEnd());

                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                } else if (cita.getEnd().toLocalTime().equals((lapso.getEnd()))) {
                    TimeLapse nuevolapso = new TimeLapse(lapso.getStart(), cita.getStart().toLocalTime());
                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                }

            } else if (cita.getStart().toLocalTime().isAfter(lapso.getStart()) || cita.getEnd().toLocalTime().isBefore(lapso.getEnd())) {
                TimeLapse nuevolapsoAntes = new TimeLapse(lapso.getStart(), cita.getStart().toLocalTime());
                if (nuevolapsoAntes.getDuration() > 60) {
                    nuevosLapsos.add(nuevolapsoAntes);
                }
                TimeLapse nuevolapsoDespues = new TimeLapse(cita.getEnd().toLocalTime(), lapso.getEnd());
                if (nuevolapsoDespues.getDuration() > 60) {
                    nuevosLapsos.add(nuevolapsoDespues);
                }
            }
        }
        return nuevosLapsos;
    }

    private void asignarListaDia(Schedule cronograma, DayOfWeek dia, List<TimeLapse> lapsos) {
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
    }

}
