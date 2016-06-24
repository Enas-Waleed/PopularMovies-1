package re.sourcecode.popularmovies.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import re.sourcecode.popularmovies.BuildConfig;
import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.adapters.MoviePostersGridViewAdapter;
import re.sourcecode.popularmovies.models.MovieParcelable;

public class FetchMovieData extends AsyncTask<String, Void, ArrayList> {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();
    private Context context;
    private MoviePostersGridViewAdapter mMoviesAdapter;

    public FetchMovieData(Context current, MoviePostersGridViewAdapter adapter) {
        this.context = current;
        this.mMoviesAdapter = adapter;
    }

    private ArrayList<MovieParcelable> getMovieDataFromJson(String theMovieDbJsonStr, String posterSize) throws JSONException {
        ArrayList<MovieParcelable> results;
        JSONArray theMovieArray;
        JSONObject theMovieDbJson;
        Uri.Builder posterURLBuilder;

        //Jsonobject to extract
        final String MDB_RESULTS = context.getResources().getString(R.string.moviedb_json_results);
        final String MDB_POSTER = context.getResources().getString(R.string.moviedb_json_poster_path);
        final String MDB_TITLE = context.getResources().getString(R.string.moviedb_json_title);
        final String MDB_PLOT = context.getResources().getString(R.string.moviedb_json_plot);
        final String MDB_USER_RATING = context.getResources().getString(R.string.moviedb_json_user_rating);
        final String MDB_RELEASE_DATE = context.getResources().getString(R.string.moviedb_json_release);

        //Base url for poster
        posterURLBuilder = Uri.parse(context.getResources().getString(R.string.moviedb_poster_url)).buildUpon();

        if (posterSize == context.getResources().getString(R.string.moviedb_poster_uri_w342)) {
            posterURLBuilder.appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w342));
        } else {
            posterURLBuilder.appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w185));
        }
        // parse the data from server as json
        theMovieDbJson = new JSONObject(theMovieDbJsonStr);
        theMovieArray = theMovieDbJson.getJSONArray(MDB_RESULTS);

        results = new ArrayList<MovieParcelable>();

        for (int i = 0; i < theMovieArray.length(); i++) {
            JSONObject aMovie = theMovieArray.getJSONObject(i);

            String posterFileName = aMovie.getString(MDB_POSTER);
            String title = aMovie.getString(MDB_TITLE);
            String plot = aMovie.getString(MDB_PLOT);
            String user_rating = aMovie.getString(MDB_USER_RATING);
            String release_date = aMovie.getString(MDB_RELEASE_DATE);

            results.add(new MovieParcelable(posterFileName, title, plot, user_rating, release_date));

        }

        return results;

    }

    @Override
    protected ArrayList<MovieParcelable> doInBackground(String... params) {
        // If there's no sort order, return null.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String theMovieDbJsonStr = null;

        int numResults = 4;
        String sortOrder;

        try {

            if (params[0].equals(context.getResources().getString(R.string.pref_default_sort_by))) {
                sortOrder = context.getResources().getString(R.string.moviedb_uri_popular);
            } else {
                sortOrder = context.getResources().getString(R.string.moviedb_uri_top_rated);
            }

            Uri builtUri = Uri.parse(context.getResources().getString(R.string.moviedb_url)).buildUpon()
                    .appendPath(sortOrder) //TODO: set correct string from params
                    .appendQueryParameter(context.getResources().getString(R.string.moviedb_query_api_key), BuildConfig.THEMOVIEDB_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, url.toString());

            // Create the request to api.themoviedb.org, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                Log.e(LOG_TAG, "No data from themoviedb");
                return null;
            }
            theMovieDbJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(theMovieDbJsonStr, context.getResources().getString(R.string.moviedb_poster_uri_w185)); // returns a ArrayList of MovieParcelable
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList movieParcelables) {
        super.onPostExecute(movieParcelables);
        if (movieParcelables != null) {
            // clear any old movies
            mMoviesAdapter.clear();
            // iterate over the new ArrayList of MovieParcelable objects
            Iterator iterator = movieParcelables.iterator();
            while (iterator.hasNext()) {
                mMoviesAdapter.add((MovieParcelable) iterator.next());
            }
        }
        mMoviesAdapter.notifyDataSetChanged();
    }
}
