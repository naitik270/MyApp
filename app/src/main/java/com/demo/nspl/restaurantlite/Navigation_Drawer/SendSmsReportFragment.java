package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.MultipleSelectionGroupAdapter;
import com.demo.nspl.restaurantlite.Adapter.MultipleSelectionSenderIDAdapter;
import com.demo.nspl.restaurantlite.Adapter.MultipleSelectionTitleAdapter;
import com.demo.nspl.restaurantlite.Adapter.SendSmsReportAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.EndlessRecyclerViewScrollListener;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.SendSmsActivity;
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;


public class SendSmsReportFragment extends Fragment {

    protected static final String TAG = SendSmsReportFragment.class.getSimpleName();
    FloatingActionButton fab, fab_filter;
    RecyclerView rv;
    SendSmsReportAdapter adapter;
    SQLiteDatabase db;
    LinearLayout progress_bar_layout;


    public SendSmsReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Bulk SMS Report");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_sms, container, false);
        // Inflate the layout for this fragment

        ClsGlobal.isFristFragment = true;

        rv = view.findViewById(R.id.rv);
        fab = view.findViewById(R.id.fab);
        progress_bar_layout = view.findViewById(R.id.progress_bar_layout);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        fab_filter = view.findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);
        fab_filter.setVisibility(View.VISIBLE);

        adapter = new SendSmsReportAdapter(getActivity());
        rv.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SendSmsActivity.class);
            startActivity(intent);


            /*ClsSmsCustomerGroup currentSmsCustomerGroup = new ClsSmsCustomerGroup();
//            currentSmsCustomerGroup = ClsSmsCustomerGroup.getQryByID(getActivity());

            currentSmsCustomerGroup.setGroupId(9);
            currentSmsCustomerGroup.setGroupName(currentSmsCustomerGroup.getGroupName());
            currentSmsCustomerGroup.setActive("YES");
            currentSmsCustomerGroup.setEntryDate(ClsGlobal.getEntryDate());
            currentSmsCustomerGroup.setTotalCount(currentSmsCustomerGroup.getTotalCount());
            currentSmsCustomerGroup.setRemark("");

            ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
//            clsMessageFormat = ClsMessageFormat.getQryByID(getActivity());

            clsMessageFormat.setId(1);
            clsMessageFormat.setTitle("hsy");
            clsMessageFormat.setMessage_format("MSG_Format");
            clsMessageFormat.setType("offer");
            clsMessageFormat.setDefault("no");
            clsMessageFormat.setRemark("");

            Intent intent = new Intent(getActivity(), SendSmsPreviewActivity.class);
            intent.putExtra("Group Name", currentSmsCustomerGroup);
//                intent.putExtra("Sender Id", currentSmsIdSetting);
            intent.putExtra("Sender Id", ClsGlobal.DefaultSenderId);
            intent.putExtra("Sms title", clsMessageFormat);
            intent.putExtra("Sms Format", "hello Bro ##Customer Name##");
            intent.putExtra("Bulk Sms Title", "India_".concat(ClsGlobal.getRandom()));
            intent.putExtra("Sms Type", "Transactional");
            startActivity(intent);
*/

        });

        adapter.SetOnClickListener((obj, position) -> {
            Intent intent = new Intent(getActivity(), SmsLogsActivity.class);
            intent.putExtra("ClsSMSBulkMaster", obj);
            startActivity(intent);


            Gson gson = new Gson();
            String jsonInString = gson.toJson(obj);
            Log.e("--lstAsynctask--", "Request: " + jsonInString);

            Log.e("--lstAsynctask--", "Successfully: " + obj.getGroupName());


            Log.e("--lstAsynctask--", "Successfully: " + obj.getGroupName());


        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ViewData(++page);
            }
        });

        fillTitleList();
        fillGroupList();
        fillSenderIDList();

        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilters();
            }
        });

        return view;
    }

    TextView txt_select_title, txt_select_group, txt_select_senderId;
    TextView txt_sms_from, txt_sms_to;
    ImageView iv_clear_title, iv_clear_group, iv_clear_senderId, clear_date;
    String _titleName = "";
    String _GroupName = "";
    String _senderId = "";
    String _fromDate = "", _toDate = "";
    String _where = "";


    void applyFilters() {
        Dialog dialogFilter = new Dialog(getActivity());
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setContentView(R.layout.dialog_bulk_sms_filter);
        Objects.requireNonNull(dialogFilter.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogFilter.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogFilter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button btn_search = dialogFilter.findViewById(R.id.btn_search);
        Button btn_clear_all = dialogFilter.findViewById(R.id.btn_clear_all);
        ImageButton bt_close = dialogFilter.findViewById(R.id.bt_close);

        txt_select_title = dialogFilter.findViewById(R.id.txt_select_title);
        iv_clear_title = dialogFilter.findViewById(R.id.iv_clear_title);

        txt_select_group = dialogFilter.findViewById(R.id.txt_select_group);
        iv_clear_group = dialogFilter.findViewById(R.id.iv_clear_group);

        txt_sms_from = dialogFilter.findViewById(R.id.txt_sms_from);
        txt_sms_to = dialogFilter.findViewById(R.id.txt_sms_to);
        clear_date = dialogFilter.findViewById(R.id.clear_date);

        txt_select_senderId = dialogFilter.findViewById(R.id.txt_select_senderId);
        iv_clear_senderId = dialogFilter.findViewById(R.id.iv_clear_senderId);

        bt_close.setOnClickListener(v -> dialogFilter.dismiss());

        fromDate();
        toDate();

        clear_date.setOnClickListener(view -> {
            txt_sms_from.setText("");
            txt_sms_to.setText("");
            _fromDate = "";
            _toDate = "";

        });
        txt_select_title.setText(TextUtils.join(",", selectedTitlelst).toUpperCase().replace("'", ""));
        txt_select_title.setOnClickListener(view -> {
            selectionDialog("Title");
        });

        iv_clear_title.setOnClickListener(view -> {
            checkUncheckTitle(false);
            txt_select_title.setText("");
            selectedTitlelst.clear();
            _titleName = "";
        });

        txt_select_group.setText(TextUtils.join(",", selectedGrouplst).toUpperCase().replace("'", ""));
        txt_select_group.setOnClickListener(view -> selectionDialog("Group"));

        iv_clear_group.setOnClickListener(view -> {
            checkUncheckGroup(false);
            txt_select_group.setText("");
            selectedGrouplst.clear();
            _GroupName = "";
        });

        txt_select_senderId.setText(TextUtils.join(",", selectedSenderID).toUpperCase().replace("'", ""));
        txt_select_senderId.setOnClickListener(view -> selectionDialog("SenderID"));

        iv_clear_senderId.setOnClickListener(view -> {
            checkUncheckSenderID(false);
            txt_select_senderId.setText("");
            selectedSenderID.clear();
            _senderId = "";
        });

        btn_search.setOnClickListener(view -> {

            _where = "";
            filtersCondition();

            ViewData(0);

            dialogFilter.dismiss();
            dialogFilter.hide();

        });

        btn_clear_all.setOnClickListener(view -> {

            _where = "";

//Date selection is Clear & reset.
            txt_sms_from.setText("");
            txt_sms_to.setText("");
            _fromDate = "";
            _toDate = "";

//Title Selection is Clear & reset.
            txt_select_title.setText("");
            _titleName = "";
            selectedTitlelst.clear();


//Group Selection is Clear & reset.
            txt_select_group.setText("");
            _GroupName = "";
            selectedGrouplst.clear();

//SenderID Selection is Clear & reset.
            txt_select_senderId.setText("");
            _senderId = "";
            selectedSenderID.clear();
        });

        dialogFilter.show();
        dialogFilter.getWindow().setAttributes(lp);
    }

    private void filtersCondition() {


        if (!_fromDate.equalsIgnoreCase("") && !_toDate.equalsIgnoreCase("")) {
            _where = _where.concat(" AND DATE([SendDate]) between "
                    .concat("('".concat(ClsGlobal.getDbDateFormat(_fromDate)).concat("')"))
                    .concat(" AND ")
                    .concat("('".concat(ClsGlobal.getDbDateFormat(_toDate)).concat("')")));
        } else if (!_fromDate.equalsIgnoreCase("")) {
            _where = _where.concat(" AND DATE([SendDate]) = ".concat("('"
                    .concat(ClsGlobal.getDbDateFormat(_fromDate)).concat("')")));
        } else if (!_toDate.equalsIgnoreCase("")) {
            _where = _where.concat(" AND DATE([SendDate]) = ".concat("('"
                    .concat(ClsGlobal.getDbDateFormat(_toDate)).concat("')")));
        }

        if (!_titleName.equalsIgnoreCase("")) {
            _where = _where.concat(" AND [Title] IN ")
                    .concat("(")
                    .concat(_titleName)
                    .concat(")");
        }

        if (!_GroupName.equalsIgnoreCase("")) {
            _where = _where.concat(" AND [GroupName] IN ")
                    .concat("(")
                    .concat(_GroupName)
                    .concat(")");
        }

        if (!_senderId.equalsIgnoreCase("")) {
            _where = _where.concat(" AND [SenderID] IN ")
                    .concat("(")
                    .concat(_senderId)
                    .concat(")");
        }

        Log.d("--SmsBulk--", "where: " + _where);
    }

    int mYear, mMonth, mDay;

    private void fromDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        txt_sms_from.setText(ClsGlobal.getChangeDateFormat(_fromDate));


        txt_sms_from.setOnClickListener(v -> {
            @SuppressLint("SimpleDateFormat") DatePickerDialog dpd = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                    (view, year, month, day) -> {
                        c.set(year, month, day);

                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

                        _fromDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

                        txt_sms_from.setText(date);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            Calendar d = Calendar.getInstance();
            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

    }

    private void toDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        txt_sms_to.setText(ClsGlobal.getChangeDateFormat(_toDate));

        txt_sms_to.setOnClickListener(v -> {
            @SuppressLint("SimpleDateFormat") DatePickerDialog dpd = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                    (view, year, month, day) -> {
                        c.set(year, month, day);
                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());


                        _toDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        txt_sms_to.setText(date);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            Calendar d = Calendar.getInstance();
            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

    }


    ArrayList<String> selectedTitlelst = new ArrayList<>();
    ArrayList<String> selectedGrouplst = new ArrayList<>();
    ArrayList<String> selectedSenderID = new ArrayList<>();
    RecyclerView rv_select_title;
    LinearLayout ll_bottom;
    ProgressBar progress_bar;
    EditText edit_search;
    MultipleSelectionTitleAdapter adapterTitle = new MultipleSelectionTitleAdapter(getActivity());
    MultipleSelectionGroupAdapter adapterGroup = new MultipleSelectionGroupAdapter(getActivity());
    MultipleSelectionSenderIDAdapter adapterSenderID = new MultipleSelectionSenderIDAdapter(getActivity());
    boolean flag;

    private void selectionDialog(String _mode) {

        final Dialog dialogMultipleSelect = new Dialog(getActivity());
        dialogMultipleSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMultipleSelect.setContentView(R.layout.dialog_multiple_selection_sms_cust);
        dialogMultipleSelect.setTitle("Select Title");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogMultipleSelect.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogMultipleSelect.getWindow().setAttributes(lp);

        LinearLayout ll_search = dialogMultipleSelect.findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        rv_select_title = dialogMultipleSelect.findViewById(R.id.rv_select_customer);
        rv_select_title.setLayoutManager(new LinearLayoutManager(getActivity()));
        ll_bottom = dialogMultipleSelect.findViewById(R.id.ll_bottom);

        Button btn_select_item = dialogMultipleSelect.findViewById(R.id.btn_select_item);
        Button btn_clear_list = dialogMultipleSelect.findViewById(R.id.btn_clear_list);

        progress_bar = dialogMultipleSelect.findViewById(R.id.progress_bar);
        edit_search = dialogMultipleSelect.findViewById(R.id.edit_search);

        ImageView img_clear = dialogMultipleSelect.findViewById(R.id.img_clear);

        img_clear.setOnClickListener(v -> edit_search.setText(""));

        dialogMultipleSelect.setCanceledOnTouchOutside(true);
        dialogMultipleSelect.setCancelable(true);

        if (_mode != null && _mode.equalsIgnoreCase("Title")) {

            if (lstTitle != null && lstTitle.size() != 0) {

                adapterTitle = new MultipleSelectionTitleAdapter(getActivity());
                adapterTitle.AddItems(lstTitle);//FILL ADP
                adapterTitle.OnCharacterClick((clsCustomerMaster, position, holder) -> {

                    flag = !holder.txt_label.isChecked();
                    holder.txt_label.setChecked(flag);

                    if (holder.txt_label.isChecked()) {

                        holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                        holder.linear_layout.setBackgroundResource(0);
                    }

                    //UPDATE CHECKED LIST
                    for (ClsSMSBulkMaster obj : lstTitle) {
                        if (obj.getTitle().equalsIgnoreCase(clsCustomerMaster.getTitle())) {
                            obj.setSelected(flag);
                            lstTitle.indexOf(obj);
                            break;
                        }
                    }
                });

                rv_select_title.setAdapter(adapterTitle);

                dialogMultipleSelect.show();
            } else {
                Toast.makeText(getActivity(), "NO RECORD FOUND", Toast.LENGTH_SHORT).show();
            }

            edit_search.setOnEditorActionListener((s, actionId, event) -> {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ClsGlobal.hideKeyboard(getActivity());
                    return true;
                }
                return true;
            });


            edit_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String _filterTxt = "";

                    if (s != null && s.length() != 0)
                        _filterTxt = s.toString();

                    filterMain(_filterTxt, lstTitle);
                }
            });


            btn_select_item.setOnClickListener(v -> {

                selectedTitlelst = new ArrayList<>();
                for (ClsSMSBulkMaster obj : lstTitle) {

                    if (obj.isSelected()) {
                        selectedTitlelst.add("'".concat(obj.getTitle()).concat("'"));
                    }
                }

                txt_select_title.setText(TextUtils.join(",", selectedTitlelst).toUpperCase().replace("'", ""));
                _titleName = TextUtils.join(",", selectedTitlelst);

                dialogMultipleSelect.dismiss();
            });

            btn_clear_list.setOnClickListener(v -> {

                checkUncheckTitle(false);
                edit_search.setText("");
                txt_select_title.setText("");

            });

        } else if (_mode != null && _mode.equalsIgnoreCase("Group")) {

            if (lstGroup != null && lstGroup.size() != 0) {

                adapterGroup = new MultipleSelectionGroupAdapter(getActivity());
                adapterGroup.AddItems(lstGroup);//FILL ADP
                adapterGroup.OnCharacterClick((clsCustomerMaster, position, holder) -> {

                    flag = !holder.txt_label.isChecked();
                    holder.txt_label.setChecked(flag);

                    if (holder.txt_label.isChecked()) {

                        holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                        holder.linear_layout.setBackgroundResource(0);
                    }

                    //UPDATE CHECKED LIST
                    for (ClsSMSBulkMaster obj : lstGroup) {
                        if (obj.getGroupName().equalsIgnoreCase(clsCustomerMaster.getGroupName())) {
                            obj.setSelected(flag);
                            lstGroup.indexOf(obj);
                            break;
                        }
                    }
                });

                rv_select_title.setAdapter(adapterGroup);

                dialogMultipleSelect.show();
            } else {
                Toast.makeText(getActivity(), "NO RECORD FOUND", Toast.LENGTH_SHORT).show();
            }


            edit_search.setOnEditorActionListener((s, actionId, event) -> {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ClsGlobal.hideKeyboard(getActivity());
                    return true;
                }
                return true;
            });


            edit_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String _filterTxt = "";

                    if (s != null && s.length() != 0)
                        _filterTxt = s.toString();

                    filterGroup(_filterTxt, lstGroup);
                }
            });

            btn_select_item.setOnClickListener(v -> {

                selectedGrouplst = new ArrayList<>();
                for (ClsSMSBulkMaster obj : lstGroup) {

                    if (obj.isSelected()) {
                        selectedGrouplst.add("'".concat(obj.getGroupName()).concat("'"));
                    }
                }

                txt_select_group.setText(TextUtils.join(",", selectedGrouplst).toUpperCase().replace("'", ""));
                _GroupName = TextUtils.join(",", selectedGrouplst);

                dialogMultipleSelect.dismiss();
            });

            btn_clear_list.setOnClickListener(v -> {

                checkUncheckGroup(false);
                edit_search.setText("");
                txt_select_group.setText("");

            });


        } else if (_mode != null && _mode.equalsIgnoreCase("SenderID")) {

            if (lstSenderID != null && lstSenderID.size() != 0) {

                adapterSenderID = new MultipleSelectionSenderIDAdapter(getActivity());
                adapterSenderID.AddItems(lstSenderID);//FILL ADP
                adapterSenderID.OnCharacterClick((clsCustomerMaster, position, holder) -> {

                    flag = !holder.txt_label.isChecked();
                    holder.txt_label.setChecked(flag);

                    if (holder.txt_label.isChecked()) {

                        holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        holder.ic_Check.setColorFilter(ContextCompat.getColor(getActivity(),
                                R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                        holder.linear_layout.setBackgroundResource(0);
                    }

                    //UPDATE CHECKED LIST
                    for (ClsSMSBulkMaster obj : lstSenderID) {
                        if (obj.getSenderID().equalsIgnoreCase(clsCustomerMaster.getSenderID())) {
                            obj.setSelected(flag);
                            lstSenderID.indexOf(obj);
                            break;
                        }
                    }
                });

                rv_select_title.setAdapter(adapterSenderID);

                dialogMultipleSelect.show();
            } else {
                Toast.makeText(getActivity(), "NO RECORD FOUND", Toast.LENGTH_SHORT).show();
            }


            edit_search.setOnEditorActionListener((s, actionId, event) -> {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ClsGlobal.hideKeyboard(getActivity());
                    return true;
                }
                return true;
            });


            edit_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String _filterTxt = "";

                    if (s != null && s.length() != 0)
                        _filterTxt = s.toString();

                    filterSenderID(_filterTxt, lstSenderID);
                }
            });

            btn_select_item.setOnClickListener(v -> {

                selectedSenderID = new ArrayList<>();
                for (ClsSMSBulkMaster obj : lstSenderID) {

                    if (obj.isSelected()) {
                        selectedSenderID.add("'".concat(obj.getSenderID()).concat("'"));
                    }
                }

                txt_select_senderId.setText(TextUtils.join(",", selectedSenderID).toUpperCase().replace("'", ""));
                _senderId = TextUtils.join(",", selectedSenderID);

                dialogMultipleSelect.dismiss();
            });

            btn_clear_list.setOnClickListener(v -> {

                checkUncheckSenderID(false);
                edit_search.setText("");
                txt_select_senderId.setText("");

            });
        }
    }

    List<ClsSMSBulkMaster> filterTitle = new ArrayList();

    private void filterMain(String text, List<ClsSMSBulkMaster> lst) {

        filterTitle = StreamSupport.stream(lst)
                .filter(str -> str.getTitle().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());


        if (text.isEmpty()) {
            adapterTitle.AddItems(lstTitle);
        }

        //update recyclerview
        if (filterTitle.size() != 0) {
            ll_bottom.setVisibility(View.GONE);
            rv_select_title.setVisibility(View.VISIBLE);
            adapterTitle.AddItems(filterTitle);

        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            rv_select_title.setVisibility(View.GONE);
        }

    }

    List<ClsSMSBulkMaster> lstFilterGroup = new ArrayList();

    private void filterGroup(String text, List<ClsSMSBulkMaster> lst) {

        lstFilterGroup = StreamSupport.stream(lst)
                .filter(str -> str.getGroupName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        if (text.isEmpty()) {
            adapterGroup.AddItems(lstTitle);
        }

        //update recyclerview
        if (lstFilterGroup.size() != 0) {
            ll_bottom.setVisibility(View.GONE);
            rv_select_title.setVisibility(View.VISIBLE);
            adapterGroup.AddItems(lstFilterGroup);

        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            rv_select_title.setVisibility(View.GONE);
        }

    }

    List<ClsSMSBulkMaster> lstFilterSenderID = new ArrayList();

    private void filterSenderID(String text, List<ClsSMSBulkMaster> lst) {

        lstFilterSenderID = StreamSupport.stream(lst)
                .filter(str -> str.getSenderID().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        if (text.isEmpty()) {
            adapterSenderID.AddItems(lstSenderID);
        }

        //update recyclerview
        if (lstFilterSenderID.size() != 0) {
            ll_bottom.setVisibility(View.GONE);
            rv_select_title.setVisibility(View.VISIBLE);
            adapterSenderID.AddItems(lstFilterSenderID);

        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            rv_select_title.setVisibility(View.GONE);
        }

    }

    private List<ClsSMSBulkMaster> lstTitle;

    @SuppressLint("WrongConstant")
    private void fillTitleList() {

        db = Objects.requireNonNull(getActivity()).openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstTitle = new ArrayList<>();
        lstTitle = new ClsSMSBulkMaster().getSmsTitleList(db);
        db.close();
    }

    private List<ClsSMSBulkMaster> lstGroup;

    @SuppressLint("WrongConstant")
    private void fillGroupList() {

        db = Objects.requireNonNull(getActivity()).openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstGroup = new ArrayList<>();
        lstGroup = new ClsSMSBulkMaster().getSmsGroupList(db);
        db.close();
    }

    private List<ClsSMSBulkMaster> lstSenderID;

    @SuppressLint("WrongConstant")
    private void fillSenderIDList() {

        db = Objects.requireNonNull(getActivity()).openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstSenderID = new ArrayList<>();
        lstSenderID = new ClsSMSBulkMaster().getSmsSenderIDList(db);
        db.close();
    }

    private void checkUncheckSenderID(boolean check_uncheck) {
        for (ClsSMSBulkMaster Obj : lstSenderID) {
            Obj.setSelected(check_uncheck);
            lstSenderID.set(lstSenderID.indexOf(Obj), Obj);
        }
        adapterSenderID.notifyDataSetChanged();
    }

    private void checkUncheckGroup(boolean check_uncheck) {
        for (ClsSMSBulkMaster Obj : lstGroup) {
            Obj.setSelected(check_uncheck);
            lstGroup.set(lstGroup.indexOf(Obj), Obj);
        }
        adapterGroup.notifyDataSetChanged();
    }

    private void checkUncheckTitle(boolean check_uncheck) {
        for (ClsSMSBulkMaster Obj : lstTitle) {
            Obj.setSelected(check_uncheck);
            lstTitle.set(lstTitle.indexOf(Obj), Obj);
        }
        adapterTitle.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    private void ViewData(int currentPageNo) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<ClsSMSBulkMaster>> asyncTask =
                new AsyncTask<Void, Void, List<ClsSMSBulkMaster>>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progress_bar_layout.setVisibility(View.VISIBLE);
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    protected List<ClsSMSBulkMaster> doInBackground(Void... voids) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        db = getActivity().openOrCreateDatabase(ClsGlobal.Database_Name,
                                Context.MODE_APPEND, null);
                        String _paging = "";

                        int pageSize = 30;
                        Log.e("paging", "Page no:- " + currentPageNo);
                        int skip = pageSize * (currentPageNo - 1);

                        _paging = "LIMIT ".concat("" + skip).concat(", ").concat("" + pageSize);


                        return ClsSMSBulkMaster.getList("", _paging,getActivity(), db);
                    }

                    @Override
                    protected void onPostExecute(List<ClsSMSBulkMaster> list) {
                        super.onPostExecute(list);
                        progress_bar_layout.setVisibility(View.GONE);
                        if (list.size() > 0) {
                            adapter.AddItems(list);
                        }
                        db.close();
                    }
                };
        asyncTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewData(0);
    }
}
