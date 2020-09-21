package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.LayerItemAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.EndlessRecyclerViewScrollListener;
import com.demo.nspl.restaurantlite.MultipleImage.DisplayMultipleImgActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddItemActivity;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    private FloatingActionButton fab;
    private TextView empty_title_text;
    private RecyclerView rv;
    LayerItemAdapter cu;
    ProgressBar progress_bar;

    public static boolean onResumeCall = false;

    List<ClsLayerItemMaster> list = new ArrayList<>();

    String _whereSearch = "";

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layer_item, container, false);

        fab = view.findViewById(R.id.fab);
        rv = view.findViewById(R.id.rv);
        progress_bar = view.findViewById(R.id.progress_bar);

        setHasOptionsMenu(true);
        ClsGlobal.isFristFragment = true;
        empty_title_text = view.findViewById(R.id.empty_title_text);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddItemActivity.class);
            startActivity(intent);
        });

        ViewData();

        return view;
    }

    private void ViewData() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new LoadAsyncTask(++page, "").execute();

            }
        });


        new LoadAsyncTask(1, "").execute();


        cu = new LayerItemAdapter(getContext());

        cu.SetOnClickListener((clsLayerItemMaster, position) -> {

            final Dialog dialog = new Dialog(getContext());

            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);


            RelativeLayout lyout_update = dialog.findViewById(R.id.lyout_update);
            RelativeLayout lyout_delete = dialog.findViewById(R.id.lyout_delete);

            RelativeLayout layout_copy = dialog.findViewById(R.id.layout_copy);
            View view_copy = dialog.findViewById(R.id.view_copy);
            layout_copy.setVisibility(View.VISIBLE);
            view_copy.setVisibility(View.VISIBLE);

            RelativeLayout rl_add_img = dialog.findViewById(R.id.rl_add_img);
            View view_upload_image = dialog.findViewById(R.id.view_upload_image);

            rl_add_img.setVisibility(View.GONE);
            view_upload_image.setVisibility(View.GONE);

            dialog.getWindow().setAttributes(lp);


            layout_copy.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                int currentObjId = clsLayerItemMaster.getLAYERITEM_ID();
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                intent.putExtra("ID", currentObjId);
                intent.putExtra("Mode", "copyItem");
                startActivity(intent);

            });

            lyout_update.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                int currentObjId = clsLayerItemMaster.getLAYERITEM_ID();
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                intent.putExtra("ID", currentObjId);
                startActivity(intent);

            });
            dialog.show();

            lyout_delete.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", (dialog12, which) -> {

                    int currentObj = clsLayerItemMaster.getLAYERITEM_ID();
                    Log.e("currentObj", String.valueOf(currentObj));

                    ClsLayerItemMaster DeleteCurrent = new ClsLayerItemMaster();
                    DeleteCurrent.setLAYERITEM_ID(currentObj);

                    int getResult = ClsLayerItemMaster.Delete(DeleteCurrent, getContext());
                    int getTagsDeleteResult = ClsLayerItemMaster.DeleteTagsById(currentObj, getContext());
                    int getItemLayerResult = ClsLayerItemMaster.DeleteItemLayerById(currentObj, getContext());

                    Log.e("getTagsDeleteResult", String.valueOf(getTagsDeleteResult));
                    Log.e("getItemLayerResult", String.valueOf(getItemLayerResult));

                    if (getResult == 1) {
                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        cu.remove(position);
                    } else {
                        Toast.makeText(getContext(), "Error while Deleting", Toast.LENGTH_SHORT).show();
                    }

                });
                alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                    dialog1.dismiss();
                    dialog1.cancel();
                });
                // Showing Alert Message
                alertDialog.show();
            });

        });


        cu.setOnViewImg((objClsLayerItemMaster, position) -> {

            int currentObjId = objClsLayerItemMaster.getLAYERITEM_ID();
            Intent intent = new Intent(getContext(), DisplayMultipleImgActivity.class);
            intent.putExtra("ID", currentObjId);
            intent.putExtra("itemName", objClsLayerItemMaster.getITEM_NAME());
            intent.putExtra("itemCode", objClsLayerItemMaster.getITEM_CODE());
            intent.putExtra("_imgMode", "Item");
            intent.putExtra("imgSave", "AddImg");
            startActivity(intent);

        });

        rv.setAdapter(cu);
        cu.notifyDataSetChanged();


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
                list.clear();
                _whereSearch = "";
                ViewData();
                rv.setVisibility(View.VISIBLE);
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("--searchManager--", "onQueryTextSubmit: " + query);
                _whereSearch = "";

                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND [TLIM].[ITEM_NAME] LIKE '%".concat(query.trim()).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR [TLIM].[ITEM_CODE] LIKE '%".concat(query.trim()).concat("%'"));

                    new LoadAsyncTask(0, "Search").execute();
//                    ViewData();

                } else {
//                    ViewData();
//                    _whereSearch="";
                    rv.setVisibility(View.VISIBLE);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("--searchManager--", "onQueryTextChange: " + newText);
                return true;
            }
        });

        searchView.setQueryHint("Search by name or code");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            list.clear();
            _whereSearch = "";
            ViewData();
            rv.setVisibility(View.VISIBLE);
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (onResumeCall) {
            list.clear();
            ViewData();

            onResumeCall = false;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
        rv.clearOnScrollListeners();
    }

    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsLayerItemMaster>> {

        int currentPageNo;
        String mode = "";

        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);
        }

        LoadAsyncTask(int currentPageNo, String mode) {
            this.currentPageNo = currentPageNo;
            this.mode = mode;
        }


        @Override
        protected List<ClsLayerItemMaster> doInBackground(Void... voids) {
            String _paging = "";
            if (mode.equalsIgnoreCase("")) {
                int pageSize = 30;
                Log.e("paging", "Page no:- " + currentPageNo);
                int skip = pageSize * (currentPageNo - 1);
                Log.e("paging skip:-", String.valueOf(skip));

                _paging = "LIMIT ".concat("" + skip).concat(", ").concat("" + pageSize);

                Log.e("paging qurey:- ", _paging);
                Log.e("add", "listnew:-" + list.size());

            } else {
                list.clear();
            }


            List<ClsLayerItemMaster> listnew = new ArrayList<>();
//
//            listnew =

            if (ClsLayerItemMaster.getAllList(getContext(), _whereSearch, _paging).size() > 0) {
                listnew = ClsLayerItemMaster.getAllList(getContext(), _whereSearch, _paging);

                if (listnew.size() > 0) {

                    for (ClsLayerItemMaster current : listnew) {

                        ClsLayerItemMaster clsLayerItemMaster = new ClsLayerItemMaster();

                        clsLayerItemMaster.setLAYERITEM_ID(current.getLAYERITEM_ID());
                        clsLayerItemMaster.setITEM_NAME(current.getITEM_NAME());
                        clsLayerItemMaster.setITEM_CODE(current.getITEM_CODE());
                        clsLayerItemMaster.setMIN_STOCK(current.getMIN_STOCK());
                        clsLayerItemMaster.setMAX_STOCK(current.getMAX_STOCK());
                        clsLayerItemMaster.setUNIT_CODE(current.getUNIT_CODE());
                        clsLayerItemMaster.setHSN_SAC_CODE(current.getHSN_SAC_CODE());
                        clsLayerItemMaster.setSLAB_NAME(current.getSLAB_NAME());
                        clsLayerItemMaster.setTAX_SLAB_ID(current.getTAX_SLAB_ID());
                        clsLayerItemMaster.setTAX_APPLY(current.getTAX_APPLY());
                        clsLayerItemMaster.setRATE_PER_UNIT(current.getRATE_PER_UNIT());
                        clsLayerItemMaster.setOpening_Stock(current.getOpening_Stock());
                        clsLayerItemMaster.setSGST(current.getSGST());
                        clsLayerItemMaster.setCGST(current.getCGST());
                        clsLayerItemMaster.setIGST(current.getIGST());
                        clsLayerItemMaster.setWHOLESALE_RATE(current.getWHOLESALE_RATE());
                        clsLayerItemMaster.setTAX_TYPE(current.getTAX_TYPE());
                        clsLayerItemMaster.setIN(current.getIN());
                        clsLayerItemMaster.setOUT(current.getOUT());
                        clsLayerItemMaster.setTagList(current.getTagList());
                        clsLayerItemMaster.set_saleRateIncludingTax(current.get_saleRateIncludingTax());
                        clsLayerItemMaster.set_wholesaleRateIncludingTax(current.get_wholesaleRateIncludingTax());
                        clsLayerItemMaster.setAverageSaleRate(current.getAverageSaleRate());
                        clsLayerItemMaster.setREMARK(current.getREMARK());
                        clsLayerItemMaster.setACTIVE(current.getACTIVE());
                        clsLayerItemMaster.setDISPLAY_ORDER(current.getDISPLAY_ORDER());

                        list.add(clsLayerItemMaster);

                    }

                }


            }

            Log.e("list_Item", " list.addAll call " + list.size());


            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);
            Log.e("Result", "list---" + jsonInString);
            _paging = "";

            return list;
        }


        @Override
        protected void onPostExecute(List<ClsLayerItemMaster> list) {
            super.onPostExecute(list);
         /*   Log.e("list_Item", "list_Item call " + list.size());

            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);
            Log.e("Result", "list---" + jsonInString);
*/

            progress_bar.setVisibility(View.GONE);
            cu.AddItems(list);


            if (list.size() == 0) {
                empty_title_text.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.INVISIBLE);
            }


        }
    }

}
