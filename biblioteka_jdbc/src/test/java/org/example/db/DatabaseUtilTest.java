package org.example.db;
import org.example.model.Book;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;


public class DatabaseUtilTest {
//    private static LocalDate setReturnDateManually() {
//        System.out.println("Podaj datę zwrotu w formacie RRRR-MM-DD: ");
//        String inputDate = scanner.nextLine();
//        return LocalDate.parse(inputDate);
//    }

    @Test
    public void SetReturnDateManuallyTest() {
        String inputDate = "2020-12-10";

        LocalDate expectedDate = LocalDate.of(2020, 12, 10);
        LocalDate actualDate = LocalDate.parse(inputDate);
        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void ListBooksTest() {
        Date kiedystam = Date.valueOf(LocalDate.of(2023, 1, 1));
        Date jeszczepozniej = Date.valueOf(LocalDate.of(2111, 1, 1));

        Book book1 = new Book("LPG", "Moto Doradca", "1111", true, "mnie",
                kiedystam, jeszczepozniej);
        Book book2 = new Book("Wietnam", "Robert Makłowicz", "2222", true, "mnie",
                kiedystam, jeszczepozniej);

        List<Book> foundBooks = new ArrayList<>();
        foundBooks.add(book1);
        foundBooks.add(book2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        if (foundBooks.isEmpty()) {
            System.out.println("Nie znaleziono książek.");
        } else {
            System.out.println("--- Znalezione książki ---");
            foundBooks.forEach(Book::printBookInfo);
        }

        // Sprawdzamy czy wypisane zostały poprawne informacje o książkach
        String expectedOutput = "--- Znalezione książki ---\n" +
                "Tytuł: LPG, Autor: Moto Doradca, ISBN: 1111, Status: Niedostępna, " +
                "Wypożyczona przez: mnie, Data wypożyczenia: 2023-01-01, Data zwrotu: 2111-01-01\n" +
                "Tytuł: Wietnam, Autor: Robert Makłowicz, ISBN: 2222, Status: Niedostępna, Wypożyczona" +
                " przez: mnie, Data wypożyczenia: 2023-01-01, Data zwrotu: 2111-01-01";
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }

}
