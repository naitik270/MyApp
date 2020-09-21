package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Adapter.TaxSlabAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickTaxSlab;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddCategoryActivity;
import com.demo.nspl.restaurantlite.activity.AddTaxSlabActivity;
import com.demo.nspl.restaurantlite.classes.ClsCategory;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TaxSlabFragment extends Fragment {


    private List<ClsTaxSlab> list;
    private FloatingActionButton fab;
    private TextView empty_title_text;
    private RecyclerView rv;
    private RadioButton rbYES, rbNO;
    TaxSlabAdapter cu;

    public TaxSlabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Tax Slab");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tax_slab, container, false);
        // Inflate the layout for this fragment

        fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        rv = v.findViewById(R.id.rv);

        rbYES = v.findViewById(R.id.rbYES);
        rbNO = v.findViewById(R.id.rbNO);

        ClsGlobal.isFristFragment = true;
        empty_title_text = v.findViewById(R.id.empty_title_text);


        ViewData();
        fab.setOnClickListener(v1 -> {

            Intent intent = new Intent(getContext(), AddTaxSlabActivity.class);
            startActivity(intent);

        });


        return v;
    }


    private void ViewData() {
        list = new ArrayList<>();

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ;

        list = new ClsTaxSlab(getActivity()).getList();

        if (list.size() == 0) {
            Log.e("list_Item", "list_Item call");
            empty_title_text.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
        }

        cu = new TaxSlabAdapter(getContext(), list);

        cu.SetOnClickListener((clsTaxSlab, position) -> {


            final Dialog dialog = new Dialog(getActivity());
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_delete_edit);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);


            ll_update.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                int currentUpdate = clsTaxSlab.getTaxSlabId();

                Intent intent = new Intent(getContext(), AddTaxSlabActivity.class);
                intent.putExtra("UpdateId", currentUpdate);
                startActivity(intent);

            });

            ll_delete.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {

                        ClsTaxSlab delete = new ClsTaxSlab();
                        delete.setTaxSlabId(clsTaxSlab.getTaxSlabId());

                        int Result = ClsTaxSlab.Delete(delete, getContext());
                        if (Result == 1) {
                            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            cu.remove(position);
                        } else {
                            Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                        dialog1.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            });

            dialog.show();
        });

        rv.setAdapter(cu);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewData();

    }
}
