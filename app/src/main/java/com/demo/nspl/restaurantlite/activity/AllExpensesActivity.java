package com.demo.nspl.restaurantlite.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.demo.nspl.restaurantlite.Adapter.DisplayAllExpensesAdapterNew;
import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

//import com.lowagie.text.Document;
//import com.lowagie.text.Element;
//import com.lowagie.text.FontFactory;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;


public class AllExpensesActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt_nodata;
    List<ClsExpenseMasterNew> lstClsExpenseMasterNews;
    List<ClsVendor> lstClsVendorMasters;
    List<ClsVendor> listVendorSearch;
    List<ClsExpenseType> lstClsExpenseTypeMasters;
    private AdapterExpenseType expAdapter;
    StickyListHeadersListView list;
    int mYear, mMonth, mDay;
    Calendar c;
    private CustomAdapter adapter;
    Dialog dialog;
    String _FromDate = "";
    private List<ClsExpenseType> listExpenseTypeSearch;
    TextView expensetype_id;
    List<ClsExpenseType> list_expensetype;
    String _ToDate = "";
    private Dialog dialogvendor;
    private List<ClsVendor> list_vendor;
    private Dialog dialogExpenseType;
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expenses);

        ClsGlobal.isFristFragment = true;
        Log.e("onCreate", "onCreate call");
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("All Expenses");
            setSupportActionBar(toolbar);
        }

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AllExpensesActivity"));
        }

        ClsPermission.checkpermission(AllExpensesActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();

    }

    private void main() {
        Log.e("main", "main call");
        txt_nodata = findViewById(R.id.txt_nodata);
        list = findViewById(R.id.list);


//        rv_all_exp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ViewAllExpenses("");

        DialogFilters();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, AllExpensesActivity.this);
                return;
            }

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        ViewAllExpenses("");

    }


    private void ViewAllExpenses(String _w) {

        String _where = _w;
        Log.e("ViewAllExpenses", "ViewAllExpenses call");

        lstClsExpenseMasterNews = new ArrayList<>();
        lstClsExpenseMasterNews = new ClsExpenseMasterNew(getApplicationContext()).getListAllExp(_where);
        Log.e("HeaderView", "ViewAllExpenses: " + lstClsExpenseMasterNews.size());


//        for (ClsExpenseMasterNew obj : lstClsExpenseMasterNews) {
//            Log.e("HeaderView", "Expense Type Name: " + obj.getExpense_type_name());
//        }
//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(lstClsExpenseMasterNews);
//        Log.e("DisplayBarActivity:-- ", jsonInString);

        if (lstClsExpenseMasterNews != null && lstClsExpenseMasterNews.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            /*set month unique index*/
            //steps
            //unique month list
            List<String> uniqueList = new ArrayList<>();
            for (ClsExpenseMasterNew _ObjExp : lstClsExpenseMasterNews) {
                if (!uniqueList.contains(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()))) {
                    uniqueList.add(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()));
                }
            }

            int srNO = 0;
            for (String monthName : uniqueList) {
                if (srNO > 62) {
                    srNO = 0;
                }
                String uniqueID = ClsGlobal.getMonthIndex(srNO);  //a,b
                Double finalAmount = 0.0;
                //String uniqueID = ClsGlobalDatabase.getMonthIndex(uniqueList.indexOf(monthName));//a,b
                for (ClsExpenseMasterNew _ObjExp : lstClsExpenseMasterNews) {
                    if (monthName.equalsIgnoreCase(ClsGlobal.getMonthYear(_ObjExp.getReceipt_date()))) {
                        //assign unique index/id/position
                        //  finalAmount = finalAmount + _ObjExp.getGRAND_TOTAL();
                        _ObjExp.setMonthUniqueIndex(uniqueID);
                        //_ObjExp.set_finalAmount(finalAmount);
                        //set in list
                        lstClsExpenseMasterNews.set(lstClsExpenseMasterNews.indexOf(_ObjExp), _ObjExp);
                    }
                }
                srNO++;
            }
            DisplayAllExpensesAdapterNew displayAllExpensesAdapterNew = new DisplayAllExpensesAdapterNew(lstClsExpenseMasterNews, AllExpensesActivity.this);
            list.setAdapter(displayAllExpensesAdapterNew);
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
    }


    private void DialogFilters() {
        dialog = new Dialog(AllExpensesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_vendorbill_details);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        final TextView from_date = dialog.findViewById(R.id.from_date);
        final TextView to_date = dialog.findViewById(R.id.to_date);
//        final ImageButton clear_date = dialog.findViewById(R.id.clear_date);

        final TextView vendor_id = dialog.findViewById(R.id.vendor_id);
        final TextView txt_vendor = dialog.findViewById(R.id.txt_vendor);
        final TextView selected_vendor = dialog.findViewById(R.id.selected_vendor);
        final TextView txt_expensetype = dialog.findViewById(R.id.txt_expensetype);
        final ImageButton cancel_vendor = dialog.findViewById(R.id.cancel_vendor);

        final TextView expensetype_id = dialog.findViewById(R.id.expensetype_id);
        final TextView selected_expensetype = dialog.findViewById(R.id.selected_expensetype);
        final ImageButton cancel_expensetype = dialog.findViewById(R.id.cancel_expensetype);

        final EditText edt_rec_no = dialog.findViewById(R.id.edt_rec_no);
        final ImageButton rec_clear = dialog.findViewById(R.id.rec_clear);

        final Button btn_search = dialog.findViewById(R.id.btn_search);
        final Button btn_clear = dialog.findViewById(R.id.btn_clear);


        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(AllExpensesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                from_date.setText(date);

                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                Calendar d = Calendar.getInstance();

                dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_date.setText("");
                from_date.setText("");
                selected_vendor.setText("");
                vendor_id.setText("");
                selected_expensetype.setText("");
                expensetype_id.setText("");
                edt_rec_no.setText("");


                Log.e("Clear", "Date" + to_date.getText().toString());
                Log.e("Clear", "FromDate" + from_date.getText().toString());
                Log.e("Clear", "SelectedVendor" + selected_vendor.getText().toString());

            }
        });

        txt_expensetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogExpenseType = new Dialog(AllExpensesActivity.this);
                dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogExpenseType.setContentView(R.layout.dialog_multiple_selection);
                dialogExpenseType.setTitle("Select Vendor");


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogExpenseType.getWindow().setAttributes(lp);


                ListView listView = dialogExpenseType.findViewById(R.id.listview);
                Button btn_Select = dialogExpenseType.findViewById(R.id.button);
                dialogExpenseType.setCanceledOnTouchOutside(true);
                dialogExpenseType.setCancelable(true);
                list_expensetype = new ArrayList<>();
                listExpenseTypeSearch = list_expensetype = new ClsExpenseType(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [EXPENSE_TYPE_NAME]");
                expAdapter = new AdapterExpenseType(AllExpensesActivity.this, (ArrayList<ClsExpenseType>) list_expensetype);
                listView.setAdapter(expAdapter);

                btn_Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected[] = expAdapter.getSelectedFlags();
                        ArrayList<String> selectedItems = new ArrayList<>();
                        ArrayList<String> ExpenseTypeId = new ArrayList<>();

                        for (int i = 0; i < isSelected.length; i++) {
                            if (isSelected[i]) {
                                selectedItems.add(list_expensetype.get(i).getExpense_type_name());
                                ExpenseTypeId.add(String.valueOf(list_expensetype.get(i).getExpense_type_id()));
                            }
                        }

                        selected_expensetype.setText(TextUtils.join(",", selectedItems));
                        expensetype_id.setText(TextUtils.join(",", ExpenseTypeId));

                        Toast.makeText(AllExpensesActivity.this, "Selected: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        dialogExpenseType.dismiss();
                    }
                });

                dialog.show();
            }

        });


        cancel_expensetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_expensetype.setText("");
                expensetype_id.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vendor = vendor_id.getText().toString();
                String expenseType = expensetype_id.getText().toString();
                String RecNo = edt_rec_no.getText().toString();
                String fromdate = _FromDate;
                String todate = _ToDate;

                String _where = "";

                if (!fromdate.isEmpty() && !todate.isEmpty()) {
                    _where = _where.concat(" AND em.[RECEIPT_DATE] between "
                            .concat("('".concat(fromdate).concat("')"))
                            .concat(" AND ")
                            .concat("('".concat(todate).concat("')")));
                } else if (!fromdate.isEmpty()) {
                    _where = _where.concat(" AND em.[RECEIPT_DATE] = ".concat("('".concat(fromdate).concat("')")));
                } else if (!todate.isEmpty()) {
                    _where = _where.concat(" AND em.[RECEIPT_DATE] = ".concat("('".concat(todate).concat("')")));
                }

                if (!vendor.isEmpty()) {
                    _where = _where.concat(" AND em.[VENDOR_ID] IN ")
                            .concat("(")
                            .concat(vendor.trim())
                            .concat(")");
                }

                if (!expenseType.isEmpty()) {
                    _where = _where.concat(" AND em.[EXPENSE_TYPE_ID] IN ")
                            .concat("(")
                            .concat(expenseType.trim())
                            .concat(")");
                }

                if (!RecNo.isEmpty()) {
                    _where = _where.concat(" AND em.[RECEIPT_NO] LIKE ")
                            .concat("'%")
                            .concat(RecNo.trim())
                            .concat("%'");
                }

                ViewAllExpenses(_where);
                dialog.dismiss();
                dialog.hide();

            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(AllExpensesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                to_date.setText(date);

                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                Calendar d = Calendar.getInstance();

                dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        txt_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogvendor = new Dialog(AllExpensesActivity.this);
                dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogvendor.setContentView(R.layout.dialog_multiple_selection);
                dialogvendor.setTitle("Select Vendor");

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogvendor.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogvendor.getWindow().setAttributes(lp);


                ListView listView = dialogvendor.findViewById(R.id.listview);
                Button btn_Select = dialogvendor.findViewById(R.id.button);
                dialogvendor.setCanceledOnTouchOutside(true);
                dialogvendor.setCancelable(true);
                list_vendor = new ArrayList<>();
                listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [VENDOR_NAME]");
                adapter = new CustomAdapter(AllExpensesActivity.this, (ArrayList<ClsVendor>) list_vendor);
                listView.setAdapter(adapter);

                btn_Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected[] = adapter.getSelectedFlags();
                        ArrayList<String> selectedItems = new ArrayList<>();
                        ArrayList<String> vendorId = new ArrayList<>();

                        for (int i = 0; i < isSelected.length; i++) {
                            if (isSelected[i]) {
                                selectedItems.add(list_vendor.get(i).getVendor_name());
                                vendorId.add(String.valueOf(list_vendor.get(i).getVendor_id()));
                            }
                        }
                        selected_vendor.setText(TextUtils.join(",", selectedItems));
                        vendor_id.setText(TextUtils.join(",", vendorId));

                        Toast.makeText(AllExpensesActivity.this, "Selected: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();
                        dialogvendor.dismiss();
                    }
                });

                dialogvendor.show();


            }

        });
        selected_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogvendor = new Dialog(AllExpensesActivity.this);
                dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogvendor.setContentView(R.layout.dialog_multiple_selection);
                dialogvendor.setTitle("Select Vendor");

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogvendor.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogvendor.getWindow().setAttributes(lp);

                ListView listView = dialogvendor.findViewById(R.id.listview);
                Button btn_Select = dialogvendor.findViewById(R.id.button);
                dialogvendor.setCanceledOnTouchOutside(true);
                dialogvendor.setCancelable(true);



                lstClsVendorMasters = new ArrayList<>();
                listVendorSearch = lstClsVendorMasters = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [VENDOR_NAME]");
                final CustomAdapter adapter = new CustomAdapter(AllExpensesActivity.this, (ArrayList<ClsVendor>) lstClsVendorMasters);
                listView.setAdapter(adapter);

                btn_Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected[] = adapter.getSelectedFlags();
                        ArrayList<String> selectedItems = new ArrayList<>();
                        ArrayList<String> vendorId = new ArrayList<>();

                        for (int i = 0; i < isSelected.length; i++) {
                            if (isSelected[i]) {
                                selectedItems.add(lstClsVendorMasters.get(i).getVendor_name());
                                vendorId.add(String.valueOf(lstClsVendorMasters.get(i).getVendor_id()));
                            }
                        }
                        selected_vendor.setText(TextUtils.join(",", selectedItems));
                        vendor_id.setText(TextUtils.join(",", vendorId));

//                        Toast.makeText(AllExpensesActivity.this, "Selected: " + listCustomerMain.toString(), Toast.LENGTH_SHORT).show();
                        dialogvendor.dismiss();
                    }
                });
                dialogvendor.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_expense, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.filter) {
//            Intent intent = new Intent(AllExpensesActivity.this, AddExpenseActivity.class);
//            intent.putExtra("FLAG", "InventoryStock");
//            startActivity(intent);
            dialog.show();


            return true;
        }

        if (id == R.id.excel) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllExpensesActivity.this);
            alertDialog.setTitle("Export Record To Excel?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    List<String> list = new ArrayList<>();
                    list.add("Expense Name");
                    list.add("Vendor Name");
                    list.add("Employee Name");
                    list.add("Receipt No");
                    list.add("Receipt Date");
                    list.add("Amount");
                    list.add("Other Tax1");
                    list.add("Other Value1");
                    list.add("Other Tax2");
                    list.add("Other Value2");
                    list.add("Other Tax3");
                    list.add("Other Value3");
                    list.add("Other Tax4");
                    list.add("Other Value4");
                    list.add("Other Tax5");
                    list.add("Other Value5");
                    list.add("Discount");
                    list.add("Grand Total");
                    list.add("Entry Date");
                    list.add("Remark");

                    ClsExportToExcel.createExcelSheet("ExpensePath", list, "All Expense", lstClsExpenseMasterNews, null,null);

                    Toast.makeText(getApplicationContext(), "Export Record successfully", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    dialog.cancel();

                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            alertDialog.show();
            dialog.dismiss();
            dialog.cancel();
            return true;
        }


        if (id == R.id.pdf) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllExpensesActivity.this);
            alertDialog.setTitle("Export Record To PDF?");

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    List<String> list = new ArrayList<>();
                    list.add("Expense");
                    list.add("Vendor");
                    list.add("Employee");
                    list.add("Rec.No");
                    list.add("Rec Date");
                    list.add("Amount");
                    list.add("Tax1");
                    list.add("Value1");
                    list.add("Tax2");
                    list.add("Value2");
                    list.add("Tax3");
                    list.add("Value3");
                    list.add("Tax4");
                    list.add("Value4");
                    list.add("Tax5");
                    list.add("Value5");
                    list.add("Discount");
                    list.add("Total");
                    list.add("Remark");

                    DemoForPDF(list);

                    dialog.dismiss();
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "PDF done", Toast.LENGTH_LONG).show();

                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            alertDialog.show();

//            Intent intent = new Intent(AllExpensesActivity.this(), VendorBillDetailsActivity.class);
//            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void DemoForPDF(List<String> columns) {
        Document document = new Document(PageSize.A4);
        String PathName = "PDFSheet" + System.currentTimeMillis() + ".pdf";
        try {
            String path = Environment.getExternalStorageDirectory() + "/" + ClsGlobal.AppFolderName + "/Pdf/";    //c/shplt/exl/fl.xls
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, PathName);
            PdfPTable table = new PdfPTable(columns.size() + 1); // 2 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //

            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(document, fOut);
            document.open();
            document.add(new Paragraph("All Expenses To PDF"));
            PdfPCell _SrNO = new PdfPCell(new Paragraph("#", FontFactory.getFont(FontFactory.HELVETICA, 6)));
            _SrNO.setHorizontalAlignment(Element.ALIGN_CENTER);
            _SrNO.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(_SrNO);
            for (String c : columns) {

                Log.d("columns--", "c: " + c);
                Log.d("columns--", "columns.indexOf(c): " + columns.indexOf(c));

                Log.d("columns--", "c.length: " + columns.indexOf(c.length()));


                PdfPCell _headerCell = new PdfPCell(new Paragraph(c, FontFactory.getFont(FontFactory.HELVETICA, 6)));
                Log.d("FILE--", "cell3: " + _headerCell);
//                _headerCell.setPaddingLeft(10);
                _headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                _headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(_headerCell);


            }

            for (int i = 0; i < lstClsExpenseMasterNews.size(); i++) {


                String _IndexVal = String.valueOf(i + 1);
                PdfPCell cell = new PdfPCell(new Paragraph(_IndexVal, FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell.setBorderColor(BaseColor.BLACK);
//                cell.setPaddingLeft(10);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_LEFT);

                table.addCell(cell);


                PdfPCell cell1 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getExpense_type_name(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell1.setBorderColor(BaseColor.BLACK);
//                cell1.setPaddingLeft(10);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell2 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getVendor_name(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell2.setBorderColor(BaseColor.BLACK);
//                cell2.setPaddingLeft(10);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);


                PdfPCell cell3 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getEmployee_name(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell3.setBorderColor(BaseColor.BLACK);
//                cell3.setPaddingLeft(10);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell4 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getReceipt_no(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell4.setBorderColor(BaseColor.BLACK);
//                cell4.setPaddingLeft(10);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);


                PdfPCell cell5 = new PdfPCell(new Paragraph(ClsGlobal.getPDFFormat(lstClsExpenseMasterNews.get(i).getReceipt_date()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell5.setBorderColor(BaseColor.BLACK);
//                cell5.setPaddingLeft(10);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell6 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getAmount()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell6.setBorderColor(BaseColor.BLACK);
//                cell6.setPaddingLeft(10);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell7 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getOther_tax1(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell7.setBorderColor(BaseColor.BLACK);
//                cell7.setPaddingLeft(10);
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell8 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getOther_val1()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell8.setBorderColor(BaseColor.BLACK);
//                cell8.setPaddingLeft(10);
                cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell9 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getOther_tax2(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell9.setBorderColor(BaseColor.BLACK);
//                cell9.setPaddingLeft(10);
                cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell10 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getOther_val2()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell10.setBorderColor(BaseColor.BLACK);
//                cell10.setPaddingLeft(10);
                cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell11 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getOther_tax3(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell11.setBorderColor(BaseColor.BLACK);
//                cell11.setPaddingLeft(10);
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell12 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getOther_val3()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell12.setBorderColor(BaseColor.BLACK);
//                cell12.setPaddingLeft(10);
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell13 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getOther_tax4(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell13.setBorderColor(BaseColor.BLACK);
//                cell13.setPaddingLeft(10);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell14 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getOther_val4()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell14.setBorderColor(BaseColor.BLACK);
//                cell14.setPaddingLeft(10);
                cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell15 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getOther_tax5(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                cell15.setBorderColor(BaseColor.BLACK);
//                cell15.setPaddingLeft(10);
                cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell16 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getOther_val5()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell16.setBorderColor(BaseColor.BLACK);
//                cell16.setPaddingLeft(10);
                cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell17 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getDiscount()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell17.setBorderColor(BaseColor.BLACK);
//                cell17.setPaddingLeft(10);
                cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell18 = new PdfPCell(new Paragraph(String.valueOf(lstClsExpenseMasterNews.get(i).getGRAND_TOTAL()), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell18.setBorderColor(BaseColor.BLACK);
//                cell18.setPaddingLeft(10);
                cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell19 = new PdfPCell(new Paragraph(lstClsExpenseMasterNews.get(i).getRemark(), FontFactory.getFont(FontFactory.HELVETICA, 6)));
                cell19.setBorderColor(BaseColor.BLACK);
//                cell19.setPaddingLeft(10);
                cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell19.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
                table.addCell(cell9);
                table.addCell(cell10);
                table.addCell(cell11);
                table.addCell(cell12);
                table.addCell(cell13);
                table.addCell(cell14);
                table.addCell(cell15);
                table.addCell(cell16);
                table.addCell(cell17);
                table.addCell(cell18);
                table.addCell(cell19);


//                document.add(table);
//                document.close();
            }
            document.add(table);
        } catch (Exception e) {
            Log.e("PDFCreator", "ioException:" + e);

        } finally {
            document.close();
        }
        viewPdf(PathName, "/" + ClsGlobal.AppFolderName + "/Pdf/");
    }

    private void viewPdf(String file, String directory) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Log.e("File", "pdfFile" + pdfFile);
        //   Uri uri=Uri.fromFile(pdfFile);
        Uri outputUri = FileProvider.getUriForFile(AllExpensesActivity.this, AUTHORITY, pdfFile);

        Log.e("File", "uri--->>" + outputUri);

        Intent viewFile = new Intent(Intent.ACTION_VIEW);
        viewFile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        viewFile.setDataAndType(outputUri, "application/pdf");
        startActivity(viewFile);

        Log.d("FILE--", "path-- " + viewFile.setDataAndType(outputUri, "application/pdf"));

        try {
            startActivity(viewFile);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AllExpensesActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }


    public class CustomAdapter extends BaseAdapter {
        private ArrayList<ClsVendor> list_vendor = new ArrayList<ClsVendor>();
        private Context context;
        private boolean isSelected[];

        public CustomAdapter(Context context, ArrayList<ClsVendor> counteryList) {
            this.context = context;
            this.list_vendor = counteryList;
            isSelected = new boolean[counteryList.size()];
        }

        @Override
        public int getCount() {
            return list_vendor.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = (CheckedTextView) view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(String.valueOf(list_vendor.get(position).getVendor_name()));
//            holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];

                    if (holder.checkedTextView.isChecked()) {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundResource(0);
                    }
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout relativeLayout;
            CheckedTextView checkedTextView;
        }

    }

    public class AdapterExpenseType extends BaseAdapter {
        private ArrayList<ClsExpenseType> data = new ArrayList<ClsExpenseType>();
        private Context context;
        private boolean isSelected[];

        public AdapterExpenseType(Context context, ArrayList<ClsExpenseType> data) {
            this.context = context;
            this.data = data;
            isSelected = new boolean[data.size()];
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = (CheckedTextView) view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(String.valueOf(data.get(position).getExpense_type_name()));
//            holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];

                    if (holder.checkedTextView.isChecked()) {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
//                        holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
                        holder.relativeLayout.setBackgroundResource(0);
                    }
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout relativeLayout;
            CheckedTextView checkedTextView;
        }
    }
}
