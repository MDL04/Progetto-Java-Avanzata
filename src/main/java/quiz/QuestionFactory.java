package quiz;

import model.WordDocumentMatrix;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class QuestionFactory {
    private final WordDocumentMatrix matrix;
    private final Random random = new Random();

    public QuestionFactory(WordDocumentMatrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("WordDocumentMatrix non può essere null");
        }
        this.matrix = matrix;
    }

    // Overload con lingua
    public Question generateQuestion(int id, QuestionType type, String lingua) {
        validateMatrix();
        return switch (type) {
            case ABSOLUTE_FREQUENCY -> generateAbsoluteFrequencyQuestion(id);
            case COMPARISON -> generateComparisonQuestion(id);
            case DOCUMENT_SPECIFIC -> generateDocumentSpecificQuestion(id);
            case EXCLUSION -> generateExclusionQuestion(id, lingua);
        };
    }

    public Question generateAbsoluteFrequencyQuestion(int id) {
        validateMatrix();
        String doc = getRandomDocument();
        String parola = getRandomWord();

        List<String> templates = QuestionType.ABSOLUTE_FREQUENCY.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda = String.format(template, parola, doc);
        int risposta = matrix.getFrequenza(doc, parola);
        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.ABSOLUTE_FREQUENCY, domanda, opzioni, String.valueOf(risposta));
    }

//    public Question generateComparisonQuestion(int id) {
//        validateMatrix();
//        String doc = getRandomDocument();
//        List<String> parole = getRandomWords(10);
//
//        List<String> templates = QuestionType.COMPARISON.getQuestionList();
//        String template = templates.get(random.nextInt(templates.size()));
//
//        String domanda;
//        int risposta;
//
//        switch (template) {
//            case "Quante volte compare la parola più frequente nel documento '%s'?" -> {
//                domanda = String.format(template, doc);
//                risposta = parole.stream()
//                        .mapToInt(p -> matrix.getFrequenza(doc, p))
//                        .max()
//                        .orElse(0);
//            }
//            case "Qual è la frequenza massima di una parola nel documento '%s'?" -> {
//                domanda = String.format(template, doc);
//                risposta = matrix.getTutteLeParole().stream()
//                        .mapToInt(p -> matrix.getFrequenza(doc, p))
//                        .max()
//                        .orElse(0);
//            }
//            case "Qual è la differenza di frequenza tra '%s' e '%s' nel documento '%s'?" -> {
//                String p1 = getRandomWord();
//                String p2 = getRandomWord();
//                domanda = String.format(template, p1, p2, doc);
//                int freq1 = matrix.getFrequenza(doc, p1);
//                int freq2 = matrix.getFrequenza(doc, p2);
//                risposta = Math.abs(freq1 - freq2);
//            }
//            case "Quante volte in più compare '%s' rispetto a '%s' nel documento '%s'?" -> {
//                String p1 = getRandomWord();
//                String p2 = getRandomWord();
//                domanda = String.format(template, p1, p2, doc);
//                int freq1 = matrix.getFrequenza(doc, p1);
//                int freq2 = matrix.getFrequenza(doc, p2);
//                risposta = Math.max(0, freq1 - freq2);
//            }
//            case "Qual è la somma delle frequenze di '%s' e '%s' nel documento '%s'?" -> {
//                String p1 = getRandomWord();
//                String p2 = getRandomWord();
//                domanda = String.format(template, p1, p2, doc);
//                int freq1 = matrix.getFrequenza(doc, p1);
//                int freq2 = matrix.getFrequenza(doc, p2);
//                risposta = freq1 + freq2;
//            }
//            default -> throw new IllegalStateException("Template non valido: " + template);
//        }
//
//        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));
//        Collections.shuffle(opzioni);
//        return new Question(id, QuestionType.COMPARISON, domanda, opzioni, String.valueOf(risposta));
//    }

    public Question generateComparisonQuestion(int id) {
        validateMatrix();
        String doc = getRandomDocument();
        List<String> parole = getRandomWords(10);

        List<String> templates = QuestionType.COMPARISON.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        String risposta;

        switch (template) {
            case "Quante volte compare la parola più frequente nel documento '%s'?" -> {
                // Trova la parola con massima frequenza

                String parolaMax = parole.stream()
                        .max(Comparator.comparingInt(p -> matrix.getFrequenza(doc, p)))
                        .orElse(parole.get(0));

                int freqMax = matrix.getFrequenza(doc, parolaMax);

                domanda = String.format(template, doc);
                risposta = String.valueOf(freqMax);
            }
            case "Qual è la frequenza massima di una parola nel documento '%s'?" -> {
                int freq = matrix.getTutteLeParole().stream()
                        .mapToInt(p -> matrix.getFrequenza(doc, p))
                        .max()
                        .orElse(0);
                domanda = String.format(template, doc);
                risposta = String.valueOf(freq);
            }
            case "Qual è la differenza di frequenza tra '%s' e '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                int diff = Math.abs(matrix.getFrequenza(doc, p1) - matrix.getFrequenza(doc, p2));

                domanda = String.format(template, p1, p2, doc);
                risposta = String.valueOf(diff);

            }
            case "Quante volte in più compare '%s' rispetto a '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                int delta = Math.max(0, matrix.getFrequenza(doc, p1) - matrix.getFrequenza(doc, p2));

                domanda = String.format(template, p1, p2, doc);
                risposta = String.valueOf(delta);
            }
            case "Qual è la somma delle frequenze di '%s' e '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                int sum = matrix.getFrequenza(doc, p1) + matrix.getFrequenza(doc, p2);

                domanda = String.format(template, p1, p2, doc);
                risposta = String.valueOf(sum);
            }
            default -> throw new IllegalStateException("Template non valido: " + template);
        }

        int rispostaInt = Integer.parseInt(risposta);
        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(rispostaInt));
        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.COMPARISON, domanda, opzioni, risposta);
    }

    public Question generateDocumentSpecificQuestion(int id) {
        validateMatrix();
        String parola = getRandomWord();
        List<String> docs = new ArrayList<>(matrix.getDocumenti());

        List<String> templates = QuestionType.DOCUMENT_SPECIFIC.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        int risposta;

        switch (template) {
            case "In quanti documenti compare la parola '%s'?" -> {
                domanda = String.format(template, parola);
                risposta = (int) docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .map(freq -> freq > 0 ? 1 : 0)
                        .sum();
            }
            case "Quante volte compare la parola '%s' nel documento dove è più frequente?" -> {
                domanda = String.format(template, parola);
                risposta = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .max()
                        .orElse(0);
            }
            case "Qual è la frequenza totale della parola '%s' in tutti i documenti?" -> {
                domanda = String.format(template, parola);
                risposta = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .sum();
            }
            case "In quanti documenti la parola '%s' compare almeno 2 volte?" -> {
                domanda = String.format(template, parola);
                risposta = (int) docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .map(freq -> freq >= 2 ? 1 : 0)
                        .sum();
            }
            case "Qual è la frequenza media (arrotondata) della parola '%s' nei documenti dove compare?" -> {
                domanda = String.format(template, parola);
                List<Integer> frequenze = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .filter(freq -> freq > 0)
                        .boxed()
                        .toList();
                risposta = frequenze.isEmpty() ? 0 :
                        (int) Math.round(frequenze.stream().mapToInt(Integer::intValue).average().orElse(0));
            }
            default -> throw new IllegalStateException("Template non valido: " + template);
        }

        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));
        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.DOCUMENT_SPECIFIC, domanda, opzioni, String.valueOf(risposta));
    }


    // ====== Domanda di esclusione da vocabolario ======
    public Question generateExclusionQuestion(int id, String lang) {
        validateMatrix();
        Set<String> vocabolario = caricaVocabolario("vocabulary/vocab_" + lang + ".txt");
        Set<String> parolePresenti = matrix.getTutteLeParole();

        List<String> paroleMaiApparse = vocabolario.stream()
                .filter(p -> !parolePresenti.contains(p))
                .toList();
        List<String> paroleApparse = parolePresenti.stream()
                .filter(vocabolario::contains)
                .toList();

        if (paroleMaiApparse.isEmpty() || paroleApparse.size() < 3)
            throw new IllegalStateException("Non ci sono abbastanza parole per la domanda di esclusione da vocabolario.");

        String rispostaCorretta = paroleMaiApparse.get(random.nextInt(paroleMaiApparse.size()));
        List<String> opzioni = new ArrayList<>();
        opzioni.add(rispostaCorretta);
        Collections.shuffle(paroleApparse);
        opzioni.addAll(paroleApparse.subList(0, 3));
        Collections.shuffle(opzioni);

        String domanda = "Quale di queste parole non appare mai in nessun documento?";
        return new Question(id, QuestionType.EXCLUSION, domanda, opzioni, rispostaCorretta);
    }

    private Set<String> caricaVocabolario(String path) {
        // Controlla se il file esiste nel classpath
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("File vocabolario non trovato nel classpath: " + path);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            Set<String> set = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String parola = line.trim().toLowerCase();
                if (!parola.isBlank()) set.add(parola);
            }
            return set;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento del vocabolario: " + path, e);
        }
    }

    // ========== Metodi di supporto ==========

    private List<String> generaDistrattoriNumerici(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);

        while (opzioni.size() < 4) {
            int offset = random.nextInt(Math.max(5, corretta / 2 + 1)) + 1;
            int distrattore = corretta + (random.nextBoolean() ? offset : -offset);
            opzioni.add(Math.max(distrattore, 0));
        }

        return opzioni.stream()
                .map(String::valueOf)
                .sorted((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)))
                .toList();
    }

    private String getRandomDocument() {
        if (matrix == null || matrix.getDocumenti() == null || matrix.getDocumenti().isEmpty()) {
            throw new IllegalStateException("Nessun documento disponibile nella matrice");
        }
        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        return docs.get(random.nextInt(docs.size()));
    }

    private String getRandomWord() {
        if (matrix == null || matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola disponibile nella matrice");
        }
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        return parole.get(random.nextInt(parole.size()));
    }

    private List<String> getRandomWords(int count) {
        if (matrix == null || matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola disponibile nella matrice");
        }
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        Collections.shuffle(parole);
        return parole.subList(0, Math.min(count, parole.size()));
    }

    private void validateMatrix() {
        if (matrix == null) {
            throw new IllegalStateException("WordDocumentMatrix non inizializzata");
        }
        if (matrix.getDocumenti() == null || matrix.getDocumenti().isEmpty()) {
            throw new IllegalStateException("Nessun documento caricato nella matrice");
        }
        if (matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola trovata nei documenti");
        }
    }
}