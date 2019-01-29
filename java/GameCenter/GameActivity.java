package fall2018.csc2017.GameCenter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The account obtained from the login screen.
     */
    public static UserAccount currentUserAccount;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        currentUserAccount =
                (UserAccount) getIntent().getSerializableExtra("currentUserAccount");
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.numCols);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = displayWidth / Board.numCols;
                        columnHeight = displayHeight / Board.numRows;
                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board.numRows; row++) {
            for (int col = 0; col != Board.numCols; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.numRows;
            int col = nextPos % Board.numCols;
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        updateHighScores();
        createAutoSave();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        updateHighScores();
        createAutoSave();
    }

    /**
     * Updates the high scores of the currentUserAccount if a new high score was achieved.
     */
    private void updateHighScores() {
        boolean scoreUpdated = false;
        int complexity = boardManager.getComplexity();
        int numMoves = boardManager.getMoves();
        if (boardManager.puzzleSolved()) {
            if (complexity == 3 && currentUserAccount.getTop3x3() > numMoves) {
                currentUserAccount.setTop3x3(numMoves);
                scoreUpdated = true;
            } else if (complexity == 4 && currentUserAccount.getTop4x4() > numMoves) {
                currentUserAccount.setTop4x4(numMoves);
                scoreUpdated = true;
            } else if (complexity == 5 && currentUserAccount.getTop5x5() > numMoves) {
                currentUserAccount.setTop5x5(numMoves);
                scoreUpdated = true;
            }
        }
        if (scoreUpdated) {
            updateUserAccounts(complexity, numMoves);
        }
    }

    /**
     * Writes new high scores to file.
     * Helper method for updateHighScores.
     *
     * @param complexity the complexity of BoardManager
     * @param numMoves   the number of moves committed in BoardManager
     */
    private void updateUserAccounts(int complexity, int numMoves) {
        LoginScreen.userAccountList.remove(currentUserAccount);
        if (complexity == 3) {
            currentUserAccount.setTop3x3(numMoves);
        } else if (complexity == 4) {
            currentUserAccount.setTop4x4(numMoves);
        } else {
            currentUserAccount.setTop5x5(numMoves);
        }
        LoginScreen.userAccountList.add(currentUserAccount);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    openFileOutput(LoginScreen.USER_ACCOUNTS_FILENAME, MODE_PRIVATE));
            outputStream.writeObject(LoginScreen.userAccountList);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void createAutoSave() {
        System.out.println(LoginScreen.userAccountList.size());
        LoginScreen.userAccountList.remove(currentUserAccount);
        System.out.println(LoginScreen.userAccountList.size());
        currentUserAccount.addGame("autoSave", boardManager);
        System.out.println(boardManager.getMoves());
        LoginScreen.userAccountList.add(currentUserAccount);
        System.out.println(LoginScreen.userAccountList.size());
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    openFileOutput(LoginScreen.USER_ACCOUNTS_FILENAME, MODE_PRIVATE));
            outputStream.writeObject(LoginScreen.userAccountList);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        System.out.println("autosave created");
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
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

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
