package re.sourcecode.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.models.MovieTrailerParcelable;

/**
 * Created by olem on 9/9/16.
 */
public class MovieTrailersListViewAdapter extends ArrayAdapter<MovieTrailerParcelable> {

    private final String LOG_TAG = MovieTrailersListViewAdapter.class.getSimpleName();
    ArrayList<MovieTrailerParcelable> mTrailers;

    public MovieTrailersListViewAdapter(Context context, ArrayList<MovieTrailerParcelable> trailers) {
        super(context,0,trailers);
        this.mTrailers = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d(LOG_TAG, Integer.toString(position));
        MovieTrailerParcelable trailer = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movie_trailer, parent, false);
        }

        TextView trailerNameText = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerNameText.setText(trailer.getTrailerName());
        return convertView;
    }

    @Override
    public int getCount() {
        //Log.d(LOG_TAG, Integer.toString(mReviews.size()));
        return mTrailers.size();
    }

}

