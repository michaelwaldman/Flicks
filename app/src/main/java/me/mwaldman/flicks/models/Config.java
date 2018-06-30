package me.mwaldman.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Config {
    String imageBaseUrl;

    //poster size
    String posterSize;
    String backdropSize;
    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop sizes and use the option at index 1 or w780 as fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }
    public Config(){

    }
    //helper for creating urls
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path);

    }
    public String getImageBaseUrl(){
        return imageBaseUrl;
    }
    public String getPosterSize(){
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
