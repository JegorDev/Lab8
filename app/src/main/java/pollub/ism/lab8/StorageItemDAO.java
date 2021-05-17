package pollub.ism.lab8;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StorageItemDAO {

    @Insert
    public void insert(StorageItem item);

    @Update
    void update(StorageItem item);

    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :selectedVegetableName")
    int findQuantityByName(String selectedVegetableName);

    @Query("UPDATE Warzywniak SET QUANTITY = :selectedVegetableNewAmount WHERE NAME = :selectedVegetableName")
    void updateQuantityByName(String selectedVegetableName, int selectedVegetableNewAmount);

    @Query("SELECT COUNT(*) FROM Warzywniak")
    int getDistinctVegetablesAmount();
}
