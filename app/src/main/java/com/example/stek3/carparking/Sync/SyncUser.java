package com.example.stek3.carparking.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stek3.carparking.Repository.UserRepo;
import com.example.stek3.carparking.SQLite.DbHelper;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.stek3.carparking.Users.CurrentUserID;

/**
 * Created by Stek3 on 02-Apr-18.
 */

public class SyncUser extends AsyncTask<Void,Void,Boolean> {

Context context;
    Timer tm;

    public SyncUser(){

    }

    public SyncUser(Context context){
        this.context=context;
    }

    public void Stop(){

        if(tm != null) {
            tm.cancel();
            tm = null;
        }
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {

        tm=new Timer();
        tm.schedule(new TimerTask() {
            int count=0;

            @Override
            public void run() {

                UserRepo Repo=new UserRepo(context);
                Repo.SynchronizeUser(Repo.getUser(CurrentUserID));

                Log.e("Synchronise User","Success");

                Log.e("Timer",String.valueOf(count+=1));

               // DbHelper helper=new DbHelper(context);

                //Log.e("Saved User 3",helper.getUser().getFirstName());

            }
        },5000,2000);

        return null;
    }

}
