package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class GroupView extends Activity {

    private ParseObject currentGroup;
    private String objId;
    private boolean debug;

    private void LoadComments(){
        ParseRelation<ParseObject> comments =  currentGroup.getRelation(Constants.GropuRelationComments);
        comments.getQuery().orderByAscending("createdAt").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    SetCommentsToListView(parseObjects);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SetCommentsToListView(List<ParseObject> values){
        ArrayList<String> comments = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            comments.add(values.get(i).get(Constants.CommentContent).toString());
        }

        ListView commentsListView = (ListView)findViewById(R.id.GroupViewCommentsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, comments);
        commentsListView.setAdapter(adapter);
        commentsListView.setSelection(comments.size() - 1);
    }

    private void LoadGifts(){
        ParseRelation<ParseObject> giftsInGroup = currentGroup.getRelation(Constants.GroupRelationGift);
        if(giftsInGroup != null) {
            //ParseQuery<ParseObject> gifts = giftsInGroup.getQuery();
            //gifts.countInBackground(new CountCallback() {
            giftsInGroup.getQuery().countInBackground(new CountCallback() {
                public void done(int i, ParseException e) {
                    if (e == null) {
                        if(debug) {
                            Toast.makeText(getApplicationContext(), "Cadouri in grup " + i, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "cad " + e.toString(), Toast.LENGTH_LONG).show();
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
                    ((TextView)findViewById(R.id.GropuViewName)).setText(parseObject.getString(Constants.GroupName));
                    ((TextView)findViewById(R.id.GroupViewDescription)).setText(parseObject.getString(Constants.GroupDescription));
                    LoadComments();
                    LoadGifts();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Eroare :" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class AddCommentButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String txt = ((EditText)findViewById(R.id.GroupViewAddCommentText)).getText().toString();
            ((EditText)findViewById(R.id.GroupViewAddCommentText)).setText("");
            if(txt.isEmpty()){
                Toast.makeText(getApplicationContext(),"Comentariul nu poate fi gol!",Toast.LENGTH_SHORT).show();
            }
            else{
                ParseRelation<ParseObject> comments =  currentGroup.getRelation(Constants.GropuRelationComments);
                ParseObject newComment = new ParseObject(Constants.CommentObject);
                newComment.put(Constants.CommentContent,((MyApplication)getApplication()).currentUser.getUsername() + " : " + txt);
                comments.add(newComment);
                newComment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            currentGroup.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getApplicationContext(),"Comentariu salvat", Toast.LENGTH_SHORT).show();
                                        LoadComments();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Eroare: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
    class ViewGiftsButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent GotoViewGifts = new Intent(getApplicationContext(),ViewGroupGifts.class);
            GotoViewGifts.putExtra(Constants.ViewGiftsGroupId, currentGroup.getObjectId());
            startActivity(GotoViewGifts);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        debug = ((MyApplication)getApplication()).debug;

        Intent startIntent = getIntent();
        if(startIntent != null){
            objId = startIntent.getStringExtra(Constants.GroupToView);
            LoadGroup(objId);
            if(debug) {
                Toast.makeText(getApplicationContext(), objId, Toast.LENGTH_LONG).show();
            }
        }
        ((Button)findViewById(R.id.viewGiftsBtn)).setOnClickListener(new ViewGiftsButtonClick());
        ((Button)findViewById(R.id.GroupViewAddCommentButton)).setOnClickListener(new AddCommentButtonClick());

        ((Button)findViewById(R.id.GroupViewAddNewUserButton)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent GoToAddUser = new Intent(getApplicationContext(),AddUserToGroup.class);
                GoToAddUser.putExtra(Constants.GroupId, objId);
                startActivity(GoToAddUser);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_view, menu);
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
