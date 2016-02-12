package com.example.claudiu.investitiipublice.IRObjects;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.claudiu.investitiipublice.IRUserInterface.MainActivity;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by claudiu on 2/9/16.
 */
public class ContractManager {
    private static final String SERVER_IP = "http://192.168.0.104:5000";
    private static final int MAX_MOCKUP_CONTRACTS = 50;
    private static final String URL_GET_ORDERS = SERVER_IP + "/getOrders?lat=%s&lng=%s&zoom=%s";
    private static final String URL_GET_ORDER = SERVER_IP + "/getOrder?id=%s";
    private static final String URL_GET_COMPANY = SERVER_IP + "/getFirm?name=%s";
    private static final String URL_GET_JUSTIFICA = SERVER_IP + "/justify?id=%s";
    private static final String URL_GET_CATEGORY = SERVER_IP + "/categoryDetails?categoryName=%s";
    private static final String URL_GET_TOP_COMPANIES = SERVER_IP + "/getTop10Firm";
    private static final String URL_GET_STATISTICS = SERVER_IP + "/getStatisticsArea?lat=%s&lng=%s&zoom=%s";



    public static LinkedList<Contract> contracts = new LinkedList<Contract>();

    private static void addMockContracts(Contract test) {
        Contract newContract = null;
        Random r = new Random();


        for (int i = 0; i < MAX_MOCKUP_CONTRACTS; i++) {

            try {
                newContract = (Contract)test.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            System.out.println("Clone " + newContract.latitude + ", " + newContract.longitude);

            int high = 220;
            int low = 10;
            double longitude = low + r.nextInt(high - low);
            newContract.longitude = Math.floor(newContract.longitude) + (double)(longitude / 1000);

            high = 490;
            low = 370;
            double latitude = low + r.nextInt(high - low);
            newContract.latitude = Math.floor(newContract.latitude) + (double)(latitude/ 1000);

            System.out.println("Adding contract at " + newContract.latitude + newContract.longitude + " randoms " + latitude + ", " + longitude);

            contracts.add(newContract);
        }
    }


    public static void getAllContracts(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        System.out.println("Getting all contracts");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_ORDERS, 0, 0, 0),
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        MainActivity ma = (MainActivity)context;
                        ma.storeAllContracts(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error connecting to server", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Request sent");
    }

    public static LinkedList<Contract> getContractList(Serializable entity, int entityType) {

        return contracts;
    }
}
