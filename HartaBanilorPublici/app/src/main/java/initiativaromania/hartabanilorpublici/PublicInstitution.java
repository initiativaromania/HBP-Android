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
    public String CUI;
    public LatLng position;
    public int version;

    public PublicInstitution(String CUI, double longitude, double latitude, int version) {
        this.position = new LatLng(latitude, longitude);
        this.CUI = CUI;
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
