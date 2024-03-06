package org.example.model;
import java.sql.Date;
import java.time.LocalDate;

public class Book {
    String title;
    String author;
    String isbn;
    boolean isBorrowed;
    String borrower;
    LocalDate borrowDate;
    LocalDate returnDate;

    public Book(String title, String author, String isbn, Boolean isBorrowed, String borrower,
                Date borrowDate, Date returnDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = isBorrowed;
        this.borrower = borrower;
        this.borrowDate = borrowDate.toLocalDate();
        this.returnDate = returnDate.toLocalDate();
    }
    public Book(String title, String author, String isbn, Boolean isBorrowed)
    {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = isBorrowed;
    }

    public void printBookInfo() {
        String availability = isBorrowed ? "Niedostępna" : "Dostępna";
        System.out.printf("Tytuł: %s, Autor: %s, ISBN: %s, Status: %s",
                title, author, isbn, availability);
        if (isBorrowed) {
            System.out.printf(", Wypożyczona przez: %s, Data wypożyczenia: %s, Data zwrotu: %s",
                    borrower, borrowDate, returnDate);
        }
        System.out.println();
    }
}
