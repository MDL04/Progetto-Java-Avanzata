package Actors;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private int best_score_easy;
    private int best_score_normal;
    private int best_score_hard;
    private int partite_easy;
    private int partite_normal;
    private int partite_hard;
    private String url_avatar;

    public User(Long id, String username, String email, String password, boolean isAdmin, int best_score_easy, int best_score_normal, int best_score_hard, int partite_easy, int partite_normal, int partite_hard, String url_avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.best_score_easy = best_score_easy;
        this.best_score_normal = best_score_normal;
        this.best_score_hard = best_score_hard;
        this.partite_easy = partite_easy;
        this.partite_normal = partite_normal;
        this.partite_hard = partite_hard;
        this.url_avatar = url_avatar;
    }



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

    public int getBest_score_easy() {
        return best_score_easy;
    }

    public void setBest_score_easy(int best_score_easy) {
        this.best_score_easy = best_score_easy;
    }

    public int getBest_score_normal() {
        return best_score_normal;
    }

    public void setBest_score_normal(int best_score_normal) {
        this.best_score_normal = best_score_normal;
    }

    public int getBest_score_hard() {
        return best_score_hard;
    }

    public void setBest_score_hard(int best_score_hard) {
        this.best_score_hard = best_score_hard;
    }

    public int getPartite_easy() {
        return partite_easy;
    }

    public void setPartite_easy(int partite_easy) {
        this.partite_easy = partite_easy;
    }

    public int getPartite_normal() {
        return partite_normal;
    }

    public void setPartite_normal(int partite_normal) {
        this.partite_normal = partite_normal;
    }

    public int getPartite_hard() {
        return partite_hard;
    }

    public void setPartite_hard(int partite_hard) {
        this.partite_hard = partite_hard;
    }

    public String getUrl_avatar() {
        return url_avatar;
    }

    public void setUrl_avatar(String url_avatar) {
        this.url_avatar = url_avatar;
    }


}

