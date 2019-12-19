package cn.xmzt.www.selfdrivingtools.bean;

public class GetDestinationBean {

    /**
     * groupId : 2676308753
     * destination :
     * destinationCoordinate :
     * gatherTime : 10-30 08:00
     * creator : 186222
     * id : 708
     */

    private String groupId;
    private String destination;
    private String destinationCoordinate;
    private String gatherTime;
    private int creator;
    private long id;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public void setDestinationCoordinate(String destinationCoordinate) {
        this.destinationCoordinate = destinationCoordinate;
    }

    public String getGatherTime() {
        return gatherTime;
    }

    public void setGatherTime(String gatherTime) {
        this.gatherTime = gatherTime;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
