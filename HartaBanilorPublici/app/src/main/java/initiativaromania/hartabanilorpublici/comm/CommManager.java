package initiativaromania.hartabanilorpublici.comm;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by claudiu on 9/16/17.
 */

public class CommManager {

    private static final String SERVER_IP = "http://hbp-api.azurewebsites.net/api/";

    /* Requests to server */
    private static final String URL_GET_PI_SUMMARY              = SERVER_IP + "PublicInstitutionSummary/";
    private static final String URL_GET_PI_INFO                 = SERVER_IP + "InstitutionByID/";
    private static final String URL_GET_PI_ACQS                 = SERVER_IP + "InstitutionContracts/";
    private static final String URL_GET_PI_TENDERS              = SERVER_IP + "InstitutionTenders/";
    private static final String URL_GET_AD                      = SERVER_IP + "Contract/";
    private static final String URL_GET_TENDER                  = SERVER_IP + "Tender/";
    private static final String URL_GET_AD_COMPANY              = SERVER_IP + "ADCompany/";
    private static final String URL_GET_TENDER_COMPANY          = SERVER_IP + "TenderCompany/";
    private static final String URL_GET_AD_COMPANIES_BY_PI      = SERVER_IP + "ADCompaniesByInstitution/";
    private static final String URL_GET_TENDER_COMPANIES_BY_PI  = SERVER_IP + "TenderCompaniesByInstitution/";


    /* Bundle keys */
    public static final String BUNDLE_INST_TYPE                 = "bundle_inst_type";
    public static final String BUNDLE_PI_ID                     = "bundle_pi_id";
    public static final String BUNDLE_PI_NAME                   = "bundle_pi_name";
    public static final String BUNDLE_PI_ACQS                   = "bundle_pi_acqs";
    public static final String BUNDLE_PI_TENDERS                = "bundle_pi_tenders";
    public static final String BUNDLE_CONTRACT_ID               = "bundle contract_id";
    public static final String BUNDLE_CONTRACT_TYPE             = "bundle contract_type";
    public static final String BUNDLE_CONTRACT_PI_ID            = "bundle contract_pi_id";
    public static final String BUNDLE_CONTRACT_PI_NAME          = "bundle contract_pi_name";
    public static final String BUNDLE_CONTRACT_COMP_ID          = "bundle contract_comp_id";
    public static final String BUNDLE_CONTRACT_COMP_NAME        = "bundle contract_comp_name";
    public static final String BUNDLE_COMPANY_ID                = "bundle_company_id";
    public static final String BUNDLE_COMPANY_NAME              = "bundle_company_name";
    public static final String BUNDLE_COMPANY_TYPE              = "bundle_company_type";



    /* JSON fields */
    public static final String JSON_PI_NAME                     = "nume_institutie";
    public static final String JSON_PI_NO_ACQS                  = "nr_achizitii";
    public static final String JSON_PI_NO_TENDERS               = "nr_licitatii";
    public static final String JSON_PI_CUI                      = "CUI";
    public static final String JSON_PI_ADDRESS                  = "Adresa";
    public static final String JSON_ACQ_ID                      = "ContracteId";
    public static final String JSON_TENDER_ID                   = "LicitatieID";
    public static final String JSON_CONTRACT_TITLE              = "TitluContract";
    public static final String JSON_CONTRACT_NR                 = "NumarContract";
    public static final String JSON_CONTRACT_VALUE_RON          = "ValoareRON";

    public static final String JSON_AD_PROCEDURE_TYPE           = "TipProcedura";
    public static final String JSON_AD_PARTICIP_DATE            = "DataAnuntParticipare";
    public static final String JSON_AD_FINAL_CONTRACT_TYPE      = "TipIncheiereContract";
    public static final String JSON_AD_NUMBER                   = "NumarContract";
    public static final String JSON_AD_DATE                     = "DataContract";
    public static final String JSON_AD_TITLE                    = "TitluContract";
    public static final String JSON_AD_VALUE                    = "Valoare";
    public static final String JSON_AD_CURRENCY                 = "Moneda";
    public static final String JSON_AD_VALUE_EUR                = "ValoareEUR";
    public static final String JSON_AD_VALUE_RON                = "ValoareRON";
    public static final String JSON_AD_CPV_CODE                 = "CPVCode";
    public static final String JSON_AD_INSTITUTION_ID           = "InstitutiePublicaID";
    public static final String JSON_AD_COMPANY_ID               = "CompanieId";

    public static final String JSON_TENDER_CONTRACT_TYPE        = "TipContract";
    public static final String JSON_TENDER_PROCEDURE_TYPE       = "TipProcedura";
    public static final String JSON_TENDER_ANN_OFFER_NUMBER     = "NumarAnuntAtribuire";
    public static final String JSON_TENDER_OFFER_DATE           = "DataAnuntAtribuire";
    public static final String JSON_TENDER_FINAL_CONTRACT_TYPE  = "TipIncheiereContract";
    public static final String JSON_TENDER_OFFER_CRITERIA       = "TipCriteriiAtribuire";
    public static final String JSON_TENDER_NUMBER_OFFERS        = "NumarOfertePrimite";
    public static final String JSON_TENDER_SUBCONTRACT          = "Subcontractat";
    public static final String JSON_TENDER_NUMBER               = "NumarContract";
    public static final String JSON_TENDER_DATE                 = "DataContract";
    public static final String JSON_TENDER_TITLE                = "TitluContract";
    public static final String JSON_TENDER_VALUE                = "Valoare";
    public static final String JSON_TENDER_CURRENCY             = "Moneda";
    public static final String JSON_TENDER_VALUE_EUR            = "ValoareEUR";
    public static final String JSON_TENDER_VALUE_RON            = "ValoareRON";
    public static final String JSON_TENDER_CPV_CODE             = "CPVCode";
    public static final String JSON_TENDER_PARTICIP_NUMBER      = "NumarAnuntParticipare";
    public static final String JSON_TENDER_PARTICIP_DATE        = "DataAnuntParticipare";
    public static final String JSON_TENDER_PARTICIP_ESTIM_VALUE = "ValoareEstimataParticipare";
    public static final String JSON_TENDER_DEPOSITS             = "DepoziteGarantii";
    public static final String JSON_TENDER_FINANCE              = "ModalitatiFinantare";
    public static final String JSON_TENDER_PARTICIP_ESTIM_CURR  = "MonedaValoareEstimataParticipare";
    public static final String JSON_TENDER_INSTITUTION_ID       = "InstitutiePublicaID";
    public static final String JSON_TENDER_COMPANY_ID           = "CompanieId";

    public static final String JSON_COMPANY_NAME                = "Nume";
    public static final String JSON_COMPANY_ID                  = "CompanieId";
    public static final String JSON_COMPANY_ADDRESS             = "Adresa";
    public static final String JSON_COMPANY_CUI                 = "CUI";


    public static RequestQueue queue;


    /* Init the Communication Manager */
    public static void init(Context context) {
        System.out.println("Initializing the communication manager");
        queue = Volley.newRequestQueue(context);
    }


    /* Send server request */
    public static void request(final CommManagerResponse commManagerResponse, String URL) {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, URL,
                        (JSONArray) null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("CommManager: Got response from the server");
                        if (commManagerResponse != null)
                            commManagerResponse.processResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("GOT ERRROR " + error + " " + error.toString());
                        error.printStackTrace();
                        if (commManagerResponse != null)
                            commManagerResponse.onErrorOccurred("Eroare conectare la server");
                    }
                });

        queue.add(jsObjRequest);
    }

    /* Send a request to the server for Public Institution Summary */
    public static void requestPISummary(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Summary request to URL " + URL_GET_PI_SUMMARY + publicInstitutionID);
        request(commManagerResponse, URL_GET_PI_SUMMARY + publicInstitutionID);
    }


    /* Send a request to the server for Public Institution Summary */
    public static void requestPIInfo(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Info request to URL " + URL_GET_PI_INFO + publicInstitutionID);
        request(commManagerResponse, URL_GET_PI_INFO + publicInstitutionID);
    }


    /* Send a request to the server for public institution direct acquisitions */
    public static void requestPIAcqs(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Acqs request to URL " + URL_GET_PI_ACQS + publicInstitutionID);
        request(commManagerResponse, URL_GET_PI_ACQS + publicInstitutionID);
    }


    /* Send a request to the server for public institution tenders */
    public static void requestPITenders(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Tenders request to URL " + URL_GET_PI_TENDERS + publicInstitutionID);
        request(commManagerResponse, URL_GET_PI_TENDERS + publicInstitutionID);
    }


    /* Send a request to the server for a direct acquisition */
    public static void requestAD(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send AD request to URL " + URL_GET_AD + contractID);
        request(commManagerResponse, URL_GET_AD + contractID);
    }


    /* Send a request to the server for a tender */
    public static void requestTender(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send Tender request to URL " + URL_GET_TENDER + contractID);
        request(commManagerResponse, URL_GET_TENDER + contractID);
    }


    /* Send a request to the server for an AD Company */
    public static void requestADCompany(final CommManagerResponse commManagerResponse, int companyID) {
        System.out.println("Send ADCompany request to URL " + URL_GET_AD_COMPANY + companyID);
        request(commManagerResponse, URL_GET_AD_COMPANY + companyID);
    }


    /* Send a request to the server for a Tender Company */
    public static void requestTenderCompany(final CommManagerResponse commManagerResponse, int companyId) {
        System.out.println("Send TenderCompany request to URL " + URL_GET_TENDER_COMPANY + companyId);
        request(commManagerResponse, URL_GET_TENDER_COMPANY + companyId);
    }


    /* Send a request to the server for all AD Companies in a PI */
    public static void requestADCompaniesByPI(final CommManagerResponse commManagerResponse,
                                              int publicInstitutionID) {
        System.out.println("Send ADCompanyByPI request to URL " +
                URL_GET_AD_COMPANIES_BY_PI + publicInstitutionID);
        request(commManagerResponse, URL_GET_AD_COMPANIES_BY_PI + publicInstitutionID);
    }


    /* Send a request to the server for all Tender Companies in a PI */
    public static void requestTenderCompaniesByPI(final CommManagerResponse commManagerResponse,
                                                  int publicInstitutionID) {
        System.out.println("Send TenderCompaniesByPI request to URL " +
                URL_GET_TENDER_COMPANIES_BY_PI + publicInstitutionID);
        request(commManagerResponse, URL_GET_TENDER_COMPANIES_BY_PI + publicInstitutionID);
    }
}
