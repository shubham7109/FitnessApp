package edu.iastate.bitfitx.Models;

public class WeightModel {

    private String weightInPounds;
    private long timeInMillis;

    public WeightModel(String weightInPounds, long timeInMillis) {
        this.weightInPounds = weightInPounds;
        this.timeInMillis = timeInMillis;
    }

    public String getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(String weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
