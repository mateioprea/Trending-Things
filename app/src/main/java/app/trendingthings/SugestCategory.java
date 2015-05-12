package app.trendingthings;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class SugestCategory extends Activity {

    private boolean debug;

    class SaveSuggestedCategoryClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            EditText txt = (EditText)findViewById(R.id.suggestedCategoryName);
            if(txt.getText().toString().isEmpty()){
                if(debug) {
                    Toast.makeText(getApplicationContext(), "Completati numele categoriei", Toast.LENGTH_LONG).show();
                }
            }
            else{
                ParseObject newSuggestedCategory = new ParseObject(Constants.SuggestedCategoryObject);
                newSuggestedCategory.put(Constants.SuggestedCategoryName, txt.getText().toString());
                newSuggestedCategory.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(), "Sugestie salvata.", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Eroare " + e.toString(), Toast.LENGTH_LONG).show();

                        }
                        finish();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugest_category);

        debug = ((MyApplication)getApplication()).debug;

        ((Button)findViewById(R.id.SuggestCategorySaveButton)).setOnClickListener(new SaveSuggestedCategoryClick());
        ((Button)findViewById(R.id.SuggestCategoryCancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sugest_category, menu);
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
