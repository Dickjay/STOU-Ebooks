package com.porar.ebooks.stou.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_BookShelf;
import com.porar.ebook.control.DialogLogin;
import com.porar.ebook.control.DialogProfile_Show;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Porar on 9/28/2015.
 */
public class Fragment_Shelf_Phone extends Fragment implements RadioGroup.OnCheckedChangeListener {


    private String Tag = "Fragment_Shelf_Phone";
    public static Handler handler = new Handler();
    private Toolbar toolbar;

    private AdjustableImageView refresh_imageview, edit_imageview, profile_imageview;
    private FrameLayout frameLayout;

    private SegmentedGroup segment_mode;
    private RadioButton radio_all_book, radio_favorite_book, radio_thrash_book;

    public static boolean isDragAndDrop = false;

    private boolean isAllBooks = disableStated();
    private boolean isFavoriteBoook = disableStated();
    public static boolean isThrashBook = false;

    private boolean isClickedEvent = false;

    public static boolean isDeletedThrashBook = false;
    LoadAPIResultString apiResultString;
    public static Fragment_Shelf_Phone fragment_shelf_phone;

    public ArrayList<Model_EBook_Shelf_List> arrayListoffline;
    private ProgressDialog progressDialog;


    public static Fragment_Shelf_Phone newInstance() {
        fragment_shelf_phone = new Fragment_Shelf_Phone();
        return fragment_shelf_phone;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stou_shelf_phone, container, disableStated());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initView(view);
    }

    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void initView(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_stou_shelf_phone);
        refresh_imageview = (AdjustableImageView) view.findViewById(R.id.refresh_imageview);
        edit_imageview = (AdjustableImageView) view.findViewById(R.id.edit_imageview);
        profile_imageview = (AdjustableImageView) view.findViewById(R.id.profile_imageview);

        refresh_imageview.setOnClickListener(refreshListener);
        edit_imageview.setOnClickListener(editListener);
        profile_imageview.setOnClickListener(profileListener);


        segment_mode = (SegmentedGroup) view.findViewById(R.id.segment_mode);
        segment_mode.setOnCheckedChangeListener(this);

        radio_all_book = (RadioButton) view.findViewById(R.id.radio_button_all_ebooks);
        radio_favorite_book = (RadioButton) view.findViewById(R.id.radio_button_favorite_ebooks);
        radio_thrash_book = (RadioButton) view.findViewById(R.id.radio_button_thrash_ebooks);

        radio_all_book.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_favorite_book.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_thrash_book.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadItem() {
        if (Shared_Object.isOfflineMode) {
            // get ebooks from devices if is offline mode
            arrayListoffline = Class_Manage.getEbooksShelfInOffLineMode(getActivity());
            if (arrayListoffline != null && arrayListoffline.size() > 0) {
                setProfileImage();
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance(arrayListoffline));
                enabletButton(disableStated(), enableStated(), enableStated());
            }
        } else {
            if (isUserLogin()) {
                setProfileImage();
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance());
                enabletButton(disableStated(), enableStated(), enableStated());
            } else {
                showDialogLogin();
            }

        }
        if (Fragment_MainPublicUser_Phone.onLoadShelf) {
            if (!Shared_Object.isOfflineMode) {
                if (isUserLogin()) {
                    isAllBooks = enableStated();
                    replaceFragmet(Fragment_AllEbooks.newInstance());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            enabletButton(disableStated(), enableStated(), enableStated());
                        }
                    });
                } else {

                    replaceFragmet(Fragment_Empty.newInstance());
                    showDialogLogin();
                }
                setProfileImage();
            } else {

            }
            Fragment_MainPublicUser_Phone.onLoadShelf = false;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (isClickedEvent) {
            return;
        }
        switch (checkedId) {
            case R.id.radio_button_all_ebooks:


                if (Shared_Object.isOfflineMode) {
                    showToastMessage();
                    return;
                }
                // is user select AllEbooks mode
                if (isAllBooks) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = enableStated();
                        isDeletedThrashBook = disableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = enableStated();
                        isFavoriteBoook = disableStated();
                        isThrashBook = disableStated();
                        replaceFragmet(Fragment_AllEbooks.newInstance());
                        edit_imageview.setImageResource(R.drawable.ic_action_delete);
                        enabletButton(disableStated(), enableStated(), enableStated());
                    } else {
                        showDialogLogin();
                    }
                }
                return;
            case R.id.radio_button_favorite_ebooks:


                if (Shared_Object.isOfflineMode) {
                    showToastMessage();
                    return;
                }
                // is user select favoriteEBoook mode
                if (isFavoriteBoook) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = enableStated();
                        isDeletedThrashBook = disableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = disableStated();
                        isFavoriteBoook = enableStated();
                        isThrashBook = disableStated();
                        replaceFragmet(Fragment_FavoriteEbooks.newInstance());
                        edit_imageview.setImageResource(R.drawable.ic_action_delete);
                        enabletButton(enableStated(), disableStated(), enableStated());
                    } else {
                        showDialogLogin();
                    }
                }
                return;
            case R.id.radio_button_thrash_ebooks:


                if (Shared_Object.isOfflineMode) {
                    showToastMessage();
                    return;
                }
                // is user select ithrashEBook mode
                if (isThrashBook) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = enableStated();
                        isDeletedThrashBook = enableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = disableStated();
                        isFavoriteBoook = disableStated();
                        isThrashBook = enableStated();
                        replaceFragmet(Fragment_ThrashEbooks.newInstance());
                        enabletButton(enableStated(), enableStated(), disableStated());
                        changeIconEditBooks(edit_imageview, R.drawable.ic_action_save);

                    } else {
                        showDialogLogin();
                    }
                }
                return;
            default:
                if (isClickedEvent) {
                    return;
                }

                if (Shared_Object.isOfflineMode) {
                    showToastMessage();
                    return;
                }
                // is user select AllEbooks mode
                if (isAllBooks) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = enableStated();
                        isDeletedThrashBook = disableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = enableStated();
                        isFavoriteBoook = disableStated();
                        isThrashBook = disableStated();
                        replaceFragmet(Fragment_AllEbooks.newInstance());
                        edit_imageview.setImageResource(R.drawable.ic_action_delete);
                        enabletButton(disableStated(), enableStated(), enableStated());
                    } else {
                        showDialogLogin();
                    }
                }
                return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void setProfileImage() {
        try {
            if (Shared_Object.getCustomerDetail.getCID() > 0) {
                ImageDownloader_forCache imageDownloader_forCache = new ImageDownloader_forCache();
                imageDownloader_forCache.download(Shared_Object.getCustomerDetail.getPictureUrl(), profile_imageview);
            } else {

                changeIconEditBooks(profile_imageview, R.drawable.ic_action_person);
            }
        } catch (NullPointerException e) {
            changeIconEditBooks(profile_imageview, R.drawable.ic_action_person);
        }
    }

    private boolean isUserLogin() {
        if (Shared_Object.getCustomerDetail.getCID() <= 0) {
            return disableStated();
        } else {
            return enableStated();
        }
    }

    private void showDialogLogin() {
        DialogLogin dialogLogin_show = new DialogLogin(getActivity(), R.style.PauseDialogAnimation, Fragment_Shelf_Phone.this) {
            @Override
            public void loginComplete() {
                Shared_Object.loadConfigData(getActivity());
                setProfileImage();
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance());
                enabletButton(disableStated(), enableStated(), enableStated());
            }
        };
        dialogLogin_show.show();
    }


    private View.OnClickListener refreshListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }

            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();

                return;
            }
            // if user login to stou-ebooks
            if (isUserLogin()) {
                isClickedEvent = enableStated();
                // reset mode to AllEbooks mode
                if (isFavoriteBoook) {
                    isFavoriteBoook = disableStated();
                }
                if (isDeletedThrashBook) {
                    isDeletedThrashBook = disableStated();
                }
                if (isThrashBook) {
                    isThrashBook = disableStated();
                }
                if (isDragAndDrop) {
                    isThrashBook = disableStated();
                }

                isAllBooks = enableStated();
                setProfileImage();
                replaceFragmet(Fragment_AllEbooks.newInstance());
                changeIconEditBooks(edit_imageview, R.drawable.ic_action_delete);
                enabletButton(disableStated(), enableStated(), enableStated());

            } else {
                // show dialog login and register to use stou-ebooks app
                showDialogLogin();
            }
        }
    };


    private View.OnClickListener editListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }

            if (Shared_Object.isOfflineMode) {
                showToastMessage();
                return;
            }
            // Restore EBooks from thrash mode
            if (isDeletedThrashBook) {
                isClickedEvent = enableStated();
                isDragAndDrop = disableStated();
                isThrashBook = disableStated();
                isDeletedThrashBook = disableStated();
                isAllBooks = enableStated();
                Fragment_FavoriteEbooks.eneableDragDrop(isDragAndDrop);
                replaceFragmet(Fragment_AllEbooks.newInstance());
                changeIconEditBooks(edit_imageview, R.drawable.ic_action_delete);
                enabletButton(disableStated(), enableStated(), enableStated());
            } else {
                // Save EBooks from drag and drop && remove Ebooks to thrash mode
                if (isDragAndDrop) {

                    // confirm user clicked event
                    isClickedEvent = enableStated();
                    //cancel drag and drop
                    isDragAndDrop = disableStated();
                    if (isAllBooks) {
                        SendRequestSortEbookIDTask sendRequestSortEbookIDTask = new SendRequestSortEbookIDTask(Fragment_AllEbooks.array_deleted_ebooksID,
                                Fragment_AllEbooks.adapter_recyclerView_bookShelf, Fragment_AllEbooks.array_sort_ebooksID, Fragment_AllEbooks.arraylist_shelf_ebooksID, true);
                        sendRequestSortEbookIDTask.execute();


                    } else if (isFavoriteBoook) {
                        SendRequestSortEbookIDTask sendRequestSortEbookIDTask = new SendRequestSortEbookIDTask(Fragment_FavoriteEbooks.array_deleted_ebooksID,
                                Fragment_FavoriteEbooks.adapter_recyclerView_bookShelf, Fragment_FavoriteEbooks.array_sort_ebooksID, Fragment_FavoriteEbooks.arraylist_shelf_ebooksID, false);
                        sendRequestSortEbookIDTask.execute();

                    }
                    // change icon edit_imageview to R.drawable.ic_action_delete
                    changeIconEditBooks(edit_imageview, R.drawable.ic_action_delete);
                    enabeButtonClick();

                } else {

                    // enable drag and drop && remove Ebooks to thrash mode
                    if (isUserLogin()) {
                        isClickedEvent = enableStated();
                        isDragAndDrop = enableStated();
                        if (!isThrashBook) {
                            if (isAllBooks) {
                                replaceFragmet(Fragment_AllEbooks.newInstance());
                                enabletButton(disableStated(), enableStated(), enableStated());
                            }
                            if (isFavoriteBoook) {
                                replaceFragmet(Fragment_FavoriteEbooks.newInstance());
                                enabletButton(enableStated(), disableStated(), enableStated());
                            }
                        }
                        // change icon edit_imageview to R.drawable.ic_action_save
                        changeIconEditBooks(edit_imageview, R.drawable.ic_action_save);
                        disableButtonClick();
                    } else {
                        showDialogLogin();
                    }
                }
            }


        }

        private void disableButtonClick() {

            radio_all_book.setEnabled(disableStated());
            radio_favorite_book.setEnabled(disableStated());
            radio_thrash_book.setEnabled(disableStated());
            refresh_imageview.setOnClickListener(null);
            profile_imageview.setOnClickListener(null);

        }

        private void enabeButtonClick() {
            radio_all_book.setEnabled(enableStated());
            radio_favorite_book.setEnabled(enableStated());
            radio_thrash_book.setEnabled(enableStated());
            refresh_imageview.setOnClickListener(refreshListener);
            profile_imageview.setOnClickListener(profileListener);
        }
    };


    private View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
                return;
            }

            if (!isUserLogin()) {
                showDialogLogin();
            } else {
                DialogProfile_Show dialogProfile_Show = new DialogProfile_Show(getActivity(),
                        R.style.PauseDialog, Fragment_Shelf_Phone.this);
                dialogProfile_Show
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface v) {
                                // if logout success ,next step reset data to use stou-ebooks any imageProfile or shelf background
                                // and show dialog login to use stou-eboos app
                                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                                    setProfileImage();
                                    resetAccount();
                                    showDialogLogin();
                                }
                            }
                        });
                dialogProfile_Show.show();
            }
        }

        private void resetAccount() {
            AppMain.pList_default_ebookshelf = null;
            StaticUtils.arrShilf.clear();
            StaticUtils.Login = 0;
            StaticUtils.isBechalorDegree = disableStated();
            StaticUtils.isAMasterDegree = disableStated();
            StaticUtils.shelfID = 0;
            StaticUtils.pageID = 0;
            StaticUtils.phonePage = 0;
            StaticUtils.phoneValue = 0;

            // reset to fragment empty mode
            if (isAllBooks) {
                isAllBooks = disableStated();
            }
            if (isFavoriteBoook) {
                isFavoriteBoook = disableStated();
            }
            if (isThrashBook) {
                isThrashBook = disableStated();
                isDeletedThrashBook = disableStated();
                isDragAndDrop = disableStated();
                edit_imageview.setImageResource(R.drawable.ic_action_delete);
            }
            addFragmentEmpty();
        }

        private void addFragmentEmpty() {
            enabletButton(enableStated(), enableStated(), enableStated());
            replaceFragmet(Fragment_Empty.newInstance());
        }
    };

    public void replaceFragmet(Fragment fragment) {
        ReplaceFragmentTask replaceFragmentTask = new ReplaceFragmentTask(fragment);
        replaceFragmentTask.execute();
    }


    class ReplaceFragmentTask extends AsyncTask<Void, Void, Boolean> {
        private Fragment fragment;
        private FragmentTransaction fragmentTransaction;

        ReplaceFragmentTask(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out);
            return enableStated();
        }

        @Override
        protected void onPostExecute(Boolean cancelTask) {
            super.onPostExecute(cancelTask);
            fragmentTransaction.commit();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    isClickedEvent = false;
                }
            }, 500);
            cancel(cancelTask);
        }

    }

    private void changeIconEditBooks(final ImageView imageView, final int drawable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(drawable);
            }
        });
    }

    private void enabletButton(boolean allBook, boolean favoriteBook, boolean thrashBook) {
        if (!allBook) {
            radio_all_book.setChecked(enableStated());
        }
        if (!favoriteBook) {
            radio_favorite_book.setChecked(enableStated());
        }
        if (!thrashBook) {
            radio_thrash_book.setChecked(enableStated());
        }


    }


    private boolean enableStated() {
        return true;
    }

    private boolean disableStated() {
        return false;
    }

    private void showToastMessage() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();
        isClickedEvent = false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createProgressDialog() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Saving..",
                true, false);
    }

    private void closeDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public class SendRequestSortEbookIDTask extends AsyncTask<Void, Void, Boolean> {
        public ArrayList<Integer> array_deleted_ebooksID;
        public Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf;
        public ArrayList<String> array_sort_ebooksID;
        public ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID;
        public boolean isAllEbooks = false;

        SendRequestSortEbookIDTask(ArrayList<Integer> array_deleted_ebooksID, Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf,
                                   ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID, boolean isAllEbooks) {
            this.array_deleted_ebooksID = array_deleted_ebooksID;
            this.adapter_recyclerView_bookShelf = adapter_recyclerView_bookShelf;
            this.array_sort_ebooksID = array_sort_ebooksID;
            this.arraylist_shelf_ebooksID = arraylist_shelf_ebooksID;
            this.isAllEbooks = isAllEbooks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (builderEbookID(adapter_recyclerView_bookShelf, array_sort_ebooksID, arraylist_shelf_ebooksID)) {
                    return true;
                } else {
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            RequestDeletedEbookIDTask sendRequestDeletedEbookIDTask = new RequestDeletedEbookIDTask(array_deleted_ebooksID, isAllEbooks);
            sendRequestDeletedEbookIDTask.execute();
            cancel(true);
        }
    }

    private boolean builderEbookID(Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf, ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) throws IOException {
        if (arraylist_shelf_ebooksID != null && arraylist_shelf_ebooksID.size() > 0) {

            StringBuilder stringBuilder = setStringBuilderEBooksID(adapter_recyclerView_bookShelf, array_sort_ebooksID, arraylist_shelf_ebooksID);
            if (stringBuilder != null) {
                sendRequestOkHttp(stringBuilder);
                clearSortEBooksID(array_sort_ebooksID, arraylist_shelf_ebooksID);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private void sendRequestOkHttp(StringBuilder stringBuilder) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("apikey", "f4666c85ab34b5397519ef4b4e471b43")
                .add("cid", Shared_Object.getCustomerDetail.getCID() + "")
                .add("book_list", stringBuilder + "")
                .build();
        Request request = new Request.Builder()
                .url("http://203.150.225.223/stoubookapi/api/save_book_index.php")
                .post(formBody)
                .build();
        client.newCall(request).execute();
    }

    private StringBuilder setStringBuilderEBooksID(Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf, ArrayList<String> array_bid, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) {
        if (adapter_recyclerView_bookShelf != null && adapter_recyclerView_bookShelf.getItemCount() > 0) {

            int allItem = adapter_recyclerView_bookShelf.getItemCount();

            for (int pos = 0; pos < allItem; pos++) {
                String currentItem = arraylist_shelf_ebooksID.get(pos).getBID() + "";
                array_bid.add(currentItem);
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (String value : array_bid) {
                stringBuilder.append(value).append(',');
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            return stringBuilder;
        }
        return null;
    }

    private void clearSortEBooksID(ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) {
        arraylist_shelf_ebooksID.clear();
        array_sort_ebooksID.clear();
    }

    public class RequestDeletedEbookIDTask extends AsyncTask<Void, Void, Boolean> {
        public ArrayList<Integer> array_deleted_ebooksID;
        public boolean isAllEbooks = false;

        RequestDeletedEbookIDTask(ArrayList<Integer> array_deleted_ebooksID, boolean isAllEbooks) {
            this.array_deleted_ebooksID = array_deleted_ebooksID;
            this.isAllEbooks = isAllEbooks;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (startDeletedEBookID(array_deleted_ebooksID)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            closeDialog();
            if (isAllBooks) {
                enabletButton(disableStated(), enableStated(), enableStated());
                replaceFragmet(Fragment_AllEbooks.newInstance(progressDialog));
            } else {
                enabletButton(enableStated(), disableStated(), enableStated());
                replaceFragmet(Fragment_FavoriteEbooks.newInstance(progressDialog));
            }
            cancel(true);
        }
    }

    private boolean startDeletedEBookID(ArrayList<Integer> array_deleted_ebooksID) {


        if (array_deleted_ebooksID != null && array_deleted_ebooksID.size() > 0) {

            for (final Integer EBooksID : array_deleted_ebooksID) {

                // String url =
                // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                String url = AppMain.DELETE_BOOK_SHELF_URL;
                url += "bid=" + EBooksID;
                url += "&cid="
                        + Shared_Object.getCustomerDetail.getCID();
                url += "&confirm=0";

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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (strResult.contains("1")) {

                                        Toast.makeText(getActivity(),
                                                "delete to trash",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                } catch (NullPointerException e) {
                                    Toast.makeText(
                                            getActivity(),
                                            "WARNING: An error has ocurred. Delete BOOK-ID "
                                                    + EBooksID + " each",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(),
                                            "WARNING: Please Refresh.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                Class_Manage.deleteEbooksCache(getActivity(), EBooksID);
            }
            clearDeletedEBooksID(array_deleted_ebooksID);
            return true;
        } else {
            return false;
        }
    }


    private void clearDeletedEBooksID(ArrayList<Integer> array_deleted_ebooksID) {
        array_deleted_ebooksID.clear();
    }


}
