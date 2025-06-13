package model;

import java.time.LocalDateTime;

public class GameSession {
    private int id;
    private int userId;
    private LocalDateTime date;
    private int score;
    private String difficulty;

    public GameSession(int id, int userId, LocalDateTime date, int score, String difficulty) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.score = score;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameSession{")
                .append("id=").append(id)
                .append(", userId=").append(userId)
                .append(", date=").append(date)
                .append(", score=").append(score)
                .append(", difficulty='").append(difficulty).append('\'')
                .append('}');
        return sb.toString();
    }
}
