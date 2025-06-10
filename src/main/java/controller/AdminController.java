package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class AdminController {
    @FXML
    private ListView<String> documentList;

    @FXML
    private ListView<String> stopwordList;

    @FXML
    private Label messageLabel;


    @FXML
    public void handleLoadDocumentFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di documento (.txt)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            documentList.getItems().add(file.getAbsolutePath());
            messageLabel.setText("Documento caricato: " + file.getName());
            //WDMManager.delete(); // per rigenerare l’indice
        }
    }

    @FXML
    public void handleLoadStopwordFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di stopwords");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                List<String> words = Files.readAllLines(file.toPath());
                stopwordList.getItems().addAll(words);
                messageLabel.setText("Stopwords caricate da: " + file.getName());
                //WDMManager.delete();
            } catch (IOException e) {
                messageLabel.setText("Errore nella lettura del file di stopwords.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleRemoveSelectedDocument() {
        String selected = documentList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            documentList.getItems().remove(selected);
            messageLabel.setText("Documento rimosso.");
            //WDMManager.delete();
        }
    }

    @FXML
    public void handleRemoveSelectedStopword() {
        String selected = stopwordList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            stopwordList.getItems().remove(selected);
            messageLabel.setText("Stopword rimossa.");
            //WDMManager.delete();
        }
    }

    private Stage getStage() {
        return (Stage) messageLabel.getScene().getWindow();
    }
}