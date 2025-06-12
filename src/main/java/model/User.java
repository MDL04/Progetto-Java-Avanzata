package model;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private int bestScoreEasy;
    private int bestScoreMedium;
    private int bestScoreHard;
    private int partiteEasy;
    private int partiteMedium;
    private int partiteHard;
    private int scoreMedium;
    private int scoreHard;
    private int scoreEasy;
    private String urlAvatar;

    public User(Long id, String username, String email, String password, boolean isAdmin, int bestScoreEasy, int bestScoreMedium, int bestScoreHard, int partiteEasy, int partiteMedium, int partiteHard, int scoreEasy, int scoreMedium, int scoreHard, String urlAvatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.bestScoreEasy = bestScoreEasy;
        this.bestScoreMedium = bestScoreMedium;
        this.bestScoreHard = bestScoreHard;
        this.partiteEasy = partiteEasy;
        this.partiteMedium = partiteMedium;
        this.partiteHard = partiteHard;
        this.scoreEasy = scoreEasy;
        this.scoreMedium = scoreMedium;
        this.scoreHard = scoreHard;
        this.urlAvatar = urlAvatar;
    }

    public User(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getBestScoreEasy() {
        return bestScoreEasy;
    }

    public void setBestScoreEasy(int bestScoreEasy) {
        this.bestScoreEasy = bestScoreEasy;
    }

    public int getBestScoreMedium() {
        return bestScoreMedium;
    }

    public void setBestScoreMedium(int bestScoreMedium) {
        this.bestScoreMedium = bestScoreMedium;
    }

    public int getBestScoreHard() {
        return bestScoreHard;
    }

    public void setBestScoreHard(int bestScoreHard) {
        this.bestScoreHard = bestScoreHard;
    }

    public int getPartiteEasy() {
        return partiteEasy;
    }

    public void setPartiteEasy(int partiteEasy) {
        this.partiteEasy = partiteEasy;
    }

    public int getPartiteMedium() {
        return partiteMedium;
    }

    public void setPartiteMedium(int partiteMedium) {
        this.partiteMedium = partiteMedium;
    }

    public int getPartiteHard() {
        return partiteHard;
    }

    public void setPartiteHard(int partiteHard) {
        this.partiteHard = partiteHard;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public int getScoreMedium() {
        return scoreMedium;
    }

    public void setScoreMedium(int scoreMedium) {
        this.scoreMedium = scoreMedium;
    }

    public int getScoreHard() {
        return scoreHard;
    }

    public void setScoreHard(int scoreHard) {
        this.scoreHard = scoreHard;
    }

    public int getScoreEasy() {
        return scoreEasy;
    }

    public void setScoreEasy(int scoreEasy) {
        this.scoreEasy = scoreEasy;
    }
}

