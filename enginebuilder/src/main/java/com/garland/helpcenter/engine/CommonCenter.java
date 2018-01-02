package com.garland.helpcenter.engine;

import java.io.Serializable;

/**
 * Created by lemon on 11/3/2017.
 */

public class CommonCenter implements Serializable {
    private static final long serialVersionUID="CommonCenter".hashCode();

    public String area;
	public CommonLocation location;

    public CommonCenter(String area, CommonLocation location) {
        this.area = area;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Lat"+this.location.latitude+"   Lon"+this.location.longitude;
    }
}
