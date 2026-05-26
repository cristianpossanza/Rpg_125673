package it.unicam.cs.mpgc.rpg125673.model.entity;
/**
 * Interfaccia base per tutte le entità viventi del gioco.
 * Definisce le azioni e le statistiche fondamentali.
 */
public interface Personaggio {
    String getNome();
    int getPuntiVita();          // Punti vita attuali
    int getPuntiVitaMassimi();   // Punti vita massimi
    int getAttacco();            // Potenza di attacco

    void subisciDanno(int danno);

    default boolean isVivo(){
        return getPuntiVita() > 0;
    }
}
