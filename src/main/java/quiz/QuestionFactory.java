package quiz;

import model.WordDocumentMatrix;

import java.util.*;

public class QuestionFactory {
    private final WordDocumentMatrix matrix;
    private final Random random = new Random();

    public QuestionFactory(WordDocumentMatrix matrix) {
        this.matrix = matrix;
    }

    public Question generateQuestion(int id, QuestionType type) {
        return switch (type) {
            case ABSOLUTE_FREQUENCY -> generateAbsoluteFrequencyQuestion(id);
            case COMPARISON -> generateComparisonQuestion(id);
            case DOCUMENT_SPECIFIC -> generateDocumentSpecificQuestion(id);
            case EXCLUSION -> generateExclusionQuestion(id);
        };
    }

    public Question generateAbsoluteFrequencyQuestion(int id) {
        String doc = getRandomDocument();
        String parola = getRandomWord();
        int freq = matrix.getFrequenza(doc, parola);

        List<String> templates = QuestionType.ABSOLUTE_FREQUENCY.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        List<String> opzioni = new ArrayList<>();
        String risposta;

        switch (template) {
            case "Quante volte compare la parola '%s' nel documento '%s'?" -> {
                domanda = String.format(template, parola, doc);
                risposta = String.valueOf(freq);
                opzioni = generaDistrattoriNumerici(freq);
            }
            case "La parola '%s' compare nel documento '%s':" -> {
                domanda = String.format(template, parola, doc);
                if (freq == 0) risposta = "0-2 volte";
                else if (freq <= 2) risposta = "0-2 volte";
                else if (freq <= 5) risposta = "3-5 volte";
                else if (freq <= 9) risposta = "6-9 volte";
                else risposta = ">=10 volte";
                opzioni = List.of("0-2 volte", "3-5 volte", "6-9 volte", ">=10 volte");
            }
            case "La parola '%s', nel documento '%s' ha una frequenza:" -> {
                domanda = String.format(template, parola, doc);
                if (freq <= 1) risposta = "minima (0-1)";
                else if (freq <= 3) risposta = "bassa (2-3)";
                else if (freq <= 5) risposta = "media (4-5)";
                else risposta = "alta (>=6)";
                opzioni = List.of("minima (0-1)", "bassa (2-3)", "media (4-5)", "alta (>=6)");
            }
            case "La parola '%s' compare nel documento '%s' più o meno di '%d' volte? " -> {
                int soglia = random.nextInt(10) + 1;
                domanda = String.format(template, parola, doc, soglia);
                risposta = freq > soglia ? "più" : freq < soglia ? "meno" : "uguale";
                opzioni = List.of("più", "meno", "uguale", "non compare");
            }
            default -> throw new IllegalStateException("Template non valido");
        }

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.ABSOLUTE_FREQUENCY, domanda, opzioni, risposta);
    }

    public Question generateComparisonQuestion(int id) {
        String doc = getRandomDocument();
        List<String> parole = getRandomWords(30);

        List<String> templates = QuestionType.COMPARISON.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        List<String> opzioni;
        String risposta;

        switch (template) {
            case "Quale parola è più frequente tra '%s', '%s', '%s', '%s' nel documento '%s'?" -> {
                List<String> scelte = parole.subList(0, 4);
                risposta = getMaxFrequenzaParola(doc, scelte);
                domanda = String.format(template, scelte.get(0), scelte.get(1), scelte.get(2), scelte.get(3), doc);
                opzioni = new ArrayList<>(scelte);
            }
            case "Qual è la parola più frequente nel documento '%s'?" -> {
                Map<String, Integer> freq = getFrequenzeParole(doc, parole);
                risposta = getMax(freq);
                opzioni = getDistractors(freq, risposta);
                opzioni.add(risposta);
                domanda = String.format(template, doc);
            }
            case "Quale parola è meno frequente tra '%s', '%s', '%s', '%s' nel documento '%s'?" -> {
                List<String> scelte = parole.subList(0, 4);
                risposta = getMinFrequenzaParola(doc, scelte);
                domanda = String.format(template, scelte.get(0), scelte.get(1), scelte.get(2), scelte.get(3), doc);
                opzioni = new ArrayList<>(scelte);
            }
            case "Qual è la parola meno frequente nel documento '%s'?" -> {
                Map<String, Integer> freq = getFrequenzeParole(doc, parole);
                risposta = getMin(freq);
                opzioni = getDistractors(freq, risposta);
                opzioni.add(risposta);
                domanda = String.format(template, doc);
            }
            default -> throw new IllegalStateException("Template non valido");
        }

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.COMPARISON, domanda, opzioni, risposta);
    }

    public Question generateDocumentSpecificQuestion(int id) {
        String parola = getRandomWord();
        List<String> documenti = getRandomDocuments(4);

        List<String> templates = QuestionType.EXCLUSION.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        List<String> opzioni;
        String risposta;

        switch (template) {
            case "Quale di queste parole non compare nel testo del documento '%s'?" -> {
                String doc = getRandomDocument();
                List<String> parole = getRandomWords(3);
                String fake = "fantasma" + random.nextInt(1000);
                opzioni = new ArrayList<>(parole);
                opzioni.add(fake);
                domanda = String.format(template, doc);
                risposta = fake;
            }
            case "Quale parola non è presente nel testo dei vari documenti?" -> {
                List<String> parole = getRandomWords(3);
                String fake = "illusione" + random.nextInt(1000);
                opzioni = new ArrayList<>(parole);
                opzioni.add(fake);
                domanda = String.format(template);
                risposta = fake;
            }
            case "La parola '%s' compare nei testi?" -> {
                domanda = String.format(template, parola);
                boolean inTutti = documenti.stream().allMatch(doc -> matrix.getFrequenza(doc, parola) > 0);
                boolean inNessuno = documenti.stream().noneMatch(doc -> matrix.getFrequenza(doc, parola) > 0);
                risposta = inTutti ? "in tutti i documenti" : inNessuno ? "no" : "in uno dei documenti";
                opzioni = List.of("sì", "no", "in uno dei documenti", "in tutti i documenti");
            }
            case "Quale parola tra '%s' e '%s' è presente nel testo del documento?" -> {
                String doc = getRandomDocument();
                String p1 = getRandomWord();
                String p2 = "nonpresente" + random.nextInt(1000);
                opzioni = List.of(p1, p2, "una delle due", "nessuna delle due");
                domanda = String.format(template, p1, p2);
                risposta = matrix.getFrequenza(doc, p1) > 0 ? p1 : p2;
            }
            default -> throw new IllegalStateException("Template non valido");
        }

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.EXCLUSION, domanda, opzioni, risposta);
    }

    public Question generateExclusionQuestion(int id) {
        List<String> templates = QuestionType.EXCLUSION.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        Collections.shuffle(parole);

        String domanda;
        List<String> opzioni;
        String corretta;

        switch (template) {
            case "In quale documento compare più spesso la parola '%s'?" -> {
                String parola = parole.get(0);
                Map<String, Integer> freq = new HashMap<>();
                for (String d : docs) freq.put(d, matrix.getFrequenza(d, parola));
                corretta = Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey();
                domanda = String.format(template, parola);
                opzioni = docs.stream().limit(4).toList();
                if (!opzioni.contains(corretta)) {
                    opzioni = new ArrayList<>(opzioni);
                    opzioni.set(0, corretta);
                }
            }
            case "In che documento è presente la parola '%s'?" -> {
                String parola = parole.get(0);
                String presenteIn = docs.stream().filter(d -> matrix.getFrequenza(d, parola) > 0).findFirst().orElse("Nessuno");
                domanda = String.format(template, parola);
                opzioni = new ArrayList<>(docs);
                opzioni.add("Nessuno");
                corretta = presenteIn;
            }
            case "Nella totalità dei documenti, qual è il termine più ricorrente?" -> {
                Map<String, Integer> freq = new HashMap<>();
                for (String p : parole) {
                    int total = docs.stream().mapToInt(d -> matrix.getFrequenza(d, p)).sum();
                    freq.put(p, total);
                }
                corretta = Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey();
                domanda = template;
                opzioni = parole.stream().filter(p -> !p.equals(corretta)).limit(3).toList();
                opzioni = new ArrayList<>(opzioni);
                opzioni.add(corretta);
            }
            case "In che documento non compare la parola '%s'?" -> {
                String parola = parole.get(0);
                String nonPresente = docs.stream().filter(d -> matrix.getFrequenza(d, parola) == 0).findFirst().orElse("Tutti");
                domanda = String.format(template, parola);
                opzioni = new ArrayList<>(docs);
                opzioni.add("Tutti");
                corretta = nonPresente;
            }
            default -> throw new IllegalStateException("Template non riconosciuto: " + template);
        }

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.EXCLUSION, domanda, opzioni, corretta);
    }

    // ========== Metodi di supporto ==========

    private List<String> generaDistrattoriNumerici(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);
        while (opzioni.size() < 4) {
            int offset = random.nextInt(5) + 1;
            int distr = corretta + (random.nextBoolean() ? offset : -offset);
            opzioni.add(Math.max(distr, 0));
        }
        return opzioni.stream().map(String::valueOf).toList();
    }

    private String getRandomDocument() {
        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        return docs.get(random.nextInt(docs.size()));
    }

    private String getRandomWord() {
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        return parole.get(random.nextInt(parole.size()));
    }

    private List<String> getRandomWords(int count) {
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        Collections.shuffle(parole);
        return parole.subList(0, Math.min(count, parole.size()));
    }

    private List<String> getRandomDocuments(int count) {
        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        Collections.shuffle(docs);
        return docs.subList(0, Math.min(count, docs.size()));
    }

    private Map<String, Integer> getFrequenzeParole(String doc, List<String> parole) {
        Map<String, Integer> freq = new HashMap<>();
        for (String p : parole) freq.put(p, matrix.getFrequenza(doc, p));
        return freq;
    }

    private String getMaxFrequenzaParola(String doc, List<String> parole) {
        return parole.stream().max(Comparator.comparingInt(p -> matrix.getFrequenza(doc, p))).orElse("");
    }

    private String getMinFrequenzaParola(String doc, List<String> parole) {
        return parole.stream().min(Comparator.comparingInt(p -> matrix.getFrequenza(doc, p))).orElse("");
    }

    private String getMax(Map<String, Integer> map) {
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("");
    }

    private String getMin(Map<String, Integer> map) {
        return map.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("");
    }

    private List<String> getDistractors(Map<String, Integer> freq, String exclude) {
        return freq.keySet().stream().filter(p -> !p.equals(exclude)).limit(3).toList();
    }
}

