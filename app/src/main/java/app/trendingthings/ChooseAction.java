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

    private boolean debug;

    class GroupsClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent GoToGroups = new Intent(getApplicationContext(), MyGroups.class);
            startActivity(GoToGroups);
        }
    }

    class SuggestClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent GoToSuggest = new Intent(getApplicationContext(), SugestCategory.class);
            startActivity(GoToSuggest);
        }
    }

    class AddGiftClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent goToListArticles = new Intent(getApplicationContext(), ListArticlesActivity.class);
            startActivity(goToListArticles);
        }
    }

    class SearchGiftClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent goToSearch = new Intent(getApplicationContext(), SearchGift.class);
            startActivity(goToSearch);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        debug = ((MyApplication)getApplication()).debug;

        ((Button)findViewById(R.id.buttonAddGift)).setOnClickListener(new AddGiftClick());

        ((Button)findViewById(R.id.buttonSearchGift)).setOnClickListener(new SearchGiftClick());

        ((Button)findViewById(R.id.buttonCreateGroup)).setOnClickListener(new GroupsClick());

        ((Button)findViewById(R.id.buttonSuggest)).setOnClickListener(new SuggestClick());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_action, menu);
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
