package re.sourcecode.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.models.MovieReviewParcelable;

/**
 * Created by olem on 9/7/16.
 */
public class MovieReviewsListViewAdapter extends ArrayAdapter<MovieReviewParcelable> {


    private static final String LOG_TAG = MovieReviewsListViewAdapter.class.getSimpleName();
    private ArrayList<MovieReviewParcelable> mReviews;

    public MovieReviewsListViewAdapter(Context context, ArrayList<MovieReviewParcelable> reviews) {
        super(context,0, reviews);
        this.mReviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        //Log.d(LOG_TAG, Integer.toString(position));
        MovieReviewParcelable review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movie_reviews, parent, false);
        }

        TextView reviewContentText = (TextView) convertView.findViewById(R.id.review_text);
        TextView reviewAuthorText = (TextView) convertView.findViewById(R.id.review_author);
        reviewAuthorText.setText(review.getReviewAuthor());
        reviewContentText.setText(review.getReviewContent());
        Log.d(LOG_TAG, Integer.toString(position));
        return convertView;
    }
    @Override
    public int getCount() {
        return mReviews.size();
    }

}
