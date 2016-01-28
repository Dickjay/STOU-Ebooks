package com.porar.ebook.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.porar.ebooks.stou.R;

/**
 * Created by Porar on 10/27/2015.
 */
public class DialogCatagoriesList {
    public static int check_pos = 0;

    public interface CatagoriesCallback{
         void setCatiD(int pos);
    }

    public void buildDialog(Fragment activity, String[] item,final CatagoriesCallback catagoriesCallback){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity.getActivity());
        // set title
//        alertDialogBuilder.setTitle(activity.getResources().getString(R.string.allbook));
        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setSingleChoiceItems(item, check_pos, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog1, int position) {
                        // TODO Auto-generated method stub
                        check_pos = position;
                        catagoriesCallback.setCatiD(position);

                        dialog1.dismiss();
                    }
                })
                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog1,int id) {
                        dialog1.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
