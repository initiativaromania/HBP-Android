package initiativaromania.hartabanilorpublici.ui;

import android.graphics.ColorFilter;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 4/14/18.
 */

public class LoadableListFragment extends Fragment {
    private ProgressBar progressBar = null;
    protected boolean startLoading = false;

    public void initProgressBar(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        System.out.println("Progress bar created");
    }

    public void displayProgressBar() {
        System.out.println("Displaying progress bar");
        if (progressBar == null) {
            startLoading = true;
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        System.out.println("Hide progress bar");

        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        startLoading = false;
    }
}
