package cn.xmzt.www.roomdb.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 页面数据保存实体
 * @author Averysk
 */
@Entity
public class PageData {
    @PrimaryKey
    @ColumnInfo(name = "funTypes")
    public int funTypes;//功能类型 TODO 有新类型记得注释 9001 首页内容
    @ColumnInfo(name = "content")
    public String content;// 所有内容的json数据
}