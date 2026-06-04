package it.unicam.cs.mpgc.rpg125673.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg125673.model.entity.Mostro;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Questa classe si occupa ESCLUSIVAMENTE di caricare i mostri dal file JSON.
 * Rispetta il Single Responsibility Principle.
 */
public class MostriRepository {
    private final Map<String, Mostro> mappaMostri;

    public MostriRepository() {
        this.mappaMostri = new HashMap<>();
        caricaMostriDaJson();
    }

    private void caricaMostriDaJson() {
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("mostri.json"))
            );
            Type tipoListaMostri = new TypeToken<List<Mostro>>() {
            }.getType();
            List<Mostro> mostriLetti = gson.fromJson(reader, tipoListaMostri);
            for (Mostro m : mostriLetti) {
                this.mappaMostri.put(m.getNome().toLowerCase(), m);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Errore durante il caricamneto dei mostri: " + e.getMessage());
        }
    }

    /**
     * Cerca un mostro nella HashMap in modo istantaneo.
     */
    public Mostro getMostro (String nome){
        Mostro mostro = this.mappaMostri.get(nome.toLowerCase());
            if (mostro != null) {
                return new Mostro(mostro.getNome(), mostro.getPuntiVitaMassimi(), mostro.getAttacco(), mostro.getRicompensaEsperienza());
                }
            return null;
        }
}
