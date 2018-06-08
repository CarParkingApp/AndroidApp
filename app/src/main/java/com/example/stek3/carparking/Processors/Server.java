package com.example.stek3.carparking.Processors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.StringEntityHC4;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static java.io.FileDescriptor.in;

/**
 * Created by Juliet on 08-Jun-18.
 */

public class Server {


    public static class Get extends AsyncTask<Void,Void,String> {

        String url = "";
        Context context;


        public Get(Context context, String URL) {
            this.context = context;
            this.url = URL;
        }

        @Override
        protected String doInBackground(Void... params) {


            String result = null;
            InputStream inputStream = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet request = new HttpGet();
                URI website = new URI(url);
                request.setURI(website);
                HttpResponse httpResponse = httpclient.execute(request);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if (inputStream != null) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;

                    inputStream.close();

                } else {
                    result = "Did not work!";
                }

            } catch (Exception e) {

                Log.e("Response",e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

           // Toast.makeText(context,s,Toast.LENGTH_LONG).show();

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
                    result = "Did not work!";
                }


            } catch (Exception e) {

                Log.e("InputStream", e.getLocalizedMessage());
            }

            Log.e("resp",result);
            return result;
        }



        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        }
    }
}
