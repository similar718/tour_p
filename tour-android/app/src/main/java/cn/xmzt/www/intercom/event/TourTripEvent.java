package cn.xmzt.www.intercom.event;

public class TourTripEvent {

    /*
    群id
     */
    private String groupId;
    /*
    线路id
     */
    private String lineId;
    /*
    行程id
     */
    private String tripId;

    public TourTripEvent(String groupId, String lineId, String tripId) {
        this.groupId = groupId;
        this.lineId = lineId;
        this.tripId = tripId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
