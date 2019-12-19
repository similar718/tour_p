package cn.xmzt.www.roomdb.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(primaryKeys = {"id","funTypes"}) // 组合主键和主键两个只能存在一个
public class ScenicContents {
    @NonNull
    @ColumnInfo(name = "id")
    public long id; // 景区编号
    @NonNull
    @ColumnInfo(name = "funTypes")
    public int funTypes;//功能类型 TODO 有新类型记得注释 1001 景区内容 1002 景区音频文件
    @ColumnInfo(name = "content")
    public String content;// 景区里面所有内容的json数据
}