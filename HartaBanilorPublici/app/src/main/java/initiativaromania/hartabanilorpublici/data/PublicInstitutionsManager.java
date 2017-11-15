package initiativaromania.hartabanilorpublici.data;

import com.google.android.gms.maps.model.LatLngBounds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import initiativaromania.hartabanilorpublici.data.PublicInstitution;
import initiativaromania.hartabanilorpublici.ui.MapFragment;

/**
 * Created by claudiu on 8/19/17.
 */

public class PublicInstitutionsManager {

    public static final String PI_FILENAME = "institutii_publice_pins_v0.csv";

    /* This list contains all the public institutions */
    private static LinkedList<PublicInstitution> piList;
    private static Semaphore piSemaphore = new Semaphore(1);


    /* Fill the list of public institutions */
    public static void populatePIs(final MapFragment mapFragment) {

        if (mapFragment == null)
            System.out.println("NULL fragment");
        if (mapFragment.getActivity() == null)
            System.out.println("NULL activity fragment");

        if (piList != null)
            return;

        System.out.println("Populating the list of public institutions");
        piList = new LinkedList<PublicInstitution>();

        /* Read PIs from asset file */
        new Thread(new Runnable() {
            public void run() {
                BufferedReader reader = null;
                String[] lineArray;


                try {
                    piSemaphore.acquire();

                    reader = new BufferedReader(
                            new InputStreamReader(mapFragment.getActivity().getAssets().open(PI_FILENAME)));

                    String mLine;
                    while ((mLine = reader.readLine()) != null) {
                        lineArray = mLine.split(",");
                        if (lineArray.length < 4)
                            continue;

                        PublicInstitution pi = new PublicInstitution(Integer.parseInt(lineArray[0]),
                                Double.parseDouble(lineArray[1]),
                                Double.parseDouble(lineArray[2]),
                                Integer.parseInt(lineArray[3]));
                        piList.add(pi);

                    }

                    piSemaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Reading PIs from file completed");
                if (mapFragment.clusterManager != null)
                    System.out.println("Cluster manager available");
                else
                    System.out.println("Cluster manager is not available");
            }
        }).start();
    }


    /* Build a list with all the public instituions that are visible in this
    projections and return it */
    public static LinkedList<PublicInstitution> getVisiblePIs(LatLngBounds bounds) {
        LinkedList<PublicInstitution> visiblePIs = new LinkedList<PublicInstitution>();
        int index = 0;

        try {
            piSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (piList == null || piList.size() == 0)
            System.out.println("Null piList");
        else
            System.out.println("PiList " + piList.size());

        for (PublicInstitution pi : piList) {
            if (bounds.contains(pi.position)) {
                index++;
                visiblePIs.add(pi);
            }
        }

        System.out.println(index + " pis added out of total " + piList.size());

        piSemaphore.release();

        return visiblePIs;
    }
}