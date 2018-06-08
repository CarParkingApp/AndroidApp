package com.example.stek3.carparking.Repository;

import android.content.Context;
import android.util.Log;

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

    public  void  RegisterUser(String NIN,String UserName,String Email,String PhoneNumber,String Password)
    {
        try
        {
            url=new URL("http://192.168.43.164/dcss/api/Parking/users");
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept", "application/json");
//
//            OutputStreamWriter os=new OutputStreamWriter(conn.getOutputStream());

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

            p.execute(UserObject.toString());

            Log.e("Response from Regist",p.getStatus().toString());

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
                 url = new URL("http://192.168.43.164/dcss/api/Parking/users/1");
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

                JSONObject UserObject = new JSONObject(sb.toString());
                JSONObject NationalRegObject = UserObject.getJSONObject("NationalRegInfo");
                user.setFirstName(NationalRegObject.getString("FirstName"));
                user.setID(UserObject.getInt("ID"));
                user.setLastName(NationalRegObject.getString("LastName"));
                user.setEmail(UserObject.getString("Email"));
                user.setUserName(UserObject.getString("UserName"));


            } catch (Exception ex) {

                Log.e("Error For Me", ex.toString());
            }

        return  user;
    }
}
