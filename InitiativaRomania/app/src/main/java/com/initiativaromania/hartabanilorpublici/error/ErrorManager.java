package com.initiativaromania.hartabanilorpublici.error;

import android.content.Context;
import android.widget.Toast;

public class ErrorManager {

    public static void handleError(Context context, Throwable t) {
        Toast.makeText(context, "Incercati mai traziu..", Toast.LENGTH_SHORT).show();
    }

}
