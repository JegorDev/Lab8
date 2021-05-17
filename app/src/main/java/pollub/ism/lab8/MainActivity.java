package pollub.ism.lab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pollub.ism.lab8.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    private String chosenVegetableName =  null;
    private Integer chosenVegetableAmount = null;

    public enum StorageAction {STORE, GIVEAWAY};

    private StorageManagement storageManagement;

    private void update(){
        chosenVegetableAmount = storageManagement.storageItemDAO().findQuantityByName(chosenVegetableName);
        binding.textUnit.setText("Stan magazynu dla " + chosenVegetableName + " wynosi: " + chosenVegetableAmount);

        StringBuilder changesHistory = new StringBuilder();

        for(StorageUpdate storageUpdate: storageManagement.StorageUpdateDAO().findStorageUpdateByProduct(chosenVegetableName)){
            changesHistory.append(storageUpdate.date+","+storageUpdate.oldAmount+"->"+storageUpdate.newAmount);
        }
        binding.productHistory.setText(changesHistory);

    }

    private void changeState(StorageAction action){

        Integer amountChange = null;
        Integer newAmount = null;

        try{
            amountChange = Integer.parseInt(binding.editAmount.getText().toString());
        }catch (NumberFormatException exception){
            Log.d(TAG,"Error parsing number");
            return;
        }
        finally {
            binding.editAmount.setText("");
        }

        switch (action){
            case STORE:
                newAmount = chosenVegetableAmount + amountChange;
                break;
            case GIVEAWAY:
                newAmount = chosenVegetableAmount - amountChange;
                break;
        }
        storageManagement.storageItemDAO().updateQuantityByName(chosenVegetableName,newAmount);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();

        StorageUpdate storageUpdate = new StorageUpdate(dateTime.format(timeFormatter),chosenVegetableName,(newAmount-amountChange),newAmount);
        storageManagement.StorageUpdateDAO().update(storageUpdate);


        update();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Products, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

        storageManagement = Room.databaseBuilder(getApplicationContext(), StorageManagement.class, StorageManagement.DB_NAME)
                .allowMainThreadQueries().build();

        if(storageManagement.storageItemDAO().getDistinctVegetablesAmount()== 0){
            String [] products = getResources().getStringArray(R.array.Products);
            for(String product:products){
                StorageItem storageItem = new StorageItem();
                storageItem.NAME = product;
                storageItem.QUANTITY = 0;
                storageManagement.storageItemDAO().insert(storageItem);
            }
        }
        binding.buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(StorageAction.STORE);
            }
        });

        binding.buttonGiveaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(StorageAction.GIVEAWAY);
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenVegetableName = adapter.getItem(position).toString();
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"No vegetable is chosen");
            }
        });
    }
}