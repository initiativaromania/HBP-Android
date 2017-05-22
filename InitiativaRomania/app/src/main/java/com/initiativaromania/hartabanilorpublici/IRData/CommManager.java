/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.initiativaromania.hartabanilorpublici.IRData;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ContractActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ParticipantActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.AroundStatisticsFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.TopCompanyFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.TopVotedContractsFragment;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by claudiu on 2/9/16.
 */

public class CommManager {
    private static final String SERVER_IP;
    private static final String SERVER_PROPERTIES_FILENAME = "server.properties";

    private static final String LOG_TAG = "debugger";

    static {
        String      ip    = "localhost";
        String      port  = "80";
        Properties  prop  = new Properties();
        InputStream input = null;

        try {
            input = MainActivity.context.getAssets().open(SERVER_PROPERTIES_FILENAME); // WINNER
            prop.load(input);

            ip   = prop.getProperty("server.ip");
            port = prop.getProperty("server.port");
            Log.d(LOG_TAG, "[REMOVE_ME] [MY_DEBUG] ip: " + ip + " port " + port);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SERVER_IP = "http://" + ip + ":" + port;
        }
    }


    /* Requests to server */
    private static final String URL_GET_ORDERS              = SERVER_IP + "/getOrders?lat=%s&lng=%s&zoom=%s";
    private static final String URL_GET_ORDER               = SERVER_IP + "/getOrder?id=%s";
    private static final String URL_GET_COMPANY             = SERVER_IP + "/getFirm?name=%s";
    private static final String URL_GET_BUYER               = SERVER_IP + "/getOrdersForBuyer?name=%s";
    private static final String URL_GET_JUSTIFICA           = SERVER_IP + "/justify?id=%s";
    private static final String URL_GET_CATEGORY            = SERVER_IP + "/categoryDetails?categoryName=%s";
    private static final String URL_GET_TOP_COMPANIES       = SERVER_IP + "/getTop10Firm";
    private static final String URL_GET_INIT_DATA           = SERVER_IP + "/getInitData";
    private static final String URL_GET_TOP_VOTED_CONTRACTS = SERVER_IP + "/getTop10VotedContracts";
    private static final String URL_GET_STATISTICS          = SERVER_IP + "/getStatisticsArea?lat=%s&lng=%s&zoom=%s";
    private static final String URL_GET_ALL_BUYERS          = SERVER_IP + "/getBuyers";
    private static final String URL_GET_FIRMS_FOR_BUYER     = SERVER_IP + "/getFirmsForBuyer?name=%s";
    private static final String URL_GET_BUYERS_FOR_FIRM     = SERVER_IP + "/getBuyersForFirm?name=%s";


    /* All the buyers */
    public static LinkedList<Buyer> buyers = new LinkedList<Buyer>();

    /* All the buyers in your area */
    public static LinkedList<Buyer> aroundBuyersList = new LinkedList<>();

    /* Contracts total price i your area */
    public static double aroundTotalSum = 0;

    public static RequestQueue queue;

    public static void init(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void updateAroundBuyers() {

        float distance[] = {0};
        distance[0] = Float.MAX_VALUE;
        aroundTotalSum = 0;
        aroundBuyersList.clear();

        /* Ask again for location */
        Location l = MainActivity.currentLocation;
        if (l == null) {
            MainActivity.locationListener.initLocationService((MainActivity) MainActivity.context);
            return;
        }

        for (final Buyer itBuyer : CommManager.buyers) {

            /* Determine if a buyer is in our area */
            Location.distanceBetween(l.getLatitude(), l.getLongitude(), itBuyer.latitude,
                    itBuyer.longitude, distance);

            if (distance[0] < MainActivity.circle.getRadius()) {
                aroundTotalSum += itBuyer.totalPrice;
                CommManager.aroundBuyersList.add(itBuyer);
            }
        }

        if (CommManager.aroundBuyersList.isEmpty())
            Toast.makeText(MainActivity.context, "Nu e niciun contract in jurul tau", Toast.LENGTH_SHORT).show();
    }

    /* Send request to server to get init data for the infographic */
    public static void requestInitData(final ICommManagerResponse commManagerResponse) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_INIT_DATA,
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        commManagerResponse.processResponse(response);
                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        commManagerResponse.onErrorOccurred("Eroare conectare la server");
                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Init Request sent");
    }


    /* Send request to server to get company details */
    public static void requestCompanyDetails(final Context context, String companyName) {
        System.out.println("Getting company details for " + String.format(URL_GET_COMPANY,
                companyName.replaceAll(" ", "%20").replaceAll("'", "%27%27")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_COMPANY,
                        companyName.replaceAll(" ", "%20").replaceAll("'", "%27%27")).replaceAll("&", "%26"),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //System.out.println("Response: " + response.toString());
                        ParticipantActivity cla = (ParticipantActivity) context;
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


    /* Send request to server to get all the buyers */
    public static void requestAllBuyers(final Context context) {
        System.out.println("Getting all buyers");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_ALL_BUYERS,
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        MainActivity ma = (MainActivity) context;
                        ma.receiveAllBuyers(response);

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
    public static void requestBuyerDetails(final ICommManagerResponse commManagerResponse, String buyerName) {
        System.out.println("Getting buyer details for " + String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_BUYER,
                        buyerName.replaceAll(" ", "%20").replaceAll("'", "%27%27")).replaceAll("&", "%26"),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        commManagerResponse.processResponse(response);
                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        commManagerResponse.onErrorOccurred("Eroare conectare la server");

                    }
                });

        queue.add(jsObjRequest);
        System.out.println("Request sent");
    }


    /* Get all the details for a contract based on its contract id */
    public static void requestContract(final Context context, int contractID) {
        System.out.println("Getting the contract details for id " + contractID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, String.format(URL_GET_ORDER, contractID),
                (String) null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //System.out.println("Response: " + response);
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

    /* Get top 10 most voted contracts */
    public static void requestTop10Contracts(final TopVotedContractsFragment fragment) {
        System.out.println("Getting top 10 voted contracts");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_TOP_VOTED_CONTRACTS,
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //System.out.println("Response: " + response.toString());
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
                        //System.out.println("Response: " + response.toString());
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
                //System.out.println("Response: " + response);
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


    /* Get all company names for a buyer */
    public static void requestFirmsForBuyer(final Context context, String buyerName) {
        System.out.println("Getting Firms for buyer " + String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_FIRMS_FOR_BUYER,
                        buyerName.replaceAll(" ", "%20").replaceAll("'", "%27%27")).replaceAll("&", "%26"),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ParticipantActivity cla = (ParticipantActivity) context;
                        cla.receiveFirmsForBuyer(response);

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


    /* Get all buyer names for a company */
    public static void requestBuyersForFirm(final Context context, String firmName) {
        System.out.println("Getting Buyers for firm " + String.format(URL_GET_BUYER, firmName.replaceAll(" ", "%20")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_BUYERS_FOR_FIRM,
                        firmName.replaceAll(" ", "%20").replaceAll("'", "%27%27")).replaceAll("&", "%26"),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ParticipantActivity cla = (ParticipantActivity) context;
                        cla.receiveBuyersForFirm(response);

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
}
