package it.unicam.cs.mpgc.rpg125673.model.entity;

import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private final Map<Oggetto, Integer> zaino;
    private Arma armaEquipaggiata;

    public Inventario() {
        this.zaino = new HashMap<>();
        this.armaEquipaggiata = null;
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        if (oggetto != null) {
            this.zaino.put(oggetto, this.zaino.getOrDefault(oggetto, 0) + 1);
            System.out.println("Aggiunto allo zaino: " + oggetto.getNome() + " (Totale: " + this.zaino.get(oggetto) + ")");
        }
    }

    public void equipaggiaArma(Arma nuovaArma) {
        if (possiedeOggetto(nuovaArma)) {
            if (this.armaEquipaggiata != null) {
                aggiungiOggetto(this.armaEquipaggiata);
            }
            this.armaEquipaggiata = nuovaArma;
            rimuoviOggetto(nuovaArma);
            System.out.println("Hai equipaggiato: " + nuovaArma.getNome());
        } else {
            throw new IllegalArgumentException("Non possiedi quest'arma!");
        }
    }
        //uso l'interfaccia personaggio base
    public void consumaPozione(Pozione pozione, Personaggio bersaglio) {
        if (possiedeOggetto(pozione)) {
            bersaglio.cura(pozione.getPuntiCura());;
            rimuoviOggetto(pozione);
            System.out.println(bersaglio.getNome() + " si cura di " + pozione.getPuntiCura() + " HP.");
        } else {
            throw new IllegalArgumentException("Non possiedi questa pozione!");
        }
    }

    private boolean possiedeOggetto(Oggetto oggetto) {
        return this.zaino.containsKey(oggetto) && this.zaino.get(oggetto) > 0;
    }

    private void rimuoviOggetto(Oggetto oggetto) {
        if (possiedeOggetto(oggetto)) {
            int quantitaRimasta = this.zaino.get(oggetto) - 1;
            if (quantitaRimasta == 0) {
                this.zaino.remove(oggetto);
            } else {
                this.zaino.put(oggetto, quantitaRimasta);
            }
        }
    }

    public int getBonusAttaccoArma() {
        return (this.armaEquipaggiata != null) ? this.armaEquipaggiata.getBonusAttacco() : 0;
    }

    public Map<Oggetto, Integer> getZaino() {
        return this.zaino;
    }
}

