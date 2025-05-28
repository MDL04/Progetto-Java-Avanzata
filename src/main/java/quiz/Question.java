package quiz;

import java.util.List;

public class Question {
    private Long id;
    private QuestionType type;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private String userAnswer;
    private boolean isCorrect;


    public Question(Long id, QuestionType type, String questionText, List<String> options, String correctAnswer, boolean isCorrect) {
        this.id = id;
        this.type = type;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
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


}
