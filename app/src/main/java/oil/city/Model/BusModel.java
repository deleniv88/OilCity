package oil.city.Model;

public class BusModel {

    private String nameOfBus, textOneBus, textTwoBus,textThreeBus;
    private boolean expandable;

    public BusModel() {
    }

    public BusModel(String nameOfBus, String textOneBus, String textTwoBus, String textThreeBus, boolean expandable) {
        this.nameOfBus = nameOfBus;
        this.textOneBus = textOneBus;
        this.textTwoBus = textTwoBus;
        this.textThreeBus = textThreeBus;
        this.expandable = expandable;
    }

    public String getNameOfBus() {
        return nameOfBus;
    }

    public void setNameOfBus(String nameOfBus) {
        this.nameOfBus = nameOfBus;
    }

    public String getTextOneBus() {
        return textOneBus;
    }

    public void setTextOneBus(String textOneBus) {
        this.textOneBus = textOneBus;
    }

    public String getTextTwoBus() {
        return textTwoBus;
    }

    public void setTextTwoBus(String textTwoBus) {
        this.textTwoBus = textTwoBus;
    }

    public String getTextThreeBus() {
        return textThreeBus;
    }

    public void setTextThreeBus(String textThreeBus) {
        this.textThreeBus = textThreeBus;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
