package app.trendingthings;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ViewInvites extends Activity {

    private ParseUser currentUser;
    private List<ParseObject> InvitesFromServer;
    private boolean debug;

    private void LoadInvites(){
        ParseQuery getInvites = new ParseQuery(Constants.InviteObject);
        getInvites.whereContains(Constants.InviteUsername, currentUser.getUsername());
        getInvites.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    if(debug) {
                        Toast.makeText(getApplicationContext(), "From server: " + parseObjects.size(), Toast.LENGTH_SHORT).show();
                    }
                    InvitesFromServer = parseObjects;
                    ArrayList<String> invites = new ArrayList<String>();
                    ParseObject current;
                    for(int i = 0; i < parseObjects.size(); i++){
                        current = parseObjects.get(i);
                        invites.add("In grupul: " + current.get(Constants.InviteGroupName).toString() + "\nInvitat de: " +
                                    current.get(Constants.InviteByUser).toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
                            invites);

                    ((ListView)findViewById(R.id.viewInvitesListView)).setAdapter(adapter);
                    ((ListView)findViewById(R.id.viewInvitesListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final ParseObject invite = InvitesFromServer.get(position);
                            currentUser.addUnique(Constants.UserGroups, invite.get(Constants.InviteGroupName).toString());
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getApplicationContext(),"Invitatie acceptata",Toast.LENGTH_LONG).show();
                                        invite.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                LoadInvites();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Eroare: " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Eroare " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        currentUser = ((MyApplication)getApplication()).currentUser;
        debug = ((MyApplication)getApplication()).debug;
        LoadInvites();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_invites, menu);
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
