package fall2018.csc2017.GameCenter;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";

    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

    /**
     * The current user account obtained from the game select screen.
     */
    public static UserAccount currentUserAccount;

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardManager = new BoardManager(3, false);
        saveToFile(TEMP_SAVE_FILENAME);
        currentUserAccount =
                (UserAccount) getIntent().getSerializableExtra("currentUserAccount");
        setContentView(R.layout.activity_starting_);
        addLoadButtonListener();
        addSaveButtonListener();
        addScoreboardButtonListener();
        addLoadAutoSaveButtonListener();
    }

    /**
     * Undoes the number of moves specified in the text input, if possible.
     *
     * @param view the current view.
     */
    public void undoMoves(View view) {
        EditText movesView = findViewById(R.id.Undoers);
        String moves = movesView.getText().toString();
        int numberMoves = Integer.parseInt(moves);
        if (numberMoves > boardManager.getSavedBoards().size()) {
            Toast.makeText(this, "Invalid number of undoes", Toast.LENGTH_SHORT).show();
        } else {
            boardManager.undo(numberMoves);
            switchToGame();
        }
    }

    /**
     * Activate the start button. Once the start button is pressed, a new alert dialog
     * prompts the user to choose between the 3 difficulties, and the difficulty is set accordingly.
     *
     * @param view the current view.
     */
    public void newGame(View view) {
        // New alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(StartingActivity.this);
        builder.setTitle("Choose a Complexity");
        // Adds an array containing our 3 levels
        String[] levels = {"3x3", "4x4", "5x5", "3x3-Jorjani", "4x4-Jorjani", "5x5-Jorjani"};
        builder.setItems(levels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 3x3
                        boardManager = new BoardManager(3, false);
                        switchToGame();
                        break;
                    case 1: // 4x4
                        boardManager = new BoardManager(4, false);
                        switchToGame();
                        break;
                    case 2: // 5x5
                        boardManager = new BoardManager(5, false);
                        switchToGame();
                        break;
                    case 3: // 3x3 where the background is changed to jorjani
                        boardManager = new BoardManager(3, true);
                        switchToGame();
                        break;
                    case 4: // 4x4 where the background is changed to jorjani
                        boardManager = new BoardManager(4, true);
                        switchToGame();
                        break;
                    case 5: // 5x5 where the background is changed to jorjani
                        boardManager = new BoardManager(5, true);
                        switchToGame();
                        break;
                }
            }
        });
        // Creates and shows the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Activate the load button. Once the load button is pressed, it gives a list of saved games for
     * so that the user can select a game they saved. The games are identified by the time and date
     * of the save.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartingActivity.this);
                builder.setTitle("Choose a game");
                // add a list
                String[] games = new String[(currentUserAccount.getGameNames().size())];
                int i = 0;
                for (String s : currentUserAccount.getGameNames()) {
                    games[i++] = s;
                }
                int checkedItem = 1;
                builder.setSingleChoiceItems(games, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User selects an item from the list of game Names.
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object selectedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedGame = selectedItem.toString();
                        boardManager = currentUserAccount.getGame(selectedGame);
                        makeToastLoadedText();
                        dialog.dismiss();
                        switchToGame();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                String datetime = dateformat.format(c.getTime());
                LoginScreen.userAccountList.remove(currentUserAccount);
                currentUserAccount.addGame(datetime, boardManager);
                LoginScreen.userAccountList.add(currentUserAccount);
                // Update the file to read from
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(
                            openFileOutput(LoginScreen.USER_ACCOUNTS_FILENAME, MODE_PRIVATE));
                    outputStream.writeObject(LoginScreen.userAccountList);
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the View Scoreboard button.
     */
    private void addScoreboardButtonListener() {
        Button scoreboardButton = findViewById(R.id.ScoreboardButton);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreboard();
            }
        });
    }

    /**
     * Activate the Load Auto Save button.
     */
    private void addLoadAutoSaveButtonListener() {
        Button load = findViewById(R.id.LoadAutoSave);
        load.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<UserAccount> userAccountList;
                    InputStream inputStream = openFileInput(LoginScreen.USER_ACCOUNTS_FILENAME);
                    if (inputStream != null) {
                        ObjectInputStream input = new ObjectInputStream(inputStream);
                        userAccountList = (ArrayList<UserAccount>) input.readObject();
                        inputStream.close();
                        for(UserAccount ua: userAccountList){
                            if(ua.getUsername().equals(currentUserAccount.getUsername())){
                                currentUserAccount = ua;
                            }
                        }
                        boardManager = currentUserAccount.getGame("autoSave");
                        switchToGame();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                } catch (ClassNotFoundException e) {
                    Log.e("login activity", "File contained unexpected data type: " + e.toString());
                }
            }
        }));
    }



    /**
     * Display that there is no autosaved game to load.
     */
    private void makeToastLoadAutoSaveFailText() {
        Toast.makeText(this, "No Autosaved Game to Load", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        tmp.putExtra("currentUserAccount", currentUserAccount);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }

    /**
     * Switch to the Scoreboard view to view the per-user and per-game scoreboards.
     */
    private void switchToScoreboard() {
        Intent tmp = new Intent(this, Scoreboard.class);
        tmp.putExtra("currentUserAccount", currentUserAccount);
        startActivity(tmp);
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
}
