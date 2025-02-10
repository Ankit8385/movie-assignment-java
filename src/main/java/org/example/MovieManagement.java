package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MovieManagement {
    private Map<Integer, Movie> movies = new HashMap<>();
    private Map<Integer, Actor> actors = new HashMap<>();
    private Map<Integer, Director> directors = new HashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void loadData(String moviesFile, String actorsFile, String directorsFile) {
        loadActors(getResourcePath(actorsFile));
        loadDirectors(getResourcePath(directorsFile));
        loadMovies(getResourcePath(moviesFile));
    }

    private String getResourcePath(String filename) {
        return getClass().getClassLoader().getResource(filename).getPath();
    }

    private void loadActors(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                LocalDate dob = LocalDate.parse(data[2].trim(), DATE_FORMATTER);
                String nationality = data[3].trim();
                actors.put(id, new Actor(id, name, dob, nationality));
            }
        } catch (IOException e) {
            System.err.println("Error loading actors: " + e.getMessage());
        }
    }

    private void loadDirectors(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                LocalDate dob = LocalDate.parse(data[2].trim(), DATE_FORMATTER);
                String nationality = data[3].trim();
                directors.put(id, new Director(id, name, dob, nationality));
            }
        } catch (IOException e) {
            System.err.println("Error loading directors: " + e.getMessage());
        }
    }

    private void loadMovies(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0].trim());
                String title = data[1].trim();
                int year = Integer.parseInt(data[2].trim());
                String genre = data[3].trim();
                double rating = Double.parseDouble(data[4].trim());
                int duration = Integer.parseInt(data[5].trim());
                int directorId = Integer.parseInt(data[6].trim());

                // Parse actor IDs from the quoted string
                String actorIdsStr = data[7].trim().replace("\"", "");
                List<Integer> actorIds = Arrays.stream(actorIdsStr.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                movies.put(id, new Movie(id, title, year, genre, rating, duration, directorId, actorIds));
            }
        } catch (IOException e) {
            System.err.println("Error loading movies: " + e.getMessage());
        }
    }

    public void getMovieInformation(String titleOrId) {
        Movie movie = null;
        try {
            int id = Integer.parseInt(titleOrId);
            movie = movies.get(id);
        } catch (NumberFormatException e) {
            movie = movies.values().stream()
                    .filter(m -> m.getTitle().equalsIgnoreCase(titleOrId))
                    .findFirst()
                    .orElse(null);
        }

        if (movie == null) {
            System.out.println("Movie not found.");
            return;
        }

        Director director = directors.get(movie.getDirectorId());
        List<Actor> movieActors = movie.getActorIds().stream()
                .map(actors::get)
                .collect(Collectors.toList());

        System.out.println("\nMovie Details:");
        System.out.println(movie);
        System.out.println("Director: " + director);
        System.out.println("Actors:");
        movieActors.forEach(actor -> System.out.println("- " + actor));
    }

    public void getTopRatedMovies(int limit) {
        movies.values().stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(limit)
                .forEach(this::printMovieDetails);
    }

    public void getMoviesByGenre(String genre) {
        movies.values().stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .forEach(this::printMovieDetails);
    }

    public void getMoviesByDirector(String directorName) {
        directors.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(directorName))
                .findFirst()
                .ifPresent(director -> {
                    movies.values().stream()
                            .filter(m -> m.getDirectorId() == director.getDirectorId())
                            .forEach(this::printMovieDetails);
                });
    }

    public void getMoviesByYear(int year) {
        movies.values().stream()
                .filter(m -> m.getReleaseYear() == year)
                .forEach(this::printMovieDetails);
    }

    public void getMoviesByYearRange(int startYear, int endYear) {
        movies.values().stream()
                .filter(m -> m.getReleaseYear() >= startYear && m.getReleaseYear() <= endYear)
                .forEach(this::printMovieDetails);
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getMovieId(), movie);
        System.out.println("Movie added successfully.");
    }

    public void updateMovieRating(int movieId, double newRating) {
        Movie movie = movies.get(movieId);
        if (movie != null) {
            movie.setRating(newRating);
            System.out.println("Rating updated successfully.");
        } else {
            System.out.println("Movie not found.");
        }
    }

    public void deleteMovie(int movieId) {
        if (movies.remove(movieId) != null) {
            System.out.println("Movie deleted successfully.");
        } else {
            System.out.println("Movie not found.");
        }
    }

    public void getSortedMoviesByYear(int limit) {
        movies.values().stream()
                .sorted(Comparator.comparingInt(Movie::getReleaseYear))
                .limit(limit)
                .forEach(this::printMovieDetails);
    }

    public void getTopDirectors(int limit) {
        directors.values().stream()
                .map(director -> new AbstractMap.SimpleEntry<>(
                        director,
                        movies.values().stream()
                                .filter(m -> m.getDirectorId() == director.getDirectorId())
                                .count()
                ))
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .forEach(entry -> System.out.printf("%s - %d movies%n",
                        entry.getKey().getName(), entry.getValue()));
    }

    public void getMostActiveActors() {
        actors.values().stream()
                .map(actor -> new AbstractMap.SimpleEntry<>(
                        actor,
                        movies.values().stream()
                                .filter(m -> m.getActorIds().contains(actor.getActorId()))
                                .count()
                ))
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.printf("%s - %d movies%n",
                        entry.getKey().getName(), entry.getValue()));
    }

    public void getYoungestActorMovies(LocalDate referenceDate) {
        Actor youngestActor = actors.values().stream()
                .min(Comparator.comparing(actor -> actor.getDateOfBirth()))
                .orElse(null);

        if (youngestActor != null) {
            System.out.printf("Youngest actor: %s (Age: %d)%n",
                    youngestActor.getName(),
                    youngestActor.getAge(referenceDate));

            System.out.println("\nMovies featuring this actor:");
            movies.values().stream()
                    .filter(m -> m.getActorIds().contains(youngestActor.getActorId()))
                    .forEach(this::printMovieDetails);
        }
    }

    private void printMovieDetails(Movie movie) {
        Director director = directors.get(movie.getDirectorId());
        System.out.printf("%s (Year: %d, Rating: %.1f, Director: %s)%n",
                movie.getTitle(), movie.getReleaseYear(),
                movie.getRating(), director.getName());
    }
}