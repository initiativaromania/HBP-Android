package initiativaromania.hartabanilorpublici;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by claudiu on 8/17/17.
 */

public class PublicInstitution implements Serializable, ClusterItem {
    private static final long serialVersionUID = 1L;
    public String name;
    public LatLng position;

    public PublicInstitution(String name, double longitude, double latitude) {
        this.position = new LatLng(latitude, longitude);
        this.name = name;
    }



    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() { return name; }

    @Override
    public String getSnippet() { return "snippet"; }
}
