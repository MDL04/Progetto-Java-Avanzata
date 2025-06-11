package quiz;

import model.WordDocumentMatrix;
import java.util.*;

public class QuizEngineImpl implements QuizEngine {
    private final QuestionFactory factory;
    private final Random rand = new Random();

    public QuizEngineImpl(WordDocumentMatrix matrix) {
        this.factory = new QuestionFactory(matrix);
    }

    @Override
    public List<Question> generaQuiz(int n) {
        List<Question> quiz = new ArrayList<>();
        QuestionType[] types = QuestionType.values();
        int id = 1;

        while (quiz.size() < n) {
            QuestionType type = types[rand.nextInt(types.length)];
            Question q;
            try{
                q = switch (type){
                    case ABSOLUTE_FREQUENCY -> factory.generateAbsoluteFrequencyQuestion(id);
                    case COMPARISON -> factory.generateComparisonQuestion(id);
                    case DOCUMENT_SPECIFIC -> factory.generateDocumentSpecificQuestion(id);
                    case EXCLUSION -> factory.generateExclusionQuestion(id);
                };
                quiz.add(q);
                id++;
            }catch (Exception e){
                //ignoro se la domanda non si genera correttamente
            }
        }
        return quiz;
    }
}
