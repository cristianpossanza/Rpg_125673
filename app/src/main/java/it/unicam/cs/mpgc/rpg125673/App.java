package it.unicam.cs.mpgc.rpg125673;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

// Estendendo Application, diciamo a Java che questa è un'app JavaFX
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Diciamo a Java dove trovare il file grafico (nella cartella resources)
        URL fxmlLocation = getClass().getResource("/fxml/main.fxml");
        if (fxmlLocation == null) {
            throw new IllegalStateException("Impossibile trovare il file main.fxml. Controlla la cartella resources!");
        }

        // 2. Carichiamo il file FXML
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // 3. Creiamo la scena (la finestra) grande 600x400 pixel
        Scene scene = new Scene(root, 600, 400);

        // 4. Impostiamo il titolo e mostriamo la finestra
        primaryStage.setTitle("Dungeon Arena RPG");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Questo comando fa partire il motore grafico di JavaFX
        launch(args);
    }
}

