package com.andalus.a_zmedicines.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.andalus.a_zmedicines.Fragments.InteractionsFragment;
import com.andalus.a_zmedicines.R;
import com.andalus.a_zmedicines.Screens.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MyDrugsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews rvs = new RemoteViews(context.getPackageName() , R.layout.my_drugs_widget_list) ;
        Intent intent = new Intent(context , MyDrugsWidgerRemoteViewsService.class)  ;



        rvs.setRemoteAdapter(R.id.my_drugs_widget_listview_listview  , intent);

        Intent intent1 = new Intent(context , MainActivity.class)  ;
        intent1.putExtra("HH" , "hassan") ;
        PendingIntent pi = PendingIntent.getActivity(context  , 15 , intent1  , PendingIntent.FLAG_UPDATE_CURRENT) ;
        rvs.setOnClickPendingIntent(R.id.widger_linearLayout , pi);

        appWidgetManager.updateAppWidget(appWidgetId, rvs);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

