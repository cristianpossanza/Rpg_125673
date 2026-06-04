package it.unicam.cs.mpgc.rpg125673.service;

import it.unicam.cs.mpgc.rpg125673.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;

/**
 * Questa classe si occupa esclusivamente di gestire le regole
 * e i turni di un combattimento tra un Giocatore e un Mostro.
 */
public class GestoreBattaglia {
    private final Giocatore giocatore;
    private final Mostro nemico;
    private boolean battagliaTerminata;

    public GestoreBattaglia(Giocatore giocatore, Mostro nemico) {
        this.giocatore = giocatore;
        this.nemico = nemico;
        this.battagliaTerminata = false;
    }

    /**
     * Esecuzione Sequenziale: Il giocatore attacca. Se il mostro sopravvive, contrattacca.
     * Restituisce il resoconto del turno (da mostrare poi nella GUI).
     */
    public String eseguiTurnoAttacco() {
        if (battagliaTerminata) return "La battaglia è gia finita";

        StringBuilder logTurno = new StringBuilder();

        int dannoGiocatore = giocatore.getAttacco();
        nemico.subisciDanno(dannoGiocatore);
        logTurno.append(giocatore.getNome()).append("attacca e infligge ").append(dannoGiocatore).append(" danni!\n");

        if (!nemico.isVivo()) {
            return gestisciVittoria(logTurno);
        }

        eseguiTurnoNemico(logTurno);
        return logTurno.toString();
    }

    /**
     * Il giocatore usa il suo turno per curarsi. Il mostro ne approfitta per attaccare.
     */
    public String eseguiTurnoPozione(Pozione pozione) {
        if (battagliaTerminata) return "La battaglia è già finita!";

        StringBuilder logTurno = new StringBuilder();

        try {
            giocatore.getInventario().consumaPozione(pozione, giocatore);
            logTurno.append(giocatore.getNome()).append(" beve ").append(pozione.getNome()).append(" e recupera hp\n");
        } catch (IllegalArgumentException e) {
            logTurno.append("Non hau questa pozione nello zaino!\n)");
            return logTurno.toString();
        }

        eseguiTurnoNemico(logTurno);
        return logTurno.toString();
    }

    private void eseguiTurnoNemico(StringBuilder logTurno) {
        int dannoNemico = nemico.getAttacco();
        giocatore.subisciDanno(dannoNemico);
        logTurno.append(nemico.getNome()).append(" contrattacca infliggendo ").append(dannoNemico).append(" danni!\n");

        if (!giocatore.isVivo()) {
            battagliaTerminata = true;
            logTurno.append("\nSei stati sconfitto... GAME OVER.");
        }
    }


    private String gestisciVittoria(StringBuilder logTurno) {
        battagliaTerminata = true;
        logTurno.append("il ").append(nemico.getNome()).append(" è crollato a terra sconfitto!\n");

        int xpGuadagnata = nemico.getRicompensaEsperienza();
        giocatore.aggiungiEsperienza(xpGuadagnata); //potrebbe scattare il levelup
        logTurno.append("Hai guadagnato ").append(xpGuadagnata).append(" Punti Esperienza!");

        return logTurno.toString();
    }

    public boolean isBattagliaTerminata() {
        return battagliaTerminata;
    }
    public Mostro getNemico() {
        return nemico;
    }

}
