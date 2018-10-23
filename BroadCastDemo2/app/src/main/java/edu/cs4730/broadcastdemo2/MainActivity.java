package edu.cs4730.broadcastdemo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Nothing to see here.  Just launchers the fragment.
 * the receiver and code is in MainFragment
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MainFragment()).commit();
        }
    }

}
