package com.garland.helpcenter.utility;


import com.garland.helpcenter.engine.Engine;

import java.util.TreeMap;

/**
 * Created by lemon on 10/27/2017.
 */

public class Utility {

    private static final String emailRegex="[a-zA-Z_]+[a-zA-Z0-9_.]+@[a-z]+.com";
    private static TreeMap<String,FireList> continent;

    static {
        continent=new TreeMap<>();
        continent.put("Barisal",new FireList().add("Barisal","Barguna","Bhola","Jhalokati","Patuakhali","Pirojpur"));
        continent.put("Chittagong",new FireList().add("Brahmanbaria","Comilla","Chandpur","Lakshmipur","Noakhali","Feni","Khagrachhari","Rangamati","Bandarban","Chittagong","Cox's Bazar"));
        continent.put("Dhaka",new FireList().add("Dhaka","Gazipur","Kishoreganj","Manikganj","Munshiganj","Narayanganj","Narsingdi","Tangail","Faridpur","Gopalganj","Madaripur","Rajbari","Shariatpur"));
        continent.put("Khulna", new FireList().add("Bagerhat","Chuadanga","Jessore","Jhenaidah","Khulna","Kushtia","Magura","Meherpur","Narail","Satkhira"));
        continent.put("Mymensingh",new FireList().add("Jamalpur","Mymensingh","Netrokona","Sherpur"));
        continent.put("Rajshahi",new FireList().add("Bogra","Chapainawabganj","Joypurhat","Naogaon","Natore","Pabna","Rajshahi","Sirajganj") );
        continent.put("Rangpur", new FireList().add("Dinajpur","Gaibandha","Kurigram","Lalmonirhat","Nilphamari","Panchagarh","Rangpur","Thakurgaon"));
        continent.put("Sylhet",new FireList().add("Habiganj","Moulvibazar","Sunamganj","Sylhet"));
    }

    public static TreeMap<String, FireList> getContinent() {
        return continent;
    }

    public static final String fromOutSide="out";
    public static final String SHOULD_LOAD_FROM_OUT="keyOut";
    public static final String RESULT_CODE_YES="yes";
    public static final String RESULT_CODE_NO="no";

    public static Engine engine;
}
