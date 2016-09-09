package re.sourcecode.popularmovies.asynctasks;

import android.content.Context;
import android.net.ParseException;
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

import re.sourcecode.popularmovies.BuildConfig;
import re.sourcecode.popularmovies.MovieDetailFragment;
import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.models.MovieTrailerParcelable;

/**
 * Created by olem on 9/9/16.
 */
public class FetchTrailerData extends AsyncTask<String, Void, ArrayList> {
    private final String LOG_TAG = FetchTrailerData.class.getSimpleName();
    private Context context;
    private MovieDetailFragment.FragmentCallback mMovieDetailFragmentCallback; //callback for when the async task is finished

    public FetchTrailerData(Context current, MovieDetailFragment.FragmentCallback fragmentCallback) {
        this.mMovieDetailFragmentCallback = fragmentCallback;
        this.context = current;
    }

    @Override
    protected ArrayList<MovieTrailerParcelable> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String theMovieDbJsonStr = null;

        try {
            //params[0] = movie id
            Uri builtUri = Uri.parse(context.getResources().getString(R.string.moviedb_url)).buildUpon()
                    .appendPath(params[0])
                    .appendPath(context.getResources().getString(R.string.moviedb_uri_trailers))
                    .appendQueryParameter(context.getResources().getString(R.string.moviedb_query_api_key), BuildConfig.THEMOVIEDB_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, "Downloading trailers movie from: " + url.toString());

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
            return getTrailerDataFromJson(params[0], theMovieDbJsonStr); // returns a ArrayList of MovieParcelable
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList movieTrailerParcelables) {
        //super.onPostExecute(movieReviewsParcelables);
        mMovieDetailFragmentCallback.onFetchTrailerDataTaskDone(movieTrailerParcelables);

    }


    private ArrayList<MovieTrailerParcelable> getTrailerDataFromJson(String movieId, String theMovieDbJsonStr) throws JSONException,ParseException {
        JSONArray theTrailerArray; //array for the downloaded json list
        JSONObject theMovieDbJson; //array for the downloaded single movie json object
        ArrayList<MovieTrailerParcelable> results = new ArrayList<MovieTrailerParcelable>(); //arraylist to hold de movie parcelables

        // parse the data from server as json
        theMovieDbJson = new JSONObject(theMovieDbJsonStr);
        theTrailerArray = theMovieDbJson.getJSONArray(context.getResources().getString(R.string.moviedb_json_results));
        long aMovieId = Long.parseLong(movieId);

        for (int i = 0; i < theTrailerArray.length(); i++) {
            JSONObject aMovieTrailer = theTrailerArray.getJSONObject(i);
            results.add(new MovieTrailerParcelable(aMovieId, aMovieTrailer)); //create new parcable
        }
        return results;
    }

}
