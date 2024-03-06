package org.example.model;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.time.LocalDate;
public class BookTest {
    @Test
    public void BookConstructorTest()
    {
        String title = "American Psycho";
        String author = "Oba ma";
        String isbn = "600100100";
        Boolean isBorrowed = true;
        String borrower = "Oba ma";
        LocalDate borrowDate = LocalDate.of(2004, 11, 23);
        LocalDate returnDate = LocalDate.of(2017, 1, 20);

        LocalDate expectedDate = LocalDate.of(2020, 12, 10);

        Assertions.assertEquals(expectedDate, borrowDate);

        Book book = new Book(title, author, isbn, isBorrowed, borrower, Date.valueOf(borrowDate),
                Date.valueOf(returnDate));

        Assertions.assertEquals(title, "American Psycho", "Niepoprawny tytuł książki");
        Assertions.assertEquals(author, "Oba ma", "Niepoprawny autor książki");
        Assertions.assertEquals(isbn, "600100100", "Niepoprawny ISBN książki");
        Assertions.assertTrue(isBorrowed, "Książka powinna być wypożyczona");
        Assertions.assertEquals(borrowDate, "2004-11-23", "Niepoprawna data wypożyczenia");
        Assertions.assertEquals(returnDate, "2017-01-20", "Niepoprawna data zwrotu");
    }
}
