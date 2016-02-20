package com.example.claudiu.investitiipublice.IRObjects;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.claudiu.investitiipublice.IRUserInterface.ContractActivity;
import com.example.claudiu.investitiipublice.IRUserInterface.ContractListActivity;
import com.example.claudiu.investitiipublice.IRUserInterface.MainActivity;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.AroundStatisticsFragment;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by claudiu on 2/9/16.
 */
public class CommManager {
    private static final String SERVER_IP = "http://192.168.1.52:5000";

    /* Requests to server */
    private static final String URL_GET_ORDERS = SERVER_IP + "/getOrders?lat=%s&lng=%s&zoom=%s";
    private static final String URL_GET_ORDER = SERVER_IP + "/getOrder?id=%s";
    private static final String URL_GET_COMPANY = SERVER_IP + "/getFirm?name=%s";
    private static final String URL_GET_JUSTIFICA = SERVER_IP + "/justify?id=%s";
    private static final String URL_GET_CATEGORY = SERVER_IP + "/categoryDetails?categoryName=%s";
    private static final String URL_GET_TOP_COMPANIES = SERVER_IP + "/getTop10Firm";
    private static final String URL_GET_STATISTICS = SERVER_IP + "/getStatisticsArea?lat=%s&lng=%s&zoom=%s";


    /* All the contracts */
    public static LinkedList<Contract> contracts = new LinkedList<Contract>();
    public static RequestQueue queue;

    public static void init(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    /* Send request to server to get company details */
    public static void requestCompanyDetails(final Context context, String companyName) {
        System.out.println("Getting company details for " + String.format(URL_GET_COMPANY, companyName.replaceAll(" ", "%20")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_COMPANY, companyName.replaceAll(" ", "%20")),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        ContractListActivity cla = (ContractListActivity) context;
                        cla.receiveCompanyDetails(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error connecting to server for company details. Try again later",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Request sent");
    }


    /* Send request to server to get all the contracts */
    public static void requestAllContracts(final Context context) {

        System.out.println("Getting all contracts");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_ORDERS, 0, 0, 0),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        MainActivity ma = (MainActivity) context;
                        ma.receiveAllContracts(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error connecting to server. Try again later", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Request sent");
    }

    public static LinkedList<Contract> getEntityContractList(Serializable entity, int entityType) {
        return contracts;
    }


    /* Get all the details for a contract based on its contract id */
    public static void requestContract(final Context context, int contractID) {
        System.out.println("Getting the contract details for id " + contractID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, String.format(URL_GET_ORDER, contractID),
                (String) null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response: " + response);
                ContractActivity ca = (ContractActivity) context;
                ca.receiveContract(response);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error connection to server. Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public static void setStatisticsData(final AroundStatisticsFragment statisticsFragment, double lat, double lng, int zoom) {

        System.out.println("Getting statistics");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_STATISTICS, 0, 0, 0),
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        statisticsFragment.dataUpdated(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(statisticsFragment.getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }

    public static void justifyContract(final Context context, Contract contract) {
        System.out.println("Calling justify " + contract.id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, String.format(URL_GET_JUSTIFICA, contract.id),
                (String) null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response: " + response);
                ContractActivity ca = (ContractActivity)context;
                ca.ackJustify();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error connection to server. Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
