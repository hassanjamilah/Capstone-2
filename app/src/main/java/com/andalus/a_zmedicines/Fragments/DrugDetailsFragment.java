package com.andalus.a_zmedicines.Fragments;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.andalus.a_zmedicines.R;
import com.andalus.a_zmedicines.Widget.MyDrugsWidget;
import com.squareup.picasso.Picasso;
import java.util.List;
import com.andalus.a_zmedicines.MyClasses.Composition;
import com.andalus.a_zmedicines.MyClasses.Drug;
import com.andalus.a_zmedicines.Screens.MainActivity;
import com.andalus.a_zmedicines.Utils.AppExecutors;


public class DrugDetailsFragment extends Fragment {
    public static final String EXTR_DRUGDETAILS = "extraDrugDetails";
    private static final String SAVESTATE_DRUG_DETAILS = "saveInstanceStateOfDrug" ;


    private TextView mDetailsTextView;
    private ImageView mImageView;
    private ImageButton mFavImageButton;
    private FloatingActionButton mShareFab;
    private SearchView mSearchView;
    private NestedScrollView mScrollView;

    /**
     * This is used for generating vibration on click
     * for accessiblity
     */
    private Vibrator vib;

    private Drug mDrug;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState !=null){
            mDrug = new Drug() ;
            mDrug = savedInstanceState.getParcelable(SAVESTATE_DRUG_DETAILS) ;

        }else {
            if (getArguments() != null) {
                mDrug = getArguments().getParcelable(EXTR_DRUGDETAILS);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_drug_details, container, false);
        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        initUi(v);
        return v;
    }

    private void initUi(View v) {
        InflateViews(v);
        EnhanceSearchView();
        PrepareShareActions();
        HandleFavActions();
        handleSearchInDetailsText();
        FormatAndFillDetails();


    }

    /**
     * This method is used to fromat and put the details of the drug in
     * the drugs details textview
     */
    private void FormatAndFillDetails() {
        final StringBuilder builder = new StringBuilder();

        LiveData<List<Composition>> comps = MainActivity.mDatabase.drugsDao().getDrugComps(mDrug.getId());

        comps.observe(getActivity(), new Observer<List<Composition>>() {
            @Override
            public void onChanged(List<Composition> compositions) {
                builder.append("<h3>" + getString(R.string.drug_name) + "</h3>");
                builder.append("<p>" + mDrug.getDrug_name() + "</p>");


                builder.append("<h3>" + getString(R.string.composition) + "</h3>");

                final String xx = "hassan";
               // Log.i("hassan1", "num of comps in details is : " + compositions.size());
                String x = "";
                for (int i = 0; i < compositions.size(); i++) {
                    x = x + compositions.get(i).getComp_name() + " " + compositions.get(i).getComp_conc() + "\n";
                 //   Log.i("hassan1", x + "the x is :");
                   // Log.i("hassan1", x + "the comp in x is :" + compositions.get(i).getComp_name());

                }


                builder.append("<p>" + x + "</p>");
                builder.append("<h3>" + getString(R.string.drug_desc) + "</h3>");
                builder.append("<p>" + mDrug.getDesc() + "</p>");

                builder.append("<h3>" + getString(R.string.important_Info) + "</h3>");
                builder.append("<p>" + mDrug.getImportant_info() + "</p>");

                builder.append("<h3>" + getString(R.string.before_taking) + "</h3>");
                builder.append("<p>" + mDrug.getBefore_taking() + "</p>");

                builder.append("<h3>" + getString(R.string.overdose) + "</h3>");
                builder.append("<p>" + mDrug.getOverdose() + "</p>");

                builder.append("<h3>" + getString(R.string.side_effects) + "</h3>");
                builder.append("<p>" + mDrug.getSide_effects() + "</p>");

                builder.append("<h3>" + getString(R.string.dose) + "</h3>");
                builder.append("<p>" + mDrug.getDose() + "</p>");

                mDetailsTextView.setText(Html.fromHtml(builder.toString()));


                Picasso.get().load(mDrug.getFullImageUrl()).into(mImageView);
            }
        });
    }

    /**
     * This method is used to search in the details of the drug
     * and mark it with red color
     */
    private void handleSearchInDetailsText() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) return  false ;
                String criteria = query;
                Spanned fullText = Html.fromHtml(mDetailsTextView.getText().toString());
                if (fullText.toString().contains(criteria)) {
                    int indexOfCriteria = fullText.toString().indexOf(criteria);
                    int lineNumber = mDetailsTextView.getLayout().getLineForOffset(indexOfCriteria);
                    String highlighted = "<font color='red'>" + criteria + "</font>";
                    String x = fullText.toString().replace(criteria, highlighted);
                    mDetailsTextView.setText(Html.fromHtml(x));


                    mScrollView.scrollTo(0, mDetailsTextView.getLayout().getLineTop(lineNumber));
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * This method is used to handle the fav button clicks
     */
    private void HandleFavActions() {
        final int isFavImage = R.drawable.ic_star_black_24dp;
        final int isNotFavImage = R.drawable.ic_star_border_black_24dp;

        if (mDrug.isFav()) {
            mFavImageButton.setImageResource(isFavImage);
        } else {
            mFavImageButton.setImageResource(isNotFavImage);

        }

        mFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(30);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        boolean x = MainActivity.mDatabase.drugsDao().isDrugFav(mDrug.getId());
                        if (x) {
                            Log.i("hassan", "the drug with id : " + mDrug.getId() + "is fav");
                            mDrug.setFav(false);
                            MainActivity.mDatabase.drugsDao().updateDrug(mDrug);
                            mFavImageButton.setImageResource(isNotFavImage);
                        } else {
                            Log.i("hassan", "the drug with id : " + mDrug.getId() + "is not fav");
                            mDrug.setFav(true);
                            MainActivity.mDatabase.drugsDao().updateDrug(mDrug);
                            mFavImageButton.setImageResource(isFavImage);
                        }

                        AppWidgetManager manager = AppWidgetManager.getInstance(getContext()) ;
                        int[] ids = manager.getAppWidgetIds(new ComponentName(getContext() , MyDrugsWidget.class)) ;

                        manager.notifyAppWidgetViewDataChanged(ids , R.id.my_drugs_widget_listview_listview);

                    }
                });
            }
        });
        mFavImageButton.setBackgroundResource(0);
    }


    /**
     * This method is handling the share action of the share button
     */
    private void PrepareShareActions() {
        mShareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mDetailsTextView.getText());
                sendIntent.setType("text/html");
                startActivity(sendIntent);
            }
        });
    }


    /**
     * This is used to make the search view meets the theme of our app
     */
    private void EnhanceSearchView() {
        TextView tv = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        tv.setTextColor(getResources().getColor(R.color.bluegray_800));

        ImageView iv = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        iv.setImageResource(R.drawable.ic_close_black_24dp);
    }

    /**
     * This is used to use findviewbyid to prepare our views
     * @param v
     */
    private void InflateViews(View v) {
        mDetailsTextView = v.findViewById(R.id.drugDetailsFragment_details_TextView);
        mImageView = v.findViewById(R.id.drugDetailsFragment_imageView);
        mFavImageButton = v.findViewById(R.id.drugDetailsFragment_fav_button);
        mShareFab = v.findViewById(R.id.drugDetailsFragment_share_fab);
        mSearchView = v.findViewById(R.id.drugDetailsFragment_searchview);
        mScrollView = v.findViewById(R.id.drugDetailsFragment_details_scrollview);
        mSearchView = v.findViewById(R.id.drugDetailsFragment_searchview);
    }


    /**
     * This is a static method used to launch the details fragment from other fragments
     * such as : the main drug fragment , interaction fragment , favourit fragment
     * @param bundle
     * @param manager
     */
    public static void startDetailsFragment(Bundle bundle, FragmentManager manager) {

        DrugDetailsFragment fra = new DrugDetailsFragment();
        if (bundle != null) {
            fra.setArguments(bundle);
        }
        manager.beginTransaction()
                .replace(R.id.mainActivity_Container, fra)
                .addToBackStack(fra.getClass().getName())
                .commit();


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVESTATE_DRUG_DETAILS , mDrug);
        Log.i("hassan1" , "Saving details instance state for : " + mDrug.getDrug_name()) ;
    }

}
