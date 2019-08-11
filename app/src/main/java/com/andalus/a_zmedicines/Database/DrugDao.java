package com.andalus.a_zmedicines.Database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.andalus.a_zmedicines.MyClasses.Composition;
import com.andalus.a_zmedicines.MyClasses.Drug;

@Dao
public interface DrugDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDrug(Drug drug);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDrug(Drug drug);

    @Delete
    void delete(Drug drug);


    @Query("select * from alldrugs")
    LiveData<List<Drug>> getAllDrugs();

    @Query("select * from alldrugs where id = :id1")
    LiveData<List<Drug>> getDrugById(int id1);

    /**
     * Search in all the fields of the database
     * @param searchStr
     * @return
     */
    @Query("select * from alldrugs where drug_name like :searchStr " +
            "or `desc` like :searchStr " +
            "or `important_info` like :searchStr " +
            "or `before_taking` like :searchStr " +
            "or `overdose` like :searchStr " +
            "or `side_effects` like :searchStr " +
            "or `dose` like :searchStr " +
            "or `interactions1` like :searchStr " +
            "or `interactions2` like :searchStr   " +
            " group by id "


    )
    LiveData<List<Drug>> getDrugsBySearch(String searchStr);

    @Query("select * from alldrugs where drug_name like :searchStr " +

            " group by id "


    )
    LiveData<List<Drug>> getDrugsByName(String searchStr);


    @Query("select isFav from alldrugs where id = :id")
    boolean isDrugFav(int id);

    @Query("select * from alldrugs where interactions1 like :s  and id = :id")
    LiveData<List<Drug>> getInteraction1(String s, int id);

    @Query("select * from alldrugs where interactions2 like :s  and id = :id")
    LiveData<List<Drug>> getInteraction2(String s, int id);

    @Query("select * from alldrugs where isFav = 1")
    LiveData<List<Drug>> getFavDrugs();


    @Query("select * from alldrugs where isFav = 1")
    List<Drug> getFavDrugsForWidget();

    //====================================

    @Insert
    void insertComposition(Composition comp);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateComposition(Composition comp);

    @Delete
    void deleteComposition(Composition comp);

    @Query("select * from comps where drugid = :drugId")
    LiveData<List<Composition>> getDrugComps(int drugId);

    @Query("delete  from comps where drugid = :drugId")
    void deleteCompsForDrugID(int drugId);

    @Query("select * from comps")
    LiveData<List<Composition>> selectAllComps();
}
