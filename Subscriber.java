package com.example.spytech;

public class Subscriber {

    private String msisdn;
    private String name;
    private String cnic;
    private String address;
    private String company;

    public Subscriber(String msisdn, String name, String cnic, String address, String company) {
        this.msisdn = msisdn;
        this.name = name;
        this.cnic = cnic;
        this.address = address;
        this.company = company;
    }

    // Getters for data access
    public String getMsisdn() { return msisdn; }
    public String getName() { return name; }
    public String getCnic() { return cnic; }
    public String getAddress() { return address; }
    public String getCompany() { return company; }

    @Override
    public String toString() {
        return "PhoneNo: " + msisdn + "\nName: " + name + "\nCNIC: " + cnic + "\nAddress: " + address + "\nCompany: " + company;
    }
}
