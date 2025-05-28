package quiz;

public enum QuestionType {
    ABSOLUTE_FREQUENCY("Frequenza Assoluta",
            "Quante volte compare la parola '%s' %s?"),

    COMPARISON("Confronto",
            "Quale tra le seguenti parole è la più frequente %s?"),

    DOCUMENT_SPECIFIC("Documento Specifico",
            "In quale documento compare più spesso la parola '%s'?"),

    EXCLUSION("Esclusione",
            "Quale di queste parole NON compare mai %s?");

    private final String displayName;
    private final String questionTemplate;

    QuestionType(String displayName, String questionTemplate) {
        this.displayName = displayName;
        this.questionTemplate = questionTemplate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getQuestionTemplate() {
        return questionTemplate;
    }
}
