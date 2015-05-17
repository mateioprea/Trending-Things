package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LoginActivity extends Activity {

    private Button signUpButton;
    private Button loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Intent signupIntent;
    private boolean debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        debug = ((MyApplication)getApplication()).debug;

        ParseUser currentUser = ParseUser.getCurrentUser();
        //User is logged in => go to articles activity
        if (currentUser != null) {
            if(debug) {
                Toast.makeText(getApplicationContext(), currentUser.getUsername(), Toast.LENGTH_SHORT).show();
            }
            //save currentUser to Application for user in future activities without query
            SaveUserToApp(currentUser);
            GoToChoose();
            finish();
        }
        //User must login or signUp
        else {
            loginButton = (Button) findViewById(R.id.loginButton);
            signUpButton = (Button) findViewById(R.id.signupButton);
            usernameField = (EditText) findViewById(R.id.loginUsername);
            passwordField = (EditText) findViewById(R.id.loginPassword);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                                //TODO check if getCurrentUser is required or can use user returned by login
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                //save currentUser to Application for user in future activities without query
                                SaveUserToApp(currentUser);
                                GoToChoose();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Wrong username/password combo",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signupIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(signupIntent);
                }
            });
        }
    }

    //save currentUser to Application for user in future activities without query
    private void SaveUserToApp(ParseUser currentUser){
        MyApplication my = (MyApplication)getApplication();
        my.currentUser = currentUser;
    }

    private void GoToChoose(){
        Intent goToChoose = new Intent(getApplicationContext(), ChooseAction.class);
        startActivity(goToChoose);
    }

    @Override
    public void onDestroy() {
        //stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }
}
