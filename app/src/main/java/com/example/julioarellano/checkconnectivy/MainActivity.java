package com.example.julioarellano.checkconnectivy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo.State;


public class MainActivity extends AppCompatActivity {

    private Button checkConnection;
    private static final String DEBUG_TAG = "NetworkStatusExample";
    private boolean connectStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection = (Button) findViewById(R.id.checkButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null)
                    connectStatus = networkInfo.isConnected();
                else
                    AlertNetworkConnection();

                Log.d(DEBUG_TAG, "Connection Result");
            }
        };
        checkConnection.setOnClickListener(listener);

        BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();

                NetworkInfo info = (NetworkInfo) extras
                        .getParcelable("networkInfo");

                    State state = info.getState();
                    if (state == State.DISCONNECTED) {
                        ConnectivityManager cm =
                                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork == null)
                            AlertNetworkConnection();


                    }



            }
        };

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);


    }

    public void AlertNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Data Connection");
        builder.setMessage("Please check connection settings");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
