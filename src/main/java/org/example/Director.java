package org.example;

import java.time.LocalDate;

public class Director {
    private int directorId;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;

    public Director(int directorId, String name, LocalDate dateOfBirth, String nationality) {
        this.directorId = directorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    public int getDirectorId() { return directorId; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }

    @Override
    public String toString() {
        return String.format("Director{id=%d, name='%s', dob=%s, nationality='%s'}",
                directorId, name, dateOfBirth, nationality);
    }
}
