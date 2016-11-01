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
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ParticipantActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.ContractPageFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.TopCompanyFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.TopVotedContractsFragment;

import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by claudiu on 2/9/16.
 */

public class CommManager {
    private static final String TAG =CommManager.class.getName();
    private static final String SERVER_IP = "http://dev01.petrosol.ro:20500";

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
                        Log.i(TAG,"Response: " + response.toString());
                        commManagerResponse.processResponse(response);
                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        commManagerResponse.onErrorOccurred("Eroare conectare la server");
                    }
                });

        queue.add(jsObjRequest);
        Log.i(TAG,"Init Request sent");
    }


    /* Send request to server to get company details */
    public static void requestCompanyDetails(final Context context, String companyName) {
        Log.i(TAG,"Getting company details for " + String.format(URL_GET_COMPANY,
                companyName.replaceAll(" ", "%20").replaceAll("'", "%27%27")));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, String.format(URL_GET_COMPANY,
                        companyName.replaceAll(" ", "%20").replaceAll("'", "%27%27")).replaceAll("&", "%26"),
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG,"Response: " + response.toString());
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
        Log.i(TAG,"Request sent");
    }


    /* Send request to server to get all the buyers */
    public static void requestAllBuyers(final Context context) {
        Log.i(TAG,"Getting all buyers");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_ALL_BUYERS,
                        (String) null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response: " + response.toString());
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
        Log.i(TAG,"Request sent");
    }


    /* Send request to server to get buyer details */
    public static void requestBuyerDetails(final ICommManagerResponse commManagerResponse, String buyerName) {
        Log.i(TAG,"Getting buyer details for " + String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")));

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
        Log.i(TAG,"Request sent");
    }


    /* Get all the details for a contract based on its contract id */
    public static void requestContract(final ContractPageFragment contractPageFragment, int contractID) {
        Log.i(TAG,"Getting the contract details for id " + contractID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, String.format(URL_GET_ORDER, contractID),
                (String) null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,"Response: " + response);
                contractPageFragment.receiveContract(response);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contractPageFragment.getActivity(), "Eroare conectare la server. Incearca mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    /* Get top 10 most voted contracts */
    public static void requestTop10Contracts(final TopVotedContractsFragment fragment) {
        Log.i(TAG,"Getting top 10 voted contracts");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_TOP_VOTED_CONTRACTS,
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG,"Response: " + response.toString());
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
        Log.i(TAG,"Getting top 10 companies");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL_GET_TOP_COMPANIES,
                        (String)null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG,"Response: " + response.toString());
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
    public static void justifyContract(final ContractPageFragment contractPageFragment, Contract contract) {
        Log.i(TAG,"Calling justify " + contract.id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, String.format(URL_GET_JUSTIFICA, contract.id),
                (String) null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.i(TAG,"Response: " + response);
                contractPageFragment.contractJustification();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contractPageFragment.getActivity(), "Eroare conectare la server. Incearca mai tarziu", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }


    /* Get all company names for a buyer */
    public static void requestFirmsForBuyer(final Context context, String buyerName) {
        Log.i(TAG,"Getting Firms for buyer " + String.format(URL_GET_BUYER, buyerName.replaceAll(" ", "%20")));
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
        Log.i(TAG,"Request sent");
    }


    /* Get all buyer names for a company */
    public static void requestBuyersForFirm(final Context context, String firmName) {
        Log.i(TAG,"Getting Buyers for firm " + String.format(URL_GET_BUYER, firmName.replaceAll(" ", "%20")));
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
        Log.i(TAG,"Request sent");
    }
}
