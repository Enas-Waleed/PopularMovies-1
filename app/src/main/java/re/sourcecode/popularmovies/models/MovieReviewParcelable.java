package re.sourcecode.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by olem on 9/7/16.
 */
public class MovieReviewParcelable implements Parcelable {

    private final String LOG_TAG = MovieReviewParcelable.class.getSimpleName();
    private long movieId;
    private String reviewId;
    private String reviewAuthor;
    private String reviewContent;
    private String reviewUrl;

    // Constructors
    public MovieReviewParcelable() {

    }

    public MovieReviewParcelable(long movieId, String reviewId, String reviewAuthor, String reviewContent, String reviewUrl) {
        this.movieId = movieId;
        this.reviewId = reviewId;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
    }

    public MovieReviewParcelable(long movieId, JSONObject movie) throws JSONException {
        this.movieId = movieId;
        this.reviewId = movie.getString("id");
        this.reviewAuthor = movie.getString("author");
        this.reviewContent = movie.getString("content");
        this.reviewUrl = movie.getString("url");
    }

    // Getter and setter methods

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(movieId);
        parcel.writeString(reviewId);
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
        parcel.writeString(reviewUrl);
    }

    public static final Parcelable.Creator<MovieReviewParcelable> CREATOR
            = new Parcelable.Creator<MovieReviewParcelable>() {

        public MovieReviewParcelable createFromParcel(Parcel in) {
            return new MovieReviewParcelable(in);
        }

        public MovieReviewParcelable[] newArray(int size) {
            return new MovieReviewParcelable[size];
        }

    };

    // Parcelling part
    private MovieReviewParcelable(Parcel in) {
        movieId = in.readLong();
        reviewId = in.readString();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewUrl = in.readString();
    }

}


