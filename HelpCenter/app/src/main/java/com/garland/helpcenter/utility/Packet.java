package com.garland.helpcenter.utility;

/**
 * Created by lemon on 10/29/2017.
 */

public class Packet {
    private String t1,m1,t2,m2,t3,m3;

    public Packet(String t1, String m1, String t2, String m2, String t3, String m3) {
        this.t1 = t1;
        this.m1 = m1;
        this.t2 = t2;
        this.m2 = m2;
        this.t3 = t3;
        this.m3 = m3;
    }

    public String getT1() {
        if(t1==null) return "";
        return t1;
    }

    public String getM1() {
        if(m1==null) return "";
        return m1;
    }

    public String getT2() {
        if(t2==null) return "";
        return t2;
    }

    public String getM2() {
        if(m2==null) return "";
        return m2;
    }

    public String getT3() {
        if(t3==null) return "";
        return t3;
    }

    public String getM3() {
        if(m3==null) return "";
        return m3;
    }
}

