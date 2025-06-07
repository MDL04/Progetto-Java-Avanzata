package controller;

import javafx.fxml.FXML;

public class HomepageController {
    private RegisterController rg;


    @FXML
    private void goToLogin() {
        rg.goToLogin();
    }

}
