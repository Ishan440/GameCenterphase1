package fall2018.csc2017.GameCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * The game select screen shown after login screen.
 */
public class GameSelectScreen extends AppCompatActivity {

    /**
     * The current user account obtained from the login screen.
     */
    private UserAccount currentUserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserAccount = (UserAccount) getIntent().getSerializableExtra("currentUserAccount");
        setContentView(R.layout.activity_game_select);
        addSlidingTilesButtonListener();
    }

    /**
     * Activate the sliding tiles button.
     */
    private void addSlidingTilesButtonListener() {
        Button slidingTilesButton = findViewById(R.id.slidingTilesButton);
        slidingTilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StartingActivity.class);
                intent.putExtra("currentUserAccount", currentUserAccount);
                startActivity(intent);
            }
        });
    }
}
