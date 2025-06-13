package quiz;

import java.util.List;

public class Question {
    private int id;
    private QuestionType type;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private String userAnswer;
    private boolean isCorrect;

    /**
     * Crea un'istanza di quest'oggetto
     * @param id
     * @param type
     * @param questionText
     * @param options
     * @param correctAnswer
     */
    public Question(int id, QuestionType type, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.type = type;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    /**@return id*/
    public int getId() {
        return id;
    }

    /**@return tipo*/
    public QuestionType getType() {
        return type;
    }

    /**@return testo della domanda*/
    public String getQuestionText() {
        return questionText;
    }

    /**@return opzioni tra cui scegliere*/
    public List<String> getOptions() {
        return options;
    }

    /**@return risposta corretta*/
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**@return risposta dell'user*/
    public String getUserAnswer() {
        return userAnswer;
    }

    /**@return isCorrect*/
    public boolean isCorrect() {
        return isCorrect;
    }

    /**@param correct*/
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    /**@param userAnswer*/
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
