package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ChooseAction extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_action, menu);

        ((Button)findViewById(R.id.buttonAddGift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToListArticles = new Intent(getApplicationContext(), ListArticlesActivity.class);
                startActivity(goToListArticles);
            }
        });

        ((Button)findViewById(R.id.buttonSearchGift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TODO view list of gifts",Toast.LENGTH_LONG).show();
            }
        });

        ((Button)findViewById(R.id.buttonCreateGroup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TODO create group for birthday",Toast.LENGTH_LONG).show();
            }
        });

        ((Button)findViewById(R.id.buttonSuggest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"TODO suggest categories of gifts",Toast.LENGTH_LONG).show();
            }
        });


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
}
