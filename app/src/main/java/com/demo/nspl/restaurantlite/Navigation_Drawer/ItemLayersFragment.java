package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Adapter.LayerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import com.demo.nspl.restaurantlite.activity.AddInventoryLayerActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ItemLayersFragment extends Fragment {

    private FloatingActionButton fab;
    private TextView empty_title_text;
    private RecyclerView rv;
    LayerAdapter cu;
    List<ClsInventoryLayer> list;
    String _whereSearch= "";

    public ItemLayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Inventory Layer");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory_layer, container, false);

        fab = v.findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        rv = v.findViewById(R.id.rv);

        ClsGlobal.isFristFragment = true;
        empty_title_text = v.findViewById(R.id.empty_title_text);
        setHasOptionsMenu(true);

        fab.setOnClickListener(v1 -> {

            Intent intent = new Intent(getContext(), AddInventoryLayerActivity.class);
            startActivity(intent);

        });

        ViewData("");


        return v;
    }

    private void ViewData(String where) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        list = ClsInventoryLayer.getAllList(getContext(),  where + " AND [ACTIVE] = 'YES'");

        if (list.size() == 0) {
            empty_title_text.setVisibility(View.VISIBLE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(list);
        Log.e("jnIsonnString:-- ", jsonInString);

        cu = new LayerAdapter(getContext(), list);

        cu.SetOnClickListener((clsInventoryLayer, position) -> {

            final Dialog dialog = new Dialog(getContext());

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

            ll_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();

                    int currentObjId = clsInventoryLayer.getINVENTORYLAYER_ID();
                    Log.e("currentObjId", String.valueOf(currentObjId));
                    Intent intent = new Intent(getContext(), AddInventoryLayerActivity.class);
                    intent.putExtra("ID", currentObjId);
                    startActivity(intent);

                }
            });

            dialog.show();
            ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialog.cancel();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Confirm Delete...");
                    alertDialog.setMessage("Are you sure you want delete?");
                    alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int currentObj = clsInventoryLayer.getINVENTORYLAYER_ID();
                            Log.e("currentObj", String.valueOf(currentObj));

                            ClsInventoryLayer DeleteCurrent = new ClsInventoryLayer();
                            DeleteCurrent.setINVENTORYLAYER_ID(currentObj);

                            int getResult = ClsInventoryLayer.Delete(DeleteCurrent, getContext());

                            if (getResult == 1) {
                                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                cu.remove(position);
                            } else {
                                Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            dialog.cancel();


                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();

                }
            });


        });


        rv.setAdapter(cu);
        cu.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                ViewData("");
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("onQueryTextSubmit","onQueryTextSubmit call");

                if (!query.equals("")){

                    _whereSearch = " AND ([LAYER_NAME] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [SELECTED_LAYER_NAME] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR [DISPLAY_ORDER] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR [REMARK] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR [ACTIVE] LIKE '%".concat(query).concat("%')"));

                    Log.e("query",_whereSearch);
                    ViewData(_whereSearch);
                }else {
                    ViewData("");
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();

            ViewData("");
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


}
