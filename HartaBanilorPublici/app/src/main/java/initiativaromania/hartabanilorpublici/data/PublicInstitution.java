package initiativaromania.hartabanilorpublici.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by claudiu on 8/17/17.
 */

public class PublicInstitution implements Serializable, ClusterItem {
    private static final long serialVersionUID = 1L;
    public String name = "";
    public int id;
    public LatLng position;
    public int version;
    public int directAcqs = 0;
    public int tenders = 0;

    public PublicInstitution(int id, double longitude, double latitude, int version) {
        this.position = new LatLng(latitude, longitude);
        this.id = id;
        this.version = version;
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
