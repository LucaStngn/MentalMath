package com.example.mainClasses;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "XP_Table")
public class XP {

    @PrimaryKey
    public int ID;                                                                                  // Is only needed to avoid a new line to be inserted in the XP_Table, when the @Insert Query gets executed.
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