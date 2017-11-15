package initiativaromania.hartabanilorpublici.data;

import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by claudiu on 9/12/17.
 */

public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    public static int COMPANY_TYPE_AD           = 0;
    public static int COMPANY_TYPE_TENDER       = 1;

    public int  type;
    public String name;
    public int id;
}
