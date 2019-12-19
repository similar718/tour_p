package cn.xmzt.www.selfdrivingtools.bean;

import java.util.List;

public class GetTripDaysInfo {

    /**
     * days : 3
     * pointList : [
     * {"dayNum":1,"type":1,"coordinate":{"longitude":104.081143,"latitude":30.650767}},
     * {"dayNum":1,"type":2,"coordinate":{"longitude":104.081143,"latitude":30.650767}},
     * {"dayNum":1,"type":3,"title":"迪士尼乐园","address":"深圳","coordinate":{"longitude":114.057868,"latitude":22.543099}},
     * {"dayNum":1,"type":4,"coordinate":{"longitude":116.407526,"latitude":39.90403}},
     * {"dayNum":2,"type":1,"coordinate":{"longitude":104.146301,"latitude":30.632772}},
     * {"dayNum":2,"type":2,"coordinate":{"longitude":104.146301,"latitude":30.632772}},
     * {"dayNum":2,"type":3,"title":"著名景点XX","address":"深圳","coordinate":{"longitude":22.543099,"latitude":114.057868}},
     * {"dayNum":2,"type":4,"coordinate":{"longitude":116.407526,"latitude":39.90403}},
     * {"dayNum":3,"type":1,"coordinate":{"longitude":101.01258,"latitude":30.031793}},
     * {"dayNum":3,"type":2,"coordinate":{"longitude":101.01258,"latitude":30.031793}},
     * {"dayNum":3,"type":3,"title":"未知景点XX","address":"深圳","coordinate":{"longitude":22.543099,"latitude":114.057868}},
     * {"dayNum":3,"type":4,"coordinate":{"longitude":116.416357,"latitude":39.928353}}]
     */
    private int days;
    private List<PointListBean> pointList;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<PointListBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointListBean> pointList) {
        this.pointList = pointList;
    }

    public static class PointListBean {
        /**
         * dayNum : 1
         * type : 1 1 集合点 2 出发地 3 景点 4  住宿  // 2景点 4住宿 8早餐 9午餐 10晚餐
         * coordinate : {"longitude":104.081143,"latitude":30.650767}
         * title : 迪士尼乐园
         * address : 深圳
         */

        private int dayNum;
        private int type;
        private CoordinateBean coordinate;
        private String title;
        private String address;
        private boolean is_checked = false;

        public boolean isIs_checked() {
            return is_checked;
        }

        public void setIs_checked(boolean is_checked) {
            this.is_checked = is_checked;
        }

        public int getDayNum() {
            return dayNum;
        }

        public void setDayNum(int dayNum) {
            this.dayNum = dayNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public CoordinateBean getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(CoordinateBean coordinate) {
            this.coordinate = coordinate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public static class CoordinateBean {
            /**
             * longitude : 104.081143
             * latitude : 30.650767
             */

            private double longitude;
            private double latitude;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }
        }
    }
}
