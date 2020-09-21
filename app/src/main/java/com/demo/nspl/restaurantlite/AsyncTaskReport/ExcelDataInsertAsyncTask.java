package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.activity.Activity_ImportCustomer;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsReadData;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelDataInsertAsyncTask extends AsyncTask<Void, Void, ClsCustomerMaster.ClsExcelImport> {


    private String _where = "";
    private String mode = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    //    private PurchaseMasterAdapter purchaseMasterAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    Set<ClsReadData> lstClsGetValues = new LinkedHashSet<>();
    String PathHolder = "";


    public ExcelDataInsertAsyncTask(Context context, String PathHolder,
                                    ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
        this.PathHolder = PathHolder;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);

    }

    @SuppressLint("NewApi")
    @Override
    protected ClsCustomerMaster.ClsExcelImport doInBackground(Void... voids) {


        String filename = PathHolder.substring(PathHolder.lastIndexOf("/") + 1);
        List<ClsCustomerMaster> lstClsCustomerMasters = new ArrayList<>();

        ClsCustomerMaster.ClsExcelImport objResult = new ClsCustomerMaster.ClsExcelImport();
        objResult.setLstClsCustomerMasters(lstClsCustomerMasters);

        if (filename.endsWith(".xls")) {
            Log.d("--uri--", "IF_FILE IS CORRECT");

            File dir = new File(PathHolder);

            Log.d("--uri--", "IF_DIR:" + dir);

            File file = new File(dir.getAbsolutePath());

            Log.d("--uri--", "IF_FILE :" + file);

            Workbook workbook = null;
            //List<ClsReadData> _listDATA = new ArrayList<>();
            // List<ClsCustomerMaster> linkedHashSet = new ArrayList<>();

            try {
                workbook = Workbook.getWorkbook(file);
                if (workbook.getNumberOfSheets() != 0) {
                    String _defaultSheetName = "Sheet1";

                    boolean found = false;
                    String[] sheets = workbook.getSheetNames();
                    for (String sheetName : sheets) {
                        if (_defaultSheetName.equalsIgnoreCase(sheetName)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {

                        Sheet sheet = workbook.getSheet(0);

                        if (sheet.getRows() != 0 && sheet.getColumns() != 0) {

//                            String _mobileNo = sheet.getCell(0, 0).getContents();
//                            String _cutName = sheet.getCell(1, 0).getContents();

//                            if (!_mobileNo.isEmpty() && _mobileNo.equalsIgnoreCase("MOBILE NO")) {
//                                if (!_cutName.isEmpty() && _cutName.equalsIgnoreCase("CUSTOMER NAME")) {

                            for (int i = 1; i < sheet.getRows(); i++) {
                                ClsCustomerMaster _rowObj = new ClsCustomerMaster(context);

                                for (int j = 0; j < sheet.getColumns(); j++) {

                                    String _columnName = sheet.getCell(j, 0).getContents();//get columnName

                                    Cell cell = sheet.getCell(j, i);//get cell value

                                   /* if (!_columnName.equalsIgnoreCase("MOBILE NO")) {
                                        objResult.set_status("MobileNo column");
                                        return objResult;

                                    } else*/
                                    if (_columnName.equalsIgnoreCase("MOBILE NO")) {
                                        Log.e("--getContents--", "getContents:LEN " + cell.getContents().length());
                                        Log.e("--getContents--", "getContents:VALUE " + cell.getContents());

                                        if (cell.getContents().length() != 0) {
                                            Log.e("--getContents--", "STEP1");
                                            if (cell.getContents().length() == 10) {
                                                Log.e("--getContents--", "STEP2");
                                                if (cell.getContents().matches(ClsGlobal.MobileNo_Pattern)) {
                                                    Log.e("--getContents--", "STEP3");
                                                    _rowObj.setmMobile_No(cell.getContents());
                                                } else {
//                                                    _rowObj.setRemark("MOBILE NO MUST BE 10 DIGITS ONLY");
                                                    _rowObj.setStatus("INVALID MOBILE NO");
                                                    Log.e("--getContents--", "STEP4");
                                                }
                                            } else {
                                                Log.e("--getContents--", "STEP5");
                                                _rowObj.setRemark("MOBILE NO MUST BE 10 DIGITS ONLY");
                                                _rowObj.setStatus("INVALID MOBILE NO");
                                            }
                                            Log.e("--getContents--", "STEP6");

                                        } else {
                                            _rowObj.setStatus("MOBILE NO IS REQUIRED");
                                            Log.e("--getContents--", "STEP7");
                                        }

                                    } /*else if (!_columnName.equalsIgnoreCase("CUSTOMER NAME")) {
                                        objResult .set_status("Customer Name column");
                                        return objResult;
                                    }*/ else if (_columnName.equalsIgnoreCase("CUSTOMER NAME")) {
                                        _rowObj.setmName(cell.getContents());
                                    } else if (_columnName.equalsIgnoreCase("EMAIL")) {
                                        _rowObj.setEmail(cell.getContents());
                                    } else if (_columnName.equalsIgnoreCase("COMPNAY NAME")) {
                                        _rowObj.setCompany_Name(cell.getContents());
                                    } else if (_columnName.equalsIgnoreCase("GST NO")) {
                                       /* if (cell.getContents().length() == 15) {
                                            Log.e("--getContents--", "STEP2");
                                            _rowObj.setGST_NO(cell.getContents());
                                        } else {
                                            Log.e("--getContents--", "STEP5");
//                                            _rowObj.setRemark("GST NO MUST BE 15 CHARACTERS ONLY");
//                                            _rowObj.setStatus("INVALID GST NO");

                                            _rowObj.setRemark("MOBILE NO MUST BE 10 DIGITS ONLY");
                                            _rowObj.setStatus("INVALID MOBILE NO");

                                        }
*/
                                        _rowObj.setGST_NO(cell.getContents());

                                    } else if (_columnName.equalsIgnoreCase("ADDRESS")) {
                                        _rowObj.setAddress(cell.getContents());
                                    } else if (_columnName.equalsIgnoreCase("NOTE")) {
                                        _rowObj.setNote(cell.getContents());
                                    }


//                                else {
//                                        _rowObj.setStatus("MOBILE NO IS REQUIRED");
//                                    }
//

//                                    Gson gson = new Gson();
//                                    String jsonInString = gson.toJson(cell);
//                                    Log.d("--uri--", "jsonInString:" + jsonInString);


                                }


                                lstClsCustomerMasters.add(_rowObj);

                                //  _listDATA.add(_rowObj);


                            }
//                                } else {
//custmer name coumn not found

//                                    Log.d("--else--", "Customer Name column not found");
//                                    return "Customer Name column";
//
//                                }


                            if (lstClsCustomerMasters.size() != 0) {

                                Log.d("--else--", "DATA BEFORE:");

                                Gson gson = new Gson();
                                String jsonInString = gson.toJson(lstClsCustomerMasters);
                                Log.e("--objResult--", "objResult:BFR " + jsonInString);

                                lstClsCustomerMasters = ClsCustomerMaster.InsertExcelRecord(context, lstClsCustomerMasters);
                                objResult.set_status("DATA INSERTED SUCCESSFULLY");
                                objResult.setLstClsCustomerMasters(lstClsCustomerMasters);

                                jsonInString = gson.toJson(lstClsCustomerMasters);
                                Log.e("--objResult--", "objResult:AFTR " + jsonInString);

                                if (lstClsCustomerMasters.size() != 0) {
                                    objResult.set_totalRecords(lstClsCustomerMasters.size());

//                                Stream _listResult = lstClsCustomerMasters.stream();


                                    int success = (int) lstClsCustomerMasters.stream().filter(qry -> qry.getStatus().equalsIgnoreCase("SUCCESS")).count();
                                    int alreadyExists = (int) lstClsCustomerMasters.stream().filter(qry -> qry.getStatus().equalsIgnoreCase("EXISTS")).count();
                                    int failed = (int) lstClsCustomerMasters.stream().filter(
                                            qry -> qry.getStatus().equalsIgnoreCase("FAILED") ||
                                                    qry.getStatus().equalsIgnoreCase("INVALID MOBILE NO") ||
                                                    qry.getStatus().equalsIgnoreCase("MOBILE NO IS REQUIRED")
                                    ).count();

                                    objResult.set_successRecords(success);
                                    objResult.set_alreadyExists(alreadyExists);
                                    objResult.set_failedRecords(failed);
                                }

                            } else {
                                objResult.set_status("NO DATA IN LIST");
                            }


//                            } else {
//mobile number coumn not found
//
//                                Log.d("--else--", "Mobile No column not found");
//                                return "MobileNo column";
//
//                            }
                        } else {
                            //no data found in selected file

                            Log.d("--else--", "No Data found in selected file");

                            objResult.set_status("No Data found");


                        }
                    } else {
                        //sheet1 not found
                        Log.d("--else--", "sheet1 not found");


                        objResult.set_status("sheet1 not found");


                    }

                } else {
                    //invalid file


                    Log.d("--else--", "Invalid file");
                }
            } catch (IOException | BiffException e) {
                Log.d("--else--", "Invalid file  " + e.getMessage());

                objResult.set_status("wrong file selected");

                e.printStackTrace();


            }

        } else {

            objResult.set_status("Wrong file selected");

        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objResult);
        Log.e("--objResult--", "objResult: " + jsonInString);


        return objResult;
    }


    @Override
    protected void onPostExecute(ClsCustomerMaster.ClsExcelImport objResult) {
        super.onPostExecute(objResult);

        progressBar.setVisibility(View.GONE);


        Log.e("--objResult--", "getResult: " + objResult.get_status());


        if (objResult.get_status().equalsIgnoreCase("Wrong file selected")) {
            Toast.makeText(context, "Wrong file selected", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("sheet1 not found")) {
            Toast.makeText(context, "Sheet1 is not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("No Data found")) {
            Toast.makeText(context, "No data found in selected file", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("MobileNo column")) {
            Toast.makeText(context, "Mobile No column not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("Customer Name column")) {
            Toast.makeText(context, "Customer Name column not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("DATA INSERTED SUCCESSFULLY")) {
            Toast.makeText(context, "DATA INSERTED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        }


        Activity_ImportCustomer.getStatus = objResult.get_status();

        Activity_ImportCustomer.objImportResult = objResult;
        Activity_ImportCustomer.refreshCount();


    }

}
