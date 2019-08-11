package com.andalus.a_zmedicines.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.andalus.a_zmedicines.R;
import java.util.List;
import javax.annotation.Nullable;
import com.andalus.a_zmedicines.MyClasses.Composition;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.Screens.MainActivity;
import com.andalus.a_zmedicines.Utils.DrugsViewModel;
import com.andalus.a_zmedicines.Utils.NetworkUtils;
import com.andalus.a_zmedicines.adapters.DrugListAdapter;

public class DrugFragment extends Fragment {


    public static final String EXTRA_SEAARCH_DRUG_FROM_WIDGET ="getsearchfromwidget" ;
    private Vibrator vib ;
    private DrugsViewModel model ;
    private SearchView mSearchView ;

    /**
     * This is the adapate of the recycler view
     */
    private   DrugListAdapter adapter ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drug, container, false);
        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
       initUi(v) ;
        if (getArguments() !=null ){
            mSearchView.setIconified(false);
            mSearchView.requestFocus() ;
            model =  ViewModelProviders.of(getActivity()).get(DrugsViewModel.class) ;
        }else {
            LoadData() ;
        }

        return v;
    }


    public void LoadData(){
        model =  ViewModelProviders.of(getActivity()).get(DrugsViewModel.class) ;
        model.getAllDrugs() ;
        model.getDrugs().observe(this, new Observer<List<Drug>>() {
            @Override
            public void onChanged(List<Drug> drugs) {

                adapter.setData(drugs);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initUi(View v ) {

        /**
         * Here we initialize the SearchView
         */
        mSearchView  = v.findViewById(R.id.drugFragment_searchView) ;
        TextView tv = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) ;
        tv.setTextColor(getResources().getColor(R.color.bluegray_800));

        ImageView iv = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn) ;
        iv.setImageResource(R.drawable.ic_close_black_24dp);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Log.i("hassan" , "The search string is : " + query) ;

                     model.getDrugsBySearch(query) ;
                     updateRecycleViewData();
             return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //============================================


        /**
         * Here we prepare our recycler view
         */
        LinearLayoutManager manager =new LinearLayoutManager(getContext()) ;
        adapter = new DrugListAdapter(new DrugListAdapter.drugOnClickHandler() {
            @Override
            public void onClick(Drug drug) {
                vib.vibrate(30);
                Log.i("hassan"  ,"Show the drug fragment") ;
                showDrugDetails(drug) ;
            }
        }) ;

       // adapter.setData(mDrugs);
        RecyclerView rv = v.findViewById(R.id.mainActivity_recycerView ) ;
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);


        LiveData<List<Composition>> comps = MainActivity.mDatabase.drugsDao().selectAllComps();
        comps.observe(getActivity(), new Observer<List<Composition>>() {
            @Override
            public void onChanged(List<Composition> compositions) {
                Log.i("hassan" , "num of comps is : " + compositions.size()) ;
                for (Composition com:compositions){
                    Log.i("hassan" , "comp is :  " + com.toString()) ;
                }

            }
        });


    }

    private void showDrugDetails(Drug drug) {
         Bundle bundle  = new Bundle( );
       bundle.putParcelable(DrugDetailsFragment.EXTR_DRUGDETAILS  , drug ) ;
        DrugDetailsFragment.startDetailsFragment(bundle , getActivity().getSupportFragmentManager());
    }


    /**
     * This method is used to update the recyclerView when data is changed such as after the search
     */
    public void updateRecycleViewData(){
        model.getDrugs().observe(getActivity(), new Observer<List<Drug>>() {
            @Override
            public void onChanged(List<Drug> drugs) {
                Log.i("hassan" ,"The drug search in fragment result is : "  + drugs.size()) ;
                adapter.setData(drugs);
                if (getActivity() !=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });




    }



}
