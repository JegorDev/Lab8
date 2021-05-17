package pollub.ism.lab8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StorageUpdateDAO {

    @Insert
    public void insert(StorageUpdate storageUpdate);

    @Update
    void update(StorageUpdate storageUpdate);

    @Query("SELECT * FROM StorageUpdate WHERE name=:productName")
    List<StorageUpdate> findStorageUpdateByProduct(String productName);


}
