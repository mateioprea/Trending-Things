package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;
//modified for commit
//modified 2 for commit still learning git
/**
 * Created by matei on 4/19/15.
 */
public class SignUpActivity extends Activity {

    private Button signupSend;
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private String username;
    private String password;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signupSend = (Button) findViewById(R.id.signupButtonToParse);
        usernameField = (EditText)findViewById(R.id.loginUsernameInput);
        passwordField = (EditText)findViewById(R.id.loginUsernameInput);
        emailField = (EditText)findViewById(R.id.loginEmailInput);

        signupSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                email = emailField.getText().toString();

                if(CheckUserAndPassword(username, password) == true) {
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                //System.out.println("accepted");
                                Toast.makeText(getApplicationContext(),"User created",Toast.LENGTH_LONG).show();
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                Toast.makeText(getApplicationContext(),currentUser.getUsername(),Toast.LENGTH_LONG).show();
                                SaveUserToApp(currentUser);
                                GoToArticles();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "There was an error signing up."
                                                + e.getMessage()
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void GoToArticles(){
        Intent goToListArticles = new Intent(getApplicationContext(), ListArticlesActivity.class);
        startActivity(goToListArticles);
    }

    private void SaveUserToApp(ParseUser currentUser){
        MyApplication my = (MyApplication)getApplication();
        my.currentUser = currentUser;
    }

    public boolean CheckUserAndPassword(String user, String pass){
        String validationMessage = "Input cannot be emtpy: ";
        int validationsFailed = 0;
        if(user.isEmpty()){
            validationsFailed ++;
            validationMessage += "username ";
        }
        if(pass.isEmpty()){
            validationsFailed ++;
            if(validationsFailed == 2){
                validationMessage += ", ";
            }
            validationMessage += "password";
        }

        if(validationsFailed > 0){
            Toast.makeText(getApplicationContext(),validationMessage,Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
