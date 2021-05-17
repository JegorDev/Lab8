package pollub.ism.lab8;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "StorageUpdate")
public class StorageUpdate {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String date;
    public String name;
    public int oldAmount;
    public int newAmount;

    public StorageUpdate(String date, String name, int oldAmount, int newAmount) {
        this.date = date;
        this.name = name;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
    }
}
