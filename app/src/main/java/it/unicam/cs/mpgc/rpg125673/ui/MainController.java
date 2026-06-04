package it.unicam.cs.mpgc.rpg125673.ui;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125673.service.GestoreBattaglia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.awt.*;

public class MainController {

    @FXML private TextArea logBattaglia;
    @FXML private Button btnAttacca;
    @FXML private Button btnPozione;

    private GestoreBattaglia gestoreBattaglia;
    private Giocatore giocatore;
    private Pozione pozioneDiScorta;

    /**
     * Questo metodo viene chiamato in automatico da JavaFX appena si apre la finestra.
     * È perfetto per preparare la partita!
     */
    @FXML
    public void initialize() {
        giocatore = new Giocatore("artu");
        pozioneDiScorta = new Pozione("Pozione Piccola", "Cura 30 HP", 30);
        giocatore.getInventario().aggiungiOggetto(pozioneDiScorta);
        Mostro nemico = new Mostro("Orco furioso", 60, 10, 50);
        gestoreBattaglia = new GestoreBattaglia(giocatore, nemico);

        scriviLog("nella tua strada ha incrociato:" + nemico.getNome() + ", sconfiggilo");
        scriviLog("HP " + nemico.getNome() + ": " + nemico.getPuntiVitaMassimi());
        scriviLog("HP " + giocatore.getNome() + ": " + giocatore.getPuntiVitaMassimi());
        scriviLog("-----------------------------------------");
    }

    @FXML
    public void gestisciAttacco() {
        String risultato = gestoreBattaglia.eseguiTurnoAttacco();
        scriviLog(risultato);
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciPozione() {
        String risultato = gestoreBattaglia.eseguiTurnoPozione(pozioneDiScorta);
        scriviLog(risultato);
        controllaFineBattaglia();
    }

    //Metodi privati

    private void scriviLog(String messaggio) {
        logBattaglia.appendText(messaggio + "\n");
    }

    private void controllaFineBattaglia() {
        if (gestoreBattaglia.isBattagliaTerminata()) {
            btnAttacca.setDisable(true);
            btnPozione.setDisable(true);
            scriviLog("\n--- FINE SCONTRO ---");
        }
    }
}