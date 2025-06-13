package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import quiz.Question;
import quiz.QuizAttempt;

public class PostGameController{

    @FXML
    private TextField difficulty;

    @FXML
    private TextField timeSpent;

    @FXML
    private TextField userScore;

    @FXML
    private ListView listView;

    /**
     * Inizializza i dati della dashboard post-game
     * @param session
     * @param difficolta
     * @param tempo
     */
    public void initData(QuizAttempt session, String difficolta, String tempo) {
        userScore.setText("Punteggio: " + session.valutaRisposte() + "/" + session.getDomande().size());
        difficulty.setText("Difficoltà: " + difficolta);
        timeSpent.setText("Tempo utilizzato: " + tempo);
    }

    /**
     * Aggiorna la ListView con i parametri della sessione appena terminata
     * @param session
     */
    public void updateListView(QuizAttempt session) {
        for(Question question : session.getDomande()){
            StringBuilder sb = new StringBuilder();
            sb.append("Domanda ").append(question.getId()).append(": ").append(question.getQuestionText()).append("\n");
            sb.append("Risposta corretta: ").append(question.getCorrectAnswer()).append("\n");
            if (session.getRisposteUtente().containsKey(question.getId())) {
                sb.append("La tua risposta: ").append(session.getRisposteUtente().get(question.getId())).append("\n");
            } else {
                sb.append("Non hai risposto a questa domanda.\n");
            }
            listView.getItems().add(sb.toString());
        }
    }
}
