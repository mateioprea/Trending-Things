package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;


public class NewGroup extends Activity {

    private ParseUser currentUser;
    private boolean debug;

    class SaveGroupButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            TextView txt;
            String value;
            final String name;
            ParseObject newGroup = new ParseObject(Constants.GroupObject);

            txt = (EditText)findViewById(R.id.newGroupName);
            value = txt.getText().toString();
            name = value;
            if(value.isEmpty()){
                Toast.makeText(getApplicationContext(),"Completati numele grupului!",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                newGroup.put(Constants.GroupName,value);
            }

            txt = (EditText)findViewById(R.id.newGroupDescription);
            value = txt.getText().toString();
            if(value.isEmpty()){
                Toast.makeText(getApplicationContext(),"Completati descrierea grupului!",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                newGroup.put(Constants.GroupDescription,value);
            }

            DatePicker datePick;
            datePick = (DatePicker)findViewById(R.id.datePicker);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, datePick.getYear());
            cal.set(Calendar.MONTH, datePick.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, datePick.getDayOfMonth());

            Date chooseDate;
            chooseDate = cal.getTime();

            newGroup.put(Constants.GroupDate, chooseDate);

            newGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(),"Grup salvat cu success",Toast.LENGTH_SHORT).show();
                        currentUser.addUnique(Constants.UserGroups, name);
                        currentUser.saveInBackground();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Eroare " + e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    class CancelButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        currentUser = ((MyApplication)getApplication()).currentUser;
        debug = ((MyApplication)getApplication()).debug;

        ((Button)findViewById(R.id.buttonSaveNewGroup)).setOnClickListener(new SaveGroupButtonClick());

        ((Button)findViewById(R.id.buttonCancelNewGroup)).setOnClickListener(new CancelButtonClick());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);
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
