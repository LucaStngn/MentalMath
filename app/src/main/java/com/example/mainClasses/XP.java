package com.example.mainClasses;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 *  XP.java
 *  This class defines how one 'XP Object' looks like.
 *  It is used to store the user's current experience points persistently.
 */


@Entity(tableName = "XP_Table")
public class XP {

    @PrimaryKey
    public int ID;                                                                                  // Is only needed to avoid a new line to be inserted in the XP_Table, when the @Insert Query gets executed. (As you can't use the XP column as primary key)
    public int XP;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }
}