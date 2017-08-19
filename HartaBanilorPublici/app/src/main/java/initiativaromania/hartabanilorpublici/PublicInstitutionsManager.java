package initiativaromania.hartabanilorpublici;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by claudiu on 8/19/17.
 */

public class PublicInstitutionsManager {

    /* This set holds the public institutions that are not yet
    displayed on the map */
    private static HashSet<PublicInstitution> piSet;

    /* This list contains all the public institutions */
    private static LinkedList<PublicInstitution> piList;


    /* Fill the list of public institutions */
    public static void populatePIs() {

        if (piList != null)
            return;

        System.out.println("Populating the list of public institutions");
        piList = new LinkedList<PublicInstitution>();

        Random randomGenerator = new Random();
        for (int i = 0; i < 13000; i++) {
            int randomlat = randomGenerator.nextInt(370);
            int randomlong = randomGenerator.nextInt(600);
            double latitude = 44 + (double)randomlat / 100;
            double longitude = 22.5 + (double)randomlong / 100;
            piList.add(new PublicInstitution("Spitalul Universitar Bucuresti",longitude, latitude));
        }
    }


    /* Recreate the set of public institutions from the list */
    public static void resetPiSet() {
        piSet = new HashSet<PublicInstitution>();

        System.out.println("Resetting the PI set");

        if (piList == null || piList.size() == 0)
            populatePIs();

        for (PublicInstitution pi : piList)
            piSet.add(pi);

        System.out.println("The PI set has " + piSet.size());
    }


    /* Remove from the set the public instituions that are visible in this
    projections and return them */
    public static LinkedList<PublicInstitution> popVisiblePIs(GoogleMap mMap) {
        LinkedList<PublicInstitution> visiblePIs = new LinkedList<PublicInstitution>();
        int index = 0;
        PublicInstitution pi;

        System.out.println("Creating the list of visible public institutions");
        if (piSet == null || piSet.size() == 0)
            System.out.println("Null piSet");
        else
            System.out.println("PISEt " + piSet.size());

        Iterator<PublicInstitution> it = piSet.iterator();

        while (it.hasNext()) {
            pi = it.next();
            if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(pi.position)) {
                index++;
                visiblePIs.add(pi);
                it.remove();
            }
        }

        System.out.println(index + " visible public institutions removed from the set");

        return visiblePIs;
    }
}
