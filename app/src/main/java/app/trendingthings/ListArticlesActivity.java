package app.trendingthings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by matei on 4/19/15.
 */
public class ListArticlesActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Bitmap picBitmap;

    public class TakeFromServer implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ParseQuery<ParseObject> getLastPicture = ParseQuery.getQuery("TestObject1");
            getLastPicture.orderByDescending("createdAt");
            getLastPicture.setLimit(1);
            getLastPicture.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(),parseObjects.get(0).getCreatedAt().toString(),Toast.LENGTH_LONG).show();
                        GetPicture(parseObjects.get(0));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void GetPicture(ParseObject latestObject){
            ParseFile objectPicture = (ParseFile)latestObject.get("picture");
            objectPicture.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(), "Successfully retreived" + bytes.length, Toast.LENGTH_LONG).show();
                        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public class SavePhoto implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ParseObject toSave = new ParseObject("TestObject1");
            ParseUser currentUser = ((MyApplication)getApplication()).currentUser;
            toSave.put("User",currentUser.getUsername());
            toSave.put("Message","Test salvare obiect");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] byteArray = stream.toByteArray();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            ParseFile picture = new ParseFile(timeStamp + ".jpg", byteArray);

            picture.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(getApplicationContext(), "Fisier salvat " + byteArray.length, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            toSave.put("picture", picture);

            toSave.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(),"Obiect salvat cu success",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Eroare : " + e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public class TakePhoto implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }
                catch (IOException ex){
                    Toast.makeText(getApplicationContext(),ex.toString(), Toast.LENGTH_LONG).show();
                }

                if(photoFile != null){
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE );
                }else {
                    Toast.makeText(getApplicationContext(), "Could not find camera! ", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Could not find camera! ", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_articles);

        MyApplication myApp = (MyApplication)getApplication();
        ParseUser currentUser = myApp.currentUser;

        TextView helloUser = (TextView)findViewById(R.id.HelloUserTextView);
        helloUser.setText("Hi, " + currentUser.getUsername() + " nice to see you!");

        Button takePhoto = (Button)findViewById(R.id.TakePhotoButton);
        takePhoto.setOnClickListener(new TakePhoto());

        Button saveTest = (Button)findViewById(R.id.SaveObjectButton);
        saveTest.setOnClickListener(new SavePhoto());

        Button takeFromServer = (Button)findViewById(R.id.TakeFromServer);
        takeFromServer.setOnClickListener(new TakeFromServer());

        //Toast.makeText(getApplicationContext(),"From Article List " + currentUser.getUsername(),Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add menu to top of activity from menu_trending_things
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trending_things, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_signOut:
                SignOutUser();
                NavigateToLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SignOutUser(){
        ParseUser.logOutInBackground();
        MyApplication myApp = (MyApplication)getApplication();
        myApp.currentUser = null;
    }

    private void NavigateToLogin(){
        Intent navigateToLogin = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(navigateToLogin);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(),mCurrentPhotoPath,Toast.LENGTH_LONG).show();
            GalleryAddPic();
            SetPicture();
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap)extras.get("data");
            //ImageView resultPicture = (ImageView)findViewById(R.id.imageView);
            //resultPicture.setImageBitmap(imageBitmap);
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void GalleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(getApplicationContext(),"Send intent for scann",Toast.LENGTH_LONG).show();
    }

    private void SetPicture(){
        ImageView myImageView = (ImageView)findViewById(R.id.imageView);

        int targetW = myImageView.getWidth();
        int targetH = myImageView.getHeight();

        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmpOptions);

        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;

        int scaleFactor = Math.min(photoH/targetH, photoW/targetW);

        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = scaleFactor;

        picBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmpOptions);

        myImageView.setImageBitmap(picBitmap);
        Toast.makeText(getApplicationContext(),"Set Picture complete " + picBitmap.getByteCount(),Toast.LENGTH_LONG).show();
    }
}
