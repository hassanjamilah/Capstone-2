package com.andalus.a_zmedicines.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.andalus.a_zmedicines.Database.DrugsDatabase;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.R;

import java.util.List;

public class MyDrugsWidgerRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyDrugsWigetRemoteViews(getApplicationContext());
    }


    class MyDrugsWigetRemoteViews implements  RemoteViewsFactory{

        private Context mContext ;
        private DrugsDatabase mDatabase ;
        private List<Drug> mDrugs ;

        public MyDrugsWigetRemoteViews(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Log.i("hassan2" ,"Widget notify datachanged") ;
            mDatabase = DrugsDatabase.getDatabaseInstance(mContext) ;
            mDrugs= mDatabase.drugsDao().getFavDrugsForWidget() ;

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mDrugs !=null && mDrugs.size()>0){
                return mDrugs.size();
            }else {
                return 0 ;
            }

        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName() , R.layout.my_drugs_widget) ;
            rv.setTextViewText(R.id.appwidget_text ,  mDrugs.get(position).getDrug_name());



            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
