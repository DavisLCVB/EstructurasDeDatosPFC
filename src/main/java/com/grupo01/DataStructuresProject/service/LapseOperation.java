package com.grupo01.DataStructuresProject.service;

import com.grupo01.DataStructuresProject.frontformat.DateTimeLapseID;
import com.grupo01.DataStructuresProject.utils.BooleanContainer;
import com.grupo01.DataStructuresProject.utils.DateTimeLapse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

@NoArgsConstructor
@Service
public class LapseOperation {
    public ArrayList<DateTimeLapseID> mergeDateTimeLapses(DateTimeLapseID a, DateTimeLapseID b, BooleanContainer flag){
        ArrayList<DateTimeLapseID> result = new ArrayList<>();
        if (a.getEnd().isBefore(b.getStart()) || b.getEnd().isBefore(a.getStart()) || a.getEnd().equals(b.getStart()) || b.getEnd().equals(a.getStart())){
            result.add(a);
            return result;
        }
        flag.setValue(true);
        if(a.getStart().equals(b.getStart()) && a.getEnd().equals(b.getEnd())) {
            a.getProfessionalID().addAll(b.getProfessionalID());
            result.add(a);
            return result;
        }
        LocalDateTime start = a.getStart().isAfter(b.getStart()) ? a.getStart() : b.getStart();
        LocalDateTime end = a.getEnd().isBefore(b.getEnd()) ? a.getEnd() : b.getEnd();
        DateTimeLapseID intersection = new DateTimeLapseID(start, end);
        intersection.getProfessionalID().addAll(a.getProfessionalID());
        intersection.getProfessionalID().addAll(b.getProfessionalID());
        if (!a.getStart().equals(start)) {
            DateTimeLapseID nonIntersecting1 = new DateTimeLapseID(a.getStart(), start);
            nonIntersecting1.getProfessionalID().addAll(a.getProfessionalID());
            result.add(nonIntersecting1);
        }
        if (!b.getStart().equals(start)) {
            DateTimeLapseID nonIntersecting2 = new DateTimeLapseID(b.getStart(), start);
            nonIntersecting2.getProfessionalID().addAll(b.getProfessionalID());
            result.add(nonIntersecting2);
        }
        if (!a.getEnd().equals(end)) {
            DateTimeLapseID nonIntersecting3 = new DateTimeLapseID(end, a.getEnd());
            nonIntersecting3.getProfessionalID().addAll(a.getProfessionalID());
            result.add(nonIntersecting3);
        }
        if (!b.getEnd().equals(end)) {
            DateTimeLapseID nonIntersecting4 = new DateTimeLapseID(end, b.getEnd());
            nonIntersecting4.getProfessionalID().addAll(b.getProfessionalID());
            result.add(nonIntersecting4);
        }
        result.add(intersection);
        return result;
    }

    public ArrayList<DateTimeLapseID> mergeDateTimeLapses(ArrayList<DateTimeLapseID> lapses, DateTimeLapseID newLapse){
        if(lapses.isEmpty()) {
            lapses.add(newLapse);
            return lapses;
        }
        ArrayList<DateTimeLapseID> result = new ArrayList<>();
        var flag = new BooleanContainer(false);
        for (DateTimeLapseID lapse : lapses) {
            result.addAll(mergeDateTimeLapses(lapse, newLapse, flag));
        }
        if(!flag.isValue()) {
            result.add(newLapse);
        }
        result.sort(Comparator.comparing(DateTimeLapse::getStart));
        for(var lapse: result){
            var id = lapse.getProfessionalID();
            var idsNoDup = new ArrayList<String>();
            for(var i: id){
                if(!idsNoDup.contains(i)){
                    idsNoDup.add(i);
                }
            }
            lapse.setProfessionalID(idsNoDup);
        }
        return result;
    }
}
