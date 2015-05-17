package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ViewGroupGifts extends Activity {

    private List<ParseObject> giftsInGroup;
    private ParseObject group;

    private void LoadGroup(String groupId){
        ParseQuery getGroup = new ParseQuery(Constants.GroupObject);
        getGroup.getInBackground(groupId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    group = parseObject;
                    LoadGifts(parseObject);
                }
                else{
                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void LoadGifts(final ParseObject group){
        ParseQuery giftsInGroup = group.getRelation(Constants.GroupRelationGift).getQuery();
        giftsInGroup.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List list, ParseException e) {
                if(e == null) {
                    ((TextView)findViewById(R.id.gitfsInGroupCount)).setText(group.get(Constants.GroupName).toString() +
                            "\n" + "Au fost gasite : " + list.size() + " cadouri" );
                    //Toast.makeText(getApplicationContext(), "Au fost gasite :" + list.size() + " cadouri", Toast.LENGTH_SHORT).show();
                    LoadGiftToListView(list);
                }
                else{
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadGiftToListView(List<ParseObject> gifts){
        giftsInGroup = gifts;
        ArrayList<String> giftsToString = new ArrayList<>();
        for(int i = 0; i < gifts.size(); i++){
            giftsToString.add(gifts.get(i).get(Constants.GiftName).toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, giftsToString);
        ((ListView)findViewById(R.id.giftsInGroupListView)).setAdapter(adapter);
        ((ListView)findViewById(R.id.giftsInGroupListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String giftId = giftsInGroup.get(position).getObjectId();
                Intent goToGiftView = new Intent(getApplicationContext(), ViewGift.class);
                goToGiftView.putExtra(Constants.GiftId, giftId);
                goToGiftView.putExtra(Constants.FromGroupGifts, "True");
                startActivity(goToGiftView);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_gifts);

        Intent received = getIntent();

        LoadGroup(received.getStringExtra(Constants.ViewGiftsGroupId));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_group_gifts, menu);
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
