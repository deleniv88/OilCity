package oil.city.Model;

public class RatingRest {

    private String userName;
    private String restorauntId;
    public String rateValue;
    private String comment;

    public RatingRest() {
    }

    public RatingRest(String userName, String restorauntId, String rateValue, String comment) {
        this.userName = userName;
        this.restorauntId = restorauntId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRestorauntId() {
        return restorauntId;
    }

    public void setRestorauntId(String restorauntId) {
        this.restorauntId = restorauntId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
