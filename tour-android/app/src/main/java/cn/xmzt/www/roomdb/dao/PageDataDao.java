package cn.xmzt.www.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import cn.xmzt.www.roomdb.beans.PageData;

import java.util.List;

@Dao
public interface PageDataDao {
    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM PageData")
    List<PageData> getDataAll();

    /**
     * 根据指定查询条件 根据id和类型查询数据
     *
     * @return
     */
    @Query("SELECT * FROM PageData WHERE funTypes = :funTypes")
    PageData getPageData(int funTypes);

    /**
     * 项数据库添加数据
     *
     * @param PageDataList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PageData> PageDataList);

    /**
     * 项数据库添加数据
     * @param PageData
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PageData PageData);

    /**
     * 修改数据
     * @param PageData
     */
    @Update()
    int update(PageData PageData);

    /**
     * 删除数据
     * @param PageData
     */
    @Delete()
    void delete(PageData PageData);
}