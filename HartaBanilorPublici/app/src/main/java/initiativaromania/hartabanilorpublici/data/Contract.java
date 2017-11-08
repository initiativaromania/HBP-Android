package initiativaromania.hartabanilorpublici.data;

import java.io.Serializable;

/**
 * Created by claudiu on 9/12/17.
 */

public class Contract implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;

    public static final int CONTRACT_TYPE_DIRECT_ACQUISITION   = 1;
    public static final int CONTRACT_TYPE_TENDER               = 2;

    public int type;
    public int id;


    /* Contract info as returned by the server */
    public String procedureType;
    public String participationDate;
    public String finaliseContractType;
    public String number;
    public String date;
    public String title;
    public double value;
    public String currency;
    public double valueEUR;
    public double valueRON;
    public String CPVCode;
    public int institutionID;
    public int companyID;

    public PublicInstitution pi;
    public Company company;


    public int votes;


    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
