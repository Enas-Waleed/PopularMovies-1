package re.sourcecode.popularmovies;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            //no saved instance yet in the moviedetail, so we will only create the fragment view
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment()).commit();
        }
    }


}
