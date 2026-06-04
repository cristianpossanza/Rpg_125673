package it.unicam.cs.mpgc.rpg125673.ui;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125673.service.GestoreBattaglia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MainController {
//chiedere a se: Il controller non è troppo lungo? non sarebbe meglio creare piu controller?
    @FXML private Label lblNomeGiocatore;
    @FXML private Label lblHpGiocatore;
    @FXML private Label lblArmaGiocatore;
    @FXML private Label lblInventario;

    @FXML private Label lblNomeMostro;
    @FXML private Label lblHpMostro;

    @FXML private TextArea logBattaglia;
    @FXML private Button btnAttacca;
    @FXML private Button btnPozione;
    @FXML private Button btnEquipaggia;

    private GestoreBattaglia gestoreBattaglia;
    private Giocatore giocatore;
    private Pozione pozioneDiScorta;
    private Arma spadaDiScorta;

    @FXML
    public void initialize() {
        giocatore = new Giocatore("Artù");
        Mostro nemico = new Mostro("Orco Furioso", 60, 10, 50);
        pozioneDiScorta = new Pozione("Pozione Piccola", "Cura 30 HP", 30);
        spadaDiScorta = new Arma("Spada Lunga", "Una spada affilata", 12);
        giocatore.getInventario().aggiungiOggetto(pozioneDiScorta);
        giocatore.getInventario().aggiungiOggetto(pozioneDiScorta); // Ne diamo 2!
        giocatore.getInventario().aggiungiOggetto(spadaDiScorta);
        gestoreBattaglia = new GestoreBattaglia(giocatore, nemico);
        lblNomeGiocatore.setText(giocatore.getNome());
        lblNomeMostro.setText(nemico.getNome());
        scriviLog("Un " + nemico.getNome() + " selvatico appare!");
        scriviLog("La battaglia ha inizio!");
        aggiornaStatistiche();
    }

    // --- AZIONI DEI BOTTONI ---

    @FXML
    public void gestisciAttacco() {
        String risultato = gestoreBattaglia.eseguiTurnoAttacco();
        scriviLog(risultato);
        aggiornaStatistiche();
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciPozione() {
        String risultato = gestoreBattaglia.eseguiTurnoPozione(pozioneDiScorta);
        scriviLog(risultato);
        aggiornaStatistiche();
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciEquipaggia() {
        try {
            giocatore.getInventario().equipaggiaArma(spadaDiScorta);
            scriviLog(giocatore.getNome() + " ha equipaggiato " + spadaDiScorta.getNome() + "!");
            aggiornaStatistiche();
        } catch (IllegalArgumentException e) {
            scriviLog("Non hai l'arma nello zaino!");
        }
    }


    // --- METODI PRIVATI ---
    /**
     * Questo metodo è il cuore della UI: legge i dati dal Model e li stampa a schermo.
     */
    private void aggiornaStatistiche() {
        lblHpGiocatore.setText("HP: " + giocatore.getPuntiVita() + " / " + giocatore.getPuntiVitaMassimi());

        if (giocatore.getInventario().getZaino().isEmpty()) {
            lblInventario.setText("Zaino vuoto");
        } else {
            StringBuilder testoZaino = new StringBuilder();
            for (var entry : giocatore.getInventario().getZaino().entrySet()) {
                String nomeOggetto = entry.getKey().getNome();
                int quantita = entry.getValue();
                testoZaino.append("- ").append(nomeOggetto).append(" (x").append(quantita).append(")\n");
            }
            lblInventario.setText(testoZaino.toString());
        }
        Mostro nemico = gestoreBattaglia.getNemico();
        lblHpMostro.setText("HP: " + nemico.getPuntiVita() + " / " + nemico.getPuntiVitaMassimi());
    }

    private void scriviLog(String messaggio) {
        logBattaglia.appendText(messaggio + "\n");
    }

    private void controllaFineBattaglia() {
        if (gestoreBattaglia.isBattagliaTerminata()) {
            btnAttacca.setDisable(true);
            btnPozione.setDisable(true);
            btnEquipaggia.setDisable(true);
            scriviLog("\n--- FINE SCONTRO ---");
        }
    }
}