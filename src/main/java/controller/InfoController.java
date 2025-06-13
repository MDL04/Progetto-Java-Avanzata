package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoController {

    @FXML private TextFlow descriptionFlow;

    /**
     * Inizializza la view
     */
    @FXML
    public void initialize() {
        Text t = new Text(
                "Wordageddon è un gioco a quiz basato sull’analisi del testo.\n\n" +
                        "Dopo aver effettuato il login, l’utente affronta una serie di domande\n" +
                        "generate automaticamente a partire dal contenuto di uno o più documenti.\n\n" +
                        "Le domande si dividono in 4 categorie:\n" +
                        "- Frequenza assoluta\n" +
                        "- Confronto tra parole\n" +
                        "- Esclusione di parole non presenti\n" +
                        "- Analisi su documenti specifici\n\n" +
                        "Le parole non significative (stopwords) vengono ignorate.\n" +
                        "Al termine, punteggio e tempo vengono salvati in una sessione di gioco.\n" +
                        "L’obiettivo: rispondere correttamente e scalare la classifica!"
        );
        t.setWrappingWidth(580);
        t.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        descriptionFlow.getChildren().add(t);
    }

    /**
     * Permette di tornare alla homepage
     * @param event
     */
    @FXML
    private void goToHome(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
