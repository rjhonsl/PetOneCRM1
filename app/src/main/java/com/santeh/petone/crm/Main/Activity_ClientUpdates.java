package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

/**
 * Created by rjhonsl on 12/21/2015.
 */
public class Activity_ClientUpdates extends FragmentActivity {

    ImageButton btnAddUpdate;

    Activity activity;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_updates);
        activity = this;
        context = Activity_ClientUpdates.this;

        btnAddUpdate = (ImageButton) findViewById(R.id.btn_addupdate);


        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = Helper.common.dialogYesNoWithMultiLineEditText(activity, "Describe your client visit.", "", "Updates", "CANCEL", "OK", R.color.blue);
                Button btnOK = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnCancel = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

    }

}
