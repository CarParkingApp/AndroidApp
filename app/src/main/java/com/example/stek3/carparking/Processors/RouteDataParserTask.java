package com.example.stek3.carparking.Processors;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stek3 on 24-Mar-18.
 */

public class RouteDataParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
{

    GoogleMap mMap=null;

    public RouteDataParserTask(GoogleMap map)
    {

        this.mMap=map;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("ParserTask",jsonData[0].toString());
            RouteDataParser parser = new RouteDataParser();
            Log.d("ParserTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("ParserTask","Executing routes");
            Log.d("ParserTask",routes.toString());

        } catch (Exception e) {
            Log.d("ParserTask",e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++)
            {
                HashMap<String, String> point = path.get(j);
if(point!=null && !point.containsKey("Direction")) {
    double lat = Double.parseDouble(point.get("lat"));
    double lng = Double.parseDouble(point.get("lng"));
    LatLng position = new LatLng(lat, lng);

    points.add(position);


}

                //Log.e("Points", points.get(j).toString());
            }


            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.BLUE);

            Log.d("onPostExecute","onPostExecute lineoptions decoded");
        }



        // Drawing polyline in the Google Map for the i-th route
//        if(lineOptions != null)
//        {
//            if(mMap!=null) {
//                mMap.addPolyline(lineOptions);
//                Log.e("Map Status","Polyline added to map");
//
//
//            }
//            else {
//                Log.e("Map Status","Map is null");
//            }
//        }
//        else
//            {
//            Log.d("onPostExecute","without Polylines drawn");
//        }
    }
}
