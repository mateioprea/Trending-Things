package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyGroups extends Activity {

    private final int ReqCode = 404;
    private List<ParseObject> myGroups;
    private ParseUser currentUser;
    private boolean debug;
    private boolean goToInvites = true;

    private boolean hasGift = false;
    private String giftId;

    class ViewInvitesClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(goToInvites) {
                Intent goToViewInvites = new Intent(getApplicationContext(), ViewInvites.class);
                startActivity(goToViewInvites);
            }
        }
    }

    class NewGroupClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent goToNewCreateNewGroup = new Intent(getApplicationContext() ,NewGroup.class);
            startActivityForResult(goToNewCreateNewGroup,ReqCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        Intent receivedIntent = getIntent();
        if(receivedIntent != null){
            String lala = receivedIntent.getStringExtra(Constants.Action);
            if(lala != null) {
                if (lala.equals(Constants.Recommend)){
                    ((Button) findViewById(R.id.buttonCreateNew)).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.viewInvitesButton)).setVisibility(View.GONE);
                    hasGift = true;
                    giftId = receivedIntent.getStringExtra(Constants.GiftId);
                }
            }
        }
        else{
            ((TextView)findViewById(R.id.chooseGroupText)).setVisibility(View.GONE);
        }

        currentUser = ((MyApplication)getApplication()).currentUser;
        debug = ((MyApplication)getApplication()).debug;

        ((Button)findViewById(R.id.buttonCreateNew)).setOnClickListener(new NewGroupClick());
        ((Button)findViewById(R.id.viewInvitesButton)).setOnClickListener(new ViewInvitesClick());
        PopulateGroupList();
        CheckForInvites();
    }

    private void SetValuesOnListView(List<ParseObject> values){
        myGroups = values;
        Date current = Calendar.getInstance().getTime();
        ArrayList<String> userGroups = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            userGroups.add((values.get(i)).get(Constants.GroupName).toString() + "\n" + "\t" +
                    (((Date)values.get(i).get(Constants.GroupDate)).getTime() - current.getTime())/(24 * 60 * 60 * 1000) +
            " zile ramase");
        }

        ListView groups = (ListView)findViewById(R.id.listViewGroups);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, userGroups);
        groups.setAdapter(adapter);

        groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), myGroups.get(position).getString(Constants.GroupName), Toast.LENGTH_LONG).show();
                if(hasGift){
                    final int pos = position;
                    ParseQuery getGift = new ParseQuery(Constants.GiftObject);
                    getGift.getInBackground(giftId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                ParseRelation<ParseObject> giftsOnGroup = myGroups.get(pos).getRelation(Constants.GroupRelationGift);
                                giftsOnGroup.add(parseObject);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Toast.makeText(getApplicationContext(),"Cadou sugerat in grup", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Gift-group " + e.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                myGroups.get(pos).saveInBackground();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"get gift" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                else {
                    Intent goToGroupView = new Intent(getApplicationContext(), GroupView.class);
                    goToGroupView.putExtra(Constants.GroupToView, myGroups.get(position).getObjectId());
                    startActivity(goToGroupView);
                }
            }
        });
    }

    private void CheckForInvites(){
        ParseUser current = ((MyApplication)getApplication()).currentUser;
        ParseQuery countInvites = new ParseQuery(Constants.InviteObject);
        countInvites.whereContains(Constants.InviteUsername, current.getUsername());
        int x = 0;
        try {
            x = countInvites.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String buttonText;
        if( x == 0){
            buttonText = "Nu aveti invitatii active";
            goToInvites = false;
        }
        else{
            buttonText = "Aveti " + x + " invitatii active";
        }


        ((Button)findViewById(R.id.viewInvitesButton)).setText(buttonText);
    }

    private void PopulateGroupList(){
        List<ParseObject> groupsFromUser = currentUser.getList(Constants.UserGroups);
        if(groupsFromUser != null) {
            if(debug) {
                Toast.makeText(getApplicationContext(), "Grupuri de la user " + groupsFromUser.size(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            groupsFromUser = new ArrayList<ParseObject>();
        }

        ParseQuery<ParseObject> getGroups = ParseQuery.getQuery(Constants.GroupObject);
        getGroups.orderByAscending(Constants.GroupDate);
        getGroups.whereContainedIn(Constants.GroupName,groupsFromUser);
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
    protected void onResume() {
        super.onResume();
        PopulateGroupList();
        CheckForInvites();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            PopulateGroupList();
            if(debug) {
                Toast.makeText(getApplicationContext(), "Grup creat" + requestCode, Toast.LENGTH_LONG).show();
            }
        }
        else{
            if(debug) {
                Toast.makeText(getApplicationContext(), "Grup anulat" + requestCode, Toast.LENGTH_LONG).show();
            }
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
