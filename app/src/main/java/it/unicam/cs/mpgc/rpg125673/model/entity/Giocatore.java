package it.unicam.cs.mpgc.rpg125673.model.entity;
/**
 * Rappresenta l'eroe controllato dall'utente.
 * Estende PersonaggioBase ereditandone le statistiche e utilizza la Composizione
 * per delegare la gestione degli oggetti alla classe Inventario (Principio SRP).
 * Sfrutta il Polimorfismo facendo l'override del calcolo dell'attacco.
 */
public class Giocatore extends PersonaggioBase {

    private int esperienza;
    private int livello;
    private final Inventario inventario;

    // Costruttore vuoto per futuri salvataggi su Database
    public Giocatore() {
        super();
        this.inventario = new Inventario();
    }

    // Costruttore che useremo noi nel gioco
    public Giocatore(String nome) {
        this(); //chiama il costruttore vuoto qui sopra (che fa gia super() e crea l'inventario!)

        //Impostiamo le statistiche base del giocatore tramite i setter
        this.setNome(nome);
        this.setPuntiVitaMassimi(100);
        this.setPuntiVita(100);
        this.setAttacco(15);
        this.esperienza = 0;
        this.livello = 1;
    }

    //chiedo all'inventario il bonus dell'arma e lo somma alla sua forza base
    @Override
    public int getAttacco() {
        return super.getAttacco() + this.inventario.getBonusAttaccoArma();
    }

    //Metodo delegato dall'inventario
    public Inventario getInventario() {
        return this.inventario;
    }


    public void aggiungiEsperienza(int quantita) {
        if (quantita > 0) {
            this.esperienza += quantita;
            controllaAumentoLivello();
        }
    }

    private void controllaAumentoLivello() {
        if (this.esperienza >= (this.livello * 100)) {
            this.livello++;
            System.out.println("AUMENTO DI LIVELLO! " + getNome() + "ha raggiunto il livello " + livello + "!");
        }
    }

    public int getLivello() { return this.livello; }
    public int getEsperienza() { return this.esperienza; }
    public void setLivello(int livello) { this.livello = livello; }
    public void setEsperienza(int esperienza) { this.esperienza = esperienza; }
}
