package it.unicam.cs.mpgc.rpg125673.ui;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125673.service.GestorePartita;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller del pattern MVC per l'interfaccia JavaFX.
 * Non contiene logica di business, ma si limita ad intercettare gli eventi grafici, invocare i metodi
 * del GestorePartita e aggiornare dinamicamente i componenti visivi.
 */
public class MainController {

    @FXML private Label lblRound;
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
    @FXML private Button btnProssimoRound;

    private GestorePartita partita;

    @FXML
    public void initialize() {
        partita = new GestorePartita();
        String nomeEroe = chiediNomeGiocatore();
        String logIniziale = partita.iniziaNuovaPartita(nomeEroe);
        scriviLog("Benvenuto, valoroso " + partita.getGiocatore().getNome() + "!");
        scriviLog(logIniziale);
        aggiornaStatistiche();
    }

    @FXML
    public void avanzaRound() {
        String log = partita.avanzaRound();
        scriviLog("\n--- ROUND " + partita.getRoundCorrente() + " ---");
        scriviLog(log);

        btnAttacca.setDisable(false);
        btnPozione.setDisable(false);
        btnEquipaggia.setDisable(false);
        btnProssimoRound.setVisible(false);

        aggiornaStatistiche();
    }

    @FXML
    public void gestisciAttacco() {
        scriviLog(partita.getBattagliaCorrente().eseguiTurnoAttacco());
        aggiornaStatistiche();
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciPozione() {
        List<Pozione> pozioniDisponibili = new ArrayList<>();
        for (Oggetto ogg : partita.getGiocatore().getInventario().getZaino().keySet()) {
            if (ogg instanceof Pozione p) pozioniDisponibili.add(p);
        }

        if (pozioniDisponibili.isEmpty()) {
            mostraErrore("Zaino Vuoto", "Non hai pozioni!");
            return;
        }

        if (pozioniDisponibili.size() == 1) {
            scriviLog(partita.getBattagliaCorrente().eseguiTurnoPozione(pozioniDisponibili.get(0)));
        } else {
            ChoiceDialog<Pozione> dialog = new ChoiceDialog<>(pozioniDisponibili.get(0), pozioniDisponibili);
            dialog.setTitle("Scegli Pozione");
            dialog.setHeaderText("Quale pozione vuoi bere?");
            Optional<Pozione> scelta = dialog.showAndWait();
            if (scelta.isPresent()) {
                scriviLog(partita.getBattagliaCorrente().eseguiTurnoPozione(scelta.get()));
            } else {
                return;
            }
        }
        aggiornaStatistiche();
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciEquipaggia() {
        mostraErrore("Info", "L'arma migliore viene equipaggiata in automatico ad ogni round!");
    }


    // --- METODI PRIVATI DI SUPPORTO ---
    private void aggiornaStatistiche() {
        Giocatore g = partita.getGiocatore();
        Mostro m = partita.getBattagliaCorrente().getNemico();

        lblRound.setText("ROUND " + partita.getRoundCorrente() + " / " + partita.getMaxRound());
        lblNomeGiocatore.setText(g.getNome());
        lblNomeMostro.setText(m.getNome());

        lblHpGiocatore.setText("HP: " + g.getPuntiVita() + " / " + g.getPuntiVitaMassimi());
        lblHpMostro.setText("HP: " + m.getPuntiVita() + " / " + m.getPuntiVitaMassimi());

        Arma armaEq = g.getInventario().getArmaEquipaggiata();
        if (armaEq != null) {
            lblArmaGiocatore.setText("Arma: " + armaEq.getNome() + " (+" + armaEq.getBonusAttacco() + " Danni)\nDanno Totale: " + g.getAttacco());
        } else {
            lblArmaGiocatore.setText("Arma: Mani nude (Danno Totale: " + g.getAttacco() + ")");
        }

        if (g.getInventario().getZaino().isEmpty()) {
            lblInventario.setText("Zaino vuoto");
        } else {
            StringBuilder testoZaino = new StringBuilder();
            for (Map.Entry<Oggetto, Integer> entry : g.getInventario().getZaino().entrySet()) {
                testoZaino.append("- ").append(entry.getKey().getNome()).append(" (x").append(entry.getValue()).append(")\n");
            }
            lblInventario.setText(testoZaino.toString());
        }
    }

    private void controllaFineBattaglia() {
        if (partita.getBattagliaCorrente().isBattagliaTerminata()) {
            btnAttacca.setDisable(true);
            btnPozione.setDisable(true);
            btnEquipaggia.setDisable(true);

            if (partita.getGiocatore().isVivo()) {
                if (partita.getRoundCorrente() < partita.getMaxRound()) {
                    scriviLog("\n--- ROUND SUPERATO! ---");
                    btnProssimoRound.setVisible(true);
                } else {
                    scriviLog("\n HAI VINTO IL GIOCO! ");
                    mostraErrore("VITTORIA", "Complimenti, hai completato il Dungeon!");
                }
            } else {
                mostraErrore("SCONFITTA", "Sei caduto in battaglia... Riprova!");
            }
        }
    }

    private void scriviLog(String messaggio) { logBattaglia.appendText(messaggio + "\n"); }

    private String chiediNomeGiocatore() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Creazione Personaggio");
        dialog.setHeaderText("Benvenuto in Dungeon Arena RPG!");
        dialog.setContentText("Inserisci il nome del tuo eroe:");
        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                return result.get().trim();
            } else if (!result.isPresent()) {
                System.exit(0);
            } else {
                mostraErrore("Nome non valido", "Il nome non può essere vuoto.");
            }
        }
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}