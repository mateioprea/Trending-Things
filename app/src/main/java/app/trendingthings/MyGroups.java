package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MyGroups extends Activity {

    private final int ReqCode = 404;

    class NewGroupClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent goToNewCreateNewGroup = new Intent(getApplicationContext() ,NewGroup.class);
            startActivityForResult(goToNewCreateNewGroup,ReqCode);
        }
    }

    String[] userGroups = new String[]{
            "Grup 1",
            "Grup 12",
            "Grup 13",
            "Grup 14",
            "Grup 15",
            "Grup 16",
            "Grup 17",
            "Grup 18",
            "Grup 19",
            "Grup 10",
            "Grup 11",
            "Grup 12"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        ((Button)findViewById(R.id.buttonCreateNew)).setOnClickListener(new NewGroupClick());

        PopulateGroupList();
    }

    private void SetValuesOnListView(List<ParseObject> values){
        ArrayList<String> userGroups = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            userGroups.add((values.get(i)).get(Constants.GroupName).toString() + "\n" +
                    values.get(i).get(Constants.GroupDate).toString());
        }

        ListView groups = (ListView)findViewById(R.id.listViewGroups);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, userGroups);
        groups.setAdapter(adapter);
    }

    private void PopulateGroupList(){
        ParseQuery<ParseObject> getGroups = ParseQuery.getQuery(Constants.GroupObject);
        getGroups.orderByDescending(Constants.GroupDate);
        getGroups.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    SetValuesOnListView(parseObjects);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Eroare " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            PopulateGroupList();
            Toast.makeText(getApplicationContext(),"Grup creat" + requestCode, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Grup anulat" + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_groups, menu);
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
