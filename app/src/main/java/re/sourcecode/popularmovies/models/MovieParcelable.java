package re.sourcecode.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;


public class MovieParcelable implements Parcelable {
    private String posterFileName;
    private String title;
    private String plot;
    private String userRating;
    private String releaseDate;

    // Constructor
    public MovieParcelable(String posterFileName, String title, String plot, String userRating, String releaseDate) {
        this.posterFileName = posterFileName;
        this.title = title;
        this.plot = plot;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    // Getter and setter methods
    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
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

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // Parcelling part
    private MovieParcelable(Parcel in) {
        posterFileName = in.readString();
        title = in.readString();
        plot = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterFileName);
        parcel.writeString(title);
        parcel.writeString(plot);
        parcel.writeString(userRating);
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


}
