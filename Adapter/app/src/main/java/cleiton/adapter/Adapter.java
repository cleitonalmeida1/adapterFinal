package cleiton.adapter;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Cleiton Gonçalves on 16/04/2016.
 */
public class Adapter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }
}
