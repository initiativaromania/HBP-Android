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
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.TopCompanyFragment;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.TopVotedContractsFragment;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by claudiu on 2/9/16.
 */
public class CommManager {
    //private static final String SERVER_IP = "http://dev01.petrosol.ro:20500";
    private static final String SERVER_IP = "http://192.168.0.104:5000";
    
    /* Requests to server */
    private static final String URL_GET_ORDERS = SERVER_IP + "/getOrders?lat=%s&lng=%s&zoom=%s";
    private static final String URL_GET_ORDER = SERVER_IP + "/getOrder?id=%s";
    private static final String URL_GET_COMPANY = SERVER_IP + "/getFirm?name=%s";
    private static final String URL_GET_BUYER = SERVER_IP + "/getOrdersForBuyer?name=%s";
    private static final String URL_GET_JUSTIFICA = SERVER_IP + "/justify?id=%s";
    private static final String URL_GET_CATEGORY = SERVER_IP + "/categoryDetails?categoryName=%s";
    private static final String URL_GET_TOP_COMPANIES = SERVER_IP + "/getTop10Firm";
    private static final String URL_GET_TOP_VOTED_CONTRACTS = SERVER_IP + "/getTop10VotedContracts";
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
                        Toast.makeText(context, "Eroare conectare la server",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Request sent");
    }


    /* Send request to server to get buyer details */
    public static void requestBuyerDetails(final Context context, String buyerName) {
        System.out.println("Getting buyer details for " + String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        ContractListActivity cla = (ContractListActivity) context;
                        cla.receiveBuyerDetails(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Eroare conectare la server",
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
                        Toast.makeText(context, "Eroare conectare la server. Incearca mai tarziu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Eroare conectare la server. Incearca mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    public static void requestStatsAround(final AroundStatisticsFragment statisticsFragment, double lat, double lng, int zoom) {

        System.out.println("Getting statistics around you");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_STATISTICS, lat, lng, zoom),
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        //statisticsFragment.dataUpdated(response);
                        statisticsFragment.displayStatsInArea();

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(statisticsFragment.getContext(), "Eroare conectare la server", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }


    /* Get top 10 most voted contracts */
    public static void requestTop10Contracts(final TopVotedContractsFragment fragment) {
        System.out.println("Getting top 10 voted contracts");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_TOP_VOTED_CONTRACTS,
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        fragment.displayTop10Contracts(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(fragment.getContext(), "Eroare conectare la server", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }


    /* Get top 10 companies */
    public static void requestTop10Companies(final TopCompanyFragment fragment) {
        System.out.println("Getting top 10 companies");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_TOP_COMPANIES,
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        fragment.displayTop10Companies(response);

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(fragment.getContext(), "Eroare conectare la server", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }


    /* Call server to add a request to justify a contract */
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
                Toast.makeText(context, "Eroare conectare la server. Incearca mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
