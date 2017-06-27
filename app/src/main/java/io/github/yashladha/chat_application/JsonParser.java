package io.github.yashladha.chat_application;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

final class JsonParser {

    private static String TAG = "Json Parser";

    public static ArrayList<String> getUsers (String data) {
        ArrayList<String> temp = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String uid = keys.next();
                Log.d("Test", keys.toString());
                Log.d("Key user received: ", uid);
                JSONObject status = (JSONObject) object.getJSONObject(uid).get("status");
                if ((boolean) status.get("Alive"))
                    temp.add(uid);
            }
            Log.d("Users added: ", temp.toString());
        } catch (JSONException e) {
            Log.d("Json Parser: ", "Json Exception occured");
            e.printStackTrace();
        }
        return temp;
    }
}
