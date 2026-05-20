package it.unicam.cs.mpgc.rpg125673.model.entity;

public abstract class PersonaggioBase implements Personaggio {

    // Tolto il "final", ora le variabili possono essere modificate dai Setter
    private String nome;
    private int puntiVita;
    private int puntiVitaMassimi;
    private int attacco;

    // 1. ECCO IL COSTRUTTORE VUOTO RICHIESTO DAL TUTOR (Fondamentale per JPA/Hibernate)
    public PersonaggioBase() {
    }

    // 2. Aggiungiamo i metodi Setter per impostare i valori
    public void setNome(String nome) { this.nome = nome; }
    public void setPuntiVita(int puntiVita) { this.puntiVita = puntiVita; }
    public void setPuntiVitaMassimi(int puntiVitaMassimi) { this.puntiVitaMassimi = puntiVitaMassimi; }
    public void setAttacco(int attacco) { this.attacco = attacco; }

    // --- I Getter rimangono uguali ---
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

    // NOTA: isVivo() è stato rimosso perché ora ci pensa l'interfaccia!
}
