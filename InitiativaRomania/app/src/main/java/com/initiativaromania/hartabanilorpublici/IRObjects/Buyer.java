package com.initiativaromania.hartabanilorpublici.IRObjects;

import java.io.Serializable;

/**
 * Created by claudiu on 3/24/16.
 */
public class Buyer implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public double longitude;
    public double latitude;

    public long totalPrice;
}
