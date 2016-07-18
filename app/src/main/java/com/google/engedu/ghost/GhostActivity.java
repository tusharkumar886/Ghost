package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String fragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try {
            dictionary = new SimpleDictionary(getApplicationContext().getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView status = (TextView) findViewById(R.id.gameStatus);
        TextView text =(TextView)findViewById(R.id.ghostText);
        // Do computer turn stuff then make it the user's turn again
        String string;
        if(fragment.length()==0){
            char a = (char)(random.nextInt(26)+97);
            fragment = new String();
            fragment = a + fragment;
            text.setText(fragment);
            userTurn = true;
            status.setText(USER_TURN);
            return;
        }

        if(fragment != null && fragment.length() >= 4){
            if(dictionary.isWord(fragment)){
                string = fragment + "is a word";
                text.setText(string);
                status.setText("Computer Wins");
                return;
            }
        }

        string = dictionary.getAnyWordStartingWith(fragment);

        if(string == null){
            string = "No more word can be formed with" + fragment + "_";
            text.setText(string);
            status.setText("Computer Wins");
            return;
        }

        if(fragment != null){
            char c = (char)(random.nextInt(26)+97);
            fragment = c + fragment;
            text.setText(fragment);
            userTurn = true;
            status.setText(USER_TURN);
            return;
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        fragment = new String();
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);

        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    /**
     * onKeyUp
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getUnicodeChar() < 'a' || event.getUnicodeChar() > 'z'){
            TextView status = (TextView)findViewById(R.id.gameStatus);
            status.setText("Invalid key.");
            return super.onKeyUp(keyCode, event);
        }else{
            TextView status = (TextView) findViewById(R.id.gameStatus);
            status.setText("Valid key.");
            fragment = (event.getDisplayLabel()+fragment.toLowerCase());
            TextView text = (TextView) findViewById(R.id.ghostText);
            text.setText(fragment);
            computerTurn();
            return true;
        }
    }

    /**
     * Handler for the "Challenge" button.
     * Checks whether or not the given fragment is a valid prefix
     *
     * @param view
     * @return true
     */
    public boolean challenge(View view) {
        TextView text = (TextView)findViewById(R.id.ghostText);
        TextView status = (TextView)findViewById(R.id.gameStatus);

        if (fragment.length() >= 4) {
            if (dictionary.isWord(fragment)){
                String a = fragment + " is a word!";
                text.setText(a);
                status.setText("Player wins");
            }
        }

        String s = dictionary.getAnyWordStartingWith(fragment);

        if ( s != null ) {
            s = "This is a valid word:\n" + s;
            text.setText(s);
            status.setText("Computer wins");
        } else {
            s = "No word can be formed with " + fragment + "_";
            text.setText(s);
            status.setText("Player wins");
        }
        return false;
    }
}