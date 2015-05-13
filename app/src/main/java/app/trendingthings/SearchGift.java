package app.trendingthings;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SearchGift extends Activity {

    private List<ParseObject> giftCategories;

    private String[] sexes = {"Masculin", "Feminim", "Unisex"};

    class SearchGiftClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ParseQuery getGifts = new ParseQuery(Constants.GiftObject);

            Spinner spinnerCat = (Spinner)findViewById(R.id.searchSpinnerCategorie);
            String category = spinnerCat.getSelectedItem().toString();

            if(category.equals("Toate") == false){
                getGifts.whereEqualTo(Constants.GiftCategory, category);
            }

            Spinner spinnerSex = (Spinner)findViewById(R.id.searchSpinnerSex);
            String sex = spinnerSex.getSelectedItem().toString();

            if(sex.equals("Unisex") == false) {
                getGifts.whereEqualTo(Constants.GiftPersonSex, sex);
            }

            EditText numericValue;
            String value;

            numericValue = (EditText)findViewById(R.id.searchAgeInputMax);
            value = numericValue.getText().toString();
            if(value.isEmpty() == false){
                getGifts.whereLessThanOrEqualTo(Constants.GiftAge, Integer.parseInt(value));
            }

            numericValue = (EditText)findViewById(R.id.searchAgeInputMin);
            value = numericValue.getText().toString();
            if(value.isEmpty() == false){
                getGifts.whereGreaterThanOrEqualTo(Constants.GiftAge, Integer.parseInt(value));
            }

            numericValue = (EditText)findViewById(R.id.searchPretInputMin);
            value = numericValue.getText().toString();
            if(value.isEmpty() == false){
                getGifts.whereGreaterThanOrEqualTo(Constants.GiftPrice, Integer.parseInt(value));
            }

            numericValue = (EditText)findViewById(R.id.searchPretInputMax);
            value = numericValue.getText().toString();
            if(value.isEmpty() == false){
                getGifts.whereLessThanOrEqualTo(Constants.GiftPrice, Integer.parseInt(value));
            }

            getGifts.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if(e == null) {
                        Toast.makeText(getApplicationContext(), i + "", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void LoadCategories(){
        ParseQuery<ParseObject> getCategories = ParseQuery.getQuery(Constants.Categories);
        getCategories.orderByAscending(Constants.CategoryId);
        getCategories.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    giftCategories = parseObjects;
                    SetCategoriesSpinnerValues(giftCategories);
                }
                else{
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SetCategoriesSpinnerValues(List<ParseObject> values){

        Spinner spinnerCategory = (Spinner)findViewById(R.id.searchSpinnerCategorie);
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("Toate");
        for(int i = 0; i < values.size(); i++){
            stringValues.add(values.get(i).get(Constants.CategoryDescription).toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringValues);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }

    private void LoadSex(){
        Spinner spinnerSex = (Spinner)findViewById(R.id.searchSpinnerSex);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sexes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_gift);

        LoadCategories();
        LoadSex();

        ((Button)findViewById(R.id.searchButtonGift)).setOnClickListener(new SearchGiftClick());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_gift, menu);
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
