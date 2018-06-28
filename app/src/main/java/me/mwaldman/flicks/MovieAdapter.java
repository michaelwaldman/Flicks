package me.mwaldman.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.mwaldman.flicks.models.Config;
import me.mwaldman.flicks.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    //initialize w/ list
    Config config;
    Context context;
    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }
    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context and create the inflater
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }
    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        //populate view w/ movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        //load the image using glide
        Glide.with(context).load(imageUrl).placeholder(R.drawable.flicks_movie_placeholder).error(R.drawable.flicks_movie_placeholder).into(holder.ivPosterImage);
    }
    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }
    }
}
