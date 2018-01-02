package com.garland.helpcenter.engine;

import java.io.Serializable;

/**
 * Created by lemon on 11/3/2017.
 */

public class CommonLocation implements Serializable {
    private static final long serialVersionUID="CommonLocation".hashCode();

    public double latitude;
    public double longitude;

    public CommonLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CommonLocation(String loc) {
        this.latitude=Double.parseDouble(loc.substring(0,loc.indexOf(',')));
        this.longitude=Double.parseDouble(loc.substring(loc.indexOf(',')+1));
    }

}
