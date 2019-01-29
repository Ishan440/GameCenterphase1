package fall2018.csc2017.GameCenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * The scoreboard screen that is displayed after "View Scoreboards" button is clicked.
 * Displays the per-user scoreboard (top user of each complexity) and the per-game scoreboard
 * (top score of current user for each complexity).
 */
public class Scoreboard extends AppCompatActivity {

    /**
     * The user account file containing all UserAccount objects.
     */
    private static final String USER_ACCOUNTS_FILENAME = "accounts.ser";

    /**
     * Array List of UserAccount objects, which store account usernames and passwords.
     */
    private ArrayList<UserAccount> userAccountList;

    /**
     * Current user account that is signed into.
     */
    private UserAccount currentUserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        setUserAccountList(USER_ACCOUNTS_FILENAME);
        this.currentUserAccount =
                (UserAccount) getIntent().getSerializableExtra("currentUserAccount");
        TableLayout scoreboardTable = (TableLayout) findViewById(R.id.scoreboardsTable);
        String[] topScorers = findTopScorers();
        String[] topScores = findTopScores();
        for (int i = 0; i < 3; i++) {
            TableRow scoreboardRow = new TableRow(this);
            TextView headingView = new TextView(this);
            if (i == 0) {
                headingView.setText(R.string.three_by_three);
            }
            if (i == 1) {
                headingView.setText(R.string.four_by_four);
            }
            if (i == 2) {
                headingView.setText(R.string.five_by_five);
            }
            scoreboardRow.addView(headingView);
            String scorer = topScorers[i];
            String score = topScores[i];
            TextView scorerView = new TextView(this);
            scorerView.setText(scorer);
            TextView scoreView = new TextView(this);
            scoreView.setText(score);
            scoreboardRow.addView(scorerView);
            scoreboardRow.addView(scoreView);
            scoreboardTable.addView(scoreboardRow);
        }
    }

    /**
     * Returns an array of the three top scorers for each complexity, in order of 3x3, 4x4, 5x5.
     * Default top score is 1000000, which displays as "None".
     *
     * @return topScorers, a list of usernames of the top scorer for each complexity.
     */
    private String[] findTopScorers() {
        String[] topScorers = new String[3];
        topScorers[0] = "None";
        topScorers[1] = "None";
        topScorers[2] = "None";
        int top3x3 = 1000000;
        int top4x4 = 1000000;
        int top5x5 = 1000000;
        for (UserAccount user : this.userAccountList) {
            if (user.getTop3x3() < top3x3) {
                topScorers[0] = (user.getUsername() + ": " + Integer.toString(user.getTop3x3()));
                top3x3 = user.getTop3x3();
            }
            if (user.getTop4x4() < top4x4) {
                topScorers[1] = (user.getUsername() + ": " + Integer.toString(user.getTop4x4()));
                top4x4 = user.getTop4x4();
            }
            if (user.getTop5x5() < top5x5) {
                topScorers[2] = (user.getUsername() + ": " + Integer.toString(user.getTop4x4()));
                top5x5 = user.getTop5x5();
            }
        }
        return topScorers;
    }

    /**
     * Returns an array of the three top scores for each complexity, in order of 3x3, 4x4, 5x5.
     * Default top score is 1000000, which displays as "None".
     *
     * @return topScores, a list of scores of the current user for each complexity.
     */
    private String[] findTopScores() {
        for (UserAccount user : userAccountList) {
            if (this.currentUserAccount.getUsername().equals(user.getUsername())) {
                this.currentUserAccount = user;
            }
        }
        String[] topScores = new String[3];
        topScores[0] = Integer.toString(this.currentUserAccount.getTop3x3());
        topScores[1] = Integer.toString(this.currentUserAccount.getTop4x4());
        topScores[2] = Integer.toString(this.currentUserAccount.getTop5x5());
        for (int i = 0; i <3; i++) {
            if (topScores[i].equals("1000000")) {
                topScores[i] = "None";
            }
        }
        return topScores;
    }

    /**
     * Set list of user accounts from fileName.
     *
     * @param fileName the name of the file
     */
    public void setUserAccountList(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                this.userAccountList = (ArrayList<UserAccount>) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }
}
