package com.grupo01.DataStructuresProject.dao;

import com.grupo01.DataStructuresProject.datastructures.Comparable;
import com.grupo01.DataStructuresProject.models.Appointment;

public class AppointmentComparable extends Appointment implements Comparable<AppointmentComparable> {
    @Override
    public boolean lowerThan(AppointmentComparable other) {
        return this.getDate().getEnd().isBefore(other.getDate().getStart());
    }

    @Override
    public boolean greaterThan(AppointmentComparable other) {
        return this.getDate().getStart().isAfter(other.getDate().getEnd());
    }

    @Override
    public boolean equalTo(AppointmentComparable other) {
        return this.getDate().getStart().isEqual(other.getDate().getStart()) && this.getDate().getEnd().isEqual(other.getDate().getEnd());
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
