package fall2018.csc2017.GameCenter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores a user's account information.
 */
public class UserAccount implements Serializable {

    /**
     * The account user's username.
     */
    private String username;

    /**
     * The account user's password.
     */
    private String password;

    /**
     * The account user's top score for boards of 3x3 complexity.
     */
    private int top3x3;

    /**
     * The account user's top score for boards of 4x4 complexity.
     */
    private int top4x4;

    /**
     * The account user's top score for boards of 5x5 complexity.
     */
    private int top5x5;

    /**
     * The names of the account user's past games.
     */
    private Map<String, BoardManager> gameNames;

    /**
     * An instance of an account with a username, password, and a blank list of game names.
     * Default top score is 1000000, which displays as "None" on the scoreboards.
     *
     * @param username the username of the account
     * @param password the password of the account
     */
    UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.gameNames = new HashMap<String, BoardManager>();
        this.top3x3 = 1000000;
        this.top4x4 = 1000000;
        this.top5x5 = 1000000;
    }

    /**
     * Returns the username of the account.
     *
     * @return the account's username
     */
    String getUsername() {
        return username;
    }

    /**
     * Returns the password of the account.
     *
     * @return the account's password
     */
    String getPassword() {
        return password;
    }

    /**
     * Returns the account's top score for boards of 3x3 complexity.
     *
     * @return the account's top score for boards of 3x3 complexity.
     */
    int getTop3x3() {
        return top3x3;
    }

    /**
     * Sets the account's top score for boards of 3x3 complexity.
     *
     * @param top3x3 the account's new top 3x3 score
     */
    void setTop3x3(int top3x3) {
        this.top3x3 = top3x3;
    }

    /**
     * Returns the account's top score for boards of 4x4 complexity.
     *
     * @return the account's top score for boards of 4x4 complexity.
     */
    int getTop4x4() {
        return top4x4;
    }

    /**
     * Sets the account's top score for boards of 4x4 complexity.
     *
     * @param top4x4 the account's new top 3x3 score
     */
    void setTop4x4(int top4x4) {
        this.top4x4 = top4x4;
    }

    /**
     * Returns the account's top score for boards of 5x5 complexity.
     *
     * @return the account's top score for boards of 5x5 complexity.
     */
    int getTop5x5() {
        return top5x5;
    }

    /**
     * Sets the account's top score for boards of 5x5 complexity.
     *
     * @param top5x5 the account's new top 5x5 score
     */
    void setTop5x5(int top5x5) {
        this.top5x5 = top5x5;
    }

    /**
     * Adds a new game name to this account
     *
     * @param newName a new name added to the account
     */
    void addGame(String newName, BoardManager game) {
        this.gameNames.put(newName, game);
    }

    /**
     * Get a game saved by the name gameName.
     */
    BoardManager getGame(String gameName) {
        if (this.gameNames.containsKey(gameName)) {
            return this.gameNames.get(gameName);
        } else {
            return null;
        }
    }

    Set<String> getGameNames() {
        return gameNames.keySet();
    }
}
