package  com.mohit.tictaktao.OnevsOnline;

import java.util.ArrayList;
import java.util.Collections;

public class GameSession {
    public String sessionId;
    public String playerOneId;
    public String playerTwoId;
    public ArrayList<String> gameBoard; // Represents the game state, e.g., ["X", "", "O", ...]
    public String currentPlayerId; // ID of the player whose turn it is
    public String status; // e.g., "waiting", "active", "finished"
    public int scorePlayerOne;
    public int scorePlayerTwo;
    public  String playerOneName;
    public  String playerTwoName;
    public  String playerOneIcon;
    public long gamesize;

    // Default constructor required for Firestore
    public GameSession() {}

    // Constructor
    public GameSession(String playerOneId, ArrayList<String> gameBoard, String status) {
        this.playerOneId = playerOneId;
        this.gameBoard = gameBoard;
        this.status = status; // Waiting for a second player
        this.currentPlayerId = playerOneId; // Player one starts the game
        this.scorePlayerOne = 0;
        this.scorePlayerTwo = 0;
    }

    public String getPlayerOneIcon() {
        return playerOneIcon;
    }

    public void setPlayerOneIcon(String playerOneIcon) {
        this.playerOneIcon = playerOneIcon;
    }

    public long getGamesize() {
        return gamesize;
    }

    public void setGamesize(long gamesize) {
        this.gamesize = gamesize;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(String playerOneId) {
        this.playerOneId = playerOneId;
    }

    public String getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public ArrayList<String> getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(ArrayList<String> gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getScorePlayerOne() {
        return scorePlayerOne;
    }

    public void setScorePlayerOne(int scorePlayerOne) {
        this.scorePlayerOne = scorePlayerOne;
    }

    public int getScorePlayerTwo() {
        return scorePlayerTwo;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playeerOneName) {
        this.playerOneName = playeerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playeerTwoName) {
        this.playerTwoName = playeerTwoName;
    }

    public void setScorePlayerTwo(int scorePlayerTwo) {
        this.scorePlayerTwo = scorePlayerTwo;
    }
// Add getters and setters here...
}
