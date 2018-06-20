package com.example.stek3.carparking.Repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.stek3.carparking.MainActivity;
import com.example.stek3.carparking.Processors.Response;
import com.example.stek3.carparking.Processors.Server;
import com.example.stek3.carparking.SQLite.Contract;
import com.example.stek3.carparking.SQLite.DbHelper;
import com.example.stek3.carparking.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.stek3.carparking.Users.CurrentUserID;

/**
 * Created by Stek3 on 05-Mar-18.
 */

public class UserRepo
{
    URL url;
    HttpURLConnection conn;

    Context context;

    public UserRepo()
    {

    }

    public UserRepo(Context context){

        this.context=context;
    }
    public boolean SynchronizeUser(Users user)
    {
        DbHelper Database=new DbHelper(context);
        Database.InsertUser(user.getFirstName(),user.getLastName(),user.getMiddleName(),user.getUserName(),user.getEmail(),String.valueOf(user.getPhoneNumber()));


        return true;
    }

    public Users Login(String Email,String Password){

        Users LoggedInUser=new Users();

        Server.Get get=new Server.Get(context,"http://192.168.43.164/dcss/api/Security/login?Email="+Email+"&Password="+Password);

        JSONObject UserObject;


        try {
            UserObject =new JSONObject(get.execute().get());

            Log.e("current user object",UserObject.toString());

            int UserID=UserObject.getInt("ID");

           CurrentUserID=UserID;

            Log.e("current userid3",String.valueOf(CurrentUserID));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  LoggedInUser;
    }

    public  void  RegisterUser(String NIN,String UserName,String Email,String PhoneNumber,String Password)
    {
        try
        {
            url=new URL("http://192.168.43.164/dcss/api/Parking/users");

            Users user =new Users();

            user.setUserName(UserName);
            user.setEmail(Email);
            user.setPhoneNumber(PhoneNumber);

            JSONObject UserObject=new JSONObject();

            UserObject.put("UserName",user.getUserName());
            UserObject.put("Email",user.getEmail());
            UserObject.put("PhoneNumber",user.getPhoneNumber());
            UserObject.put("Password",Password);

            JSONObject NINObject=new JSONObject();
            NINObject.put("NIN",NIN);

            UserObject.put("NationalRegInfo",NINObject);

            Server server=new Server();

            Server.Post p=new Server.Post(context,url.toString());

            Response r =new Response();

            String result=p.execute(UserObject.toString()).get();

            JSONObject ResponseObject=new JSONObject(result);

            r.setMessage(ResponseObject.getString("Message"));
            r.setCode(ResponseObject.getInt("Code"));

           // boolean response=Boolean.getBoolean(p.getStatus().toString());

            Log.e("Response from post",String.valueOf(r.getCode()));

            if(r.getCode()==0)
            {
                Toast.makeText(context,r.getMessage()+System.lineSeparator()+"Please Check form",Toast.LENGTH_SHORT).show();
            }

            else
                {

                Toast.makeText(context,r.getMessage(),Toast.LENGTH_SHORT).show();

                Intent LoginIntent=new Intent(context, MainActivity.class);

                Activity thisActivity=(Activity)context;

                thisActivity.startActivity(LoginIntent);

            }



        }

        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public Users getUser(int ID) {

        Users user = new Users();

        String response=null;

            try
            {
                 url = new URL("http://192.168.43.164/dcss/api/Parking/users/"+ID);
                 conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = in.toString();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                System.out.println("Response: " + response);

                String output = null;

                while ((output = reader.readLine()) != null) {

                    sb.append(output);

                    System.out.println(sb);

                }

                Log.e("User JSON",sb.toString());

                JSONObject UserObject = new JSONObject(sb.toString());
                JSONObject NationalRegObject = UserObject.getJSONObject("NationalRegInfo");

                CurrentUserID=UserObject.getInt("ID");
                user.setFirstName(NationalRegObject.getString("FirstName"));
                user.setID(UserObject.getInt("ID"));
                user.setLastName(NationalRegObject.getString("LastName"));
                user.setEmail(UserObject.getString("Email"));
                user.setUserName(UserObject.getString("UserName"));

                Log.e("Current User 4",Integer.toString(CurrentUserID));

            } catch (Exception ex) {

                Log.e("Error For Me", ex.toString());
            }

        return  user;
    }
}
