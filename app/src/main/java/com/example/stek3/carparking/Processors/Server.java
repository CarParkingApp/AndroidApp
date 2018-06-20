package com.example.stek3.carparking.Processors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



/**
 * Created by Juliet on 08-Jun-18.
 */

public class Server {


    public static class Get extends AsyncTask<Void,Void,String> {

        URL url;
        Context context;
        HttpURLConnection conn;
        String response;

        public Get(Context context, String URL){
            this.context = context;

            try
            {
                this.url = new URL(URL);

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {


            String result = "";

            Log.e("Running","True");

            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = in.toString();

                Log.e("Input Stream",in.toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                String output = null;

                while ((output = reader.readLine()) != null) {

                    sb.append(output);
                }

                result=sb.toString();


                Log.e("URL",url.toString());
                Log.e("Response from Server",response);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//
//                HttpGet request = new HttpGet();
//                URI website = new URI(url);
//                request.setURI(website);
//                HttpResponse httpResponse = httpclient.execute(request);
//
//
//
//                // 9. receive response as inputStream
//                inputStream = httpResponse.getEntity().getContent();
//
//                // 10. convert inputstream to string
//                if (inputStream != null) {
//
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder sb=new StringBuilder();
//                    String line=null;
//
//                    while ((line = bufferedReader.readLine()) != null)
//                        sb.append(line);
//
//                    result=sb.toString();
//
//                    inputStream.close();
//
//                } else {
//                    result = "Did not work!";
//                }
//
//            } catch (Exception e) {
//
//                Log.e("Response",e.toString());
//            }



            return result;



        }

        @Override
        protected void onPostExecute(String s) {

           Log.e("Result",s);

        }

    }

    public static class Post extends AsyncTask<String,Void,String> {

        Context context;

        String url="";

        public Post(Context context,String URL){

            this.context=context;
            this.url=URL;
        }

        InputStream inputStream = null;
        String result = "";

        @Override
        protected String doInBackground(String... params) {

            Response response=new Response();

            String JSON = params[0];

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);

                StringEntity stringEntity = new StringEntity(JSON);

                // 6. set httpPost Entity
                httpPost.setEntity(stringEntity);

                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpClient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null) {

                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                    String line = "";

                    while((line = bufferedReader.readLine()) != null)
                        result += line;

                    inputStream.close();


                }
                else {

                   result="Did not work!";
                }


            } catch (Exception e) {

                Log.e("InputStream", e.getLocalizedMessage());
            }

            Log.e("resp",result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

           // Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        }
    }
}
