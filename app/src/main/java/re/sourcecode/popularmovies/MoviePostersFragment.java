package re.sourcecode.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;

import re.sourcecode.popularmovies.adapters.MoviePostersGridViewAdapter;
import re.sourcecode.popularmovies.asynctasks.FetchMovieData;
import re.sourcecode.popularmovies.models.MovieParcelable;


public class MoviePostersFragment extends Fragment {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();
    MoviePostersGridViewAdapter mMoviesAdapter; // The adapter for the gridview
    private ArrayList<MovieParcelable> movies; // ArrayList is a collection and do not need to be fixed length like a Array
    String sortOrder; // store the setting on sort order globally


    public MoviePostersFragment() {
        // Required empty public constructor
    }


    public interface Callback {
        // called on click in grid
        void onItemSelected(MovieParcelable movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if the savedInstance is set by onSavedInstanceState. If set get the saved movies ArrayList of parcels.
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            //if nothing is saved create movies arraylist for the first tim
            movies = new ArrayList<MovieParcelable>();
            //create a new gridview adapter with the new arraylist of movies
            mMoviesAdapter = new MoviePostersGridViewAdapter(getActivity(), movies);
            //set the default sortorder
            sortOrder = getActivity().getString(R.string.pref_default_sort_by);
            //get the movies for the first time
            updateMovies(sortOrder);
        } else {
            Log.d(LOG_TAG, "Getting saved movies list");
            //if movies arraylist is saved from another instance, use it.
            movies = savedInstanceState.getParcelableArrayList("movies");
            //create a new grid view adapter with the stored movies
            mMoviesAdapter = new MoviePostersGridViewAdapter(getActivity(), movies);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //get the prefernces of the activity
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get the settings for sort by
        String prefSortOrder = sharedPrefs.getString(getActivity().getString(R.string.pref_sort_by), getActivity().getString(R.string.pref_default_sort_by));
        //check if the sort order has changed, if so do update through the asynctask
        if (prefSortOrder != sortOrder) {
            sortOrder = prefSortOrder; //change the global sort order
            updateMovies(sortOrder); //run the async task and update the gui
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the fragment to this rootview
        final View rootView = inflater.inflate(R.layout.fragment_movie_posters, container, false);
        //find the gridview
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_movie_posters);
        //create the gridview adapter, pass context and the movies arraylist
        //connect the adapter to the gridview
        gridView.setAdapter(mMoviesAdapter);
        //handle clicks on the gridview, override onItemClick to do stuff when a poster is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieParcelable movie = mMoviesAdapter.getItem(position);
                ((Callback)getActivity()).onItemSelected(movie);
            }
        });
        //return rootview with gridview
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save data (the arraylist) between screen flips. TODO: save the movieposter in the arraylist?
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    private void updateMovies(String sort_by) {
        //start the asynctask that download the movie data and update the gridview adapte
        new FetchMovieData(getActivity(), mMoviesAdapter).execute(sort_by);
    }
}

