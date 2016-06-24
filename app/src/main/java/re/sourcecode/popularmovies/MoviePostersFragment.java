package re.sourcecode.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import re.sourcecode.popularmovies.adapters.MoviePostersGridViewAdapter;
import re.sourcecode.popularmovies.asynctasks.FetchMovieData;
import re.sourcecode.popularmovies.models.MovieParcelable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviePostersFragment extends Fragment {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();
    MoviePostersGridViewAdapter mMoviesAdapter;
    String sortOrder;


    public MoviePostersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        //set the default sortOrder
        sortOrder = getActivity().getString(R.string.pref_default_sort_by);
        //start the asynctask that download the moviedata
        updateMovies(sortOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        //rerun the asynctask that download the moviedata

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefSortOrder = sharedPrefs.getString(getActivity().getString(R.string.pref_sort_by), getActivity().getString(R.string.pref_default_sort_by));
        if (prefSortOrder != sortOrder) {
            sortOrder = prefSortOrder; //change the global sort order
            updateMovies(sortOrder); //run the async task and update the gui
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the rootview of this fragment
        final View rootView = inflater.inflate(R.layout.fragment_movie_posters, container, false);
        //set up gridview
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_movie_posters);
        mMoviesAdapter = new MoviePostersGridViewAdapter(getActivity());
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieParcelable movie = mMoviesAdapter.getItem(position);
                Log.d(LOG_TAG, movie.toString());
                Toast.makeText(getActivity(), movie.toString(), Toast.LENGTH_SHORT).show();
                // start the movie detail fragment
                Intent detailedIntent = new Intent(getActivity(), MovieDetailActivity.class);
                detailedIntent.putExtra("movie", movie);
                startActivity(detailedIntent);

            }
        });

        //return rootview with gridview
        return rootView;
    }

    private void updateMovies(String sort_by) {
        //start the asynctask that download the moviedata
        new FetchMovieData(getActivity(), mMoviesAdapter).execute(sort_by);
    }
}

