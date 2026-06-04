package it.unicam.cs.mpgc.rpg125673.exception;

import it.unicam.cs.mpgc.rpg125673.model.item.Oggetto;

public class OggettoNonPossedutoException extends RuntimeException{
    public OggettoNonPossedutoException(String messaggio) {
        super(messaggio);
    }
}
