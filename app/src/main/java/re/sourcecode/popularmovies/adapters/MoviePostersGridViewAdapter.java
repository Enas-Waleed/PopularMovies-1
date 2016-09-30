package re.sourcecode.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.models.MovieParcelable;

public class MoviePostersGridViewAdapter extends BaseAdapter {

    private static final String LOG_TAG = MoviePostersGridViewAdapter.class.getSimpleName();
    private final Context context;
    private ArrayList<MovieParcelable> movies;


    public MoviePostersGridViewAdapter(Context context, ArrayList movies) {
        this.context = context; //context in constructor
        this.movies = movies; //the arraylist of movies from the fragment
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView; //cast the convertview to imageview
        //if no view, set the settings for it
        if (view == null) {
            view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER);
            view.setPadding(0, 0, 0, 0);
            view.setAdjustViewBounds(true);
        }
        //preserve aspect ratio
        view.setAdjustViewBounds(true);
        //get the image URL for the current position from parcable getter
        String posterFileName = getItem(position).getPosterFileName();
        //create uri for image.
        Uri buildUri = Uri.parse(context.getResources().getString(R.string.moviedb_poster_url)).buildUpon()
                .appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w185)) // TODO: change this based on screen size
                .appendPath(posterFileName.substring(1)).build(); // remove the first char of the file name since it is a /

        Log.d(LOG_TAG, "Getting image from " + buildUri.toString());
        //trigger the download of the URL asynchronously into the image view. TODO: save the images in the parcable some how.
        Picasso.with(context) //
                .load(buildUri.toString()) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(context) //
                .into(view);

        return view;
    }

    @Override
    public int getCount() {
        //get the size of the movie array
        return movies.size();
    }

    @Override
    public MovieParcelable getItem(int position) {
        //return the current movie parcable from the position in the grid
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return the postion id number
        return position;
    }

    public void add(MovieParcelable m) {
        //add a movie parcable to the arraylist of movies.
        movies.add(m);
    }

    public void clear() {
        //clear the array.
        Log.d(LOG_TAG, "ArrayList cleared");
        movies.clear();

    }

}