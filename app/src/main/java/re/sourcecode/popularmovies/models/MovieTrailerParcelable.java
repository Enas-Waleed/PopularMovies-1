package re.sourcecode.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by olem on 9/9/16.
 */
public class MovieTrailerParcelable implements Parcelable {
    private final String LOG_TAG = MovieTrailerParcelable.class.getSimpleName();
    private long movieId;
    private String trailerId;
    private String trailerName;
    private String trailerKey;
    private String trailerSite;

    // Constructors
    public MovieTrailerParcelable() {

    }

    public MovieTrailerParcelable(long movieId, String trailerId, String trailerName, String trailerKey, String trailerSite) {
        this.movieId = movieId;
        this.trailerId = trailerId;
        this.trailerName = trailerName;
        this.trailerKey = trailerKey;
        this.trailerSite = trailerSite;
    }

    public MovieTrailerParcelable(long movieId, JSONObject trailer) throws JSONException {
        this.movieId = movieId;
        this.trailerId = trailer.getString("id");
        this.trailerName = trailer.getString("name");
        this.trailerKey = trailer.getString("key");
        this.trailerSite = trailer.getString("site");
    }

    // Getter and setter methods


    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    public void setTrailerSite(String trailerSite) {
        this.trailerSite = trailerSite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(movieId);
        parcel.writeString(trailerId);
        parcel.writeString(trailerName);
        parcel.writeString(trailerKey);
        parcel.writeString(trailerSite);
    }

    public static final Parcelable.Creator<MovieTrailerParcelable> CREATOR
            = new Parcelable.Creator<MovieTrailerParcelable>() {

        public MovieTrailerParcelable createFromParcel(Parcel in) {
            return new MovieTrailerParcelable(in);
        }

        public MovieTrailerParcelable[] newArray(int size) {
            return new MovieTrailerParcelable[size];
        }

    };

    // Parcelling part
    private MovieTrailerParcelable(Parcel in) {
        movieId = in.readLong();
        trailerId = in.readString();
        trailerName = in.readString();
        trailerSite = in.readString();
        trailerKey = in.readString();
    }
}
