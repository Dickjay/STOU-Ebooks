package com.porar.ebook.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_BookList_BachelorDegree_Phone;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_BookList_MasterDegree_Phone;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_Unit_BachelorDegree_Phone;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_Unit_MasterDegree_Phone;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Porar on 10/7/2015.
 */
public abstract class Adapter_RecyclerView_ShowEbook extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static Context mContext;
    private Fragment mFragment;

    private ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users;

    private boolean fromMasterDegree = false, unit = false;
    private boolean fromBachelorDegree = false;

    private RecyclerView recyclerView;

    private int currentPosition = 0;


    public Adapter_RecyclerView_ShowEbook(Context context, ArrayList<Model_Ebooks_Public_User> ebooks_public_users, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
        arrayList_model_ebooks_public_users = ebooks_public_users;
    }

    public Adapter_RecyclerView_ShowEbook(Context context, ArrayList<Model_Ebooks_Public_User> ebooks_public_users, Fragment_SeeAll_BookList_MasterDegree_Phone fragment, boolean fromMasterDegree, RecyclerView recyclerView) {
        mContext = context;
        mFragment = fragment;
        arrayList_model_ebooks_public_users = ebooks_public_users;
        this.fromMasterDegree = fromMasterDegree;
        this.recyclerView = recyclerView;
    }

    public Adapter_RecyclerView_ShowEbook(Context context, ArrayList<Model_Ebooks_Public_User> ebooks_public_users, Fragment_SeeAll_Unit_MasterDegree_Phone fragment, boolean fromMasterDegree, boolean unit) {
        mContext = context;
        mFragment = fragment;
        arrayList_model_ebooks_public_users = ebooks_public_users;
        this.fromMasterDegree = fromMasterDegree;
        this.unit = unit;

    }

    public Adapter_RecyclerView_ShowEbook(Context context, ArrayList<Model_Ebooks_Public_User> ebooks_public_users, Fragment_SeeAll_BookList_BachelorDegree_Phone fragment, boolean fromBachelorDegree, RecyclerView recyclerView) {
        mContext = context;
        mFragment = fragment;
        arrayList_model_ebooks_public_users = ebooks_public_users;
        this.fromBachelorDegree = fromBachelorDegree;
        this.recyclerView = recyclerView;
    }

    public Adapter_RecyclerView_ShowEbook(Context context, ArrayList<Model_Ebooks_Public_User> ebooks_public_users, Fragment_SeeAll_Unit_BachelorDegree_Phone fragment, boolean fromBachelorDegree, boolean unit) {
        mContext = context;
        mFragment = fragment;
        arrayList_model_ebooks_public_users = ebooks_public_users;
        this.fromBachelorDegree = fromBachelorDegree;
        this.unit = unit;
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList_model_ebooks_public_users.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_ebook, parent, false);
            viewHolder = new ItemViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        currentPosition = setPosition(position);
        if (holder instanceof ItemViewHolder) {
            loadCoverEbooks(((ItemViewHolder) holder).img_ebook_pcover, currentPosition);
            setEbooksName(((ItemViewHolder) holder).ptxtName_Sebook, currentPosition);
            setEbooksPublisher(((ItemViewHolder) holder).ptxtPublisher_Sebook, currentPosition);
            setEbooksPrice(((ItemViewHolder) holder).ptxtPrice_Sebook, currentPosition);
            setEbooksRating(((ItemViewHolder) holder).pRating_Sebook, currentPosition);
            displayFontType(((ItemViewHolder) holder).ptxtName_Sebook);
            displayFontType(((ItemViewHolder) holder).ptxtPublisher_Sebook);
            displayFontType(((ItemViewHolder) holder).ptxtPrice_Sebook);
            ((ItemViewHolder) holder).prelativeLayout1.setOnClickListener(onClickListener);

        } else {
            showProgressBar(((ProgressViewHolder) holder).progressBar);
        }
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(fromMasterDegree){
                if (unit) {
                    gotoDetail(arrayList_model_ebooks_public_users, currentPosition);
                }else{
                    gotoUnit(arrayList_model_ebooks_public_users.get(currentPosition).getBCode());
                }
            }else if (fromBachelorDegree) {
                if (unit) {
                    gotoDetail(arrayList_model_ebooks_public_users, currentPosition);
                }else{
                    gotoUnit(arrayList_model_ebooks_public_users.get(currentPosition).getBCode());
                }
            }else{
                gotoDetail(arrayList_model_ebooks_public_users, currentPosition);
            }

        }
    };

    public int setPosition(int position){
        return position;
    }
    public void loadCoverEbooks(ImageView imageView, int position) {

        StaticUtils.picasso.with(mContext)
                .load(arrayList_model_ebooks_public_users.get(position).getBookCover()).fit()
                .noFade()
                .into(imageView);

    }

    public void setEbooksName(TextView textView, int position) {
        textView.setText(arrayList_model_ebooks_public_users.get(position).getBookName());
    }

    public void setEbooksPublisher(TextView textView, int position) {
        textView.setText(arrayList_model_ebooks_public_users.get(position).getBookPublisher());
    }

    public void setEbooksPrice(TextView textView, int position) {
        if (arrayList_model_ebooks_public_users.get(position).getBookPrice().startsWith("0")) {
            textView.setText(arrayList_model_ebooks_public_users.get(position).getBookPrice());
        } else {
            textView.setText(arrayList_model_ebooks_public_users.get(position).getBookPrice());
        }
    }

    public void setEbooksRating(RatingBar ratingBar, int position) {
        ratingBar.setRating(arrayList_model_ebooks_public_users.get(position).getBookRate());
        ratingBar.setEnabled(false);
    }

    public void displayFontType(TextView textView) {
        textView.setTypeface(StaticUtils.getTypeface(mContext, StaticUtils.Font.DB_Helvethaica_X_Med));
    }

    public void showProgressBar(ProgressBar progressBar){
        progressBar.setIndeterminate(true);
    }

    @Override
    public int getItemCount() {
        return arrayList_model_ebooks_public_users.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_ebook_pcover;
        private TextView ptxtName_Sebook, ptxtPublisher_Sebook, ptxtPrice_Sebook;
        private RatingBar pRating_Sebook;
        private RelativeLayout prelativeLayout1;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img_ebook_pcover = (ImageView) itemView.findViewById(R.id.img_ebook_pcover);
            ptxtName_Sebook = (TextView) itemView.findViewById(R.id.ptxtName_Sebook);
            ptxtPublisher_Sebook = (TextView) itemView.findViewById(R.id.ptxtPublisher_Sebook);
            ptxtPrice_Sebook = (TextView) itemView.findViewById(R.id.ptxtPrice_Sebook);
            pRating_Sebook = (RatingBar) itemView.findViewById(R.id.pRating_Sebook);
            prelativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.prelativeLayout1);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            progressBar.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    public abstract void gotoDetail(ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users, int pos);

    public abstract void gotoUnit(String bCode);


}