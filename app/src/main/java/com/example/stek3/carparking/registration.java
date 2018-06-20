package com.example.stek3.carparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.stek3.carparking.Repository.UserRepo;

public class registration extends AppCompatActivity {

    EditText NINBox,UserNameBox,EmailBox,PhoneBox,PasswordBox,RepeatPasswordBox;
    Button RegisterButton;
        UserRepo userRepo=new UserRepo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        NINBox=(EditText)findViewById(R.id.ninEditText);
        UserNameBox=(EditText)findViewById(R.id.usernameEditText);
        EmailBox=(EditText)findViewById(R.id.emailEditText);
        PhoneBox=(EditText)findViewById(R.id.phonenumberEditText);
        PasswordBox=(EditText)findViewById(R.id.passwordEditText);
        RepeatPasswordBox=(EditText)findViewById(R.id.rptPasswordEditText);
        RegisterButton=(Button) findViewById(R.id.registerButton);


        RegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userRepo.RegisterUser(NINBox.getText().toString(),UserNameBox.getText().toString(),EmailBox.getText().toString(),PhoneBox.getText().toString(),PasswordBox.getText().toString());


                }
            });


    }
}
