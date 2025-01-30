package cakeApp.domain;

import java.io.Serializable;

public class Cake<ID> implements Identifiable<ID>, Serializable {
    private ID cakeId;
    private String cakeName;
    private String cakeFlavour;
    private double cakePrice;
    private int cakeLayers;


    public Cake(ID cakeId, String cakeName, String flavour, double price, int layers) {
        this.cakeId = cakeId;
        this.cakeName = cakeName;
        this.cakeFlavour = flavour;
        this.cakePrice = price;
        this.cakeLayers = layers;
    }

    public Cake(String name, String flavour, double price, int layers) {
        this.cakeName = name;
        this.cakeFlavour = flavour;
        this.cakePrice = price;
        this.cakeLayers = layers;
    }

    @Override
    public ID getId(){
        return cakeId;
    }

    @Override
    public void setId(ID cakeId){
        this.cakeId = cakeId;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public String getCakeFlavour() {
        return cakeFlavour;
    }

    public void setCakeFlavour(String cakeFlavour) {
        this.cakeFlavour = cakeFlavour;
    }

    public double getPrice() {
        return cakePrice;
    }

    public void setPrice(double price) {
        this.cakePrice = price;
    }

    public Integer getCakeLayers() {
        return cakeLayers;
    }

    public void setCakeLayers(Integer cakeLayers) {
        this.cakeLayers = cakeLayers;
    }

    @Override
    public String toString() {
        return cakeId + "," + cakeName + "," + cakeFlavour + "," + cakePrice + "," + cakeLayers;
    }
}


