package controller;

import dao.DocumentDAO;
import dao.StopwordDAO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Document;
import model.Stopword;
import model.WordDocumentMatrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReadingPhaseController {

    @FXML private Label timerLabel;
    @FXML private TextArea documentArea;
    @FXML private Label documentTitleLabel;
    @FXML private Button nextDocumentButton;
    @FXML private Label progressLabel;

    private List<Document> documentiDaMostrare;
    private int documentoCorrente = 0;
    private Timeline timer;
    private int tempoRimanente;
    private String difficolta;
    private String lingua;
    private int tempoPerDocumento;
    private int maxDocumenti;
    private int maxParolePerDocumento;

    public void initializeReadingPhase(String lingua, String difficolta) {
        this.lingua = lingua;
        this.difficolta = difficolta;

        switch (difficolta) {
            case "Facile" -> {
                this.maxDocumenti = 2;
                this.maxParolePerDocumento = 100;
                this.tempoPerDocumento = 90;
            }
            case "Media" -> {
                this.maxDocumenti = 2;
                this.maxParolePerDocumento = 200;
                this.tempoPerDocumento = 60;
            }
            case "Difficile" -> {
                this.maxDocumenti = 3;
                this.maxParolePerDocumento = 300;
                this.tempoPerDocumento = 120;
            }
            default -> {
                this.maxDocumenti = 2;
                this.maxParolePerDocumento = 200;
                this.tempoPerDocumento = 60;
            }
        }

        caricaDocumenti();
        mostraDocumentoCorrente();
    }

    private void caricaDocumenti() {
        List<Document> tuttiDocumenti = DocumentDAO.selectDocumentByLanguage(lingua);

        documentiDaMostrare = tuttiDocumenti.stream()
                .filter(doc -> contaParole(doc.getContent()) <= maxParolePerDocumento)
                .limit(maxDocumenti)
                .toList();

        if (documentiDaMostrare.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Nessun documento disponibile");
            alert.setContentText("Non ci sono documenti per la lingua e difficoltà selezionate");
            alert.showAndWait();
            return;
        }
    }

    private int contaParole(String testo) {
        return testo.trim().split("\\s+").length;
    }

    private void mostraDocumentoCorrente() {
        if (documentoCorrente >= documentiDaMostrare.size()) {
            avviaQuiz();
            return;
        }

        Document doc = documentiDaMostrare.get(documentoCorrente);
        documentTitleLabel.setText("Documento " + (documentoCorrente + 1) + ": " + doc.getTitle());
        documentArea.setText(doc.getContent());
        progressLabel.setText("Documento " + (documentoCorrente + 1) + " di " + documentiDaMostrare.size());

        tempoRimanente = tempoPerDocumento;
        aggiornaTimer();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoRimanente--;
            aggiornaTimer();

            if (tempoRimanente <= 0) {
                timer.stop();
                prossimoDocumento();
            }
        }));
        timer.setCycleCount(tempoPerDocumento);
        timer.play();
    }

    private void aggiornaTimer() {
        int minuti = tempoRimanente / 60;
        int secondi = tempoRimanente % 60;
        timerLabel.setText(String.format("Tempo rimanente: %02d:%02d", minuti, secondi));

        if (tempoRimanente <= 10) {
            timerLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            timerLabel.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    private void prossimoDocumento() {
        if (timer != null) {
            timer.stop();
        }
        documentoCorrente++;
        mostraDocumentoCorrente();
    }

    private void avviaQuiz() {
        try {
            WordDocumentMatrix matrix = creaMatriceDaiDocumenti();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/quiz.fxml"));
            Parent root = loader.load();

            QuizController quizController = loader.getController();
            // Passa la lingua!
            quizController.initializeQuiz(matrix, difficolta, documentiDaMostrare, lingua);

            Stage stage = (Stage) documentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz - " + difficolta);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Impossibile avviare il quiz");
            alert.showAndWait();
        }
    }

    private WordDocumentMatrix creaMatriceDaiDocumenti() {
        WordDocumentMatrix matrix = new WordDocumentMatrix();

        List<Stopword> stopwordsLingua = StopwordDAO.selectSWByLanguage(lingua);
        Set<String> tutteLeStopwords = stopwordsLingua.stream()
                .flatMap(sw -> Arrays.stream(sw.getContent().split("\\s+")))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        matrix.setStopwords(tutteLeStopwords);

        for (Document doc : documentiDaMostrare) {
            matrix.aggiungiDocumento(doc.getTitle(), doc.getContent());
        }

        return matrix;
    }
}