package it.unicam.cs.mpgc.rpg125673.repository;

import it.unicam.cs.mpgc.rpg125673.model.item.Arma;
import it.unicam.cs.mpgc.rpg125673.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125673.model.item.Pozione;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OggettiRepository {
    private final Set<Oggetto> catalogoOggetti;

    public OggettiRepository() {
        this.catalogoOggetti = new HashSet<>();
        caricaOggettiDaXml();
    }

    private void caricaOggettiDaXml() {
        try {
            //aprima il file XML dalla cartella resources
            InputStream is =
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("oggetti.xml"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);

            //inizializzo XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            //estrazione armi
            //l'esperessione "//armi/arma" dice a xpath di trovare tutti i tag <arma> dentro <armi>
            NodeList armiNodes = (NodeList) xPath.compile("//armi/arma").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < armiNodes.getLength(); i++) {
                Element elemento = (Element) armiNodes.item(i);
                String nome = elemento.getElementsByTagName("nome").item(0).getTextContent();
                String desc = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
                int bonus = Integer.parseInt(elemento.getElementsByTagName("bonusAttacco").item(0).getTextContent());

                // Aggiungiamo l'arma all'HashSet
                catalogoOggetti.add(new Arma(nome, desc, bonus));
            }

            // --- ESTRAZIONE POZIONI ---
            // Stessa cosa, ma cerchiamo "//pozioni/pozione"
            NodeList pozioniNodes = (NodeList) xPath.compile("//pozioni/pozione").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < pozioniNodes.getLength(); i++) {
                Element elemento = (Element) pozioniNodes.item(i);
                String nome = elemento.getElementsByTagName("nome").item(0).getTextContent();
                String desc = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
                int cura = Integer.parseInt(elemento.getElementsByTagName("puntiCura").item(0).getTextContent());

                // Aggiungiamo la pozione all'HashSet
                catalogoOggetti.add(new Pozione(nome, desc, cura));
            }

        } catch (Exception e) {
            System.out.println("Errore durante la lettura dell'XML: " + e.getMessage());
        }
    }

    /**
     * Usa gli STREAM (spiegati a lezione) per filtrare l'HashSet e trovare l'oggetto!
     */
    public Oggetto getOggetto(String nome) {
        return this.catalogoOggetti.stream()
                // Filtra: tieni solo l'oggetto il cui nome è uguale a quello cercato (ignorando maiuscole/minuscole)
                .filter(oggetto -> oggetto.getNome().equalsIgnoreCase(nome))
                // Prendi il primo che trovi
                .findFirst()
                // Se non trovi nulla, restituisci null
                .orElse(null);
    }

//stavo pensando di dividere questa classe in piu classi, attualmente fa due cose diverse:
//Analizza e decodifica un file XML (Logica di parsing/infrastruttura).
//Fa da "magazzino" e fornisce gli oggetti quando richiesti (Logica di repository/dominio).

//Andando a cercare su internet il compito principale di un Repository è quello di fare da magazzino e fornire i dati (quindi avere il metodo getOggetto).
//Sono giunto alla conclusione che l'errore non è avere getOggetto nel Repository, ma è avere la logica di lettura dell'XML dentro il Repository
//proverei a dividere questa classe in due.
//
//Classe 1 (LettoreXmlOggetti): Si occupa solo di aprire il file XML, leggerlo con XPath e restituire un HashSet di oggetti. Fine.
//Classe 2 (OggettiRepository): Prende l'HashSet già pronto, lo custodisce e offre il metodo getOggetto (tramite Stream) a chi ne ha bisogno nel gioco.


}
