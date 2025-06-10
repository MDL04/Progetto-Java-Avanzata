package quiz;

import model.Document;
import utils.DistractorProvider;
import utils.TextAnalyzer;
import java.util.*;

public class QuizEngine {

    private final TextAnalyzer analyzer;
    private final DistractorProvider provider;
    private final Random rand = new Random();

    public QuizEngine(TextAnalyzer analyzer, DistractorProvider provider) {
        this.analyzer = analyzer;
        this.provider = provider;
    }

}
