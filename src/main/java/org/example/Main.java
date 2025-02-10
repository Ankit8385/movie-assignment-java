package org.example;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static MovieManagement system = new MovieManagement();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load data from resource files
        system.loadData("movies.csv", "actors.csv", "directors.csv");

        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter movie ID or title: ");
                    String movieInput = scanner.nextLine();
                    system.getMovieInformation(movieInput);
                    break;

                case 2:
                    system.getTopRatedMovies(10);
                    break;

                case 3:
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    system.getMoviesByGenre(genre);
                    break;

                case 4:
                    System.out.print("Enter director name: ");
                    String directorName = scanner.nextLine();
                    system.getMoviesByDirector(directorName);
                    break;

                case 5:
                    int year = getIntInput("Enter release year: ");
                    system.getMoviesByYear(year);
                    break;

                case 6:
                    int startYear = getIntInput("Enter start year: ");
                    int endYear = getIntInput("Enter end year: ");
                    system.getMoviesByYearRange(startYear, endYear);
                    break;

                case 7:
                    addNewMovie();
                    break;

                case 8:
                    int movieId = getIntInput("Enter movie ID: ");
                    double newRating = getDoubleInput("Enter new rating: ");
                    system.updateMovieRating(movieId, newRating);
                    break;

                case 9:
                    movieId = getIntInput("Enter movie ID to delete: ");
                    system.deleteMovie(movieId);
                    break;

                case 10:
                    system.getSortedMoviesByYear(15);
                    break;

                case 11:
                    system.getTopDirectors(5);
                    break;

                case 12:
                    system.getMostActiveActors();
                    break;

                case 13:
                    system.getYoungestActorMovies(LocalDate.of(2025, 2, 10));
                    break;

                case 14:
                    System.out.println("Exit");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Movie Management System ===");
        System.out.println("1. Get Movie Information");
        System.out.println("2. Get Top 10 Rated Movies");
        System.out.println("3. Get Movies by Genre");
        System.out.println("4. Get Movies by Director");
        System.out.println("5. Get Movies by Release Year");
        System.out.println("6. Get Movies by Release Year Range");
        System.out.println("7. Add a New Movie");
        System.out.println("8. Update Movie Rating");
        System.out.println("9. Delete a Movie");
        System.out.println("10. Get 15 Movies Sorted by Year");
        System.out.println("11. Get Top 5 Directors");
        System.out.println("12. Get Most Active Actors");
        System.out.println("13. Get Youngest Actor's Movies");
        System.out.println("14. Exit");
    }

    private static void addNewMovie() {
        int movieId = getIntInput("Enter movie ID: ");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        int year = getIntInput("Enter release year: ");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        double rating = getDoubleInput("Enter rating: ");
        int duration = getIntInput("Enter duration (minutes): ");
        int directorId = getIntInput("Enter director ID: ");
        System.out.print("Enter actor IDs (space-separated): ");
        String actorIdsInput = scanner.nextLine();

        java.util.List<Integer> actorIds = Arrays.stream(actorIdsInput.split(" "))
                .map(Integer::parseInt)
                .collect(java.util.stream.Collectors.toList());

        Movie newMovie = new Movie(movieId, title, year, genre, rating, duration, directorId, actorIds);
        system.addMovie(newMovie);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}