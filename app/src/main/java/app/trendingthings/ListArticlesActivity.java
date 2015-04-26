package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by matei on 4/19/15.
 */
public class ListArticlesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_articles);

        MyApplication myApp = (MyApplication)getApplication();
        ParseUser currentUser = myApp.currentUser;

        TextView helloUser = (TextView)findViewById(R.id.HelloUserTextView);
        helloUser.setText("Hi, " + currentUser.getUsername() + " nice to see you!");
        //Toast.makeText(getApplicationContext(),"From Article List " + currentUser.getUsername(),Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add menu to top of activity from menu_trending_things
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trending_things, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_signOut:
                SignOutUser();
                NavigateToLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SignOutUser(){
        ParseUser.logOutInBackground();
        MyApplication myApp = (MyApplication)getApplication();
        myApp.currentUser = null;
    }

    private void NavigateToLogin(){
        Intent navigateToLogin = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(navigateToLogin);

    }
}
