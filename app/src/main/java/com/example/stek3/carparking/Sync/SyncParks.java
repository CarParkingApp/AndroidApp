package com.example.stek3.carparking.Sync;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.stek3.carparking.Interfaces.ParkRepoResponse;
import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.Processors.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Stek3 on 09-Apr-18.
 */
public class SyncParks extends AsyncTask<Void, Void, List<Park>> {

    Response rsp=new Response();

    List<Park> Parks = new ArrayList<>();

    Context context;
    Timer tm;

    public ParkRepoResponse delegate = null;

    public SyncParks() {

    }

    public SyncParks(Context context){
        this.context=context;
    }

    @Override
    protected List<Park> doInBackground(Void... params) {

        tm=new Timer();

        int count = 0;


        tm.schedule(new TimerTask() {


            @Override
            public void run() {

                Parks=new ArrayList<Park>();

                HttpURLConnection Con = null;
                JSONObject ParkObject = new JSONObject();
                StringBuilder sb = new StringBuilder();

                Log.e("Run", "Running");

                try {

                    URL url = new URL("http://192.168.43.164/dcss/api/Parking/Parks?lat=0.347596&lng=32.582520");
                    Con = (HttpURLConnection) url.openConnection();
                    Park finalPark;

                    Con.setRequestMethod("GET");
                    Con.setRequestProperty("Content-Type", "application/json");
                    Con.setRequestProperty("Accept", "application/json");

                    //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Con.getOutputStream());
                    //outputStreamWriter.write(ParkObject.toString());
                    //outputStreamWriter.flush();

                    Log.e("Response Status: ", Con.getResponseMessage());

                    sb = new StringBuilder();
                    if (Con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Log.e("Run", "Running");
                        InputStreamReader streamReader = new InputStreamReader(Con.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;

                        while ((response = bufferedReader.readLine()) != null) {
                            sb.append(response);

                            System.out.println(sb);
                        }
                       bufferedReader.close();

                        Log.e("My Parks JSON2:", sb.toString());


                        //Configure JSON DATA Here

                        JSONObject Data = new JSONObject(sb.toString());

                        JSONArray DataValues=null;

                        int Code=Data.getInt("Code");

                        if(Code==404)
                        {

                            rsp.setCode(404);
                            rsp.setMessage(Data.get("Message").toString());

                           // Looper.prepare();
                           // Toast.makeText(context,Data.get("Message").toString(),Toast.LENGTH_LONG).show();
                            Log.e("My Parks code:", "hello");

                        }
                        else {

                            rsp.setCode(400);
                            rsp.setMessage("NearBy Places have been Saveed");

                            if (Data.isNull("Values")) {

                                DataValues = null;
                            } else {

                                DataValues = Data.getJSONArray("Values");

                                for (int count = 0; count < DataValues.length(); count++) {
                                    JSONObject FinalParkObject = DataValues.getJSONObject(count);

                                    finalPark = new Park();

                                    finalPark.setName(FinalParkObject.getString("name"));
                                    finalPark.setReference(FinalParkObject.getString("reference"));
                                    finalPark.setPlaceID(FinalParkObject.getString("place_id"));


                                    JSONObject ParkData = FinalParkObject.getJSONObject("Park Data");

                                    finalPark.setID(ParkData.getInt("park_id"));

                                    byte[] decodedImage = Base64.decode(FinalParkObject.getString("place image"), Base64.DEFAULT);
                                    finalPark.setImage(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));

                                    Log.e("Final Image: ", FinalParkObject.getString("place image"));

                                    JSONObject FinalPlaceObject = ParkData.getJSONObject("Location");

                                    Park.Location finalParkLocation = new Park.Location();
                                    finalParkLocation.setVicinity(FinalPlaceObject.getString("name"));
                                    finalParkLocation.setLocationID(FinalPlaceObject.getInt("id"));
                                    finalParkLocation.setLatitude(FinalParkObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                    finalParkLocation.setLongitude(FinalParkObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));


                                    JSONObject FinalSpaceObject = ParkData.getJSONObject("Space");

                                    Park.Spaces finalParkSpace = new Park.Spaces();
                                    finalParkSpace.setSpaceID(FinalSpaceObject.getInt("space id"));
                                    finalParkSpace.setSpaceCount(FinalSpaceObject.getInt("space count"));
                                    finalParkSpace.setUsedSpaces(FinalSpaceObject.getInt("used spaces"));

                                    finalPark.setLocation(finalParkLocation);
                                    finalPark.setSpaces(finalParkSpace);


                                    Log.e("re", finalPark.getName());

                                    Parks.add(finalPark);
                                }
                            }
                        }
                    }
                    else

                        Log.e("testResponse", Con.getResponseMessage() + " " + String.valueOf(Con.getResponseCode()));



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (Con != null) {
                        Con.disconnect();
                    }
                }

            }
        },5000,5000);

        Log.e("Parks","jgjhk");
        Log.e("Parks",Parks.toString());

        return Parks;
    }

    @Override
    protected void onPostExecute(List<Park> resultPark) {

        if(rsp.getCode()==404){

            Log.e("rsp code",String.valueOf(rsp.getCode()));
            Toast.makeText(context,rsp.getMessage(),Toast.LENGTH_LONG).show();
        }

       // super.onPostExecute(resultPark);

    }
}