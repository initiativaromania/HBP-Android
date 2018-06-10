package initiativaromania.hartabanilorpublici.comm;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by claudiu on 6/9/18.
 */

public interface CommManagerObjectResponse {
    void processResponse(JSONObject response);
    void onErrorOccurred(String errorMsg);
}
