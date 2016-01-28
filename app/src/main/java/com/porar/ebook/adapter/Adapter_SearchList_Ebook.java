package com.porar.ebook.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PListObject;

public class Adapter_SearchList_Ebook extends ArrayAdapter<String> implements
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    ArrayList<Model_EbookList> ebookList_Searchs;
    ArrayList<Model_Publisher> publishersList;
    Context context;
    LayoutInflater inflater;
    ImageDownloader_forCache downloader_forCache;
    Ebook_Detail ebook_Detail;
    AlertDialog alertDialog;
    Model_Ebooks_Detail ebooks_Detail = null;
    ArrayList<Model_Comment_List> comment_Lists = null;

    public Adapter_SearchList_Ebook(Context context, int textViewResourceId,
                                    ArrayList<Model_EbookList> ebookList_Searchs) {
        super(context, textViewResourceId);
        this.context = context;
        this.ebookList_Searchs = ebookList_Searchs;
        this.inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        downloader_forCache = new ImageDownloader_forCache();
    }

    @Override
    public int getCount() {
        if (ebookList_Searchs.size() != 0) {
            return ebookList_Searchs.size();
        } else {
            return 1;
        }
    }

    // public int getItem_BID(int position) {
    // return ebookList_Searchs.get(position).getBID();
    // }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (ebookList_Searchs.size() == 0) {
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.search_item_ebook_noresult, null);
                TextView txtName = (TextView) convertView
                        .findViewById(R.id.txt_noresult);
                txtName.setTypeface(StaticUtils.getTypeface(context,
                        Font.DB_Helvethaica_X_Med));
            }
            return convertView;
        }
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_item_ebook, null);
            viewHolder.txtName = (TextView) convertView
                    .findViewById(R.id.ptxtName_Sebook);
            viewHolder.txtPrice = (TextView) convertView
                    .findViewById(R.id.ptxtPrice_Sebook);
            viewHolder.txtPublisher = (TextView) convertView
                    .findViewById(R.id.ptxtPublisher_Sebook);
            viewHolder.iv_cover = (ImageView) convertView
                    .findViewById(R.id.img_ebook_pcover);
            viewHolder.rating = (RatingBar) convertView
                    .findViewById(R.id.pRating_Sebook);
            viewHolder.rating.setEnabled(false);

            viewHolder.txtName.setTypeface(StaticUtils.getTypeface(context,
                    Font.DB_Helvethaica_X_Med));
            viewHolder.txtPrice.setTypeface(StaticUtils.getTypeface(context,
                    Font.DB_Helvethaica_X_Med));
            viewHolder.txtPublisher.setTypeface(StaticUtils.getTypeface(
                    context, Font.DB_Helvethaica_X_Med));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.txtName.setText(ebookList_Searchs.get(position).getName());
        viewHolder.txtPublisher.setText(ebookList_Searchs.get(position)
                .getPublisher());
        String newPriceTHB = ebookList_Searchs.get(position).getPriceTHB();
        try {
            newPriceTHB = newPriceTHB
                    .substring(0, newPriceTHB.lastIndexOf("."));
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (newPriceTHB.equals("0")) {
            viewHolder.txtPrice.setText(ebookList_Searchs.get(position)
                    .getPrice());
        } else {
            viewHolder.txtPrice.setText(newPriceTHB + " baht");
        }

        viewHolder.rating
                .setRating(ebookList_Searchs.get(position).getRating());

        OnClickToDetail toDetail = new OnClickToDetail();
        toDetail.newInstant(ebookList_Searchs.get(position));
        convertView.setOnClickListener(toDetail);

        String coverUrl = AppMain.COVER_URL
                + ebookList_Searchs.get(position).getBID() + "/"
                + ebookList_Searchs.get(position).getCover();


        StaticUtils.picasso.with(context)
                .load(coverUrl).fit()
                .noFade()
                .into(viewHolder.iv_cover);


        return convertView;
    }

    public class OnClickToDetail implements OnClickListener {
        Model_EbookList model_EbookList;
        Ebook_Detail ebook_DetailApi;
        AlertDialog alertDialog;
        Model_Ebooks_Detail ebooks_Detail = null;

        public void newInstant(Model_EbookList object) {
            model_EbookList = object;

        }

        @Override
        public void onClick(View v) {

            Log.i("OnClickToDetail", "ebookBID " + model_EbookList.getBID());
            Log.i("OnClickToDetail", "ebookName " + model_EbookList.getName());

            // DerializeObject
            Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(context, model_EbookList.getBID());
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success"
                        + model_EbookList.getBID());
                Intent intent = new Intent(context, Activity_Detail.class);
                intent.putExtra("model", DSebooks_Detail);
                intent.putExtra("DeserializeObject", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(
                        R.anim.slide_in_right, R.anim.slide_out_left);

            } else {
                ebook_DetailApi = new Ebook_Detail(context,
                        String.valueOf(model_EbookList.getBID()));
                ebook_DetailApi.setOnListener(new Throw_IntefacePlist() {

                    @Override
                    public void PList_Detail_Comment(
                            plist.xml.PList Plistdetail,
                            plist.xml.PList Plistcomment,
                            final ProgressDialog pd) {

                    }

                    @Override
                    public void PList(plist.xml.PList resultPlist,
                                      final ProgressDialog pd) {

                        try {
                            for (PListObject each : (Array) resultPlist
                                    .getRootElement()) {
                                ebooks_Detail = new Model_Ebooks_Detail(each);
                            }

                            // SerializeObject
                            ebooks_Detail.setDateTime();
                            if (Class_Manage.SaveModel_Detail(context,
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());
                            }

                            Intent intent = new Intent(context,
                                    Activity_Detail.class);
                            intent.putExtra("model", ebooks_Detail);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left);

                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                        } catch (NullPointerException e) {
                            // refresh
                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }

                            alertDialog = new AlertDialog.Builder(context)
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog
                                    .setMessage("WARNING: An error has ocurred. Please to try again ?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    "Retry",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            System.out.println("Retry");
                                            pd.show();
                                            dialog.dismiss();
                                            System.gc();
                                            ebook_DetailApi
                                                    .LoadEbooksDetailAPI();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();
                                            System.gc();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }

                    @Override
                    public void StartLoadPList() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void PList_TopPeriod(plist.xml.PList Plist1,
                                                plist.xml.PList Plist2, ProgressDialog pd) {
                        // TODO Auto-generated method stub

                    }
                });
                ebook_DetailApi.LoadEbooksDetailAPI();
            }

        }

    }

    public static class ViewHolder {
        public TextView txtName = null;
        public TextView txtPrice = null;
        public TextView txtPublisher = null;
        public ImageView iv_cover = null;
        public RatingBar rating = null;
    }
}
