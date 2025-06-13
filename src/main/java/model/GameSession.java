package model;

import java.time.LocalDateTime;

public class GameSession {
    private int id;
    private int userId;
    private LocalDateTime date;
    private int score;
    private String difficulty;

    /**
     * Crea un'istanza di quest'oggetto
     * @param id
     * @param userId
     * @param date
     * @param score
     * @param difficulty
     */
    public GameSession(int id, int userId, LocalDateTime date, int score, String difficulty) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.score = score;
        this.difficulty = difficulty;
    }

    /**@return id*/
    public int getId() {
        return id;
    }

    /**@return userId*/
    public int getUserId() {
        return userId;
    }

    /**@return date*/
    public LocalDateTime getDate() {
        return date;
    }

    /**@return score*/
    public int getScore() {return score;}

    /**@return difficulty*/
    public String getDifficulty() {return difficulty;}

    /**@param id*/
    public void setId(int id) {this.id = id;}

    /**@param userId*/
    public void setUserId(int userId) {this.userId = userId;}

    /**@param date*/
    public void setDate(LocalDateTime date) {this.date = date;}

    /**@param score*/
    public void setScore(int score) {this.score = score;}

    /**@param difficulty*/
    public void setDifficulty(String difficulty) {this.difficulty = difficulty;}

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
