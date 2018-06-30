package me.mwaldman.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.entity.mime.Header;
import me.mwaldman.flicks.models.Config;
import me.mwaldman.flicks.models.Movie;

import static me.mwaldman.flicks.MovieListActivity.API_BASE_URL;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    Config config;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    AsyncHttpClient client;
    ImageView glideScreen;

    public final static String TAG = "MovieDetailsActivity";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        glideScreen = (ImageView) findViewById(R.id.thumbnailHolder);
        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        client = new AsyncHttpClient();
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getBackdropPath());
        Glide.with(this)
                .load(imageUrl)
                .into(glideScreen);

        glideScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailsActivity.this.onClick();
            }
        });
    }

    //@OnClick(R.id.thumbnailHolder)
    protected void onClick() {
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";
        //request params
        RequestParams params = new RequestParams();
        params.put(MovieListActivity.API_KEY_PARAM, getString(R.string.api_key));
        //execute get request for JSON response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    String video = null;
                    JSONObject jsonObject = results.getJSONObject(0);
                    String key = jsonObject.getString("key");
                    //launch Intent
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("trailer", key);
                    startActivity(intent);

                } catch (JSONException e) {
                    logError("failed to parse now playing movies", e, true);
                }
            }


            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }
    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e(TAG, message, error);
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
