package com.andalus.a_zmedicines.Screens;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.andalus.a_zmedicines.Fragments.FavourateFragment;
import com.andalus.a_zmedicines.Fragments.InteractionsFragment;
import com.andalus.a_zmedicines.R;

import java.util.List;

import com.andalus.a_zmedicines.Database.DrugsDatabase;
import com.andalus.a_zmedicines.Fragments.DrugFragment;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.Utils.NetworkUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {


    private static final String SAVEINSTANCESTATE_DONT_LOAD = "saveinstancestate";
    private final int LoaderManagerkey = 526;


    public static DrugsDatabase mDatabase;

    private FirebaseAnalytics mFirebaseAnalytics;


    private BottomNavigationView mNavView;

    private CountingIdlingResource countingIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        DrugFragment fra = new DrugFragment();
        countingIdlingResource = new CountingIdlingResource("hassan001");
        if (countingIdlingResource != null) {
            countingIdlingResource.increment();
        }

        if (getIntent().getExtras() != null) {
            String x = getIntent().getExtras().getString("HH");
            Log.i("hassan", "The search from widget is : " + x);

            fra.setArguments(new Bundle());

            handleWidgetClick(x);
        } else {
            if (savedInstanceState == null) {

                startLoading();
            }
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_Container, fra)
                .commit();


        mDatabase = DrugsDatabase.getDatabaseInstance(this);

        mNavView = findViewById(R.id.bottom_navigations);
        mNavView.setOnNavigationItemSelectedListener(mNaveListener);

        initAds();
        loadAds();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVEINSTANCESTATE_DONT_LOAD, "dont load");
    }

    @Override
    protected void onResume() {
        super.onResume();

        logFireBase("Starting drugs activity");
    }

    /**
     * This is used for google analaytice to send log to firebase
     * @param s
     */
    public void logFireBase(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, s);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    /**
     * This method is usded in the tests to get the counting idle
     * to start test when recyclerview elements loading finished
     * @return
     */
    public CountingIdlingResource getCountingIdlingResource() {
        return countingIdlingResource;
    }


    /**
     * This is used to initialize the admob
     */
    private void initAds() {
        MobileAds.initialize(this);

    }

    /**
     * This method is used to start loading the ads
     */
    private void loadAds() {
        AdView ad = findViewById(R.id.mainActivity_AdView);
        AdRequest request = new AdRequest.Builder().build();
        ad.loadAd(request);
    }

    /**
     * This method is used to load data from ther server by starting the
     * Loader
     */
    public void startLoading() {
        Loader<String> loader = getSupportLoaderManager().getLoader(LoaderManagerkey);
        if (loader == null) {
            getSupportLoaderManager().initLoader(LoaderManagerkey, new Bundle(), this);
        } else {
            getSupportLoaderManager().restartLoader(LoaderManagerkey, new Bundle(), this);

        }
    }


    /**
     * This is to handle the sidget click to start searching
     * @param searchWhat
     */
    public void handleWidgetClick(String searchWhat) {
        DrugFragment fra = new DrugFragment() ;
        Bundle bundle = new Bundle( ) ;
        bundle.putString(DrugFragment.EXTRA_SEAARCH_DRUG_FROM_WIDGET , searchWhat);
        fra.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_Container , fra)
                .commit() ;
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("hassan", "The loader starts");
        return new MainActivity.myLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Log.i("hassan", "The drugs after loading data is : " + data);


        List<Drug> drugs = Drug.parseFromJsons(data, this);
        Drug.handleVersioning(drugs, this);

        if (countingIdlingResource != null)
            try{
                countingIdlingResource.decrement();
            }catch (IllegalStateException e){
                Log.i("hassan" , "The counting idling resource state is incorrect") ;
            }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


    public static class myLoader extends AsyncTaskLoader<String> {
        Context context;
        String myData;

        public myLoader(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            if (myData != null) {
                deliverResult(myData);
            } else {
                forceLoad();
            }
        }

        @Nullable
        @Override
        public String loadInBackground() {
            Log.i("hassan", "The loader start load in background");
            NetworkUtils utils = new NetworkUtils();
            String f = utils.getHttpResponse1(null);

            return f;

        }

        @Override
        public void deliverResult(@Nullable String data) {
            myData = data;
            super.deliverResult(data);
        }
    }


    /**
     * Here we handle the navigation view clicks
     */
    public BottomNavigationView.OnNavigationItemSelectedListener mNaveListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_screen:
                    Log.i("hassan", "The main drugs nav clicked");
                    StartFragment(new DrugFragment(), null);
                    logFireBase("Start the main drgus fragment.");
                    return true;
                case R.id.nav_interaactions:
                    StartFragment(new InteractionsFragment(), null);
                    Log.i("hassan", "The main interaction nav clicked");
                    logFireBase("Start the interactions fragment.");
                    return true;
                case R.id.nav_favourates:
                    StartFragment(new FavourateFragment(), null);
                    Log.i("hassan", "The main favs nav clicked");
                    logFireBase("Start the fav fragment.");
                    return true;
            }

            return false;
        }
    };

    public void StartFragment(Fragment fragment, Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_Container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();


    }

}