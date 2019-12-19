package cn.xmzt.www.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import cn.xmzt.www.roomdb.beans.KeyWords;

import java.util.List;

@Dao
public interface KeyWordsDao {
    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM KeyWords")
    List<KeyWords> getDataAll();

    /**
     * 查询当前类型的前10条数据
     *
     * @return
     */
    @Query("SELECT * FROM KeyWords WHERE funTypes = :funTypes ORDER BY time desc LIMIT :num")
    List<KeyWords> getDataTopNum(int funTypes, int num);

    /**
     * 删除当前类型里面所有的数据信息
     * @param funTypes
     */
    @Query("DELETE FROM keywords WHERE funTypes = :funTypes")
    int deleteFunTypes(int funTypes);
    /**
     * 根据指定查询条件
     *
     * @return
     */
    @Query("SELECT * FROM KeyWords WHERE name = :name")
    KeyWords getKeyWords(String name);

    /**
     * 根据指定查询条件 需要添加当前类型当前关键字的查询 不同类型可能有相同的关键字
     *
     * @return
     */
    @Query("SELECT * FROM KeyWords WHERE name = :name and funTypes = :funTypes")
    KeyWords getKeyWords(String name,int funTypes);

    /**
     * 项数据库添加数据
     *
     * @param keyWordsList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<KeyWords> keyWordsList);

    /**
     * 项数据库添加数据
     * @param keyWords
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(KeyWords keyWords);

    /**
     * 修改数据
     * @param keyWords
     */
    @Update()
    int update(KeyWords keyWords);

    /**
     * 删除数据
     * @param keyWords
     */
    @Delete()
    void delete(KeyWords keyWords);
}