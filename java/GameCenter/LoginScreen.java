package fall2018.csc2017.GameCenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;


/**
 * The login screen shown upon initial startup of the game.
 * Processes sign ups and logins of userAccounts.
 * Passes on the current signed-in userAccount to StartingActivity.
 */
public class LoginScreen extends AppCompatActivity {

    /**
     * The file containing an arrayList of all created instances of UserAccounts.
     */
    public static final String USER_ACCOUNTS_FILENAME = "accounts.ser";

    /**
     * An ArrayList of UserAccounts, read and stored in a file.
     */
    public static ArrayList<UserAccount> userAccountList;

    /**
     * The UserAccount that will be logged in; will be passed onto StartingActivity.
     */
    private static UserAccount currentUserAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccountList = new ArrayList<>();
        setUserAccountList(USER_ACCOUNTS_FILENAME);

        setContentView(R.layout.activity_login);
        addSignupButtonListener();
    }

    /**
     * Sets the userAccountList to the list of UserAccounts read from the file.
     *
     * @param fileName the name of the file
     */
    public void setUserAccountList(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream == null) {
                userAccountsToFile(fileName);
            } else {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                userAccountList = (ArrayList<UserAccount>) input.readObject();
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
     * Logs in the UserAccount if it exists and the username and password are correct.
     * Sets the currentUserAccount to the newly logged in UserAccount.
     * Returns true if the login is successful, false otherwise.
     *
     * @param username the username of the userAccount to be logged in
     * @param password the password of the userAccount to be logged in
     * @return whether the login is successful
     */
    private boolean successfulLogin(String username, String password) {
        boolean loginSuccess = false;
        setUserAccountList(USER_ACCOUNTS_FILENAME);
        for (UserAccount userAccount : userAccountList) {
            if (userAccount.getUsername().equals(username)
                    && userAccount.getPassword().equals(password)) {
                loginSuccess = true;
                currentUserAccount = userAccount;
            }
        }
        return loginSuccess;
    }

    /**
     * Signs up a new UserAccount if it does not already exist, adds it to userAccountList,
     * and sets the currentUserAccount to it.
     * Returns true if the sign up is successful, false otherwise.
     *
     * @param username the username of the new UserAccount
     * @param password the password of the new UserAccount
     * @return whether the sign up of the new UserAccount is successful
     */
    public boolean successfulSignUp(String username, String password) {
        UserAccount newUserAccount = new UserAccount(username, password);
        boolean exists = false;
        for (UserAccount userAccount : userAccountList) {
            if (userAccount.getUsername().equals(username)) {
                exists = true;
            }
        }
        if (!exists) {
            currentUserAccount = newUserAccount;
            userAccountList.add(newUserAccount);
            userAccountsToFile(USER_ACCOUNTS_FILENAME);
        }
        return !exists;
    }

    /**
     * Saves the userAccountList to a file.
     *
     * @param fileName the name of the file
     */
    public void userAccountsToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(userAccountList);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Called when the user taps the login button.
     * If the login credentials are valid, sends the currentUserAccount to StartingActivity
     * then sets the view to StartingActivity.
     */
    public void onClick(View view) {
        EditText usernameView = findViewById(R.id.Username);
        EditText passwordView = findViewById(R.id.Password);
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        Intent intent = new Intent(view.getContext(), GameSelectScreen.class);
        boolean success = successfulLogin(username, password);

        if (success) {
            intent.putExtra("currentUserAccount", currentUserAccount);
            makeToastAcceptedText();
            startActivity(intent);
        } else {
            makeToastFailedText();
        }
    }

    /**
     * Display that the login was successful.
     */
    private void makeToastAcceptedText() {
        Toast.makeText(this, "Credentials accepted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the login was unsuccessful.
     */
    private void makeToastFailedText() {
        Toast.makeText(this, "Try Again or Sign up", Toast.LENGTH_LONG).show();
    }

    /**
     * Called when the user taps the sign up button.
     */
    private void addSignupButtonListener() {
        Button signupButton = findViewById(R.id.Signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView = findViewById(R.id.Username);
                EditText passwordView = findViewById(R.id.Password);
                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();
                boolean success = successfulSignUp(username, password);
                if (success) {
                    makeToastSUAcceptedText();
                } else {
                    makeToastExistsText();
                }
            }
        });
    }

    /**
     * Display that a signup was successful.
     */
    private void makeToastSUAcceptedText() {
        Toast.makeText(this, "Sign Up successful. Please Login to continue.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that a signup failed because user already exists.
     */
    private void makeToastExistsText() {
        Toast.makeText(this, "Account already exists.", Toast.LENGTH_SHORT).show();
    }
}
