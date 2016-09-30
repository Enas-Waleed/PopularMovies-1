package re.sourcecode.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import re.sourcecode.popularmovies.models.MovieParcelable;

/**
 * Created by olem on 9/4/16.
 * To store favourite movies
 */
public final class MovieContract {
    private static final String LOG_TAG = MovieContract.class.getSimpleName();
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    static final String CONTENT_AUTHORITY = "re.sourcecode.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://re.sourcecode.popularmovies/movie/ is a valid path for
    // looking at movie data. content://re.sourcecode.popularmovie/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    static final String PATH_MOVIE = "movie";

    private MovieContract() { //will never be instantiated, so the constructor is private/supressed
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        static final String TABLE_NAME = "movie";
        // unique id of a movie
        public static final String COLUMN_MOVIE_ID = "movie_id";
        // movie title
        public static final String COLUMN_TITLE = "title";
        // movie image
        public static final String COLUMN_POSTER_FILE_NAME = "poster_file_name";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        // parse id of a record
        static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }
}
