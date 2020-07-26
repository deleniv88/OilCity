package oil.city.Model;

public class Rating {
    private String userUid;
    private String userName;
    private String eventId;
    private String rateValue;
    private String comment;


    public Rating() {
    }

    public Rating(String userUid, String userName, String eventId, String rateValue, String comment) {
        this.userUid = userUid;
        this.userName = userName;
        this.eventId = eventId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

