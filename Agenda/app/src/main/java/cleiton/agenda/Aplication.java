package cleiton.agenda;

import android.app.Application;

/**
 * Created by Cleiton Gonçalves on 18/04/2016.
 */
public class Aplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.firebase.client.Firebase.setAndroidContext(this);

    }
}