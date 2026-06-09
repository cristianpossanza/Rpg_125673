package it.unicam.cs.mpgc.rpg125673.model.entity;

/**
 * Rappresenta un'entità nemica all'interno del gioco.
 * Estende PersonaggioBase e aggiunge la logica relativa alla ricompensa
 * in punti esperienza fornita al Giocatore in caso di sconfitta.
 */
public class Mostro extends PersonaggioBase {

    private int ricompensaEsperienza;

    // Costruttore vuoto per GSON e JPA
    public Mostro() {
        super();
    }

    public Mostro(String nome, int puntiVitaMassimi, int attacco, int ricompensaEsperienza) {
        super();
        this.setNome(nome);
        this.setPuntiVitaMassimi(puntiVitaMassimi);
        this.setPuntiVita(puntiVitaMassimi); // La vita attuale parte al massimo
        this.setAttacco(attacco);
        this.ricompensaEsperienza = ricompensaEsperienza;
    }

    public int getRicompensaEsperienza() { return this.ricompensaEsperienza; }
    public void setRicompensaEsperienza(int ricompensaEsperienza) { this.ricompensaEsperienza = ricompensaEsperienza; }
}
