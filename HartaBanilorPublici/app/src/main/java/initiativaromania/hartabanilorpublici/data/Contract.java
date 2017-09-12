package initiativaromania.hartabanilorpublici.data;

import java.io.Serializable;

/**
 * Created by claudiu on 9/12/17.
 */

public class Contract implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;

    private static final int CONTRACT_TYPE_DIRECT_ACQUISITION   = 1;
    private static final int CONTRACT_TYPE_TENDER               = 2;

    public int type;
    public int id;

    public String title;
    public String address;
    public String CPVCode;
    public String valueEUR;
    public String valueRON;
    public String number;
    public String date;

    public int votes;

    public Company company = null;
    public PublicInstitution pi;

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
