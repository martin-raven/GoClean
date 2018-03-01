package com.envyus.goclean;

/**
 * Created by seby on 3/2/2018.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by parth on 6/28/17.
 */

public class GetJsonObject {

    public static JSONObject getJsonObjectFromUrl(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;


        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            String finalObject = buffer.toString();
            JSONObject parentObject = new JSONObject(finalObject);
            return parentObject;
        } catch (MalformedURLException e) {
            e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        } catch (JSONException e) {
            e.getMessage();
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

