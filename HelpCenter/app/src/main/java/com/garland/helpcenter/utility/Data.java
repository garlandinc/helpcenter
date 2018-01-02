package com.garland.helpcenter.utility;

/**
 * Created by lemon on 10/28/2017.
 */

public class Data {
    private String mail;
    private String password;
    private String district;
    private String division;
    private String name;
    private String phone1,phone2;
    private String message;

    public Data() {
    }

    public Data(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public Data(String district, String division, String message) {
        this.district = district;
        this.division = division;
        this.message = message;
    }

    public Data(String mail, String password, String district, String division, String name, String phone1, String phone2) {
        this.mail = mail;
        this.password = password;
        this.district = district;
        this.division = division;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getDistrict() {
        return district;
    }

    public String getName() {
        return name;
    }

    public String getDivision() {
        return division;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
