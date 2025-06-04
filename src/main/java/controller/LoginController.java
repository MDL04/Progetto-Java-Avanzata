package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.PasswordUtils;
import actors.User;

public class LoginController {

    @FXML
    private TextField usernameFld, emailFld;

    @FXML
    private PasswordField passwordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameFld.getText();
        String password = passwordFld.getText();
        String hashed = PasswordUtils.hashPassword(password);

        if(userDAO.checkLogin(username, hashed)){
            messageLbl.setText("Login riuscito: " + username);
        } else {
            messageLbl.setText("Credenziali errate");
        }
    }

}
