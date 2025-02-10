package org.example;

import java.time.LocalDate;
import java.time.Period;

public class Actor {
    private int actorId;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;

    public Actor(int actorId, String name, LocalDate dateOfBirth, String nationality) {
        this.actorId = actorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    public int getActorId() { return actorId; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }

    public int getAge(LocalDate currentDate) {
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    @Override
    public String toString() {
        return String.format("Actor{id=%d, name='%s', dob=%s, nationality='%s'}",
                actorId, name, dateOfBirth, nationality);
    }
}
