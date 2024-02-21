package com.grupo01.DataStructuresProject.models;

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
    protected LocalDateTime registrationDate;
}
