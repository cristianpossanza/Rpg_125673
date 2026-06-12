package it.unicam.cs.mpgc.rpg125673.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Rappresenta un record nella Hall of Fame.
 */
@Entity
public class Punteggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeGiocatore;
    private int esperienzaTotale;
    private int roundRaggiunto;
    private boolean vittoria; // true se ha battuto il drago, false se è morto prima

    public Punteggio() {
    }

    public Punteggio(String nomeGiocatore, int esperienzaTotale, int roundRaggiunto, boolean vittoria) {
        this.nomeGiocatore = nomeGiocatore;
        this.esperienzaTotale = esperienzaTotale;
        this.roundRaggiunto = roundRaggiunto;
        this.vittoria = vittoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeGiocatore() { return nomeGiocatore; }
    public void setNomeGiocatore(String nomeGiocatore) { this.nomeGiocatore = nomeGiocatore; }

    public int getEsperienzaTotale() { return esperienzaTotale; }
    public void setEsperienzaTotale(int esperienzaTotale) { this.esperienzaTotale = esperienzaTotale; }

    public int getRoundRaggiunto() { return roundRaggiunto; }
    public void setRoundRaggiunto(int roundRaggiunto) { this.roundRaggiunto = roundRaggiunto; }

    public boolean isVittoria() { return vittoria; }
    public void setVittoria(boolean vittoria) { this.vittoria = vittoria; }
}
