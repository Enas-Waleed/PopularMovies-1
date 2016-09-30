package re.sourcecode.popularmovies.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import re.sourcecode.popularmovies.data.MovieContract;


public class MovieParcelable implements Parcelable {

    private static final String LOG_TAG = MovieParcelable.class.getSimpleName();
    private long movieId;
    private String posterFileName;
    private String title;
    private String overview;
    private double userRating;
    private String releaseDate;

    // Constructors
    public MovieParcelable() {

    }

    public MovieParcelable(long movieId, String posterFileName, String title, String overview, double userRating, String releaseDate) {
        this.movieId = movieId;
        this.posterFileName = posterFileName;
        this.title = title;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public MovieParcelable(JSONObject movie) throws JSONException {
        this.movieId = movie.getLong("id");
        this.title = movie.getString("original_title");
        this.posterFileName = movie.getString("poster_path");
        this.overview = movie.getString("overview");
        this.userRating = BigDecimal.valueOf(movie.getDouble("vote_average")).doubleValue();
        this.releaseDate = movie.getString("release_date");
    }

    public MovieParcelable(Cursor cursor) {
        int idx_col_movie_id = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int idx_col_title = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int idx_col_poster_file_name = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_FILE_NAME);
        int idx_col_overview = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        int idx_col_rating = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        int idx_col_release_date = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        this.movieId = cursor.getLong(idx_col_movie_id);
        this.title = cursor.getString(idx_col_title);
        this.posterFileName = cursor.getString(idx_col_poster_file_name);
        this.overview = cursor.getString(idx_col_overview);
        this.userRating = cursor.getDouble(idx_col_rating);
        this.releaseDate = cursor.getString(idx_col_release_date);
    }


    // Getter and setter methods
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterFileName() {
        return posterFileName;
    }

    public void setPosterFileName(String posterFileName) {
        this.posterFileName = posterFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(movieId);
        parcel.writeString(posterFileName);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieParcelable> CREATOR
            = new Parcelable.Creator<MovieParcelable>() {

        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }

    };

    // Parcelling part
    private MovieParcelable(Parcel in) {
        movieId = in.readLong();
        posterFileName = in.readString();
        title = in.readString();
        overview = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

}
