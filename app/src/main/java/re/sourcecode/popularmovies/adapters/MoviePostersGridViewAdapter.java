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

    private final String LOG_TAG = MoviePostersGridViewAdapter.class.getSimpleName();
    private final Context context;
    private final ArrayList<MovieParcelable> movies = new ArrayList<MovieParcelable>(); //ArrayList is a collection and do not need to be fixed length like a Array

    public MoviePostersGridViewAdapter(Context context) {
        this.context = context; //context in constructor
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER);
            view.setPadding(0, 0, 0, 0);
            view.setAdjustViewBounds(true);
        }
        view.setAdjustViewBounds(true);
        // Get the image URL for the current position.
        String posterFileName = getItem(position).getPosterFileName();

        Uri buildUri = Uri.parse(context.getResources().getString(R.string.moviedb_poster_url)).buildUpon()
                .appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w185)) // TODO: change this based on screen size
                .appendPath(posterFileName.substring(1)).build(); // remove the first char of the file name since it is a /

        Log.d(LOG_TAG, buildUri.toString());
        // Trigger the download of the URL asynchronously into the image view.
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
        return movies.size();
    }

    @Override
    public MovieParcelable getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(MovieParcelable m) {
        movies.add(m);
    }

    public void clear() {
        movies.clear();
    }

}