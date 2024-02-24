package com.grupo01.DataStructuresProject.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class GenericUser{
    @Id
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    protected LocalDateTime registrationDate;
}
