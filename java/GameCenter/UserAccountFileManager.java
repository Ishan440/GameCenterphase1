package fall2018.csc2017.GameCenter;

import android.util.Log;
import android.support.v7.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;


/**
 * Processes reading and writing to the file containing an ArrayList of UserAccounts.
 */
public class UserAccountFileManager extends AppCompatActivity {

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
}
