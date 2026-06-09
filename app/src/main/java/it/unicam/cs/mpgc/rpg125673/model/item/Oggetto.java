package it.unicam.cs.mpgc.rpg125673.model.item;
/**
 * Interfaccia base per tutti gli elementi raccoglibili nel gioco.
 * Nuovi tipi di oggetti possono essere
 * aggiunti al gioco implementando questa interfaccia senza modificare l'Inventario.
 */
public interface Oggetto {
    String getNome();
    String getDescrizione();
}
