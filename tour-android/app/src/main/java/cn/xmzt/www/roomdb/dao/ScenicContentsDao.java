package cn.xmzt.www.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import cn.xmzt.www.roomdb.beans.ScenicContents;

import java.util.List;

@Dao
public interface ScenicContentsDao {
    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM ScenicContents")
    List<ScenicContents> getDataAll();

    /**
     * 根据指定查询条件 根据id和类型查询数据
     *
     * @return
     */
    @Query("SELECT * FROM ScenicContents WHERE id = :id and funTypes = :funTypes")
    ScenicContents getScenicContents(long id, int funTypes);

    /**
     * 根据指定查询条件 根据类型查询数据
     *
     * @return
     */
    @Query("SELECT * FROM ScenicContents WHERE funTypes = :funTypes")
    List<ScenicContents> getScenicContents(int funTypes);

    /**
     * 项数据库添加数据
     *
     * @param ScenicContentsList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScenicContents> ScenicContentsList);

    /**
     * 项数据库添加数据
     * @param ScenicContents
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ScenicContents ScenicContents);

    /**
     * 修改数据
     * @param ScenicContents
     */
    @Update()
    int update(ScenicContents ScenicContents);

    /**
     * 删除数据
     * @param ScenicContents
     */
    @Delete()
    void delete(ScenicContents ScenicContents);
}