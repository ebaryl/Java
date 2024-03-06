package org.example;

import org.example.authorization.Authenticator;
import org.example.gui.GUI;

public class Main {
    public static void main(String[] args) {
        Authenticator.connect();
        new GUI().login();
        Authenticator.disconnect();
    }
}