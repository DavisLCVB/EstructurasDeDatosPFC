package com.grupo01.DataStructuresProject.controllers;

import com.grupo01.DataStructuresProject.dao.*;
import com.grupo01.DataStructuresProject.models.*;
import com.grupo01.DataStructuresProject.service.IDGenerator;
import com.grupo01.DataStructuresProject.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.emitter.Emitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.EmitterProcessor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

        Mono<ProfessionalUser> savedProfessional = professionalDAOImp.save(professional);

        //Solamente Prueba por consola
        savedProfessional.subscribe(saved -> {
            System.out.println("Profesional guardado exitosamente:");
            System.out.println("ID: " + saved.getId());
            System.out.println("Nombre: " + saved.getFirstName());
            System.out.println("Apellido " + saved.getLastName());
            System.out.println("Horarios disponibles :" + saved.getAvailableHours());
            /*
            LocalTime horaInicio = saved.getAvailableHours().getMonday().getFirst().getStart();
            LocalTime horafin = saved.getAvailableHours().getMonday().getFirst().getEnd();
            Duration duracionHorariodisponible = Duration.between(horaInicio, horafin);
            */

        });
        return savedProfessional;
    }
    public Flux<Schedule> getAvailableScheduleProfessionale(String idArea) {
        Flux<ProfessionalUser> professionalUserFlux = professionalDAOImp.findAllByIdArea(idArea);
        EmitterProcessor<Schedule> emitter = EmitterProcessor.create();

        return professionalUserFlux.flatMap(professional -> {
            Schedule scheduleAvailable = new Schedule(professional.getAvailableHours());
            Flux<Appointment> citasFlux = appointmentDAOImp.findAllByIdProfessional(professional.getId());

           /* return citasFlux.subscribe(citasList -> {
                    DayOfWeek diaCita =citasList.getDate().getStart().getDayOfWeek();
                    DateTimeLapse intervaloCita = citasList.getDate();
                    eliminarIntervalo(scheduleAvailable, diaCita, intervaloCita);

            },emitter::error,()->{emitter.onNext(scheduleAvailable);} ); // Convertir el Mono<Schedule> a Flux<Schedule>*/
            return null;
        });
    }

    private void eliminarIntervalo(Schedule cronograma, DayOfWeek dia, DateTimeLapse intervaloCita) {

        List<TimeLapse> lapsosDia = obtenerListaDia(cronograma, dia);

        List<TimeLapse> lapsosActualizados = actualizarIntervalosDespuesDeCita(lapsosDia, intervaloCita);

        asignarListaDia(cronograma, dia, lapsosActualizados);
    }
    private List<TimeLapse> obtenerListaDia(Schedule cronograma, DayOfWeek dia) {
        switch (dia) {
            case MONDAY:
                return cronograma.getMonday();
            case TUESDAY:
                return cronograma.getTuesday();
            case WEDNESDAY:
                return cronograma.getWednesday();
            case THURSDAY:
                return cronograma.getThursday();
            case FRIDAY:
                return cronograma.getFriday();
            case SATURDAY:
                return cronograma.getSaturday();
            case SUNDAY:
                return cronograma.getSunday();
            default:
                return new ArrayList<>();
        }
    }

    private List<TimeLapse> actualizarIntervalosDespuesDeCita(List<TimeLapse> lapsosDia, DateTimeLapse cita) {
        List<TimeLapse> nuevosLapsos = new ArrayList<>();

        for (TimeLapse lapso : lapsosDia) {
            if (cita.getStart().toLocalTime().isBefore(lapso.getStart()) || cita.getEnd().toLocalTime().isAfter(lapso.getEnd())) {
                //Aqui vemos si la cita no está dentro del horario, si efectivamente no lo está es porque la cita no pertenece a este horario,
                // así que salta al siguiente lapso  *del mismo día*
                //se agregar el lapso completo sin particiones ya que el lapso no contiene la cita por lo tanto no hay necesidad de particionar.
                nuevosLapsos.add(lapso);

            } else if(cita.getStart().toLocalTime().equals(lapso.getStart()) || cita.getEnd().toLocalTime().equals(lapso.getEnd())){
                //La cita pertenece a este intervalo del horario pero un lado coincide con el inicio o final de un lapso.
                if (cita.getStart().toLocalTime().equals(lapso.getStart())) {
                    // Generar un nuevo lapso más corto antes de la cita
                    TimeLapse nuevolapso = new TimeLapse(cita.getEnd().toLocalTime(),lapso.getEnd());

                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                } else if (cita.getEnd().toLocalTime().equals((lapso.getEnd()))) {
                    TimeLapse nuevolapso = new TimeLapse(lapso.getStart(),cita.getStart().toLocalTime());
                    if (nuevolapso.getDuration() > 60) {
                        // Agregar solo si el lapso es mayor a una hora
                        nuevosLapsos.add(nuevolapso);
                    }
                }

            } else if (cita.getStart().toLocalTime().isAfter(lapso.getStart()) || cita.getEnd().toLocalTime().isBefore(lapso.getEnd())) {
                TimeLapse nuevolapsoAntes = new TimeLapse(lapso.getStart(),cita.getStart().toLocalTime());
                if(nuevolapsoAntes.getDuration() >60){
                    nuevosLapsos.add(nuevolapsoAntes);
                }
                TimeLapse nuevolapsoDespues = new TimeLapse(cita.getEnd().toLocalTime(), lapso.getEnd());
                if(nuevolapsoDespues.getDuration() >60){
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
