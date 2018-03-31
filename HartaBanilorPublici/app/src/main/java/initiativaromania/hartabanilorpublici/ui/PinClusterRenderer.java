package initiativaromania.hartabanilorpublici.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import initiativaromania.hartabanilorpublici.data.PublicInstitution;

/**
 * Created by claudiu on 3/31/18.
 */

public class PinClusterRenderer extends DefaultClusterRenderer<PublicInstitution> {
    private static final String PIN_ICON_NAME           = "pin";
    private static final String CLUSTER_ICON_NAME       = "cluster";

    private final Activity mainActivity;

    public PinClusterRenderer(Context context, GoogleMap map,
                              ClusterManager<PublicInstitution> clusterManager) {
        super(context, map, clusterManager);

        mainActivity = (Activity)context;
    }

    public BitmapDescriptor getHBPIcon(Activity mainActivity, String iconName) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(mainActivity.getResources(),
                mainActivity.getResources().getIdentifier(iconName, "drawable",
                        mainActivity.getPackageName()));

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap,
                imageBitmap.getWidth() / 3,
                imageBitmap.getHeight() / 3, false);

        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    @Override
    protected void onBeforeClusterItemRendered(PublicInstitution pi,
                                               MarkerOptions markerOptions) {
        markerOptions.icon(getHBPIcon(mainActivity, PIN_ICON_NAME));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<PublicInstitution> cluster,
                                           MarkerOptions markerOptions) {
        markerOptions.icon(getHBPIcon(mainActivity, CLUSTER_ICON_NAME))
                     .snippet(cluster.getSize() + "");
    }
}
