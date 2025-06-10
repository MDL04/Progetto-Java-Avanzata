package controller;


import dao.DocumentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Document;

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
    private ComboBox<String> languageComboBox;

    @FXML
    private void initialize() {
        languageComboBox.setValue("Seleziona linguaggio");

        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Italiano".equals(newValue)) {
                languageComboBox.setValue("");
                handleLoadItDoc();
            } else if ("Inglese".equals(newValue)) {
                languageComboBox.setValue("");
                handleLoadEnDoc();
            }
        });
    }




    @FXML
    public void handleLoadItDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di documento (.txt)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            documentList.getItems().add(file.getName());
            messageLabel.setText("Documento caricato: " + file.getName());
            //WDMManager.delete(); // per rigenerare l’indice
        }
    }

    @FXML
    public void handleLoadEnDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di documento (.txt)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            documentList.getItems().add(file.getName());
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