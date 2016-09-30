package re.sourcecode.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.adapters.MovieReviewsListViewAdapter;
import re.sourcecode.popularmovies.adapters.MovieTrailersListViewAdapter;
import re.sourcecode.popularmovies.asynctasks.FetchReviewData;
import re.sourcecode.popularmovies.asynctasks.FetchTrailerData;
import re.sourcecode.popularmovies.data.MovieContract;
import re.sourcecode.popularmovies.models.MovieParcelable;

public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private static final String ARG_MOVIE = "movie";
    private MovieParcelable mMovie;
    private Switch mFavourite;
    private MovieReviewsListViewAdapter mMoviesReviewsAdapter;
    private MovieTrailersListViewAdapter mMovieTrailersAdapter;


    public static MovieDetailFragment newInstance(final MovieParcelable movieParcelable) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movieParcelable);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // empty constructor
    }

    public interface FragmentCallback {
        void onFetchReviewDataTaskDone(ArrayList reviews);
        void onFetchTrailerDataTaskDone(ArrayList trailers);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();

        mMovie = args.getParcelable(ARG_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // set the fragment with the data from the intent
        ((TextView) rootView.findViewById(R.id.detailview_title))
                .setText(mMovie.getTitle());
        ((TextView) rootView.findViewById(R.id.detailview_plot))
                .setText(mMovie.getOverview());
        ((TextView) rootView.findViewById(R.id.detailview_rating))
                .setText(String.format("%.2f / 10", mMovie.getUserRating()));
        ((TextView) rootView.findViewById(R.id.detailview_release_date))
                .setText(mMovie.getReleaseDate());


        Uri buildUri = Uri.parse(getContext().getResources().getString(R.string.moviedb_poster_url)).buildUpon()
                .appendPath(getContext().getResources().getString(R.string.moviedb_poster_uri_w342))
                .appendPath(mMovie.getPosterFileName().substring(1)).build();
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detailview_poster);
        Log.d(LOG_TAG, "Getting image from " + buildUri.toString());
        Picasso.with(getContext()).load(buildUri).into(imageView);

        FetchTrailerData fetchTrailerData = new FetchTrailerData(getContext(), new FragmentCallback() {
            @Override
            public void onFetchReviewDataTaskDone(ArrayList reviews) {

            }

            @Override
            public void onFetchTrailerDataTaskDone(ArrayList trailers) {
                Log.d(LOG_TAG,"Got trailers " + trailers.size());

                ListView trailerList =(ListView) getActivity().findViewById(R.id.list_view_trailers);

                mMovieTrailersAdapter = new MovieTrailersListViewAdapter(getActivity(),trailers);

                trailerList.setAdapter(mMovieTrailersAdapter);

                trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view,
                                            final int position, final long id) {
                        final String trailerKey = mMovieTrailersAdapter.getItem(position).getTrailerKey();
                        final Uri youTubeUri = Uri.parse("http://www.youtube.com/watch?v=" + trailerKey);
                        Log.d(LOG_TAG,youTubeUri.toString());
                        final Intent intent = new Intent(Intent.ACTION_VIEW, youTubeUri);
                        startActivity(intent);
                    }
                });


            }
        });
       fetchTrailerData.execute(Long.toString(mMovie.getMovieId()));

        FetchReviewData fetchReviewData = new FetchReviewData(getContext(), new FragmentCallback() {
            @Override
            public void onFetchReviewDataTaskDone(ArrayList reviews) {
                Log.d(LOG_TAG, "Got reviews " + reviews.size());

                ListView reviewList = (ListView) getActivity().findViewById(R.id.list_view_reviews);

                mMoviesReviewsAdapter = new MovieReviewsListViewAdapter(getActivity(),reviews);

                reviewList.setAdapter(mMoviesReviewsAdapter);
            }

            @Override
            public void onFetchTrailerDataTaskDone(ArrayList trailers) {

            }
        });

        fetchReviewData.execute(Long.toString(mMovie.getMovieId()));


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavourite = (Switch) getActivity().findViewById(R.id.detailview_favorite_switch);

        final Uri movieUri =
                MovieContract.MovieEntry.buildMovieUri(mMovie.getMovieId());

        final Cursor cursor = getActivity().getContentResolver().query(movieUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mFavourite.setChecked(true);
            }
            cursor.close();
        }

        mFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(LOG_TAG, "onCheckedChanged");
                if (isChecked) {
                    final long movieId = mMovie.getMovieId();
                    final Uri movieUri = MovieContract.MovieEntry.buildMovieUri(movieId);

                    final Cursor cursor = getActivity().getContentResolver().query(movieUri, null, null, null, null);

                    if (cursor != null) {
                        if (!cursor.moveToFirst()) {
                            Log.d(LOG_TAG, "Not Found in DB inserting: " + mMovie.getTitle());
                            final ContentValues movieValues = new ContentValues();
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_FILE_NAME, mMovie.getPosterFileName());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getUserRating());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());

                            getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
                        }
                        cursor.close();
                    }
                } else {
                    getActivity().getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{Long.toString(mMovie.getMovieId())});
                }
            }
        });
    }

}




