package cn.xmzt.www.roomdb.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class ScenicSpotList { // 缓存景区列表一页的数据
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "scenicName")
    public String scenicName;
    @ColumnInfo(name = "photoUrl")
    public String photoUrl;
    @ColumnInfo(name = "scenicAddress")
    public String scenicAddress;
    @ColumnInfo(name = "grade")
    public int grade;
    @ColumnInfo(name = "coordinate")
    public String coordinate;
    @ColumnInfo(name = "province")
    public String province;
    @ColumnInfo(name = "city")
    public String city;
    @ColumnInfo(name = "area")
    public String area;
    @ColumnInfo(name = "explainPointCount")
    public int explainPointCount;
    @ColumnInfo(name = "listenCount")
    public int listenCount;
    @ColumnInfo(name = "distance")
    public double distance;
}