package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class ViewGift extends Activity {

    private ParseObject gift;
    private boolean debug;
    private boolean viewOnly = false;

    private void LoadFields(){
        ((TextView)findViewById(R.id.GiftNameView)).setText("   " + gift.get(Constants.GiftName).toString());
        ((TextView)findViewById(R.id.GiftDescriptionView)).setText("   " + gift.get(Constants.GiftDescription).toString());
        ((TextView)findViewById(R.id.GiftCategoryView)).setText("   " + gift.get(Constants.GiftCategory).toString());
        ((TextView)findViewById(R.id.GiftSexView)).setText("   " + gift.get(Constants.GiftPersonSex).toString());
        ((TextView)findViewById(R.id.GiftAgeView)).setText(gift.get(Constants.GiftAge).toString());
        ((TextView)findViewById(R.id.GiftPriceView)).setText(gift.get(Constants.GiftPrice).toString());
    }

    public void GetPicture(ParseObject latestObject){
        ParseFile objectPicture = (ParseFile)latestObject.get(Constants.GiftPicture);
        objectPicture.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if(e == null){
                    ((ImageView)findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
                else{
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void LoadGift(String objId){
        ParseQuery getGift = new ParseQuery(Constants.GiftObject);
        getGift.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
               if(e == null){
                   gift = parseObject;
                   GetPicture(parseObject);
                   LoadFields();
               }
                else{
                   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gift);

        debug = ((MyApplication)getApplication()).debug;

        Intent received = getIntent();

        LoadGift(received.getStringExtra(Constants.GiftId));
        ((RatingBar)findViewById(R.id.ratingBar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //TODO de facut rating
                if(debug) {
                    Toast.makeText(getApplicationContext(), rating + " ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((Button)findViewById(R.id.recommendButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String objId = gift.getObjectId();
                Intent chooseGroup = new Intent(getApplicationContext(), MyGroups.class);
                chooseGroup.putExtra(Constants.Action, Constants.Recommend);
                chooseGroup.putExtra(Constants.GiftId, objId);
                startActivity(chooseGroup);
                finish();
            }
        });

        ((Button)findViewById(R.id.backTosearchButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(received.getStringExtra(Constants.FromGroupGifts) != null){
            viewOnly = true;
            ((Button)findViewById(R.id.recommendButton)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.backTosearchButton)).setVisibility(View.GONE);
            ((RatingBar)findViewById(R.id.ratingBar)).setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_gift, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    protected void onStop() {
        super.onStop();
        if(viewOnly == false){
            double userRating = ((RatingBar)findViewById(R.id.ratingBar)).getRating();
            if(userRating > 0) {
                int currentRatingNumber = Integer.parseInt(gift.get(Constants.GiftVotes).toString());
                double rating = Double.parseDouble(gift.get(Constants.GiftRating).toString());
                double generalRating = Double.parseDouble(gift.get(Constants.GiftGeneralRating).toString());

                currentRatingNumber++;
                rating += userRating;
                generalRating = rating / currentRatingNumber;

                gift.put(Constants.GiftVotes, currentRatingNumber);
                gift.put(Constants.GiftRating, rating);
                gift.put(Constants.GiftGeneralRating, generalRating);
                gift.saveInBackground();
            }
        }
    }
}
