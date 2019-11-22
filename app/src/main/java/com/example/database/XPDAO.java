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

    // Query to insert a new row to the 'XP_table':
    @Insert(onConflict = IGNORE)
    void insertXP(XP xp);                                                                           // Has to be an Object of XP, because you can't use primitive datatypes in Room's CRUD annotations (@Insert, @Delete, @Update)

    // Query to get the current XP value:
    @Query("SELECT XP FROM XP_Table WHERE ID = 0")
    int getXP();

    // Query to set the current XP value:
    @Query ("UPDATE XP_Table SET XP = :xp WHERE ID = 0")
    void setXP(int xp);
}