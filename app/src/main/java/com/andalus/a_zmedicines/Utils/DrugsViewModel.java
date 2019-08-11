package com.andalus.a_zmedicines.Utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import com.andalus.a_zmedicines.Database.DrugsDatabase;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.Screens.MainActivity;

public class DrugsViewModel extends AndroidViewModel {

    private LiveData<List<Drug>> drugs;

    public DrugsViewModel(@NonNull Application application) {
        super(application);

    }


    public LiveData<List<Drug>> getAllDrugs() {
        DrugsDatabase database = DrugsDatabase.getDatabaseInstance(this.getApplication());
        Log.i("hassan", "Getting data from database in viewmodel");
        drugs = database.drugsDao().getAllDrugs();
        return drugs;
    }

    public LiveData<List<Drug>> getDrugsBySearch(String query) {
        drugs = MainActivity.mDatabase.drugsDao().getDrugsBySearch("%" + query + "%");
        return drugs;
    }

    public LiveData<List<Drug>> getFavDrugs() {
        DrugsDatabase database = DrugsDatabase.getDatabaseInstance(this.getApplication());
        Log.i("hassan", "Getting data from database in viewmodel");
        drugs = database.drugsDao().getFavDrugs();
        return drugs;
    }

    public LiveData<List<Drug>> getDrugs() {
        return drugs;
    }
}
