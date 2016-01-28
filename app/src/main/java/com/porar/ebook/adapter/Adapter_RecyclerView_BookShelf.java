package com.porar.ebook.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.helper.ItemTouchHelperAdapter;
import com.porar.ebook.helper.ItemTouchHelperViewHolder;
import com.porar.ebook.helper.OnStartDragListener;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Phone;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Tablet;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;
import com.porar.ebooks.widget.FlexibleDividerDecoration;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Porar on 9/29/2015.
 */
public abstract class Adapter_RecyclerView_BookShelf extends RecyclerView.Adapter<Adapter_RecyclerView_BookShelf.ItemViewHolder> implements ItemTouchHelperAdapter, FlexibleDividerDecoration.VisibilityProvider {
    private Context mContext;
    private ArrayList<Model_EBook_Shelf_List> m_array_ebook_shelf_list;
    private final OnStartDragListener mDragStartListener;
    private boolean isDeleteBook = false;
    ArrayList<Integer> integerArrayList = new ArrayList<>();
    private Fragment fragment;


    public Adapter_RecyclerView_BookShelf(Context context, ArrayList<Model_EBook_Shelf_List> array_ebook_shelf_list, OnStartDragListener dragStartListener, Fragment fragment) {
        mDragStartListener = dragStartListener;
        mContext = context;
        m_array_ebook_shelf_list = array_ebook_shelf_list;
        this.fragment = fragment;
    }

    public Adapter_RecyclerView_BookShelf(Context context, ArrayList<Model_EBook_Shelf_List> array_ebook_shelf_list, Fragment fragment) {
        mContext = context;
        m_array_ebook_shelf_list = array_ebook_shelf_list;
        this.fragment = fragment;
        mDragStartListener = null;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpater_recyclerview_list, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        try {
            String url = "http://203.150.225.223/stoubook/covers/";
            url += m_array_ebook_shelf_list.get(position).getBID() + "/";
            url += "cover.jpg";


            StaticUtils.picasso.with(mContext)
                    .load(url).fit()
                    .noFade()
                    .into(holder.img_shelf_pcover);

            holder.img_shelf_txt_bookmark.setText(" " + m_array_ebook_shelf_list.get(position).getPointer());
            holder.img_shelf_txt_bookmark.setTypeface(StaticUtils.getTypeface(mContext,
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            holder.img_shelf_txt_bookmark
                    .setTypeface(StaticUtils
                            .getTypeface(
                                    mContext,
                                    StaticUtils.Font.DB_HelvethaicaMon_X));

            // Start a drag whenever the handle view it touched
            holder.img_shelf_pcover.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (AppMain.isphone) {
                        if (Fragment_Shelf_Phone.isDragAndDrop) {
                            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                                mDragStartListener.onStartDrag(holder);
                            }
                        }
                    } else {
                        if (Fragment_Shelf_Tablet.isDragAndDrop) {
                            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                                mDragStartListener.onStartDrag(holder);
                            }
                        }
                    }


                    return false;
                }
            });
            if (AppMain.isphone) {
                bindViewHolderPhone(holder, position);
            } else {
                bindViewHolderTablet(holder, position);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            holder.img_shelf_pcover.setVisibility(View.GONE);
            holder.img_shelf_txt_bookmark.setVisibility(View.GONE);
            holder.img_shelf_pshadow.setVisibility(View.GONE);
            holder.img_shelf_covershine.setVisibility(View.GONE);
            holder.img_shelf_bookmark_green.setVisibility(View.GONE);
            holder.img_shelf_bookmark_red.setVisibility(View.GONE);
            holder.deleted_ebooks_imageview.setVisibility(View.GONE);

        }
    }

    private void bindViewHolderPhone(ItemViewHolder holder, final int position) {
        if (Fragment_Shelf_Phone.isThrashBook) {
            holder.deleted_ebooks_imageview.setVisibility(View.VISIBLE);
            holder.img_shelf_bookmark_red.setVisibility(View.INVISIBLE);
            holder.img_shelf_txt_bookmark.setVisibility(View.INVISIBLE);
            holder.img_shelf_bookmark_green.setVisibility(View.INVISIBLE);
            enableBookshelf enaBookshelf = new enableBookshelf();
            enaBookshelf.newInstant(
                    holder.img_shelf_pcover,
                    m_array_ebook_shelf_list
                            .get(position).getBID(), position);
            holder.deleted_ebooks_imageview
                    .setOnClickListener(enaBookshelf);


        } else {
            if (Fragment_Shelf_Phone.isDragAndDrop) {

                holder.deleted_ebooks_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeEbookFromShelf(position);
                    }
                });
                holder.deleted_ebooks_imageview.setVisibility(View.VISIBLE);
                holder.img_shelf_bookmark_red.setVisibility(View.INVISIBLE);
                holder.img_shelf_txt_bookmark.setVisibility(View.INVISIBLE);
                holder.img_shelf_bookmark_green.setVisibility(View.INVISIBLE);
            } else {
                holder.deleted_ebooks_imageview.setVisibility(View.INVISIBLE);
                holder.img_shelf_bookmark_red.setVisibility(View.VISIBLE);
                holder.img_shelf_txt_bookmark.setVisibility(View.VISIBLE);
                holder.img_shelf_bookmark_green.setVisibility(View.VISIBLE);
            }
            setClickReadEbook(holder, position);
            setLongClickShowDetail(holder, position);
        }
    }

    private void bindViewHolderTablet(ItemViewHolder holder, final int position) {
        if (Fragment_Shelf_Tablet.isThrashBook) {
            holder.deleted_ebooks_imageview.setVisibility(View.VISIBLE);
            holder.img_shelf_bookmark_red.setVisibility(View.INVISIBLE);
            holder.img_shelf_txt_bookmark.setVisibility(View.INVISIBLE);
            holder.img_shelf_bookmark_green.setVisibility(View.INVISIBLE);
            enableBookshelf enaBookshelf = new enableBookshelf();
            enaBookshelf.newInstant(
                    holder.img_shelf_pcover,
                    m_array_ebook_shelf_list
                            .get(position).getBID(), position);
            holder.deleted_ebooks_imageview
                    .setOnClickListener(enaBookshelf);


        } else {
            if (Fragment_Shelf_Tablet.isDragAndDrop) {

                holder.deleted_ebooks_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeEbookFromShelf(position);
                    }
                });
                holder.deleted_ebooks_imageview.setVisibility(View.VISIBLE);
                holder.img_shelf_bookmark_red.setVisibility(View.INVISIBLE);
                holder.img_shelf_txt_bookmark.setVisibility(View.INVISIBLE);
                holder.img_shelf_bookmark_green.setVisibility(View.INVISIBLE);
            } else {
                holder.deleted_ebooks_imageview.setVisibility(View.INVISIBLE);
                holder.img_shelf_bookmark_red.setVisibility(View.VISIBLE);
                holder.img_shelf_txt_bookmark.setVisibility(View.VISIBLE);
                holder.img_shelf_bookmark_green.setVisibility(View.VISIBLE);


            }
            setClickReadEbook(holder, position);
            setLongClickShowDetail(holder, position);

        }
    }

    private void setClickReadEbook(ItemViewHolder holder, final int position) {
        OnClickIntent_PageImage_silde onClickIntent_pageImage_silde = new OnClickIntent_PageImage_silde();
        onClickIntent_pageImage_silde.newInstant(holder.img_shelf_pcover, m_array_ebook_shelf_list.get(position));
        holder.img_shelf_pcover.setOnClickListener(onClickIntent_pageImage_silde);
    }

    private void setLongClickShowDetail(ItemViewHolder holder, final int position) {
        OnLongClickIntent_PageImage_silde onLongClickIntent_pageImage_silde = new OnLongClickIntent_PageImage_silde();
        onLongClickIntent_pageImage_silde.newInstant(holder.img_shelf_pcover, m_array_ebook_shelf_list.get(position));
        holder.img_shelf_pcover.setOnLongClickListener(onLongClickIntent_pageImage_silde);
    }


    public void removeEbookFromShelf(int item) {
        try {
            int position = item;
            integerArrayList.add(m_array_ebook_shelf_list.get(position).getBID());
            getBookID(integerArrayList, position);
            m_array_ebook_shelf_list.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void restoreFromThash(int item) {
        try {
            int position = item;
            m_array_ebook_shelf_list.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemDismiss(int position) {
        try {
            m_array_ebook_shelf_list.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(m_array_ebook_shelf_list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
//        if (Fragment_Shelf_Phone.isDragAndDrop) {
//            return m_array_ebook_shelf_list.size();
//        } else if (Fragment_Shelf_Phone.isThrashBook) {
//            return m_array_ebook_shelf_list.size();
//        } else {
//            return CheckEbooksItem.bookSize(m_array_ebook_shelf_list.size());
//        }
        return m_array_ebook_shelf_list.size();
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        if (AppMain.isphone) {
            if (Fragment_Shelf_Phone.isDragAndDrop) {
                return true;
            }
        } else {
            if (Fragment_Shelf_Tablet.isDragAndDrop) {
                return true;
            }
        }

        return false;
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView img_shelf_txt_bookmark;
        public final AdjustableImageView img_shelf_pshadow;
        public final AdjustableImageView img_shelf_pcover;
        public final AdjustableImageView img_shelf_covershine;
        public final AdjustableImageView img_shelf_bookmark_green;
        public final AdjustableImageView img_shelf_bookmark_red;
        public final AdjustableImageView deleted_ebooks_imageview;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img_shelf_txt_bookmark = (TextView) itemView.findViewById(R.id.img_shelf_txt_bookmark);
            img_shelf_pshadow = (AdjustableImageView) itemView.findViewById(R.id.img_shelf_pshadow);
            img_shelf_pcover = (AdjustableImageView) itemView.findViewById(R.id.img_shelf_pcover);
            img_shelf_covershine = (AdjustableImageView) itemView.findViewById(R.id.img_shelf_covershine);
            img_shelf_bookmark_green = (AdjustableImageView) itemView.findViewById(R.id.img_shelf_bookmark_green);
            img_shelf_bookmark_red = (AdjustableImageView) itemView.findViewById(R.id.img_shelf_bookmark_red);
            deleted_ebooks_imageview = (AdjustableImageView) itemView.findViewById(R.id.deleted_ebooks_imageview);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(0);

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
            getAllItem(m_array_ebook_shelf_list);
        }
    }

    public class OnLongClickIntent_PageImage_silde implements View.OnLongClickListener {
        Model_EBook_Shelf_List model;
        ImageView img_cover;

        public void newInstant(ImageView imgcover, Model_EBook_Shelf_List object) {
            model = object;
            img_cover = imgcover;
        }


        @Override
        public boolean onLongClick(View v) {
            OnLongClickCover(model);
            return true;
        }
    }

    public class OnClickIntent_PageImage_silde implements View.OnClickListener {
        Model_EBook_Shelf_List model;
        ImageView img_cover;

        public void newInstant(ImageView imgcover, Model_EBook_Shelf_List object) {
            model = object;
            img_cover = imgcover;
        }


        @Override
        public void onClick(View v) {
            OnClickCover(model);
        }

    }

    public class enableBookshelf implements View.OnClickListener {
        int bid, pos;
        String url = "";
        AlertDialog alertDialog;

        public void newInstant(ImageView imgcover, int object, int position) {
            bid = object;
            pos = position;
            url = AppMain.ENABLE_BOOK_SHELF_URL;
            url += "bid=" + object;
            url += "&cid=" + Shared_Object.getCustomerDetail.getCID();
        }

        @Override
        public void onClick(final View v) {

            alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle(AppMain.getTag());
            alertDialog.setMessage("Picked out of the trash?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "confirm",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            restoreFromThash(pos);
                            dialog.dismiss();
                            System.gc();


                            OkHttpClient okHttpClient = new OkHttpClient();

                            Request.Builder builder = new Request.Builder();
                            Request request = builder.url(url).build();

                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override

                                public void onFailure(Request request, IOException e) {
                                    updateView("Error - " + e.getMessage());
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        try {
                                            updateView(response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            updateView("Error - " + e.getMessage());
                                        }
                                    } else {
                                        updateView("Not Success - code : " + response.code());
                                    }
                                }


                                public void updateView(final String strResult) {

                                    fragment.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext,
                                                    "complete restore",
                                                    Toast.LENGTH_SHORT).show();
                                            Toast.makeText(mContext,
                                                    "please refresh",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });

                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            System.gc();
                        }
                    });
            alertDialog.show();

        }
    }

    public abstract void OnClickCover(Model_EBook_Shelf_List model);

    public abstract void OnLongClickCover(Model_EBook_Shelf_List model);

    public abstract void getBookID(ArrayList<Integer> arrayList, int position);

    public abstract void getAllItem(ArrayList<Model_EBook_Shelf_List> m_array_ebook_shelf_list);
}