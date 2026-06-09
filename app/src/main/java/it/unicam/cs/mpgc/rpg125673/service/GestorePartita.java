package it.unicam.cs.mpgc.rpg125673.service;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125673.repository.MostriRepository;
import it.unicam.cs.mpgc.rpg125673.repository.OggettiRepository;

/**
 * Service che funge da gestore dell'intera partita.
 * Mantiene lo stato globale del gioco (Round corrente, Giocatore, Repository attivi)
 * e gestisce la progressione della difficoltà, l'assegnazione automatica dell'equipaggiamento
 * e l'istanziazione delle singole battaglie.
 */
public class GestorePartita {
    private final MostriRepository mostriRepository;
    private final OggettiRepository oggettiRepository;
    private Giocatore giocatore;
    private GestoreBattaglia battagliaCorrente;

    private int roundCorrente = 1;
    private final int MAX_ROUND = 4;

    private final String[] progressioneMostri = {"Goblin", "Orco Furioso", "Golem di Pietra", "Drago di Fuoco"};
    private final String[] progressioneArmi = {"Spada di Legno", "Spada di Ferro", "Spada d'Acciaio", "Spada Leggendaria"};

    public GestorePartita() {
        this.mostriRepository = new MostriRepository();
        this.oggettiRepository = new OggettiRepository();
    }

    public String iniziaNuovaPartita(String nomeGiocatore) {
        this.giocatore = new Giocatore(nomeGiocatore);

        Pozione pozioneBase = (Pozione) oggettiRepository.getOggetto("Pozione Piccola");
        giocatore.getInventario().aggiungiOggetto(pozioneBase);
        giocatore.getInventario().aggiungiOggetto(pozioneBase);
        giocatore.getInventario().aggiungiOggetto(pozioneBase);

        return preparaRound();
    }

    public String avanzaRound() {
        if (roundCorrente < MAX_ROUND) {
            roundCorrente++;
            return preparaRound();
        }
        return "Hai già finito il gioco!";
    }

    private String preparaRound() {
        StringBuilder log = new StringBuilder();

        //GESTIONE ARMI
        giocatore.getInventario().buttaVecchieArmi();
        Arma nuovaArma = (Arma) oggettiRepository.getOggetto(progressioneArmi[roundCorrente - 1]);
        if (nuovaArma != null) {
            giocatore.getInventario().aggiungiOggetto(nuovaArma);
            giocatore.getInventario().equipaggiaArma(nuovaArma);
            log.append("Hai equipaggiato automaticamente: ").append(nuovaArma.getNome()).append("\n");
        }

        //GESTIONE POZIONI
        if (roundCorrente == MAX_ROUND) {
            Pozione pozioneBoss = (Pozione) oggettiRepository.getOggetto("Pozione Grande");
            giocatore.getInventario().aggiungiOggetto(pozioneBoss);
            giocatore.getInventario().aggiungiOggetto(pozioneBoss);
            log.append("Il boss finale si avvicina! Ricevi 2 ").append(pozioneBoss.getNome()).append("!\n");
        } else if (roundCorrente > 1) {
            Pozione pozioneBase = (Pozione) oggettiRepository.getOggetto("Pozione Piccola");
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            giocatore.getInventario().aggiungiOggetto(pozioneBase);
            log.append("Hai trovato 2 Pozioni Piccole!\n");
        }

        //GESTIONE MOSTRO E BATTAGLIA
        Mostro nemico = mostriRepository.getMostro(progressioneMostri[roundCorrente - 1]);
        if (nemico == null) nemico = new Mostro("Mostro Buggato", 10, 1, 0);

        this.battagliaCorrente = new GestoreBattaglia(giocatore, nemico);
        log.append("Un ").append(nemico.getNome()).append(" selvatico appare!\n");

        return log.toString();
    }

    public Giocatore getGiocatore() { return giocatore; }
    public GestoreBattaglia getBattagliaCorrente() { return battagliaCorrente; }
    public int getRoundCorrente() { return roundCorrente; }
    public int getMaxRound() { return MAX_ROUND; }
}
