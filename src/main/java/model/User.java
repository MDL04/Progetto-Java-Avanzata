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

    /**
     * Crea un'istanza di quest'oggetto
     * @param id
     * @param username
     * @param email
     * @param password
     * @param isAdmin
     * @param bestScoreEasy
     * @param bestScoreMedium
     * @param bestScoreHard
     * @param partiteEasy
     * @param partiteMedium
     * @param partiteHard
     * @param scoreEasy
     * @param scoreMedium
     * @param scoreHard
     * @param urlAvatar
     */
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

    /**@return id*/
    public Long getId() {
        return id;
    }

    /**@param id*/
    public void setId(Long id) {
        this.id = id;
    }

    /**@return username*/
    public String getUsername() {
        return username;
    }

    /**@param username*/
    public void setUsername(String username) {
        this.username = username;
    }

    /**@return email*/
    public String getEmail() {
        return email;
    }

    /**@param email*/
    public void setEmail(String email) {
        this.email = email;
    }

    /**@return password*/
    public String getPassword() {
        return password;
    }

    /**@param password*/
    public void setPassword(String password) {
        this.password = password;
    }

    /**@return isAdmin*/
    public boolean isAdmin() {
        return isAdmin;
    }

    /**@param admin*/
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**@return bestScoreEasy*/
    public int getBestScoreEasy() {
        return bestScoreEasy;
    }

    /**@param bestScoreEasy*/
    public void setBestScoreEasy(int bestScoreEasy) {
        this.bestScoreEasy = bestScoreEasy;
    }

    /**@return bestScoreMedium*/
    public int getBestScoreMedium() {
        return bestScoreMedium;
    }

    /**@param bestScoreMedium*/
    public void setBestScoreMedium(int bestScoreMedium) {
        this.bestScoreMedium = bestScoreMedium;
    }

    /**@return bestScoreHard*/
    public int getBestScoreHard() {
        return bestScoreHard;
    }

    /**@param bestScoreHard*/
    public void setBestScoreHard(int bestScoreHard) {
        this.bestScoreHard = bestScoreHard;
    }

    /**@return partiteEasy*/
    public int getPartiteEasy() {
        return partiteEasy;
    }

    /**@param partiteEasy*/
    public void setPartiteEasy(int partiteEasy) {
        this.partiteEasy = partiteEasy;
    }

    /**@return partiteMedium*/
    public int getPartiteMedium() {
        return partiteMedium;
    }

    /**@param partiteMedium*/
    public void setPartiteMedium(int partiteMedium) {
        this.partiteMedium = partiteMedium;
    }

    /**@return partiteHard*/
    public int getPartiteHard() {
        return partiteHard;
    }

    /**@param partiteHard*/
    public void setPartiteHard(int partiteHard) {
        this.partiteHard = partiteHard;
    }

    /**@return urlAvatar*/
    public String getUrlAvatar() {
        return urlAvatar;
    }

    /**@param urlAvatar*/
    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    /**@return scoreMedium*/
    public int getScoreMedium() {
        return scoreMedium;
    }

    /**@param scoreMedium*/
    public void setScoreMedium(int scoreMedium) {
        this.scoreMedium = scoreMedium;
    }

    /**@return scoreHard*/
    public int getScoreHard() {
        return scoreHard;
    }

    /**@param scoreHard*/
    public void setScoreHard(int scoreHard) {
        this.scoreHard = scoreHard;
    }

    /**@return scoreEasy*/
    public int getScoreEasy() {
        return scoreEasy;
    }

    /**@param scoreEasy*/
    public void setScoreEasy(int scoreEasy) {
        this.scoreEasy = scoreEasy;
    }
}

