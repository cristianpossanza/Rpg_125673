package it.unicam.cs.mpgc.rpg125673.ui;

import javafx.fxml.FXML;

public class MainController {

    // L'annotazione @FXML collega questo metodo al bottone nel file .fxml
    @FXML
    public void gestisciClickInizia() {
        System.out.println("Hai cliccato il bottone! La grafica funziona!");
    }
}