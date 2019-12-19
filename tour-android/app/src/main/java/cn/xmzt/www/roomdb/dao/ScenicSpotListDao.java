package cn.xmzt.www.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import cn.xmzt.www.roomdb.beans.ScenicSpotList;

import java.util.List;

@Dao
public interface ScenicSpotListDao {
    /**
     * 查询所有 根据距离 由近及远
     * @return
     */
    @Query("SELECT * FROM ScenicSpotList order by distance asc")
    List<ScenicSpotList> getDataAll();

    /**
     * 向数据库添加数据 批量
     *
     * @param scenicSpotListList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScenicSpotList> scenicSpotListList);

    /**
     * 项数据库添加数据
     * @param scenicSpotListList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ScenicSpotList scenicSpotListList);

    /**
     * 修改数据
     * @param scenicSpotListList
     */
    @Update()
    int update(ScenicSpotList scenicSpotListList);

    /**
     * 删除数据
     * @param scenicSpotListList
     */
    @Delete()
    void delete(ScenicSpotList scenicSpotListList);
}