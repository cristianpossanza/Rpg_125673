package it.unicam.cs.mpgc.rpg125673.model.entity;

/**
 * Classe astratta che fornisce un'implementazione scheletrica dell'interfaccia Personaggio.
 * Centralizza la logica di base (salute, danni, cure).
 */
public abstract class PersonaggioBase implements Personaggio {

    private String nome;
    private int puntiVita;
    private int puntiVitaMassimi;
    private int attacco;

    //COSTRUTTORE VUOTO (Fondamentale per JPA/Hibernate)
    public PersonaggioBase() {
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del personaggio non può essere nullo o vuoto.");
        }
        this.nome = nome;
    }

    public void setPuntiVita(int puntiVita) {
        if (puntiVita < 0) {
            this.puntiVita = 0;
        } else if (puntiVita > this.puntiVitaMassimi) {
            this.puntiVita = this.puntiVitaMassimi;
        } else {
            this.puntiVita = puntiVita;
        }
        this.puntiVita = puntiVita;
    }

    public void setPuntiVitaMassimi(int puntiVitaMassimi) {
        if (puntiVitaMassimi <= 0) {
            throw new IllegalArgumentException("I punti vita massimi devono essere maggiori di zero.");
        }
        this.puntiVitaMassimi = puntiVitaMassimi;
    }

    public void setAttacco(int attacco) {
        if (attacco < 0) {
            throw new IllegalArgumentException("L'attacco non può essere negativo.");
        }
        this.attacco = attacco;
    }

    @Override
    public String getNome() { return this.nome; }

    @Override
    public int getPuntiVita() { return this.puntiVita; }

    @Override
    public int getPuntiVitaMassimi() { return this.puntiVitaMassimi; }

    @Override
    public int getAttacco() { return this.attacco; }

    @Override
    public void subisciDanno(int danno) {
        if (danno > 0) {
            this.puntiVita -= danno;
            if (this.puntiVita < 0) {
                this.puntiVita = 0;
            }
        }
    }

    @Override
    public void cura(int punti) {
        if (punti > 0) {
            this.puntiVita += punti;
            if (this.puntiVita > this.puntiVitaMassimi) {
                this.puntiVita = this.puntiVitaMassimi; // Non supera mai il massimo
            }
        }
    }
}
