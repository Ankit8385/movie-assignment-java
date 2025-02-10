package org.example;

import java.util.List;
import java.util.ArrayList;

public class Movie {
    private int movieId;
    private String title;
    private int releaseYear;
    private String genre;
    private double rating;
    private int duration;
    private int directorId;
    private List<Integer> actorIds;

    public Movie(int movieId, String title, int releaseYear, String genre,
                 double rating, int duration, int directorId, List<Integer> actorIds) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.directorId = directorId;
        this.actorIds = actorIds;
    }

    // Getters and setters
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public int getReleaseYear() { return releaseYear; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getDuration() { return duration; }
    public int getDirectorId() { return directorId; }
    public List<Integer> getActorIds() { return actorIds; }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', year=%d, genre='%s', rating=%.1f, duration=%d}",
                movieId, title, releaseYear, genre, rating, duration);
    }
}
