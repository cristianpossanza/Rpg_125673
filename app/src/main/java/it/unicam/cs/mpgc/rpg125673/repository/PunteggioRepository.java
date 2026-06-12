package it.unicam.cs.mpgc.rpg125673.repository;

import it.unicam.cs.mpgc.rpg125673.model.entity.Punteggio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 * Si occupa di salvare e leggere i punteggi dal database H2.
 */
public class PunteggioRepository {
    private final EntityManagerFactory entityManagerFactory;

    public PunteggioRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("RpgDatabase");
    }

    public void salvaPunteggio(Punteggio punteggio) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(punteggio);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Errore durante il salvataggio nel DB: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    /**
     * Legge i migliori 5 punteggi dal database
     */
    public List<Punteggio> getMiglioriPunteggi() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Punteggio p ORDER BY p.esperienzaTotale DESC", Punteggio.class)
                    .setMaxResults(5)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}
