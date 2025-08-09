package com.example.spytech;

public class User {
    private String msisdn;
    private String Username;
    private String cnic;
    private String address;
    private String company;

    public User(String msisdn, String Username, String cnic, String address, String company) {
        this.msisdn = msisdn;
        this.Username = Username;
        this.cnic = cnic;
        this.address = address;
        this.company = company;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getUsername() {
        return Username;
    }

    public String getCnic() {
        return cnic;
    }
    public String getAddress() {
        return address;
    }

    public String getCompany() {
        return company;
    }
}
