package re.sourcecode.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import re.sourcecode.popularmovies.R;
import re.sourcecode.popularmovies.fragments.MovieDetailFragment;
import re.sourcecode.popularmovies.models.MovieParcelable;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_DETAIL = "movie_detail";

    private MovieParcelable mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            if(getIntent().hasExtra(MOVIE_DETAIL)) {
                mMovie = getIntent().getParcelableExtra(MOVIE_DETAIL);
            }
            final MovieDetailFragment fragment = MovieDetailFragment.newInstance(mMovie);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }


}
