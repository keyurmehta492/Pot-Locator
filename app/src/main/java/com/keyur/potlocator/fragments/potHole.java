package com.keyur.potlocator.fragments;

/**
 * Created by Keyur on 4/23/2017.
 */

public class potHole {

    int potid;
    Double latitude;
    Double longitude;
    Boolean isrepaired;
    String time;

    public potHole(int potid,Double latitude,Double longitude,Boolean isrepaired,String time) {
        this.setPotid(potid);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setIsrepaired(isrepaired);
        this.setTime(time);
    }

    public int getPotid() {
        return potid;
    }

    public void setPotid(int potid) {
        this.potid = potid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsrepaired() {
        return isrepaired;
    }

    public void setIsrepaired(Boolean isrepaired) {
        this.isrepaired = isrepaired;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
