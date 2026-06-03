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

        //uso StringBuiler per unire le stringhe in modo efficente
        StringBuilder logTurno = new StringBuilder();

        //turno del giocatore
        int dannoGiocatore = giocatore.getAttacco();
        nemico.subisciDanno(dannoGiocatore);
        logTurno.append(giocatore.getNome()).append("attacca e infligge ").append(dannoGiocatore).append(" danni!\n");

        //controllo se il mostro è morto
        if (!nemico.isVivo()) {
            return gestisciVittoria(logTurno);
        }

        //Turno del nemico
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
            //delego all'inventario il compito di consumare la pozione
            giocatore.getInventario().consumaPozione(pozione, giocatore);
            logTurno.append(giocatore.getNome()).append(" beve ").append(pozione.getNome()).append(" e recupera hp\n");
        } catch (IllegalArgumentException e) {
            logTurno.append("Non hau questa pozione nello zaino!\n)");
            return logTurno.toString(); //il turno si annulla se non hai la pozione
        }

        //il nemico attacca mentre ti curi
        eseguiTurnoNemico(logTurno);

        return logTurno.toString();
    }

    private void eseguiTurnoNemico(StringBuilder logTurno) {
    }

    //perche questi 2 metodi li metto privati
    //va bene la gestione di tutti questi turni all'interno di una sola classe, oppure è meglio dividere
    //in piu classi
    private String gestisciVittoria() {
        return "";
    }

    // Getter utili per l'interfaccia grafica
    public boolean isBattagliaTerminata() {
        return battagliaTerminata;
    }

    public Mostro getNemico() {
        return nemico;
    }

}
