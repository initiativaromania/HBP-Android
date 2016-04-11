package com.initiativaromania.hartabanilorpublici.IRData;

import org.json.JSONObject;

/**
 * Created by Rares Dumitrache on 4/11/2016.
 */
public interface ICommManagerResponse {
    void processResponse(JSONObject response);
    void onErrorOccurred(String errorMsg);
}
