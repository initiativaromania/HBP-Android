package initiativaromania.hartabanilorpublici.comm;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import initiativaromania.hartabanilorpublici.ui.SearchFragment;

/**
 * Created by claudiu on 9/16/17.
 */

public class CommManager {
    private static final String SERVER_IP1                      = "http://hbp-api.azurewebsites.net/api/";
    private static final String SERVER_IP2                      = "https://hbp-api2.azurewebsites.net/api/";
    private static final String SEARCH_IP                       = "https://hbp-api2.azurewebsites.net/apiV2/search/";
    private static final String SERVER_IP                       = SERVER_IP2;

    /* Requests to server */
    private static final String URL_GET_PI_SUMMARY              = SERVER_IP + "PublicInstitutionSummary/";
    private static final String URL_GET_PI_INFO                 = SERVER_IP + "InstitutionByID/";
    private static final String URL_GET_PI_ACQS                 = SERVER_IP + "InstitutionContracts/";
    private static final String URL_GET_PI_TENDERS              = SERVER_IP + "InstitutionTenders/";
    private static final String URL_GET_AD                      = SERVER_IP + "Contract/";
    private static final String URL_GET_TENDER                  = SERVER_IP + "Tender/";
    private static final String URL_GET_COMPANY                 = SERVER_IP + "Company/";
    private static final String URL_GET_AD_COMPANY              = SERVER_IP + "ADCompany/";
    private static final String URL_GET_TENDER_COMPANY          = SERVER_IP + "TenderCompany/";
    private static final String URL_GET_AD_COMPANIES_BY_PI      = SERVER_IP + "ADCompaniesByInstitution/";
    private static final String URL_GET_TENDER_COMPANIES_BY_PI  = SERVER_IP + "TenderCompaniesByInstitution/";
    private static final String URL_GET_ALL_COMPANIES_BY_PI     = SERVER_IP + "AllCompaniesByInstitution/";
    private static final String URL_GET_PI_BY_AD_COMPANY        = SERVER_IP + "InstitutionsByADCompany/";
    private static final String URL_GET_PI_BY_TENDER_COMPANY    = SERVER_IP + "InstitutionsByTenderCompany/";
    private static final String URL_GET_PI_BY_COMPANY           = SERVER_IP + "InstitutionsByCompany/";
    private static final String URL_GET_AD_COMPANY_CONTRACTS    = SERVER_IP + "ADCompanyContracts/";
    private static final String URL_GET_TENDER_COMPANY_TENDERS  = SERVER_IP + "TenderCompanyTenders/";
    private static final String URL_RED_FLAG_AD                 = SERVER_IP + "JustifyAD/";
    private static final String URL_RED_FLAG_TENDER             = SERVER_IP + "JustifyTender/";

    /* Search */
    private static final String URL_SEARCH_PUBLIC_INSTITUTION   = SEARCH_IP + "institution/";
    private static final String URL_SEARCH_COMPANY              = SEARCH_IP + "company/";
    private static final String URL_SEARCH_AD_COMPANY           = SEARCH_IP + "company/";
    private static final String URL_SEARCH_TENDER_COMPANY       = SEARCH_IP + "company/";
    private static final String URL_SEARCH_AD                   = SEARCH_IP + "contract/";
    private static final String URL_SEARCH_TENDERS              = SEARCH_IP + "tender/";

    /* Statistics */
    private static final String URL_STATS_ADS_BY_VALUE          = SERVER_IP + "report/Contracte_AD_ValoareEUR_top10";
    private static final String URL_STATS_TENDERS_BY_VALUE      = SERVER_IP + "report/Contracte_Tenders_ValoareEUR_top10";
    private static final String URL_STATS_INSTIT_BY_AD_NR       = SERVER_IP + "report/Institutii_AD_top10";
    private static final String URL_STATS_INSTIT_BY_TENDER_NR   = SERVER_IP + "report/Institutii_Tenders_top10";
    private static final String URL_STATS_COMPAN_BY_AD_NR       = SERVER_IP + "report/Company_AD_countAD_top10";
    private static final String URL_STATS_COMPAN_BY_TENDER_NR   = SERVER_IP + "report/Company_Tender_countTenders_top10";


    /* Bundle keys */
    public static final String BUNDLE_INST_TYPE                 = "bundle_inst_type";
    public static final String BUNDLE_PI_ID                     = "bundle_pi_id";
    public static final String BUNDLE_PI_NAME                   = "bundle_pi_name";
    public static final String BUNDLE_PI_CUI                    = "bundle_pi_cui";
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
    public static final String JSON_COMPANY_ACQ_ID              = "ID";
    public static final String JSON_TENDER_ID                   = "LicitatieID";
    public static final String JSON_TENDER_ID_STAT              = "LicitatiiId";
    public static final String JSON_TENDER_ID_SEARCH            = "LicitatieId";
    public static final String JSON_COMPANY_TENDER_ID           = "ID";
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
    public static final String JSON_CONTRACT_RED_FLAGS          = "NumarJustificari";

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
    public static final String JSON_COMPANY_NR_CONTRACTS        = "NrContracte";
    public static final String JSON_COMPANY_NR_TENDERS          = "NrLicitatii";


    public static final String JSON_COMPANY_PI_NAME             = "Nume";
    public static final String JSON_COMPANY_PI_ID               = "Id";
    public static final String JSON_COMPANY_PI_CUI              = "CUI";
    public static final String JSON_INSTITUTION_ID              = "InstitutiePublicaId";
    public static final String JSON_SEARCH_ID                   = "id";
    public static final String JSON_SEARCH_NAME                 = "name";
    public static final String JSON_SEARCH_TITLE                = "title";
    public static final String JSON_SEARCH_CUI                  = "reg_no";
    public static final String JSON_SEARCH_PRICE_RON            = "price_ron";
    public static final String JSON_SEARCH_DATE                 = "contract_date";
    public static final String JSON_SEARCH_COUNT                = "count";

    public static final String JSON_PI_NR_AD                    = "nrAD";
    public static final String JSON_PI_NR_TENDERS = "nrTenders";


    public static RequestQueue queue;


    /* Init the Communication Manager */
    public static void init(Context context) {
        System.out.println("Initializing the communication manager");
        queue = Volley.newRequestQueue(context);
    }


    /* Send server request for a JSON Array */
    public static void GETServerJsonArray(final CommManagerResponse commManagerResponse, String URL) {
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
                            commManagerResponse.onErrorOccurred("Nu s-a găsit niciun rezultat");
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }


    /* Send server request for a JSON Object */
    public static void GETServerJsonObject(final CommManagerObjectResponse commManagerResponse, String URL) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL,
                        (JSONObject) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                            commManagerResponse.onErrorOccurred("Nu s-a găsit niciun rezultat");
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    /* Send server POST */
    public static void post(final CommManagerResponse commManagerResponse, String URL) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("CommManager: Got response from the server " + response);
                        if (commManagerResponse != null)
                            commManagerResponse.processResponse(null);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("GOT ERRROR " + error + " " + error.toString());
                        error.printStackTrace();
                        if (commManagerResponse != null)
                            commManagerResponse.onErrorOccurred("Nu s-a găsit niciun rezultat");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                return params;
            }
        };
        queue.add(postRequest);
    }

    /* Send a GETServerJsonArray to the server for Public Institution Summary */
    public static void requestPISummary(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Summary GETServerJsonArray to URL " + URL_GET_PI_SUMMARY + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_SUMMARY + publicInstitutionID);
    }


    /* Send a GETServerJsonArray to the server for Public Institution Summary */
    public static void requestPIInfo(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Info GETServerJsonArray to URL " + URL_GET_PI_INFO + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_INFO + publicInstitutionID);
    }


    /* Send a GETServerJsonArray to the server for public institution direct acquisitions */
    public static void requestPIAcqs(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Acqs GETServerJsonArray to URL " + URL_GET_PI_ACQS + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_ACQS + publicInstitutionID);
    }


    /* Send a GETServerJsonArray to the server for public institution tenders */
    public static void requestPITenders(final CommManagerResponse commManagerResponse, int publicInstitutionID) {
        System.out.println("Send PI Tenders GETServerJsonArray to URL " + URL_GET_PI_TENDERS + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_TENDERS + publicInstitutionID);
    }


    /* Send a GETServerJsonArray to the server for a direct acquisition */
    public static void requestAD(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send AD GETServerJsonArray to URL " + URL_GET_AD + contractID);
        GETServerJsonArray(commManagerResponse, URL_GET_AD + contractID);
    }


    /* Send a GETServerJsonArray to the server for a tender */
    public static void requestTender(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send Tender GETServerJsonArray to URL " + URL_GET_TENDER + contractID);
        GETServerJsonArray(commManagerResponse, URL_GET_TENDER + contractID);
    }


    /* Send a GETServerJsonArray to the server for a Company */
    public static void requestCompany(final CommManagerResponse commManagerResponse, int companyID) {
        System.out.println("Send Company GETServerJsonArray to URL " + URL_GET_COMPANY + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_COMPANY + companyID);
    }


    /* Send a GETServerJsonArray to the server for an AD Company */
    public static void requestADCompany(final CommManagerResponse commManagerResponse, int companyID) {
        System.out.println("Send ADCompany GETServerJsonArray to URL " + URL_GET_AD_COMPANY + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_AD_COMPANY + companyID);
    }


    /* Send a GETServerJsonArray to the server for a Tender Company */
    public static void requestTenderCompany(final CommManagerResponse commManagerResponse, int companyId) {
        System.out.println("Send TenderCompany GETServerJsonArray to URL " + URL_GET_TENDER_COMPANY + companyId);
        GETServerJsonArray(commManagerResponse, URL_GET_TENDER_COMPANY + companyId);
    }


    /* Send a GETServerJsonArray to the server for all AD Companies in a PI */
    public static void requestADCompaniesByPI(final CommManagerResponse commManagerResponse,
                                              int publicInstitutionID) {
        System.out.println("Send ADCompanyByPI GETServerJsonArray to URL " +
                URL_GET_AD_COMPANIES_BY_PI + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_AD_COMPANIES_BY_PI + publicInstitutionID);
    }


    /* Send a GETServerJsonArray to the server for all Tender Companies in a PI */
    public static void requestTenderCompaniesByPI(final CommManagerResponse commManagerResponse,
                                                  int publicInstitutionID) {
        System.out.println("Send TenderCompaniesByPI GETServerJsonArray to URL " +
                URL_GET_TENDER_COMPANIES_BY_PI + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_TENDER_COMPANIES_BY_PI + publicInstitutionID);
    }

    /* Send a GETServerJsonArray to the server for all Companies in a PI */
    public static void requestAllCompaniesByPI(final CommManagerResponse commManagerResponse,
                                                  int publicInstitutionID) {
        System.out.println("Send AllCompaniesByPI GETServerJsonArray to URL " +
                URL_GET_ALL_COMPANIES_BY_PI + publicInstitutionID);
        GETServerJsonArray(commManagerResponse, URL_GET_ALL_COMPANIES_BY_PI + publicInstitutionID);
    }

    /* Send a GETServerJsonArray to the server for all Public Institutions in an AD Company */
    public static void requestPIsByADCompany(final CommManagerResponse commManagerResponse,
                                                  int companyID) {
        System.out.println("Send PISByADCompany GETServerJsonArray to URL " +
                URL_GET_PI_BY_AD_COMPANY + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_BY_AD_COMPANY + companyID);
    }

    /* Send a GETServerJsonArray to the server for all Public Institutions in a Tender Company */
    public static void requestPIsByTenderCompany(final CommManagerResponse commManagerResponse,
                                             int companyID) {
        System.out.println("Send PISByTenderCompany GETServerJsonArray to URL " +
                URL_GET_PI_BY_TENDER_COMPANY + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_BY_TENDER_COMPANY + companyID);
    }

    /* Send a GETServerJsonArray to the server for all Public Institutions in a Company */
    public static void requestPIsByCompany(final CommManagerResponse commManagerResponse,
                                                 int companyID) {
        System.out.println("Send PISByCompany GETServerJsonArray to URL " +
                URL_GET_PI_BY_COMPANY + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_PI_BY_COMPANY + companyID);
    }

    /* Send a GETServerJsonArray to the server for all the Contracts in an AD Company */
    public static void requestADCompanyContracts(final CommManagerResponse commManagerResponse,
                                                 int companyID) {
        System.out.println("Send ADCompanyContracts GETServerJsonArray to URL " +
                URL_GET_AD_COMPANY_CONTRACTS + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_AD_COMPANY_CONTRACTS + companyID);
    }

    /* Send a GETServerJsonArray to the server for all the Tenders in a Tender Company */
    public static void requestTenderCompanyTenders(final CommManagerResponse commManagerResponse,
                                                 int companyID) {
        System.out.println("Send TenderCompanyTenders GETServerJsonArray to URL " +
                URL_GET_TENDER_COMPANY_TENDERS + companyID);
        GETServerJsonArray(commManagerResponse, URL_GET_TENDER_COMPANY_TENDERS + companyID);
    }

    /* Send a red flag a direct acquisition */
    public static void requestRedFlagAD(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send Red Flag AD GETServerJsonArray to URL " + URL_RED_FLAG_AD + contractID);
        post(commManagerResponse, URL_RED_FLAG_AD + contractID);
    }

    /* Send a red flag a tender */
    public static void requestRedFlagTender(final CommManagerResponse commManagerResponse, int contractID) {
        System.out.println("Send Red Flag Tender GETServerJsonArray to URL " + URL_RED_FLAG_TENDER + contractID);
        post(commManagerResponse, URL_RED_FLAG_TENDER + contractID);
    }





    /*
     * SEARCH
     */

    /* Send a GETServerJsonObject to the server to search for a public institution */
    public static void searchPublicInstitution(final CommManagerObjectResponse commManagerResponse, String pi) {
        String encodedURL = URL_SEARCH_PUBLIC_INSTITUTION;
        pi = Normalizer.normalize(pi, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        try {
            encodedURL += URLEncoder.encode(pi, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Search Public Institutions with URL " + encodedURL);
        GETServerJsonObject(commManagerResponse, encodedURL);
    }

    /* Send a GETServerJsonObject to the server to search for an AD company */
    public static void searchCompany(final CommManagerObjectResponse commManagerResponse, String company) {
        String encodedURL = URL_SEARCH_COMPANY;
        company = Normalizer.normalize(company, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        try {
            encodedURL += URLEncoder.encode(company, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Search AD Companies with URL " + encodedURL);
        GETServerJsonObject(commManagerResponse, encodedURL);
    }

    /* Send a GETServerJsonObject to the server to search for direct acquisitions */
    public static void searchAD(final CommManagerObjectResponse commManagerResponse, String ad, int page) {
        String encodedURL = URL_SEARCH_AD;
        ad = Normalizer.normalize(ad, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        try {
            encodedURL += URLEncoder.encode(ad, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        encodedURL += "?page=" + page + "&perPage=" + SearchFragment.SEARCH_RESULT_PER_PAGE;
        System.out.println("Search ADs with URL " + encodedURL);
        GETServerJsonObject(commManagerResponse, encodedURL);
    }

    /* Send a GETServerJsonObject to the server to search for a tender */
    public static void searchTender(final CommManagerObjectResponse commManagerResponse, String tender, int page) {
        String encodedURL = URL_SEARCH_TENDERS;
        tender = Normalizer.normalize(tender, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        try {
            encodedURL += URLEncoder.encode(tender, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        encodedURL += "?page=" + page + "&perPage=" + SearchFragment.SEARCH_RESULT_PER_PAGE;
        System.out.println("Search Tenders with URL " + encodedURL);
        GETServerJsonObject(commManagerResponse, encodedURL);
    }


    /*
     * STATISTICS
     */

    /* Send a GETServerJsonArray to the server to get the TOP 10 ADs by value */
    public static void requestStatsADsByValue(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsADsByValue GETServerJsonArray to URL " + URL_STATS_ADS_BY_VALUE);
        GETServerJsonArray(commManagerResponse, URL_STATS_ADS_BY_VALUE);
    }

    /* Send a GETServerJsonArray to the server to get the TOP 10 Tenders by value */
    public static void requestStatsTenderssByValue(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsTendersByValue GETServerJsonArray to URL " + URL_STATS_TENDERS_BY_VALUE);
        GETServerJsonArray(commManagerResponse, URL_STATS_TENDERS_BY_VALUE);
    }

    /* Send a GETServerJsonArray to the server to get the TOP 10 Institutions by AD count */
    public static void requestStatsInstitutionsByADCount(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsInstitutionsByADCount GETServerJsonArray to URL " + URL_STATS_INSTIT_BY_AD_NR);
        GETServerJsonArray(commManagerResponse, URL_STATS_INSTIT_BY_AD_NR);
    }

    /* Send a GETServerJsonArray to the server to get the TOP 10 Institutions by Tender count */
    public static void requestStatsInstitutionsByTenderCount(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsInstitutionsByTenderCount GETServerJsonArray to URL " + URL_STATS_INSTIT_BY_TENDER_NR);
        GETServerJsonArray(commManagerResponse, URL_STATS_INSTIT_BY_TENDER_NR);
    }

    /* Send a GETServerJsonArray to the server to get the TOP 10 Companies by AD count */
    public static void requestStatsCompaniesByADCount(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsCompaniesByADCount GETServerJsonArray to URL " + URL_STATS_COMPAN_BY_AD_NR);
        GETServerJsonArray(commManagerResponse, URL_STATS_COMPAN_BY_AD_NR);
    }

    /* Send a GETServerJsonArray to the server to get the TOP 10 Companies by Tender count */
    public static void requestStatsCompaniesByTenderCount(final CommManagerResponse commManagerResponse) {
        System.out.println("Send StatsCompaniesByTenderCount GETServerJsonArray to URL " + URL_STATS_COMPAN_BY_TENDER_NR);
        GETServerJsonArray(commManagerResponse, URL_STATS_COMPAN_BY_TENDER_NR);
    }
}
