package cn.xmzt.www.roomdb.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(primaryKeys = {"name","funTypes"}) // 组合主键和主键两个只能存在一个
public class KeyWords {
    @NonNull
    @ColumnInfo(name = "name")
    public String name; //关键字名称
    @NonNull
    @ColumnInfo(name = "funTypes")
    public int funTypes;//功能类型 TODO 有新类型记得注释 1001 景区导览界面的搜索类型 1010 景区导览界面自动播放信息保存
    @ColumnInfo(name = "time")
    public long time;// 保存时间
}