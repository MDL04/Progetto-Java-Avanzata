package controller;

import dao.GameSessionDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Document;
import model.GameSession;
import model.User;
import model.WordDocumentMatrix;
import quiz.Question;
import quiz.QuestionFactory;
import quiz.QuizAttempt;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller che gestisce l'interfaccia del quiz, la visualizzazione delle domande,
 * la registrazione delle risposte, e la gestione della fine del quiz, inclusi i risultati.
 */

public class QuizController {

    @FXML private Label questionLabel;
    @FXML private ListView<String> optionsList;
    @FXML private Button nextButton;
    @FXML private Label resultLabel;
    @FXML private Label progressLabel;
    @FXML private Button reviewButton;
    @FXML private Button backButton;

    private User currentUser;
    private UserDAO userDAO;

    private GameSessionDAO gameSessionDAO;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String difficoltà;
    private String timeSpent;

    private QuizAttempt session;
    private int currentIndex = 0;

    /**
     * Inizializza la view del quiz
     * @param matrix
     * @param difficoltà
     * @param documentiMostrati
     * @param lingua
     */
    @FXML
    public void initialize(WordDocumentMatrix matrix, String difficoltà, List<Document> documentiMostrati, String lingua) {
        this.difficoltà = difficoltà;

        int numDomande = switch (difficoltà) {
            case "Facile" -> 5;
            case "Media" -> 8;
            case "Difficile" -> 12;
            default -> 8;
        };

        startTime = LocalDateTime.now();

        try {
            QuestionFactory factory = new QuestionFactory(matrix);
            session = new QuizAttempt(factory, numDomande, lingua);

            resultLabel.setText("Quiz avviato! Ricordi quello che hai letto?");
            mostraDomandaCorrente();

        } catch (Exception e) {
            resultLabel.setText("Errore nella generazione del quiz: " + e.getMessage());
            e.printStackTrace();
        }

        // Imposta l'handler per la chiusura con la X
        nextButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        stage.setOnCloseRequest(event -> {
                            event.consume();
                            handleExitClick();
                        });
                    }
                });
            }
        });
    }

    /**
     * Controlla la selezione della risposta e passa alla domanda successiva
     */
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

    /**
     * Mostra le domande corrente e le opzioni di risposta
     */
    private void mostraDomandaCorrente() {
        Question q = session.getDomande().get(currentIndex);
        questionLabel.setText(q.getQuestionText());
        optionsList.getItems().setAll(q.getOptions());
        optionsList.getSelectionModel().clearSelection();

        progressLabel.setText("Domanda " + (currentIndex + 1) + " di " + session.getDomande().size());

        optionsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            nextButton.setDisable(newVal == null);
        });
        nextButton.setDisable(true);
    }

    /**
     * Termina il quiz, calcola il punteggio e il tempo impiegato
     */
    private void fineQuiz() {
        int punteggio = session.valutaRisposte();
        double percentuale = session.getPercentualeCorrette();

        resultLabel.setText(String.format(
                "Quiz completato!\nPunteggio: %d/%d (%.1f%%)",
                punteggio, session.getDomande().size(), percentuale
        ));

        endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        timeSpent = String.format("%02d:%02d", absSeconds / 60, absSeconds % 60);

        mostraFineQuiz();
        updateDB();

        questionLabel.setText("Complimenti! Quiz terminato!");
        optionsList.getItems().clear();
    }

    /**
     * Mostra i pulsanti utili per rivedere i risultati o tornare
     * alla user dashboard e cela quelli non necessari
     */
    private void mostraFineQuiz() {
        reviewButton.setVisible(true);
        backButton.setVisible(true);
        nextButton.setVisible(false);
        nextButton.setManaged(false);
        optionsList.setDisable(true);
    }

    /**
     * Gestice la visualizzazione della revisione della partita appena conclusa
     * @param event
     */
    @FXML
    private void handleReview(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/postgame_dashboard.fxml"));
            Parent root = loader.load();

            PostGameController postGameController = loader.getController();
            postGameController.updateListView(session);
            postGameController.initData(session, difficoltà , timeSpent);

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

    /**
     * Permette di tornare alla user dashboard
     * @param event
     */
    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user_dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController controller = loader.getController();


            Optional<User> utenteAggiornato = userDAO.selectById(currentUser.getId());
            User nuovoUtente = utenteAggiornato.orElseThrow();
            if (nuovoUtente == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Utente non trovato nel database.");
                alert.show();
                return;
            }
            controller.setUser(nuovoUtente);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOnCloseRequest(null);
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

    /**
     * Permette di aggiornare i dati dell'user corrente nel database
     */
    private void updateDB() {

        userDAO = new UserDAO();
        gameSessionDAO = new GameSessionDAO();

        String difficulty = switch (difficoltà) {
            case "Facile" -> "easy";
            case "Media" -> "medium";
            case "Difficile" -> "hard";
            default -> "unknown";
        };
        userDAO.update(currentUser, difficulty, session.getPunteggio());

        gameSessionDAO.insert(currentUser.getId().intValue(), session.getPunteggio(), difficulty, endTime);
    }

    /**
     * Permette di uscire dalla fase di risposta del gameplay
     */
    @FXML
    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma uscita");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Se esci non potrai riprendere la tua partita.");

        ButtonType esciSenzaSalvare = new ButtonType("Esci senza salvare");
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(esciSenzaSalvare, annulla);

        Stage stage = (Stage) nextButton.getScene().getWindow();
        alert.initOwner(stage);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if (result.get() == esciSenzaSalvare) {
                stage.close();
            } else {
                alert.close();
            }
        }
    }

    /**
     * Setta l'user corrente
     * @param user*/
    public void setUser(User user) {
        this.currentUser = user;
    }
}