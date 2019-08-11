package com.andalus.a_zmedicines.MyClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "comps")
public class Composition implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int glogalid;

    private int drugid;
    private int comp_id;
    private String comp_name;
    private String comp_conc;


    public int getGlogalid() {
        return glogalid;
    }

    public void setGlogalid(int glogalid) {
        this.glogalid = glogalid;
    }

    public int getDrugid() {
        return drugid;
    }

    public void setDrugid(int drugid) {
        this.drugid = drugid;
    }

    public int getComp_id() {
        return comp_id;
    }

    public void setComp_id(int comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getComp_conc() {
        return comp_conc;
    }

    public void setComp_conc(String comp_conc) {
        this.comp_conc = comp_conc;
    }

    @Override
    public String toString() {
        String s = "Composition{" +
                "glogalid=" + glogalid +
                ", drugid=" + drugid +
                ", comp_id=" + comp_id +
                ", comp_name='" + comp_name + '\'' +
                ", comp_conc='" + comp_conc + '\'' +
                '}';
        Log.i("hassan", s);
        return s;
    }

    public static ArrayList<Composition> getCompsFromJson(JSONArray array, int id) {
        ArrayList<Composition> comps = new ArrayList<>();


        try {


            if (array != null) {
                Log.i("hassan", "The comp array size is : " + array.length());
                for (int i = 0; i < array.length(); i++) {
                    Composition comp = new Composition();
                    JSONObject obj = array.getJSONObject(i);
                    comp.setComp_conc(obj.getString("comp_conc"));
                    comp.setComp_id(obj.getInt("comp_id"));
                    comp.setComp_name(obj.getString("comp_name"));
                    comp.setDrugid(id);
                    comps.add(comp);
                    comp.toString();
                }
            } else {
                Log.i("hassan", "The comp array is null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return comps;


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.glogalid);
        dest.writeInt(this.drugid);
        dest.writeInt(this.comp_id);
        dest.writeString(this.comp_name);
        dest.writeString(this.comp_conc);
    }

    public Composition() {
    }

    protected Composition(Parcel in) {
        this.glogalid = in.readInt();
        this.drugid = in.readInt();
        this.comp_id = in.readInt();
        this.comp_name = in.readString();
        this.comp_conc = in.readString();
    }

    public static final Parcelable.Creator<Composition> CREATOR = new Parcelable.Creator<Composition>() {
        @Override
        public Composition createFromParcel(Parcel source) {
            return new Composition(source);
        }

        @Override
        public Composition[] newArray(int size) {
            return new Composition[size];
        }
    };
}