package initiativaromania.hartabanilorpublici.comm;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by claudiu on 9/16/17.
 */

public interface CommManagerResponse {
    void processResponse(JSONArray response);
    void onErrorOccurred(String errorMsg);
}
