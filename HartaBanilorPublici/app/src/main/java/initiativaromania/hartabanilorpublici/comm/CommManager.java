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


    /* Bundle keys */
    public static final String BUNDLE_INST_TYPE                 = "bundle_inst_type";
    public static final String BUNDLE_PI_ID                     = "bundle_pi_id";
    public static final String BUNDLE_PI_NAME                   = "bundle_pi_name";
    public static final String BUNDLE_PI_ACQS                   = "bundle_pi_acqs";
    public static final String BUNDLE_PI_TENDERS                = "bundle_pi_tenders";


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
                        (String) null, new Response.Listener<JSONArray>() {

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
}
