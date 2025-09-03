package controller;

import dao.UserDAO;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import utils.PasswordUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller per la schermata di registrazione dell'utente. Gestisce la registrazione di un nuovo utente,
 * la validazione dei campi di input e la navigazione tra le schermate.
 */


public class RegisterController {

    @FXML
    private TextField usernameFld, emailFld;

    @FXML
    private PasswordField passwordFld, confirmPasswordFld;

    @FXML
    private Label messageLbl;

    @FXML
    private Button regBtn;

    private final UserDAO userDAO = new UserDAO();
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final PauseTransition typingDelay = new PauseTransition(Duration.millis(300));

    @FXML
    private void initialize() {
        regBtn.disableProperty().bind(formValid.not());

        ChangeListener<String> onType = (obsValue, oldValue, newValue) -> {
            typingDelay.stop();
            typingDelay.setOnFinished(e -> {
                Optional<String> err = validateFields(
                        usernameFld.getText(),
                        emailFld.getText(),
                        passwordFld.getText(),
                        confirmPasswordFld.getText()
                );
                if (err.isPresent()) {
                    formValid.set(false);
                    messageLbl.setText(err.get());
                    messageLbl.setStyle("-fx-text-fill: red;");
                } else {
                    formValid.set(true);
                    messageLbl.setText("Dati validi");
                    messageLbl.setStyle("-fx-text-fill: green;");
                }
            });
            typingDelay.playFromStart();
        };

        usernameFld.textProperty().addListener(onType);
        emailFld.textProperty().addListener(onType);
        passwordFld.textProperty().addListener(onType);
        confirmPasswordFld.textProperty().addListener(onType);
    }

    private Optional<String> validateFields(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return Optional.of("Completa tutti i campi!");
        }
        if (!email.contains("@") || !email.substring(email.indexOf("@")).contains(".")) {
            return Optional.of("Email non valida: dominio non riconosciuto!");
        }

        if(password.length() < 7){
            return Optional.of("La password deve contenere almeno 7 caratteri!");
        }

        if(!password.equals(confirmPassword)) {
            return Optional.of("Le password non coincidono!");
        }

        try {
            if (userDAO.selectByUsername(username).isPresent()) {
                return Optional.of("Username non disponibile!");
            }

            if (userDAO.selectByEmail(email).isPresent()) {
                return Optional.of("Email non disponibile! Potresti già avere un account.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    /**
     * Gestisce la registrazione di un nuovo utente e verifica
     * che tutti i dati inseriti siano validi
     */
    @FXML
    private void handleRegister() {
        String username = usernameFld.getText();
        String email = emailFld.getText();
        String password = passwordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        Optional<String> err = validateFields(username, email, password, confirmPassword);
        if (err.isPresent()) {
            showError(err.get());
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setAdmin(false);
        user.setUrlAvatar("/images/avatar/default_avatar.png");

        if(userDAO.insert(user)){
            showSuccess("Registrazione completata!");
            goToLogin();
        }else{
            showError("Errore durante la registrazione!");
        }
    }

    /**
     * Permette di andare alla sezione Login
     */
    @FXML
    void goToLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /**
     * Permette di andare alla sezione home
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

    /**
     * Gestisce la visualizzazione di errori
     * @param message
     */
    private void showError(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: red;");
    }

    /**
     * Gestisce la visualizzazione di successo
     * @param message
     */
    private void showSuccess(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: green;");
    }

}
