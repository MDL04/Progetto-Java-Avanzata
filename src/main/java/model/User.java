package model;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private int bestScoreEasy;
    private int bestScoreNormal;
    private int bestScoreHard;
    private int partiteEasy;
    private int partiteNormal;
    private int partiteHard;
    private int scoreNormal;
    private int scoreHard;
    private int scoreEasy;
    private String urlAvatar;

    public User(Long id, String username, String email, String password, boolean isAdmin, int bestScoreEasy, int bestScoreNormal, int bestScoreHard, int partiteEasy, int partiteNormal, int partiteHard, int scoreEasy, int scoreNormal, int scoreHard, String urlAvatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.bestScoreEasy = bestScoreEasy;
        this.bestScoreNormal = bestScoreNormal;
        this.bestScoreHard = bestScoreHard;
        this.partiteEasy = partiteEasy;
        this.partiteNormal = partiteNormal;
        this.partiteHard = partiteHard;
        this.scoreEasy = scoreEasy;
        this.scoreNormal = scoreNormal;
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

    public int getBestScoreNormal() {
        return bestScoreNormal;
    }

    public void setBestScoreNormal(int bestScoreNormal) {
        this.bestScoreNormal = bestScoreNormal;
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

    public int getPartiteNormal() {
        return partiteNormal;
    }

    public void setPartiteNormal(int partiteNormal) {
        this.partiteNormal = partiteNormal;
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

    public int getScoreNormal() {
        return scoreNormal;
    }

    public void setScoreNormal(int scoreNormal) {
        this.scoreNormal = scoreNormal;
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

