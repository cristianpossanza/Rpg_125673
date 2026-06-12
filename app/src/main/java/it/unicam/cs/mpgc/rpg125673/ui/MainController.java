package it.unicam.cs.mpgc.rpg125673.ui;

import it.unicam.cs.mpgc.rpg125673.model.entity.Punteggio;
import it.unicam.cs.mpgc.rpg125673.repository.PunteggioRepository;
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
    private boolean avvisoArmaMostrato = false;
    private PunteggioRepository punteggioRepository;

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

        avvisoArmaMostrato = false;
        aggiornaStatistiche();
    }

    @FXML
    public void gestisciAttacco() {
        if (!avvisoArmaMostrato && partita.getGiocatore().getInventario().getArmaEquipaggiata() == null) {
            mostraErrore("Consiglio di Gioco", "Stai per attaccare a mani nude!\nRicordati di equipaggiare l'arma più forte che hai nello zaino prima di attaccare.");
            avvisoArmaMostrato = true;
            return;
        }
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
        Arma armaDaEquipaggiare = null;
        for (Oggetto ogg : partita.getGiocatore().getInventario().getZaino().keySet()) {
            if (ogg instanceof Arma a) {
                armaDaEquipaggiare = a;
                break;
            }
        }
        if (armaDaEquipaggiare != null) {
            try {
                partita.getGiocatore().getInventario().equipaggiaArma(armaDaEquipaggiare);
                scriviLog(partita.getGiocatore().getNome() + " impugna " + armaDaEquipaggiare.getNome() + "!");
                aggiornaStatistiche();
            } catch (Exception e) {
                mostraErrore("Errore", e.getMessage());
            }
        } else {
            mostraErrore("Zaino", "Non hai nessuna nuova arma da equipaggiare o l'hai già impugnata!");
        }
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
            boolean partitaFinita = false;
            boolean vittoriaFinale = false;

            if (partita.getGiocatore().isVivo()) {
                if (partita.getRoundCorrente() < partita.getMaxRound()) {
                    scriviLog("\n--- ROUND SUPERATO! ---");
                    btnProssimoRound.setVisible(true);
                } else {
                    scriviLog("\n HAI VINTO IL GIOCO! ");
                    mostraErrore("VITTORIA", "Complimenti, hai completato il Dungeon!");
                    partitaFinita = true;
                    vittoriaFinale = true;
                }
            } else {
                mostraErrore("SCONFITTA", "Sei caduto in battaglia... Riprova!");
                partitaFinita = true;
            }

            if (partitaFinita) {
                Punteggio p = new Punteggio(
                        partita.getGiocatore().getNome(),
                        partita.getGiocatore().getEsperienza(),
                        partita.getRoundCorrente(),
                        vittoriaFinale
                );
                punteggioRepository.salvaPunteggio(p);
                scriviLog("\n[Punteggio salvato nella Hall of Fame!]");
            }
        }
    }

    private void scriviLog(String messaggio) { logBattaglia.appendText(messaggio + "\n"); }

    private String chiediNomeGiocatore() {
        punteggioRepository = new PunteggioRepository();
        List<Punteggio> top5 = punteggioRepository.getMiglioriPunteggi();

        StringBuilder classifica = new StringBuilder("--- HALL OF FAME ---\n");
        if (top5.isEmpty()) {
            classifica.append("Nessun punteggio registrato. Sii il primo!\n");
        } else {
            for (int i = 0; i < top5.size(); i++) {
                Punteggio p = top5.get(i);
                classifica.append(i + 1).append(". ").append(p.getNomeGiocatore())
                        .append(" - ").append(p.getEsperienzaTotale()).append(" XP (Round ")
                        .append(p.getRoundRaggiunto()).append(")\n");
            }
        }
        classifica.append("\nInserisci il nome del tuo eroe:");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Creazione Personaggio");
        dialog.setHeaderText("Benvenuto in Dungeon Arena RPG!");
        dialog.setContentText(classifica.toString());

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