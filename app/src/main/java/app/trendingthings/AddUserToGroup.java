package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class AddUserToGroup extends Activity {

    private String groupId;
    private ParseObject currentGroup;
    ArrayList<String> resultUsers;
    private boolean debug;

    class SearchButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String searchUser = ((EditText)findViewById(R.id.searchUserEditText)).getText().toString();

            ParseQuery<ParseUser> searchQuery = ParseUser.getQuery();
            searchQuery.whereContains("username",searchUser);
            searchQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if(e == null){
                        if(debug) {
                            Toast.makeText(getApplicationContext(), parseUsers.size() + "", Toast.LENGTH_SHORT).show();
                        }
                        ListView userList = (ListView)findViewById(R.id.userListView);
                        resultUsers = new ArrayList<String>();
                        for(int i = 0; i < parseUsers.size(); i++){
                            resultUsers.add(parseUsers.get(i).get("username").toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1,resultUsers);
                        userList.setAdapter(adapter);
                        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject invite = new ParseObject(Constants.InviteObject);
                                String byUser = ((MyApplication)getApplication()).currentUser.getUsername();
                                final String userName = resultUsers.get(position);
                                invite.put(Constants.InviteGroupId, groupId);
                                invite.put(Constants.InviteGroupName, currentGroup.get(Constants.GroupName));
                                invite.put(Constants.InviteUsername, userName);
                                invite.put(Constants.InviteByUser, byUser);
                                invite.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Toast.makeText(getApplicationContext(),"Userul " + userName + " a fost invitat", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void LoadGroup(String objId){
        ParseQuery getGroup = new ParseQuery(Constants.GroupObject);
        getGroup.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    currentGroup = parseObject;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);

        debug = ((MyApplication)getApplication()).debug;

        Intent received = getIntent();
        groupId = received.getStringExtra(Constants.GroupId);

        LoadGroup(groupId);

        ((Button)findViewById(R.id.searchUserButton)).setOnClickListener(new SearchButtonClick());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_user_to_group, menu);
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
