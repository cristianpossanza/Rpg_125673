package it.unicam.cs.mpgc.rpg125673.model.entity;

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
