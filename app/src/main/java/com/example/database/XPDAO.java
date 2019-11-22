package com.example.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.mainClasses.XP;
import static androidx.room.OnConflictStrategy.IGNORE;

/*
 *  XPDAO.java
 *  Room (DAO - Data Access Object)
 *  Here you can define all the Database queries in form of Java methods.
 */

@Dao
public interface XPDAO {

    // Set the default entry (XP = 0) when the database is created.
    @Insert(onConflict = IGNORE)                                                                    // When there is already an entry (the PrimaryKey ID = 0), ignore it:
    void insertXP(XP xp);                                                                           // Has to be an Object of XP, because you can't use primitive datatypes (as int) in Room's CRUD annotations (@Insert, @Delete, @Update)

    // Query to get the current XP value:
    @Query("SELECT XP FROM XP_Table WHERE ID = 0")
    int getXP();

    // Query to set the current XP value:
    @Query ("UPDATE XP_Table SET XP = :xp WHERE ID = 0")
    void setXP(int xp);
}