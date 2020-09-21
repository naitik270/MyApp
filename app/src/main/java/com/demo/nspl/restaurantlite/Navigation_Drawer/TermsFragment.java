package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.TermsAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnItemClickListenerTerms;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddTermsActivity;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TermsFragment extends Fragment {

    private TermsAdapter mCu;
    private RecyclerView mRv;
    private List<ClsTerms> mList;
    private TextView empty_title_text;
    private FloatingActionButton mFab;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "TermsFragment"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ClsGlobal.isFristFragment = true;
        getActivity().setTitle("Terms");
    }

    public TermsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_terms, container, false);

        ClsGlobal.isFristFragment = true;
        mRv = v.findViewById(R.id.rv);
        empty_title_text = v.findViewById(R.id.empty_title_text);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));

        ViewData();

        mFab = v.findViewById(R.id.fab);
        mFab.setColorFilter(Color.WHITE);
        mFab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddTermsActivity.class);
            startActivity(intent);
        });

        return v;
    }


    private void ViewData(){
        mList = new ArrayList<>();
        mList = new ClsTerms(getActivity()).getList("");
        if (mList.size() == 0){
            empty_title_text.setVisibility(View.VISIBLE);
        }else {
            empty_title_text.setVisibility(View.GONE);
        }

        mCu = new TermsAdapter(getContext(),mList);

        mCu.SetOnClickListenerTerms(new OnItemClickListenerTerms() {

            @Override
            public boolean OnClick(ClsTerms clsTerms,int position) {

                final Dialog dialog = new Dialog(getContext());
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

                    Intent intent = new Intent(getContext(), AddTermsActivity.class);
                    intent.putExtra("Terms_id",clsTerms.getmTerms_id());
                    startActivity(intent);

                    dialog.dismiss();
                    dialog.cancel();

                });

                dialog.show();
                ll_delete.setOnClickListener(v -> {
                    dialog.dismiss();
                    dialog.cancel();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Confirm...");
                    alertDialog.setMessage("Are you sure you want delete?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {

                            int DeletedRows = ClsTerms.Delete(clsTerms.getmTerms_id());

                            if (DeletedRows!=0){
                                // Update the List After Deleting Item.
                                mCu.UpdateList(position);
                                Toast.makeText(getContext(),"Deleted Successfully!",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(),"Error while Deleting!",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog1.dismiss();
                            dialog1.cancel();
                        }
                    });

                    alertDialog.show();

                });



                return false;
            }
        });


        mRv.setAdapter(mCu);
    }

    @Override
    public void onResume() {
        ViewData();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }
}
