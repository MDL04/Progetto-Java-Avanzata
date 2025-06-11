package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quiz.GameSession;
import quiz.Question;
import quiz.QuestionFactory;
import model.WordDocumentMatrix;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class GameController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label questionLabel;

    @FXML
    private ListView<String> optionsList;

    @FXML
    private Button nextButton;

    @FXML
    private Label resultLabel;

    private GameSession session;
    private int currentIndex = 0;
    private WordDocumentMatrix matrix;
    private QuestionFactory factory;

    public void initialize() {
        languageComboBox.getItems().addAll("Italiano", "Inglese");
        difficultyComboBox.getItems().addAll("Facile", "Media", "Difficile");
        nextButton.setDisable(true);

        matrix = new WordDocumentMatrix();
        factory = new QuestionFactory(matrix);
    }

    @FXML
    public void handleStartQuiz(){
        String lingua = languageComboBox.getValue();
        String difficoltà = difficultyComboBox.getValue();

        if (lingua == null || difficoltà == null) {
            resultLabel.setText("Seleziona lingua e difficoltà");
            return;
        }

        int numDomande = switch (difficoltà){
            case "Facile" -> 5;
            case "Media" -> 8;
            case "Difficile" -> 10;
            default -> 8;
        };

        session = new GameSession(1, factory, numDomande);
        currentIndex = 0;
        resultLabel.setText("");
        nextButton.setDisable(false);
        mostraDomandaCorrente();
    }

    @FXML
    public void handleNextQuestion(){
        String rispostaSelezionata = optionsList.getSelectionModel().getSelectedItem();

        if (rispostaSelezionata == null) {
            resultLabel.setText("Seleziona una risposta");
            return;
        }
        Question q = session.getDomande().get(currentIndex);
        session.registraRisposta(q.getId(), rispostaSelezionata);
        currentIndex++;

        if(currentIndex >= session.getDomande().size()){
            fineQuiz();
        }else{
            mostraDomandaCorrente();
        }
    }

    @FXML
    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma uscita");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Scegli un'opzione:");

        ButtonType salva = new ButtonType("Salva e Esci");
        ButtonType esciSenzaSalvare = new ButtonType("Esci senza salvare");
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(salva, esciSenzaSalvare, annulla);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get() == salva){
                //Salvo nel database. Per farlo si può creare un metodo in gamesessionDAO. Devo quittare. -Franzab
            }
        }
    }

    private void mostraDomandaCorrente() {
        Question q = session.getDomande().get(currentIndex);
        questionLabel.setText("Domanda " + (currentIndex + 1) + ": "+ q.getQuestionText());
        optionsList.getItems().setAll(q.getOptions());
        optionsList.getSelectionModel().clearSelection();
    }

    private void fineQuiz() {
        int punteggio = session.valutaRisposte();
        resultLabel.setText("Quiz completato! Punteggio: " + punteggio + "/" + session.getDomande().size());
        nextButton.setDisable(true);
        questionLabel.setText("Fine quiz");
        optionsList.getItems().clear();
    }

    @FXML
    public void goToPostGameDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/postgamedashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Riepilogo Partita");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel caricamento della dashboard");
            alert.show();
        }

    }

}
