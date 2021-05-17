package pollub.ism.lab8;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Warzywniak")
public class StorageItem {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public int QUANTITY;
}
