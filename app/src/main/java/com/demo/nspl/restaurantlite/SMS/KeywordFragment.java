package com.demo.nspl.restaurantlite.SMS;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.initKeywordData;

public class KeywordFragment extends BottomSheetDialogFragment {

    RecyclerView rv;
    KeywordAdapter keywordAdapter;
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    //    List<ClsKeywordDescription> list = new ArrayList<>();
    PassDataBackListener listener;

    public KeywordFragment() {
        // Required empty public constructor
    }

    public void setPassDataBackListener(PassDataBackListener listener) {
        this.listener = listener;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(),
                R.layout.fragment_keyword, null);

        dialog.setContentView(view);

        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        app_bar_layout = view.findViewById(R.id.app_bar_layout);
        rv =  view.findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        keywordAdapter = new KeywordAdapter(getActivity(),initKeywordData());
        rv.setAdapter(keywordAdapter);

        keywordAdapter.SetOnClickListener((clsGetBackupDetailsList, position) ->{
            Log.e("Chech","point " + position);
            if (listener !=null){
                listener.passOnKeywordClick(clsGetBackupDetailsList,position);
            }

            dismiss();

        });

        view.findViewById(R.id.bt_close).setOnClickListener(v -> dismiss());
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    interface PassDataBackListener{
        void passOnKeywordClick(ClsKeywordDescription clsKeywordDescription, int position);
    }

}
