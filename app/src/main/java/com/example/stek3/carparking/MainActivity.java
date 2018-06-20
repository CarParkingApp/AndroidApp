package com.example.stek3.carparking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stek3.carparking.Repository.UserRepo;

import static com.example.stek3.carparking.Users.CurrentUserID;

public class MainActivity extends AppCompatActivity {

    UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView NewUserButton=(TextView)findViewById(R.id.newusrlink);

        NewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegistrationIntent=new Intent(getBaseContext(),registration.class);

                startActivity(RegistrationIntent );
            }
        });

        userRepo=new UserRepo(this);

        Button LoginButton=(Button) findViewById(R.id.loginbtn);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Message="";

                if(userRepo.Login("danie@gmail.com","password")!=null){

                    Message="Successful";

                    Log.e("current userid2",String.valueOf(CurrentUserID));

                    Intent HomeIntent=new Intent(getBaseContext(),home.class);

                    startActivity(HomeIntent);

                }
                else {

                    Message="Not Successful";
                }

                Toast.makeText(getBaseContext(),Message,Toast.LENGTH_LONG).show();


            }
        });

    }
}
