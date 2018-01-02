package com.garland.helpcenter.engine;

import java.io.Serializable;

/**
 * Created by lemon on 11/3/2017.
 * */

public class Description implements Comparable<Description>,Serializable {
    private static final long serialVersionUID="Description".hashCode();
    public String from;
    public String to;
    public int centerId;
    public String room;
    public String totalStudent;

    public Description() {
    }

    public Description(String from, String to, int centerId, String room, String totalStudent) {
        this.from = from;
        this.to = to;
        this.centerId = centerId;
        this.room = room;
        this.totalStudent = totalStudent;
    }

    @Override
    public boolean equals(Object obj) {
        Description description=(Description)obj;
        return description.from.equals(this.from)&&description.to.equals(this.to);
    }

	@Override
	public int compareTo(Description o) {
		return this.from.compareTo(o.from);
    }
    
    @Override
    public String toString() {
        return ""+this.from+"--"+this.to+"-->"+this.room;
    }
}
