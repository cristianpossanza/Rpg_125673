package it.unicam.cs.mpgc.rpg125673.exception;

/**
 * Eccezione personalizzata lanciata a runtime quando un'entità tenta di
 * consumare o equipaggiare un oggetto non presente nel proprio inventario.
 */
public class OggettoNonPossedutoException extends RuntimeException{
    public OggettoNonPossedutoException(String messaggio) {
        super(messaggio);
    }
}
