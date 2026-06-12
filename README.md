📌 Dungeon Arena RPG

Dungeon Arena RPG è un videogioco di ruolo a turni sviluppato in Java con interfaccia grafica in JavaFX. Il gioco offre una modalità "sopravvivenza" a 4 round con difficoltà crescente, caricamento dinamico di mostri e oggetti (tramite JSON e XML) e una "Hall of Fame" salvata su database locale tramite JPA/Hibernate.

🚀 Come eseguire il progetto

Prerequisiti:
- Java 25 (LTS)
- Gradle

Istruzioni:
- git clone <https://github.com/cristianpossanza/Rpg_125673>
- cd Rpg_125673

Build del progetto:
- ./gradlew build

Esecuzione:
- ./gradlew run

🤖 Uso di strumenti di AI: 

Durante lo sviluppo di questo progetto è stato utilizzato Google Gemini / ChatGPT esclusivamente come strumento di supporto tecnico e consultazione.
Nello specifico, l'AI è stata utilizzata per:
- Risolvere errori di configurazione dell'ambiente: chiarire come disattivare la Configuration Cache di Gradle 9 per permettere l'avvio corretto di JavaFX 25.
- Comprendere sintassi specifiche: generare esempi base per la navigazione dei nodi XML tramite XPath, che ho poi studiato e riadattato manualmente per il mio OggettiRepository.
- Suggerimenti sulla UI: scoprire quali classi di JavaFX utilizzare per creare popup interattivi (es. Alert e ChoiceDialog). Le porzioni di codice suggerite sono state modificate, integrate nel pattern MVC e testate personalmente.

L'intera architettura del software (implementazione scheletrica delle entità, divisione tra Controller e Service, pattern MVC), la logica del Game Loop e le scelte di Game Design sono state interamente ideate, strutturate e scritte da me. 

📌 Per una descrizione più dettagliata dell’architettura, delle scelte implementative e dell’uso dell’AI, consultare la Wiki del repository.
