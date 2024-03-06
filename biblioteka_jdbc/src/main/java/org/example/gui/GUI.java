package org.example.gui;
import java.util.Scanner;

import org.example.db.DatabaseUtil;
import org.example.db.DatabaseUtil.*;

public class GUI {
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
                    DatabaseUtil.addBook();
                    break;
                case "2":
                    DatabaseUtil.searchBooks();
                    break;
                case "3":
                    DatabaseUtil.borrowBook();
                    break;
                case "4":
                    DatabaseUtil.listBorrowedBooks();
                    break;
                case "5":
                    DatabaseUtil.listOverdueBooks();
                    break;
                case "6":
                    DatabaseUtil.listAllBooks();
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
}
