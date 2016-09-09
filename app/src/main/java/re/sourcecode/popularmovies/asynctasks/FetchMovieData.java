package re.sourcecode.popularmovies.asynctasks;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import re.sourcecode.popularmovies.BuildConfig;
import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.adapters.MoviePostersGridViewAdapter;
import re.sourcecode.popularmovies.data.MovieContract;
import re.sourcecode.popularmovies.models.MovieParcelable;

public class FetchMovieData extends AsyncTask<String, Void, ArrayList> {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();
    private Context context;
    private MoviePostersGridViewAdapter mMoviesAdapter;

    public FetchMovieData(Context current, MoviePostersGridViewAdapter adapter) {
        this.context = current;
        this.mMoviesAdapter = adapter;
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
            }
            else if (params[0].equals(context.getResources().getString(R.string.pref_sort_by_top_rated))) {
                sortOrder = context.getResources().getString(R.string.moviedb_uri_top_rated);
            }
            else {
                //get movies from DB
                return getMovieDataFromDB();
            }

            Uri builtUri = Uri.parse(context.getResources().getString(R.string.moviedb_url)).buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter(context.getResources().getString(R.string.moviedb_query_api_key), BuildConfig.THEMOVIEDB_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, "Downloading list of movies from: " + url.toString());

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
        } catch (SQLException e) {
            Log.e(LOG_TAG, "SQL Error ", e);
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
            Log.d(LOG_TAG, "Clearing adapter");
            mMoviesAdapter.clear();
            // iterate over the new ArrayList of MovieParcelable objects
            // and add them to the empty adapter
            Iterator iterator = movieParcelables.iterator();
            while (iterator.hasNext()) {
                mMoviesAdapter.add((MovieParcelable) iterator.next());
            }
        }
        mMoviesAdapter.notifyDataSetChanged();
    }

    private ArrayList<MovieParcelable> getMovieDataFromJson(String theMovieDbJsonStr, String posterSize) throws JSONException {
        JSONArray theMovieArray; //array for the downloaded json list
        JSONObject theMovieDbJson; //array for the downloaded single movie json object
        Uri.Builder posterURLBuilder; //to store the url+uri for downloading json list
        ArrayList<MovieParcelable> results = new ArrayList<MovieParcelable>(); //arraylist to hold de movie parcelables
        //Base url for poster
        posterURLBuilder = Uri.parse(context.getResources().getString(R.string.moviedb_poster_url)).buildUpon();

        if (posterSize == context.getResources().getString(R.string.moviedb_poster_uri_w342)) {
            posterURLBuilder.appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w342));
        } else {
            posterURLBuilder.appendPath(context.getResources().getString(R.string.moviedb_poster_uri_w185));
        }
        // parse the data from server as json
        theMovieDbJson = new JSONObject(theMovieDbJsonStr);
        theMovieArray = theMovieDbJson.getJSONArray(context.getResources().getString(R.string.moviedb_json_results));


        for (int i = 0; i < theMovieArray.length(); i++) {
            JSONObject aMovie = theMovieArray.getJSONObject(i);
            results.add(new MovieParcelable(aMovie)); //create new parcable
        }
        return results;
    }

    private ArrayList<MovieParcelable> getMovieDataFromDB() throws SQLException {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<MovieParcelable> results = new ArrayList<MovieParcelable>();
        Cursor cursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                final MovieParcelable aMovie = new MovieParcelable(cursor);
                results.add(aMovie);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return results;
    }
}
