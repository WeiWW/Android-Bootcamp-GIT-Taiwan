package com.tobey.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tobeyyang on 11/12/2016.
 */

public class Article implements Serializable {
    String webUrl;
    String headline;
    String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            }else {
                this.thumbNail = "";
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<Article>();

        try{
            for(int i = 0; i < array.length(); i++){
                results.add(new Article(array.getJSONObject(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return results;
    }
}
