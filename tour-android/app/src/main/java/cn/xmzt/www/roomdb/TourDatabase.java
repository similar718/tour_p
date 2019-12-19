package cn.xmzt.www.roomdb;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.VisibleForTesting;

import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.roomdb.beans.KeyWords;
import cn.xmzt.www.roomdb.beans.PageData;
import cn.xmzt.www.roomdb.beans.ScenicContents;
import cn.xmzt.www.roomdb.beans.ScenicSpotList;
import cn.xmzt.www.roomdb.dao.GroupUserInfoDao;
import cn.xmzt.www.roomdb.dao.KeyWordsDao;
import cn.xmzt.www.roomdb.dao.PageDataDao;
import cn.xmzt.www.roomdb.dao.ScenicContentsDao;
import cn.xmzt.www.roomdb.dao.ScenicSpotListDao;

@Database(entities = {KeyWords.class, ScenicSpotList.class, ScenicContents.class, PageData.class, GroupUserInfo.class}, version = 6, exportSchema = false)
public abstract class TourDatabase extends RoomDatabase {

    private static volatile TourDatabase INSTANCE;

    public static TourDatabase getDefault(Context context) {
        return buildDatabase(context);
    }

    private static TourDatabase buildDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(), TourDatabase.class, "tour")
                    .allowMainThreadQueries()//加allowMainThreadQueries()允许在主线程查询数据库不加反之
                    .fallbackToDestructiveMigration() // 强制升级
                    .build();
        }
        return INSTANCE;
    }
    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                TourDatabase.class).build();
    }
    public abstract KeyWordsDao getKeyWordsDao();

    public abstract GroupUserInfoDao getGroupUserInfoDao();

    public abstract ScenicSpotListDao getScenicSpotListDao();

    public abstract ScenicContentsDao getScenicContentDao();

    public abstract PageDataDao getPageDataDao();


    /**
     * 数据库迁移（或者升级）
     */
    /*
    Room.databaseBuilder(getApplicationContext(), MyDb.class, "database-name")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
                    + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Book "
                    + " ADD COLUMN pub_year INTEGER");
        }
    };
    */
}