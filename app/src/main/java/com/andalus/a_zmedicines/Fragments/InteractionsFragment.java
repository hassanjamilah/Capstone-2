package com.andalus.a_zmedicines.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.andalus.a_zmedicines.R;
import com.andalus.a_zmedicines.databinding.FragmentInteractionsBinding;
import com.squareup.picasso.Picasso;
import java.util.List;
import com.andalus.a_zmedicines.Database.DrugsDatabase;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.adapters.DrugListAdapter;



public class InteractionsFragment extends Fragment {


    private static final String SAVESTATE_FIRST_DRUG = "savestate_firstdrug";
    private static final String SAVESTATE_SECONDE_DRUG = "savestate_seconddrug";
    private static final String SAVESTATE_RECYCLER_VISIBLE ="savestate_isRecyclervisisble" ;
    private static final String TAG_GREEN = "green";
    private static final String TAGR_RED = "red";
    private static final String TAG_YELLOW = "yellow";
    private static final String SAVE_INTERACTIONS_DETAILS = "details_textview";
    private static final String SAVESTATE_IMAGEVIEW_STATE = "save_instance_imageview";
    FragmentInteractionsBinding binding  ;
    private DrugsDatabase mDatabase ;

    private DrugListAdapter adapter ;

    private int redID ;
    private int yellowID ;
    private int greenID ;

    Drug firstDrug ;
    Drug secondDrug ;


    private ImageView firstDrug_ImageView ;
    private ImageView secondDrug_ImageView ;

    private TextView firstDrug_TextView ;
    private TextView secondDrug_TextView ;

    private TextView firstDrug_desc_TextView ;
    private TextView secondDrug_desc_TextView ;

    private  Vibrator vib ;



    public static InteractionsFragment newInstance(String param1, String param2) {
        InteractionsFragment fragment = new InteractionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate( inflater  , R.layout.fragment_interactions, container, false) ;
       mDatabase = DrugsDatabase.getDatabaseInstance(getContext());





       adapter = new DrugListAdapter(new DrugListAdapter.drugOnClickHandler() {
           @Override
           public void onClick(Drug drug) {

           }
       }) ;



        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

       redID =R.drawable.red_circle ;
       yellowID = R.drawable.yellow_circle ;
       greenID = R.drawable.green_circle ;

        initUi() ;

        if (savedInstanceState !=null){
            firstDrug = savedInstanceState.getParcelable(SAVESTATE_FIRST_DRUG) ;
            putDataToView(firstDrug , 1);

            secondDrug = savedInstanceState.getParcelable(SAVESTATE_SECONDE_DRUG) ;
            putDataToView(secondDrug , 2);

        /*    boolean x = savedInstanceState.getBoolean(SAVESTATE_RECYCLER_VISIBLE) ;
            if (x ){
                binding.interactionsFragmentSearchRecyclerView.setVisibility(View.VISIBLE);
            }else {
                binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
            }*/

            binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
            String x = savedInstanceState.getString(SAVE_INTERACTIONS_DETAILS) ;
            if (!TextUtils.isEmpty(x)){
                binding.interactionsFragmentInterActionDetailsTextView.setText(x);

            }

            int resId = 0 ;
            String tag =""  ;
            x = savedInstanceState.getString(SAVESTATE_IMAGEVIEW_STATE) ;
            if (!TextUtils.isEmpty(x)){
                switch (x){
                    case TAG_GREEN:
                        resId = R.drawable.green_circle;
                        tag = TAG_GREEN ;
                        break;
                    case TAG_YELLOW:
                        tag = TAG_YELLOW ;
                        resId = R.drawable.yellow_circle;
                        break;
                    case TAGR_RED:
                        resId = R.drawable.red_circle;
                        tag = TAGR_RED ;
                        break;
                }
            }
            if (resId !=0){
                binding.interactionsFragmentDangerImageView.setImageResource(resId);
                binding.interactionsFragmentDangerImageView.setTag(tag);

            }

        }




        return binding.getRoot() ;


    }

    private void initUi() {

        View v1 = binding.firstDrug ;
        View v2 = binding.secondDrug ;
        binding.interactionsFragmentDangerImageView.setTag(TAG_GREEN);

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetails(firstDrug) ;
            }
        });


        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(30);
                viewDetails(secondDrug) ;

            }
        });

        binding.interactionsFragmentShareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND) ;

                sendIntent.putExtra(Intent.EXTRA_TEXT ,binding.interactionsFragmentInterActionDetailsTextView.getText()) ;
                sendIntent.setType("text/html") ;
                startActivity(sendIntent);
            }
        });

         firstDrug_ImageView  = v1.findViewById(R.id.drugListItem_DrugImage_ImageView);
       secondDrug_ImageView = v2.findViewById(R.id.drugListItem_DrugImage_ImageView) ;

         firstDrug_TextView=  v1.findViewById(R.id.drugListItem_DrugName_TextView);;
         secondDrug_TextView = v2.findViewById(R.id.drugListItem_DrugName_TextView);

         firstDrug_desc_TextView = v1.findViewById(R.id.drugListItem_Desc_TextView);;
         secondDrug_desc_TextView = v2.findViewById(R.id.drugListItem_Desc_TextView);





         binding.interactionsFragmentFirstDrugSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
             @Override
             public boolean onClose() {
                 binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
                 return true;
             }
         });

        binding.interactionsFragmentSecondDrugSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
                return true;
            }
        });


        binding.interactionsFragmentSecondDrugSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                  binding.interactionsFragmentSearchRecyclerView.setVisibility(View.VISIBLE);
                adapter.setListener(forSecondSearchViewHandler);
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
             //   binding.interactionsFragmentOthercontainer.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        binding.interactionsFragmentFirstDrugSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                binding.interactionsFragmentSearchRecyclerView.setVisibility(View.VISIBLE);
                adapter.setListener(forFirstSearchViewHandler);
                doSearch(s);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });




        binding.searchInteractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(30);
                String s = secondDrug_TextView.getText().toString() ;
                if (firstDrug == null || secondDrug == null) return ;
                LiveData<List<Drug>> inter1 = mDatabase.drugsDao().getInteraction1(s , firstDrug.getId()) ;
                binding.interactionsFragmentDangerImageView.setImageResource(greenID);
                binding.interactionsFragmentDangerImageView.setTag(TAG_GREEN);
                binding.interactionsFragmentInterActionDetailsTextView.setText("There is no interaction " +
                        " between " + firstDrug.getDrug_name() +
                        " and " + secondDrug.getDrug_name()


                );
                inter1.observe(getActivity(), new Observer<List<Drug>>() {
                    @Override
                    public void onChanged(List<Drug> drugs) {
                        if (drugs.size() >0){
                            binding.interactionsFragmentInterActionDetailsTextView.setText("The interaction " +
                                    " between " + firstDrug.getDrug_name() +
                                    " and " + secondDrug.getDrug_name() +
                                    " is Dangerous."

                                    );
                            binding.interactionsFragmentDangerImageView.setImageResource(redID);
                            binding.interactionsFragmentDangerImageView.setTag(TAGR_RED);


                        }
                    }
                });

                LiveData<List<Drug>> inter2 = mDatabase.drugsDao().getInteraction2(s  , firstDrug.getId()) ;
                inter2.observe(getActivity(), new Observer<List<Drug>>() {
                    @Override
                    public void onChanged(List<Drug> drugs) {
                        if (drugs.size()>0){
                            binding.interactionsFragmentInterActionDetailsTextView.setText("normal");
                            binding.interactionsFragmentDangerImageView.setImageResource(yellowID);
                            binding.interactionsFragmentDangerImageView.setTag(TAG_YELLOW);
                            binding.interactionsFragmentInterActionDetailsTextView.setText("The interaction " +
                                    " between " + firstDrug.getDrug_name() +
                                    " and " + secondDrug.getDrug_name() +
                                    " is Moderate."

                            );
                        }

                    }
                });

            }
        });


        android.support.v7.widget.SearchView sv = binding.getRoot().findViewById(R.id.interactionsFragment_FirstDrug_SearchView) ;
        TextView tv = sv.findViewById(android.support.v7.appcompat.R.id.search_src_text) ;
        tv.setTextColor(getResources().getColor(R.color.bluegray_800));

        ImageView iv = sv.findViewById(android.support.v7.appcompat.R.id.search_close_btn) ;
        iv.setImageResource(R.drawable.ic_close_black_24dp);


         sv = binding.getRoot().findViewById(R.id.interactionsFragment_SecondDrug_SearchView) ;
         tv = sv.findViewById(android.support.v7.appcompat.R.id.search_src_text) ;
        tv.setTextColor(getResources().getColor(R.color.bluegray_800));

         iv = sv.findViewById(android.support.v7.appcompat.R.id.search_close_btn) ;
        iv.setImageResource(R.drawable.ic_close_black_24dp);

    }

    private void viewDetails(Drug drug) {
        if (drug == null ) return ;


       // Intent intent =new Intent(getContext() , DrugDetailsActivity.class) ;
       // intent.putExtra(DrugDetailsActivity.EXTRA_DRUG , drug) ;
       // startActivity(intent);

        Bundle bundle  = new Bundle() ;
        bundle.putParcelable(DrugDetailsFragment.EXTR_DRUGDETAILS , drug);
        DrugDetailsFragment.startDetailsFragment(bundle , getActivity().getSupportFragmentManager());

    }


    public void doSearch(String query){
        LiveData<List<Drug>>  drugs = mDatabase.drugsDao().getDrugsByName("%" + query + "%") ;

        drugs.observe(getActivity(), new Observer<List<Drug>>() {
            @Override
            public void onChanged(final List<Drug> drugs) {

                Log.i("hassan" ,"the interactions drugs size is : " + drugs.size()) ;

                binding.interactionsFragmentSearchRecyclerView.setVisibility(View.VISIBLE);
                adapter.setData(drugs);
                adapter.notifyDataSetChanged();


            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(getContext()) ;
        binding.interactionsFragmentSearchRecyclerView.setLayoutManager(manager);
        binding.interactionsFragmentSearchRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVESTATE_FIRST_DRUG , firstDrug);
        outState.putParcelable(SAVESTATE_SECONDE_DRUG ,secondDrug);
        if (binding.interactionsFragmentSearchRecyclerView.getVisibility() == View.VISIBLE){
            outState.putBoolean(SAVESTATE_RECYCLER_VISIBLE ,true);
        }else {
            outState.putBoolean(SAVESTATE_RECYCLER_VISIBLE ,false);

        }
        if (!TextUtils.isEmpty(binding.interactionsFragmentInterActionDetailsTextView.getText().toString())){
            outState.putString(SAVE_INTERACTIONS_DETAILS , binding.interactionsFragmentInterActionDetailsTextView.getText().toString());
        }
        if (!TextUtils.isEmpty(binding.interactionsFragmentDangerImageView.getTag().toString())){
            outState.putString(SAVESTATE_IMAGEVIEW_STATE , binding.interactionsFragmentDangerImageView.getTag().toString());
        }







    }

    public void putDataToView(Drug drug , int num){
        if (drug == null) return ;
        if (num == 1){
            firstDrug_TextView.setText(drug.getDrug_name());
            firstDrug_desc_TextView.setText(drug.getDesc());
            Picasso.get().load(drug.getFullImageUrl()).into(firstDrug_ImageView);
        }else if (num == 2){
            secondDrug_TextView.setText(drug.getDrug_name());
            secondDrug_desc_TextView.setText(drug.getDesc());
            Picasso.get().load(drug.getFullImageUrl()).into(secondDrug_ImageView);

        }
    }

    private DrugListAdapter.drugOnClickHandler forFirstSearchViewHandler = new DrugListAdapter.drugOnClickHandler(){

        @Override
        public void onClick(Drug drug) {
            vib.vibrate(30);
            putDataToView(drug , 1);

            firstDrug = drug ;

            binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
         //   binding.interactionsFragmentOthercontainer.setVisibility(View.VISIBLE);

        }
    }  ;



    private DrugListAdapter.drugOnClickHandler forSecondSearchViewHandler = new DrugListAdapter.drugOnClickHandler(){

        @Override
        public void onClick(Drug drug) {
            vib.vibrate(30);
          putDataToView(drug , 2);
            secondDrug =drug ;

            binding.interactionsFragmentSearchRecyclerView.setVisibility(View.GONE);
        //    binding.interactionsFragmentOthercontainer.setVisibility(View.VISIBLE);
        }
    }  ;


}
