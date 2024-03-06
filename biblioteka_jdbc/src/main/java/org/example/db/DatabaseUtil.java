package org.example.db;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.example.model.Book;

import static org.example.authorization.Authenticator.connection;

public class DatabaseUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static void addBook() {

        System.out.println("--- Dodawanie nowej książki ---");
        System.out.print("Tytuł: ");
        String title = scanner.nextLine();
        System.out.print("Autor: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            String sql = "INSERT INTO tbook (title, author, isbn) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.executeUpdate();
            System.out.println("Książka została dodana.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static LocalDate setReturnDateManually() {
        System.out.println("Podaj datę zwrotu w formacie RRRR-MM-DD: ");
        String inputDate = scanner.nextLine();
        return LocalDate.parse(inputDate);
    }

    public static void searchBooks() {
        System.out.println("--- Wyszukiwanie książek ---");
        System.out.print("Wpisz fragment tytułu, autora lub numeru ISBN: ");
        String query = scanner.nextLine().toLowerCase();

        try {
            String sql = "SELECT * FROM tbook WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(isbn) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();

            List<Book> foundBooks = new ArrayList<>();
            while (resultSet.next()) {
                String foundTitle = resultSet.getString("title");
                String foundAuthor = resultSet.getString("author");
                String foundIsbn = resultSet.getString("isbn");
                Boolean foundIsBorrowed = resultSet.getBoolean("isBorrowed");
                Date foundBorrowDate = resultSet.getDate("borrowDate");
                Date foundReturnDate = resultSet.getDate("returnDate");
                String foundBorrower = resultSet.getString("borrower");
                if(foundBorrowDate == null || foundReturnDate == null
                        || foundBorrower == null)
                {
                    foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, foundIsBorrowed));
                }
                else {
                    foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, foundIsBorrowed, foundBorrower,
                            foundBorrowDate, foundReturnDate));
                }
            }
            if (foundBooks.isEmpty()) {
                System.out.println("Nie znaleziono książek.");
            } else {
                System.out.println("Znalezione książki:");
                foundBooks.forEach(Book::printBookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void borrowBook() {
        System.out.println("--- Wypożyczanie książki ---");
        System.out.print("Podaj tytuł książki: ");
        String title = scanner.nextLine();
        try {
            // wyszukanie ksiazki
            String sql = "SELECT * FROM tbook WHERE title = ? AND isBorrowed = 0";
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, title);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int bookId = resultSet.getInt("id");
                System.out.print("Imie i nazwisko wypozyczajacego:");
                String borrower = scanner.nextLine();
                System.out.println("Standardowy czas do oddania książki to 2 tygodnie.");
                System.out.print("Czy chcesz samodzielnie ustawić datę zwrotu? (T/N): ");
                String choice = scanner.nextLine();
                LocalDate borrowDate = LocalDate.now();
                LocalDate returnDate;

                if (choice.equalsIgnoreCase("T")) {
                    returnDate = setReturnDateManually();
                } else {
                    returnDate = borrowDate.plusWeeks(2);
                }

                String updateSql = "UPDATE tbook SET isBorrowed = 1, borrower = ?," +
                        "borrowDate = ?, returnDate = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, borrower);
                updateStatement.setDate(2, Date.valueOf(borrowDate));
                updateStatement.setDate(3, Date.valueOf(returnDate));
                updateStatement.setInt(4, bookId);
                updateStatement.executeUpdate();
            }
            else if (!resultSet.next()) {
                System.out.println("Nie znaleziono książki o podanym tytule lub została już wypożyczona.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void listBorrowedBooks() {
        System.out.println("--- Lista wypożyczonych książek ---");

        try {
            String sql = "SELECT * FROM tbook WHERE isBorrowed = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Book> foundBooks = new ArrayList<>();
            while (resultSet.next()) {
                String foundTitle = resultSet.getString("title");
                String foundAuthor = resultSet.getString("author");
                String foundIsbn = resultSet.getString("isbn");
                String foundBorrower = resultSet.getString("borrower");
                Date foundBorrowDate = resultSet.getDate("borrowDate");
                Date foundReturnDate = resultSet.getDate("returnDate");

                foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, true, foundBorrower,
                        foundBorrowDate, foundReturnDate));
            }
            if (foundBooks.isEmpty()) {
                System.out.println("Nie znaleziono książek.");
            } else {
                System.out.println("--- Znalezione książki ---");
                foundBooks.forEach(Book::printBookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listOverdueBooks() {
        System.out.println("--- Lista przetrzymanych książek ---");

        try {
            String sql = "SELECT * FROM tbook WHERE isBorrowed = 1 AND returnDate < ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();

            List<Book> foundBooks = new ArrayList<>();
            while (resultSet.next()) {
                String foundTitle = resultSet.getString("title");
                String foundAuthor = resultSet.getString("author");
                String foundIsbn = resultSet.getString("isbn");
                String foundBorrower = resultSet.getString("borrower");
                Date foundBorrowDate = resultSet.getDate("borrowDate");
                Date foundReturnDate = resultSet.getDate("returnDate");

                foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, true, foundBorrower,
                        foundBorrowDate, foundReturnDate));
            }
            if (foundBooks.isEmpty()) {
                System.out.println("Nie znaleziono książek.");
            } else {
                System.out.println("Znalezione książki:");
                foundBooks.forEach(Book::printBookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void listAllBooks() {
        System.out.println("--- Lista wszystkich książek ---");
        try {
            String sql = "SELECT * FROM tbook";
            Statement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            List<Book> foundBooks = new ArrayList<>();
            while (resultSet.next()) {
                String foundTitle = resultSet.getString("title");
                String foundAuthor = resultSet.getString("author");
                String foundIsbn = resultSet.getString("isbn");
                Boolean foundIsBorrowed = resultSet.getBoolean("isBorrowed");
                Date foundBorrowDate = resultSet.getDate("borrowDate");
                Date foundReturnDate = resultSet.getDate("returnDate");
                String foundBorrower = resultSet.getString("borrower");
                if (foundBorrowDate == null || foundReturnDate == null
                        || foundBorrower == null) {
                    foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, foundIsBorrowed));
                } else {
                    foundBooks.add(new Book(foundTitle, foundAuthor, foundIsbn, foundIsBorrowed, foundBorrower,
                            foundBorrowDate, foundReturnDate));
                }
            }
            if (foundBooks.isEmpty()) {
                System.out.println("Nie znaleziono książek.");
            } else {
                System.out.println("Znalezione książki:");
                foundBooks.forEach(Book::printBookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
