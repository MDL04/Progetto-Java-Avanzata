package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Document;
import model.User;
import model.WordDocumentMatrix;
import quiz.GameSession;
import quiz.Question;
import quiz.QuestionFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class QuizController {

    @FXML private Label questionLabel;
    @FXML private ListView<String> optionsList;
    @FXML private Button nextButton;
    @FXML private Label resultLabel;
    @FXML private Label progressLabel;
    @FXML private Button reviewButton;
    @FXML private Button backButton;

    private User currentUser;

    private LocalDateTime startTime;
    private String difficoltà;
    private String timeSpent;


    private GameSession session;
    private int currentIndex = 0;
    private List<Document> documentiMostrati;

    public void initializeQuiz(WordDocumentMatrix matrix, String difficoltà, List<Document> documentiMostrati) {
        this.documentiMostrati = documentiMostrati;

        this.difficoltà = difficoltà;

        // ✅ Numero domande basato sulla difficoltà
        int numDomande = switch (difficoltà) {
            case "Facile" -> 5;
            case "Media" -> 8;
            case "Difficile" -> 12;
            default -> 8;
        };

        startTime = LocalDateTime.now();


        try {
            QuestionFactory factory = new QuestionFactory(matrix);
            session = new GameSession(1, factory, numDomande);

            resultLabel.setText("Quiz avviato! Ricordi quello che hai letto?");
            mostraDomandaCorrente();

        } catch (Exception e) {
            resultLabel.setText("Errore nella generazione del quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextQuestion() {
        String rispostaSelezionata = optionsList.getSelectionModel().getSelectedItem();

        if (rispostaSelezionata == null) {
            resultLabel.setText("Seleziona una risposta!");
            return;
        }

        Question q = session.getDomande().get(currentIndex);
        session.registraRisposta(q.getId(), rispostaSelezionata);
        currentIndex++;

        if(currentIndex >= session.getDomande().size()){
            fineQuiz();
        } else {
            mostraDomandaCorrente();
        }
    }

    private void mostraDomandaCorrente() {
        Question q = session.getDomande().get(currentIndex);
        questionLabel.setText(q.getQuestionText());
        optionsList.getItems().setAll(q.getOptions());
        optionsList.getSelectionModel().clearSelection();

        progressLabel.setText("Domanda " + (currentIndex + 1) + " di " + session.getDomande().size());

        optionsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            nextButton.setDisable(newVal == null);
        });
        nextButton.setDisable(true); // Disabilita all'inizio della domanda
    }

    private void fineQuiz() {
        int punteggio = session.valutaRisposte();
        double percentuale = session.getPercentualeCorrette();

        resultLabel.setText(String.format(
                "Quiz completato!\nPunteggio: %d/%d (%.1f%%)",
                punteggio, session.getDomande().size(), percentuale
        ));

        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        timeSpent = String.format("%02d:%02d", absSeconds / 60, absSeconds % 60);

        mostraFineQuiz();
        questionLabel.setText("Complimenti! Quiz terminato!");
        optionsList.getItems().clear();

        // TODO: Salva punteggio nel database
    }

    private void mostraFineQuiz() {
        reviewButton.setVisible(true);
        backButton.setVisible(true);
        nextButton.setVisible(false);
        nextButton.setManaged(false);
        optionsList.setDisable(true);
        // Mostra punteggio o messaggio finale
    }

    @FXML
    private void handleReview(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/postgame_dashboard.fxml"));
            Parent root = loader.load();

            PostGameController postGameController = loader.getController();
            postGameController.updateListView(session);
            postGameController.initData(session, difficoltà , timeSpent);

            // Nuovo Stage per la revisione
            Stage reviewStage = new Stage();
            reviewStage.setScene(new Scene(root));
            reviewStage.setMinWidth(600);
            reviewStage.setMinHeight(527);

            reviewStage.setTitle("Revisione Partita");
            reviewStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user_dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController controller = loader.getController();
            if (currentUser != null) {
                controller.setUser(currentUser);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Area Utente");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel caricamento della dashboard utente");
            alert.show();
        }
    }
}