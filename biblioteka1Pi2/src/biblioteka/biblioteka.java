package biblioteka;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class biblioteka {

    static class Book {
        String title;
        String author;
        String isbn;
        boolean isBorrowed;
        String borrower;
        LocalDate borrowDate;
        LocalDate returnDate;

        public Book(String title, String author, String isbn) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.isBorrowed = false;
        }
        public Book(String title, String author, String isbn, LocalDate returnDate) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.isBorrowed = false;
            this.returnDate = returnDate;
        }

            public void printBookInfo() {
                System.out.printf("Tytuł: %s, Autor: %s, ISBN: %s, Status: %s",
                        title, author, isbn,
                        (isBorrowed ?
                                "Wypożyczona " + "przez: " + borrower + ", Data wypożyczenia: " +
                                borrowDate + ", Data zwrotu: " + returnDate : "Dostępna"));
                System.out.println();
            }

    }

    static class LibrarySystem {
        private final List<Book> books = new ArrayList<>();
        private final Scanner scanner = new Scanner(System.in);

        public void login() {
            System.out.println("login:admin, haslo:password\n");
            System.out.println("Logowanie do systemu biblioteki");
            System.out.print("Podaj login: ");
            String login = scanner.nextLine();
            System.out.print("Podaj hasło: ");
            String password = scanner.nextLine();

            if ("admin".equals(login) && "password".equals(password)) {
                System.out.println("Zalogowano pomyślnie.");
                mainMenu();
            } else {
                System.out.println("Nieprawidłowy login lub hasło. Spróbuj ponownie.");
                login();
            }
        }

        private void mainMenu() {
            while (true) {
                System.out.println("\n--- MENU GŁÓWNE ---");
                System.out.println("1. Dodaj książkę");
                System.out.println("2. Wyszukaj książki");
                System.out.println("3. Wypożycz książkę");
                System.out.println("4. Wylistuj wypożyczone książki");
                System.out.println("5. Wylistuj przetrzymane książki");
                System.out.println("6. Wylistuj wszystkie książki");
                System.out.println("0. Wyjście");

                System.out.print("Wybierz opcję: ");
                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        addBook();
                        break;
                    case "2":
                        searchBooks();
                        break;
                    case "3":
                        borrowBook();
                        break;
                    case "4":
                        listBorrowedBooks();
                        break;
                    case "5":
                        listOverdueBooks();
                        break;
                    case "6":
                        listAllBooks();
                        break;
                    case "0":
                        System.out.println("Zakończenie pracy.");
                        return;
                    default:
                        System.out.println("Nieprawidłowa opcja.");
                }

                System.out.print("Czy chcesz powrócić do menu głównego? (T/N): ");
                String response = scanner.nextLine().toLowerCase();
                if (!response.equals("t")) {
                    System.out.println("Zakończenie pracy.");
                    return;
                }
            }
        }


        private void addBook() {
            System.out.println("--- Dodawanie nowej książki ---");
            System.out.print("Tytuł: ");
            String title = scanner.nextLine();
            System.out.print("Autor: ");
            String author = scanner.nextLine();
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            books.add(new Book(title, author, isbn));
            System.out.println("Książka została dodana.");
        }

        private LocalDate setReturnDateManually() {
            System.out.println("Podaj datę zwrotu w formacie RRRR-MM-DD: ");
            String inputDate = scanner.nextLine();
            return LocalDate.parse(inputDate);
        }

        private void searchBooks() {
            System.out.println("--- Wyszukiwanie książek ---");
            System.out.print("Wpisz fragment tytułu, autora lub numeru ISBN: ");
            String query = scanner.nextLine().toLowerCase();

            List<Book> foundBooks = books.stream()
                    .filter(book -> book.title.toLowerCase().contains(query) ||
                            book.author.toLowerCase().contains(query) ||
                            book.isbn.toLowerCase().contains(query))
                    .toList();

            if (foundBooks.isEmpty()) {
                System.out.println("Nie znaleziono książek.");
            } else {
                System.out.println("Znalezione książki:");
                foundBooks.forEach(Book::printBookInfo);
            }
        }


        private void borrowBook() {
            System.out.println("--- Wypożyczanie książki ---");
            System.out.print("Podaj tytuł książki: ");
            String title = scanner.nextLine();

            Book book = books.stream()
                    .filter(b -> b.title.equalsIgnoreCase(title) && !b.isBorrowed)
                    .findFirst()
                    .orElse(null);

            if (book == null) {
                System.out.println("Książka nie jest dostępna lub nie istnieje.");
                return;
            }

            System.out.print("Imię i nazwisko osoby wypożyczającej: ");
            String borrower = scanner.nextLine();

            System.out.println("Standardowy czas do oddania książki to 2 tygodnie.");
            System.out.print("Czy chcesz samodzielnie ustawić datę zwrotu? (T/N): ");
            String choice = scanner.nextLine();

            book.isBorrowed = true;
            book.borrower = borrower;
            book.borrowDate = LocalDate.now();

            if (choice.equalsIgnoreCase("T")) {
                book.returnDate = setReturnDateManually();
                System.out.println("Pomyślnie zmieniono datę zwrotu");
            } else {
                book.returnDate = LocalDate.now().plusWeeks(2);
            }

            System.out.println("Książka została wypożyczona.");
        }


        private void listBorrowedBooks() {
            System.out.println("--- Lista wypożyczonych książek ---");
            books.stream()
                    .filter(book -> book.isBorrowed)
                    .forEach(Book::printBookInfo);
        }

        private void listOverdueBooks() {
            System.out.println("--- Lista przetrzymanych książek ---");
            LocalDate today = LocalDate.now();
            books.stream()
                    .filter(book -> book.isBorrowed && book.returnDate.isBefore(today))
                    .forEach(Book::printBookInfo);
        }

        private void listAllBooks() {
            System.out.println("--- Lista wszystkich książek ---");
            books.forEach(Book::printBookInfo);
        }


    }

    public static void main(String[] args) {
        new LibrarySystem().login();
    }

}
