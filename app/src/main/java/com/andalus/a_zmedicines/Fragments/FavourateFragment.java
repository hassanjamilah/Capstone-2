package com.andalus.a_zmedicines.Fragments;



import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.andalus.a_zmedicines.R;

import java.util.List;

import com.andalus.a_zmedicines.Database.DrugsDatabase;
import com.andalus.a_zmedicines.MyClasses.Drug;

import com.andalus.a_zmedicines.Utils.DrugsViewModel;
import com.andalus.a_zmedicines.adapters.DrugListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavourateFragment extends Fragment {

    Vibrator vib  ;

    DrugsViewModel model ;

    private DrugsDatabase mDatabase ;
    private DrugListAdapter mAdapter ;
    private RecyclerView mRecyclerView ;
    public FavourateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourate, container, false) ;
        mDatabase = DrugsDatabase.getDatabaseInstance(getContext()) ;
        mRecyclerView = v.findViewById(R.id.favFragment_recycerView) ;

        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);


        mAdapter = new DrugListAdapter(new DrugListAdapter.drugOnClickHandler() {
            @Override
            public void onClick(Drug drug) {
                    vib.vibrate(50);
                    showDrugDetails(drug);
            }
        }) ;
        final LinearLayoutManager manager  = new LinearLayoutManager(getContext()) ;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        loadData() ;

        return v;
    }

    private void loadData() {

        model = ViewModelProviders.of(getActivity()).get(DrugsViewModel.class);

                model.getFavDrugs() ;






        model.getDrugs().observe(getActivity(), new Observer<List<Drug>>() {
            @Override
            public void onChanged(List<Drug> drugs) {
                mAdapter.setData(drugs);
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    private void showDrugDetails(Drug drug) {
        //Intent intent =new Intent(getContext() , DrugDetailsActivity.class) ;
       // intent.putExtra(DrugDetailsActivity.EXTRA_DRUG , drug) ;
       // startActivity(intent);

            Bundle bundle  = new Bundle() ;
            bundle.putParcelable(DrugDetailsFragment.EXTR_DRUGDETAILS , drug);
            DrugDetailsFragment.startDetailsFragment(bundle , getActivity().getSupportFragmentManager());

    }

}
