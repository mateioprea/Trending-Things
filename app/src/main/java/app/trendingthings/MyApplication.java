package app.trendingthings;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by matei on 4/19/15.
 */
public class MyApplication extends Application{

    public ParseUser currentUser = null;

    @Override public void onCreate() {
        super.onCreate();
        String ApplicationID = "R00AbrkG2MRHiSYt9oNKux0VaRHRWctg0XeWrE4y";
        String ClientKey = "6LHoCA6HfJpV0vaiwpCSTJLNUX21TS6Tq675b6NT";
        Parse.initialize(this, ApplicationID, ClientKey);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
