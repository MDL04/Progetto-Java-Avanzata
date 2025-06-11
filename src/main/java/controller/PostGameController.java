package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import quiz.GameSession;

import java.util.Date;

public class PostGameController{

    @FXML
    private TextField difficulty;

    @FXML
    private TextField timeSpent;

    @FXML
    private TextField userScore;

    @FXML
    private ListView listView;

    public void initData(GameSession session, String difficolta, Date tempo) {
        userScore.setText("Punteggio: " + session.valutaRisposte() + "/" + session.getDomande().size());
        difficulty.setText("Difficoltà: " + difficolta);
        timeSpent.setText("Tempo utilizzato: " + tempo);
    }
}
