package it.unicam.cs.mpgc.rpg125673.ui;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125673.repository.MostriRepository;
import it.unicam.cs.mpgc.rpg125673.repository.OggettiRepository;
import it.unicam.cs.mpgc.rpg125673.service.GestoreBattaglia;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;
import java.util.Optional;

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

    private GestoreBattaglia gestoreBattaglia;
    private Giocatore giocatore;
    private MostriRepository mostriRepo;
    private OggettiRepository oggettiRepo;

    private int roundCorrente = 1;
    private final int MAX_ROUND = 4;

    private final String[] progressioneMostri = {"Goblin", "Orco Furioso", "Golem di Pietra", "Drago di Fuoco"};
    private final String[] progressioneArmi = {"Spada di Legno", "Spada di Ferro", "Spada d'Acciaio", "Spada Leggendaria"};

    private Arma armaDelRoundAttuale;
    private Pozione pozioneBase;

    @FXML
    public void initialize() {
        mostriRepo = new MostriRepository();
        oggettiRepo = new OggettiRepository();
        pozioneBase = (Pozione) oggettiRepo.getOggetto("Pozione Piccola");
        String nomeEroe = chiediNomeGiocatore();
        giocatore = new Giocatore(nomeEroe);
        giocatore.getInventario().aggiungiOggetto(pozioneBase);
        giocatore.getInventario().aggiungiOggetto(pozioneBase);
        giocatore.getInventario().aggiungiOggetto(pozioneBase);

        preparaRound();
    }

    /**
     * Prepara l'arena in base al round corrente.
     */
    private void preparaRound() {
        lblRound.setText("ROUND " + roundCorrente + " / " + MAX_ROUND);

        giocatore.getInventario().buttaVecchieArmi();

        String nomeNuovaArma = progressioneArmi[roundCorrente - 1];
        armaDelRoundAttuale = (Arma) oggettiRepo.getOggetto(nomeNuovaArma);

        if (armaDelRoundAttuale != null) {
            giocatore.getInventario().aggiungiOggetto(armaDelRoundAttuale);
            giocatore.getInventario().equipaggiaArma(armaDelRoundAttuale);
        }

        if (roundCorrente == MAX_ROUND) {
            pozioneBase = (Pozione) oggettiRepo.getOggetto("Pozione Grande");
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            scriviLog("\nIl boss finale si avvicina! Il gioco ti regala 2 " + pozioneBase.getNome() + "!");
        } else if (roundCorrente > 1) {
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            scriviLog("\nHai trovato 2 Pozioni Piccole e una " + armaDelRoundAttuale.getNome() + "!");
        } else {
            scriviLog("Inizi l'avventura con una " + armaDelRoundAttuale.getNome() + "!");
        }

        String nomeMostro = progressioneMostri[roundCorrente - 1];
        Mostro nemico = mostriRepo.getMostro(nomeMostro);

        if (nemico == null) {
            nemico = new Mostro("Mostro Buggato", 10, 1, 0);
        }

        gestoreBattaglia = new GestoreBattaglia(giocatore, nemico);

        lblNomeGiocatore.setText(giocatore.getNome());
        lblNomeMostro.setText(nemico.getNome());

        scriviLog("\nLungo la strada hai incontrato " + nemico.getNome());

        btnAttacca.setDisable(false);
        btnPozione.setDisable(false);
        btnEquipaggia.setDisable(false);
        btnProssimoRound.setVisible(false);

        aggiornaStatistiche();
    }

    @FXML
    public void avanzaRound() {
        roundCorrente++;
        preparaRound();
    }

    @FXML
    public void gestisciAttacco() {
        scriviLog(gestoreBattaglia.eseguiTurnoAttacco());
        aggiornaStatistiche();
        controllaFineBattaglia();
    }

    @FXML
    public void gestisciPozione() {
        java.util.List<Pozione> pozioniDisponibili = new java.util.ArrayList<>();

        for (Oggetto ogg : giocatore.getInventario().getZaino().keySet()) {
            if (ogg instanceof Pozione p) {
                pozioniDisponibili.add(p);
            }
        }

        if (pozioniDisponibili.isEmpty()) {
            mostraErrore("Zaino Vuoto", "Non hai nessuna pozione nello zaino!");
            return;
        }

        if (pozioniDisponibili.size() == 1) {
            usaPozioneScelta(pozioniDisponibili.get(0));
            return;
        }

        ChoiceDialog<Pozione> dialog = new ChoiceDialog<>(pozioniDisponibili.get(0), pozioniDisponibili);
        dialog.setTitle("Scegli Pozione");
        dialog.setHeaderText("Quale pozione vuoi bere?");
        dialog.setContentText("Scegli:");

        Optional<Pozione> scelta = dialog.showAndWait();

        if (scelta.isPresent()) {
            usaPozioneScelta(scelta.get());
        }
    }

    @FXML
    public void gestisciEquipaggia() {
        try {
            giocatore.getInventario().equipaggiaArma(armaDelRoundAttuale);
            scriviLog(giocatore.getNome() + " impugna " + armaDelRoundAttuale.getNome() + "!");
            aggiornaStatistiche();
        } catch (Exception e) {
            mostraErrore("Attenzione", e.getMessage());
        }
    }

    // --- METODI PRIVATI DI SUPPORTO ---

    private void aggiornaStatistiche() {
        lblHpGiocatore.setText("HP: " + giocatore.getPuntiVita() + " / " + giocatore.getPuntiVitaMassimi());

        Arma armaEquipaggiata = giocatore.getInventario().getArmaEquipaggiata();
        if (armaEquipaggiata != null) {
            lblArmaGiocatore.setText("Arma: " + armaEquipaggiata.getNome() + " (+" + armaEquipaggiata.getBonusAttacco() + " Danni)\nDanno Totale: " + giocatore.getAttacco());
        } else {
            lblArmaGiocatore.setText("Arma: Mani nude (Danno Totale: " + giocatore.getAttacco() + ")");
        }

        // Aggiorna Inventario
        if (giocatore.getInventario().getZaino().isEmpty()) {
            lblInventario.setText("Zaino vuoto");
        } else {
            StringBuilder testoZaino = new StringBuilder();
            for (java.util.Map.Entry<Oggetto, Integer> entry : giocatore.getInventario().getZaino().entrySet()) {
                testoZaino.append("- ").append(entry.getKey().getNome()).append(" (x").append(entry.getValue()).append(")\n");
            }
            lblInventario.setText(testoZaino.toString());
        }

        // Aggiorna HP Mostro
        Mostro nemico = gestoreBattaglia.getNemico();
        lblHpMostro.setText("HP: " + nemico.getPuntiVita() + " / " + nemico.getPuntiVitaMassimi());
    }

    private void controllaFineBattaglia() {
        if (gestoreBattaglia.isBattagliaTerminata()) {
            btnAttacca.setDisable(true);
            btnPozione.setDisable(true);
            btnEquipaggia.setDisable(true);

            if (giocatore.isVivo()) {
                if (roundCorrente < MAX_ROUND) {
                    scriviLog("\n--- ROUND SUPERATO! Preparati al prossimo... ---");
                    btnProssimoRound.setVisible(true);
                } else {
                    scriviLog("\n HAI SCONFITTO IL DRAGO! HAI VINTO IL GIOCO! ");
                    mostraErrore("VITTORIA", "Complimenti, hai completato il Dungeon!");
                }
            } else {
                mostraErrore("SCONFITTA", "Sei caduto in battaglia... Riprova!");
            }
        }
    }

    private String chiediNomeGiocatore() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Creazione Personaggio");
        dialog.setHeaderText("Benvenuto in Dungeon Arena RPG!");
        dialog.setContentText("Inserisci il nome del tuo eroe:");

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String nome = result.get().trim();
                if (!nome.isEmpty()) {
                    return nome;
                } else {
                    mostraErrore("Nome non valido", "Il nome non può essere vuoto. Riprova.");
                }
            } else {
                System.exit(0);
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

    private void scriviLog(String messaggio) {
        logBattaglia.appendText(messaggio + "\n");
    }

    private void usaPozioneScelta(Pozione pozioneDaUsare) {
        scriviLog(gestoreBattaglia.eseguiTurnoPozione(pozioneDaUsare));
        aggiornaStatistiche();
        controllaFineBattaglia();
    }
}