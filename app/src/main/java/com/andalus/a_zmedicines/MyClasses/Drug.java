package com.andalus.a_zmedicines.MyClasses;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.andalus.a_zmedicines.Screens.MainActivity;
import com.andalus.a_zmedicines.Utils.AppExecutors;


@Entity(tableName = "alldrugs")
public class Drug implements Parcelable {
    private static final String BASE_URL = "https://azmedicine.000webhostapp.com/images/";
    @PrimaryKey
    private int id;
    private int version;
    private String drug_name;
    private String image_url;
    private String desc;
    private String important_info;
    private String before_taking;
    private String overdose;
    private String side_effects;
    private String dose;

    private ArrayList<String> interactions1;


    private ArrayList<String> interactions2;

    @Ignore
    private ArrayList<Composition> composition;

    private boolean isFav;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImportant_info() {
        return important_info;
    }

    public void setImportant_info(String important_info) {
        this.important_info = important_info;
    }

    public String getBefore_taking() {
        return before_taking;
    }

    public void setBefore_taking(String before_taking) {
        this.before_taking = before_taking;
    }

    public String getOverdose() {
        return overdose;
    }

    public void setOverdose(String overdose) {
        this.overdose = overdose;
    }

    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public ArrayList<String> getInteractions1() {
        return interactions1;
    }

    public void setInteractions1(ArrayList<String> interactions1) {
        this.interactions1 = interactions1;
    }

    public ArrayList<String> getInteractions2() {
        return interactions2;
    }

    public void setInteractions2(ArrayList<String> interactions2) {
        this.interactions2 = interactions2;
    }

    public ArrayList<Composition> getComposition() {
        return composition;
    }

    public void setComposition(ArrayList<Composition> composition) {
        this.composition = composition;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    @Override
    public String toString() {
        String s = "Drug{" +
                "id=" + id +
                ", version=" + version +
                ", drug_name='" + drug_name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", desc='" + desc + '\'' +
                ", important_info='" + important_info + '\'' +
                ", before_taking='" + before_taking + '\'' +
                ", overdose='" + overdose + '\'' +
                ", side_effects='" + side_effects + '\'' +
                ", dose='" + dose + '\'' +
                ", interactions1=" + interactions1 +
                ", interactions2=" + interactions2 +
                ", composition=" + composition +
                ", isFav=" + isFav +
                '}';
        Log.i("hassan", s);
        return s;
    }

    public static ArrayList<Drug> parseFromJsons(String json, Context context) {

        ArrayList<Drug> drugs = new ArrayList<>();


        try {
            JSONArray array = new JSONArray(json);
            Log.i("hassan", "The array size is : " + array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);


                Drug drug = new Drug();
                drug.setId(obj.getInt("id"));
                drug.setVersion(obj.getInt("version"));
                drug.setDrug_name(obj.getString("drug_name"));
                drug.setImage_url(obj.getString("image_url"));
                drug.setDesc(obj.getString("desc"));
                drug.setImportant_info(obj.getString("important_info"));
                drug.setBefore_taking(obj.getString("before_taking"));
                drug.setOverdose(obj.getString("overdose"));
                drug.setSide_effects(obj.getString("side_effects"));
                drug.setDose(obj.getString("dose"));
                drug.setInteractions1(new ArrayList<String>());
                drug.setInteractions2(new ArrayList<String>());

                JSONArray inter1 = obj.getJSONArray("interactions1");
                if (inter1 != null) {
                    Log.i("hassan", "The inter1 array size is" + inter1.length());
                    for (int i1 = 0; i1 < inter1.length(); i1++) {
                        Log.i("hassan", "Adding the item : " + inter1.get(i1).toString());
                        drug.getInteractions1().add(inter1.get(i1).toString());
                    }
                } else {
                    Log.i("hassan", "The inter1 array is null");
                }

                JSONArray inter2 = obj.getJSONArray("interactions2");
                if (inter2 != null) {
                    Log.i("hassan", "The inter2 array size is" + inter2.length());
                    for (int i1 = 0; i1 < inter2.length(); i1++) {
                        Log.i("hassan", "Adding the item for inter2 : " + inter2.get(i1).toString());
                        drug.getInteractions2().add(inter2.get(i1).toString());
                    }
                } else {
                    Log.i("hassan", "The inter2 array is null");
                }

                JSONArray medcomps = obj.getJSONArray("composition");
                drug.setComposition(Composition.getCompsFromJson(medcomps, drug.getId()));


                // drug.setComposition();
                drugs.add(drug);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("hassan", "Error loading json array");
        }

        return drugs;
    }


    public String getFullImageUrl() {
        String x = BASE_URL + getId() + ".jpg";
        return x;
    }


    /**
     * ToDo:
     */
    public static List<Drug> handleVersioning(final List<Drug> serverDrugs, final LifecycleOwner owner) {


        LiveData<List<Drug>> databaseDrugs = MainActivity.mDatabase.drugsDao().getAllDrugs();
        databaseDrugs.observe(owner, new Observer<List<Drug>>() {
            @Override
            public void onChanged(List<Drug> databaseDrugs) {
                boolean founded = false;
                for (Drug sDrug : serverDrugs) {
                    //  Log.i("hassan" , "The handleVersioning server drug is : " + sDrug.toString()) ;
                    // Log.i("hassan" , "=========================================") ;
                    for (Drug dDrug : databaseDrugs) {
                        // Log.i("hassan" , "The handleVersioning database drug drug is : " + dDrug.toString()) ;

                        if (dDrug.getId() == sDrug.getId()) {
                            //       Log.i("hassan" , "The handleVersioning founded  drug drug is : " + sDrug.toString()) ;
                            founded = true;
                            if (dDrug.getVersion() < sDrug.getVersion()) {
                                //       Log.i("hassan" , "The handleVersioning founded  different version  : " + sDrug.toString()) ;
                                if (dDrug.isFav) {
                                    //         Log.i("hassan" , "The handleVersioning the database drug is fav") ;
                                    sDrug.setFav(true);
                                }
                                final Drug dd = sDrug;
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainActivity.mDatabase.drugsDao().updateDrug(dd);
                                        MainActivity.mDatabase.drugsDao().deleteCompsForDrugID(dd.getId());
                                        ArrayList<Composition> comps = dd.getComposition();
                                        for (Composition comp : comps) {
                                            comp.setDrugid(dd.getId());
                                            MainActivity.mDatabase.drugsDao().insertComposition(comp);
                                        }
                                    }
                                });


                            }
                        }
                    }
                    if (!founded) {
                        //Log.i("hassan" , "The handleVersioning the server drug not found and will be inserted") ;
                        final Drug ddd = sDrug;
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.mDatabase.drugsDao().insertDrug(ddd);
                                MainActivity.mDatabase.drugsDao().deleteCompsForDrugID(ddd.getId());
                                ArrayList<Composition> comps = ddd.getComposition();
                                for (Composition comp : comps) {
                                    comp.setDrugid(ddd.getId());
                                    Log.i("hassan", "will insert comp " + comp.toString());
                                    MainActivity.mDatabase.drugsDao().insertComposition(comp);
                                }
                            }
                        });

                    }
                    founded = false;
                }
            }
        });


        /**
         * we return the server drugs for two reasons :
         * 1. not to query database again
         * 2. this list is now update for the favourates
         */
        return serverDrugs;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.version);
        dest.writeString(this.drug_name);
        dest.writeString(this.image_url);
        dest.writeString(this.desc);
        dest.writeString(this.important_info);
        dest.writeString(this.before_taking);
        dest.writeString(this.overdose);
        dest.writeString(this.side_effects);
        dest.writeString(this.dose);
        dest.writeStringList(this.interactions1);
        dest.writeStringList(this.interactions2);
        dest.writeTypedList(this.composition);
        dest.writeByte(this.isFav ? (byte) 1 : (byte) 0);
    }

    public Drug() {
    }

    protected Drug(Parcel in) {
        this.id = in.readInt();
        this.version = in.readInt();
        this.drug_name = in.readString();
        this.image_url = in.readString();
        this.desc = in.readString();
        this.important_info = in.readString();
        this.before_taking = in.readString();
        this.overdose = in.readString();
        this.side_effects = in.readString();
        this.dose = in.readString();
        this.interactions1 = in.createStringArrayList();
        this.interactions2 = in.createStringArrayList();
        this.composition = in.createTypedArrayList(Composition.CREATOR);
        this.isFav = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Drug> CREATOR = new Parcelable.Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel source) {
            return new Drug(source);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };
}
