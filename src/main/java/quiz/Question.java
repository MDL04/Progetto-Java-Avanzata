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


    public Question(int id, QuestionType type, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.type = type;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public QuestionType getType() {
        return type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
