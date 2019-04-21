package weshare.groupfour.derek.util;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Holder.initial(this);
    }
}
