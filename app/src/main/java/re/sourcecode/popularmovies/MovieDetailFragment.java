package re.sourcecode.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import re.sourcecode.popularmovies.models.MovieParcelable;

public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private MovieParcelable movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = intent.getParcelableExtra("movie");
            // set the fragment with the data from the intent
            ((TextView) rootView.findViewById(R.id.detailview_title))
                    .setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.detailview_plot))
                    .setText(movie.getPlot());
            ((TextView) rootView.findViewById(R.id.detailview_rating))
                    .setText(movie.getUserRating() + "/10");
            ((TextView) rootView.findViewById(R.id.detailview_release_date))
                    .setText(movie.getReleaseDate());
            Uri buildUri = Uri.parse(getContext().getResources().getString(R.string.moviedb_poster_url)).buildUpon()
                    .appendPath(getContext().getResources().getString(R.string.moviedb_poster_uri_w342))
                    .appendPath(movie.getPosterFileName().substring(1)).build();
            ImageView imageView = (ImageView) rootView.findViewById(R.id.detailview_poster);
            Log.d(LOG_TAG, "Getting image from " + buildUri.toString());
            Picasso.with(getContext()).load(buildUri).into(imageView);

        }

        return rootView;
    }

}


