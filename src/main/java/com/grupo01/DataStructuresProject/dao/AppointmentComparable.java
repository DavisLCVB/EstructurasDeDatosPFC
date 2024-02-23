package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.datastructures.Comparable;
import com.grupo01.DataStructuresProject.models.Appointment;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Document
@Getter
public class AppointmentComparable implements Comparable<AppointmentComparable> {
    private Appointment appointment;

    @Override
    public boolean lowerThan(AppointmentComparable other) {
        return this.appointment.getDate().getEnd().isBefore(other.appointment.getDate().getStart());
    }

    @Override
    public boolean greaterThan(AppointmentComparable other) {
        return this.appointment.getDate().getStart().isAfter(other.appointment.getDate().getEnd());
    }

    @Override
    public boolean equalTo(AppointmentComparable other) {
        return this.appointment.getDate().getStart().isEqual(other.appointment.getDate().getStart()) && this.appointment.getDate().getEnd().isEqual(other.appointment.getDate().getEnd());
    }

    @Override
    public boolean lowerOrEqualTo(AppointmentComparable other) {
        return this.equalTo(other) || this.lowerThan(other);
    }

    @Override
    public boolean greaterOrEqualTo(AppointmentComparable other) {
        return this.equalTo(other) || this.greaterThan(other);
    }
}
