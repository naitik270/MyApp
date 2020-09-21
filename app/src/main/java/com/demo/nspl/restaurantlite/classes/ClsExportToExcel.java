package com.demo.nspl.restaurantlite.classes;


import android.os.Environment;
import android.util.Log;

import com.demo.nspl.restaurantlite.ExportData.ClsExportPaymentReportData;
import com.demo.nspl.restaurantlite.ExportData.ClsExportSingleCustomerData;
import com.demo.nspl.restaurantlite.ExportData.ClsExportSingleVendorData;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Stock.ClsStock;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Abhishek on 17-05-2018.
 */

public class ClsExportToExcel {


    public static String createExcelSheet(String _filePath, List<String> columns, String _type,
                                          Object object, ClsVendor clsVendor, ClsCustomerMaster clsCustomerMaster) {



        String FileNameExpXls = "exlSheet_" + ClsGlobal.getRandom() + ".xls";
        Log.d("--customerReport--", "FileNameExpxls-- " + FileNameExpXls);
        Log.d("--customerReport--", "_filePath-- " + _filePath);

//        File directory = new File(Environment.getExternalStorageDirectory(), Fnamexls);//d://


//        String _folderName = "/fTouchPOSDocument/";
        String _folderName = "/fTouchPOSDemoDoc/";
        String _finalExportCustomer = "/AAAA/";


        String _fileNameToSave = "CustomerReport_".concat(ClsGlobal.getRandom().concat(".xls"));
        String _fileNameToSingleCustomer = "SingleCustomerReport".concat(ClsGlobal.getRandom().concat(".xls"));
        String _fileNameToSingleVendor = "SingleVendorReport".concat(ClsGlobal.getRandom().concat(".xls"));
        String _fileNameToPaymentReport = "PaymentReport".concat(ClsGlobal.getRandom().concat(".xls"));
        String _fileNameToStockReport = "StockReport".concat(ClsGlobal.getRandom().concat(".xls"));
        String _fileNameToFinalCustomerReport = "Customer".concat(ClsGlobal.getRandom().concat(".xls"));

        File _fileGenerateToFolder;

        if (_filePath != null && _filePath.equalsIgnoreCase("ExpensePath")) {

            Log.d("--customerReport--", "IF_BLOCk");

            File directory = new File(Environment.getExternalStorageDirectory() + "/" + ClsGlobal.AppFolderName + "/Excel/");//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), FileNameExpXls);//c/shplt/exl/fl.xls

        } else if (_filePath.equalsIgnoreCase("CustomerReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToSave);//c/shplt/exl/fl.xls
            Log.d("--customerReport--", "ELSE_IF_fileGenerate:" + _fileGenerateToFolder);
            Log.d("--customerReport--", "ELSE_IF_fileName:" + _fileNameToSave);
        } else if (_filePath.equalsIgnoreCase("SingleCustomerReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToSingleCustomer);//c/shplt/exl/fl.xls
            Log.d("--customerReport--", "ELSE_IF_Single:" + _fileGenerateToFolder);
            Log.d("--customerReport--", "ELSE_IF_Single:" + _fileNameToSingleCustomer);
        } else if (_filePath.equalsIgnoreCase("SingleVendorReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToSingleVendor);//c/shplt/exl/fl.xls
            Log.d("--customerReport--", "ELSE_IF_Single:" + _fileGenerateToFolder);
            Log.d("--customerReport--", "ELSE_IF_Single:" + _fileNameToSingleVendor);
        } else if (_filePath.equalsIgnoreCase("PaymentReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToPaymentReport);//c/shplt/exl/fl.xls
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileGenerateToFolder);
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileNameToPaymentReport);
        } else if (_filePath.equalsIgnoreCase("StockReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToStockReport);//c/shplt/exl/fl.xls
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileGenerateToFolder);
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileNameToStockReport);
        }else if (_filePath.equalsIgnoreCase("CustomerFinalReportPath")) {

            Log.d("--customerReport--", "--------------------ELSE_IF_BLOCK---------------");
            File directory = new File(Environment.getExternalStorageDirectory() + _folderName);//c/shplt/exl/fl.xls
            if (!directory.exists()) {
                directory.mkdirs();
            }
            _fileGenerateToFolder = new File(directory.getAbsolutePath(), _fileNameToFinalCustomerReport);//c/shplt/exl/fl.xls
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileGenerateToFolder);
            Log.d("--Payment--", "ELSE_IF_Single:" + _fileNameToFinalCustomerReport);
        } else {
            Log.d("--customerReport--", "ELSE");

            _fileGenerateToFolder = new File(_filePath);
            if (!_fileGenerateToFolder.exists()) {
                _fileGenerateToFolder.mkdirs();
            }

        }

        Log.d("--customerReport--", "before: " + _fileGenerateToFolder);

        WorkbookSettings wbSettings = new WorkbookSettings();
        Log.d("--customerReport--", "wbSettings: " + wbSettings);

        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {

            if (_type.equalsIgnoreCase("All Expense")) {

                Log.d("--customerReport--", "All Expense");

                List<ClsExpenseMasterNew> list = (List<ClsExpenseMasterNew>) object;


                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
//                Log.d("FILE--", "workbook-- " + workbook);
                WritableSheet sheet = workbook.createSheet("DemoSheet1", 0);
//                Log.d("FILE--", "sheet-- " + sheet);
                //columns
//                Log.e("ETM_Fragment", "List data: " + list.toString());


                try {

                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
                    int widthInChars = 20;

                    for (String c : columns) {

                        sheet.addCell(new Label(0, 0, "SR.NO", cellFormat));
                        sheet.setColumnView(columns.indexOf(c) + 1, widthInChars);
                        sheet.addCell(new Label(columns.indexOf(c) + 1, 0, c, cellFormat));
                    }

                } catch (RowsExceededException e) {

                    e.printStackTrace();
                } catch (WriteException e) {

                    e.printStackTrace();
                }

                //generate cell
                for (int i = 0; i < list.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + 1, _IndexVal));

                        sheet.addCell(new Label(1, i + 1, list.get(i).getExpense_type_name()));
                        sheet.addCell(new Label(2, i + 1, list.get(i).getVendor_name()));
                        sheet.addCell(new Label(3, i + 1, list.get(i).getEmployee_name()));
                        sheet.addCell(new Label(4, i + 1, list.get(i).getReceipt_no()));
                        sheet.addCell(new Label(5, i + 1, ClsGlobal.getPDFFormat(list.get(i).getReceipt_date())));
                        sheet.addCell(new Label(6, i + 1, String.valueOf(list.get(i).getAmount())));
                        sheet.addCell(new Label(7, i + 1, list.get(i).getOther_tax1()));
                        sheet.addCell(new Label(8, i + 1, String.valueOf(list.get(i).getOther_val1())));
                        sheet.addCell(new Label(9, i + 1, list.get(i).getOther_tax2()));
                        sheet.addCell(new Label(10, i + 1, String.valueOf(list.get(i).getOther_val2())));
                        sheet.addCell(new Label(11, i + 1, list.get(i).getOther_tax3()));
                        sheet.addCell(new Label(12, i + 1, String.valueOf(list.get(i).getOther_val3())));
                        sheet.addCell(new Label(13, i + 1, list.get(i).getOther_tax4()));
                        sheet.addCell(new Label(14, i + 1, String.valueOf(list.get(i).getOther_val4())));
                        sheet.addCell(new Label(15, i + 1, list.get(i).getOther_tax5()));
                        sheet.addCell(new Label(16, i + 1, String.valueOf(list.get(i).getOther_val5())));
                        sheet.addCell(new Label(17, i + 1, String.valueOf(list.get(i).getDiscount())));
                        sheet.addCell(new Label(18, i + 1, String.valueOf(list.get(i).getGRAND_TOTAL())));


                        sheet.addCell(new Label(19, i + 1, ClsGlobal.getEntryDateFormat(list.get(i).getEntry_date())));
                        sheet.addCell(new Label(20, i + 1, list.get(i).getRemark()));

                    } catch (RowsExceededException e) {

                        e.printStackTrace();
                    } catch (WriteException e) {

                        e.printStackTrace();
                    }
                }
                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());

                    e.printStackTrace();
                }
            } else if (_type.equalsIgnoreCase("Customer Report")) {

                Log.d("--customerReport--", "Customer Report");


                List<ClsCustomerMaster> lstClsCustomerMasters =
                        (List<ClsCustomerMaster>) object;

                if (lstClsCustomerMasters.size() ==0){
                    return "No Record Found";
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsCustomerMasters);
                Log.d("--customerReport--", "CustomerReport: " + jsonInString);

                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
//                Log.d("--customerReport--", "workbook: " + workbook);
                WritableSheet sheet = workbook.createSheet("DemoSheet1", 0);
//                Log.d("--customerReport--", "sheet: " + sheet);

                //columns
                Log.e("--customerReport--", "lstData: " + lstClsCustomerMasters.toString());

                try {

                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
                    int widthInChars = 20;

                    for (String c : columns) {
                        sheet.addCell(new Label(0, 0, "SR#", cellFormat));
                        sheet.setColumnView(columns.indexOf(c) + 1, widthInChars);
                        sheet.addCell(new Label(columns.indexOf(c) + 1, 0, c, cellFormat));
                    }

                } catch (RowsExceededException e) {

                    e.printStackTrace();
                } catch (WriteException e) {

                    e.printStackTrace();
                }

                //generate cell
                for (int i = 0; i < lstClsCustomerMasters.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + 1, _IndexVal));

                        sheet.addCell(new Label(1, i + 1, lstClsCustomerMasters.get(i).getmMobile_No()));
                        sheet.addCell(new Label(2, i + 1, lstClsCustomerMasters.get(i).getmName()));
                        sheet.addCell(new Label(3, i + 1, lstClsCustomerMasters.get(i).getCompany_Name()));
                        sheet.addCell(new Label(4, i + 1, lstClsCustomerMasters.get(i).getGST_NO()));
                        sheet.addCell(new Label(5, i + 1, lstClsCustomerMasters.get(i).getAddress()));
                        sheet.addCell(new Label(6, i + 1, lstClsCustomerMasters.get(i).getEmail()));
                        sheet.addCell(new Label(7, i + 1, String.valueOf(lstClsCustomerMasters.get(i).getCredit())));
                        sheet.addCell(new Label(8, i + 1, String.valueOf(lstClsCustomerMasters.get(i).getOpeningStock())));
                        sheet.addCell(new Label(9, i + 1, lstClsCustomerMasters.get(i).getBalanceType()));
                        sheet.addCell(new Label(10, i + 1, lstClsCustomerMasters.get(i).getNote()));

                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }


                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());
                    e.printStackTrace();
                }

            } else if (_type.equalsIgnoreCase("Single Customer")) {

                int _skipRow = 8;

                List<ClsExportSingleCustomerData> lstClsCustomerMasters =
                        (List<ClsExportSingleCustomerData>) object;


                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
                WritableSheet sheet = workbook.createSheet("DemoSheet1", 0);
                //columns
                Log.e("--Single--", "lstData: " + lstClsCustomerMasters.toString());

                try {

                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

// Customer Details Cell Format
                    WritableCellFormat forVendorDetails = new WritableCellFormat(cellFont);

                    sheet.addCell(new Label(0, 0, "Name: ", forVendorDetails));
                    sheet.addCell(new Label(1, 0, clsCustomerMaster.getmName()));
                    sheet.addCell(new Label(2, 0, ""));
                    sheet.addCell(new Label(3, 0, "Mobile: ", forVendorDetails));
                    sheet.addCell(new Label(4, 0, clsCustomerMaster.getmMobile_No()));

                    sheet.addCell(new Label(0, 1, "Company Name: ", forVendorDetails));
                    sheet.addCell(new Label(1, 1, clsCustomerMaster.getCompany_Name()));
                    sheet.addCell(new Label(2, 1, ""));
                    sheet.addCell(new Label(3, 1, "Gst No: ", forVendorDetails));
                    sheet.addCell(new Label(4, 1, clsCustomerMaster.getGST_NO()));

                    sheet.addCell(new Label(0, 2, "Credit Limit: ", forVendorDetails));
                    sheet.addCell(new Label(1, 2, String.valueOf(clsCustomerMaster.getCredit())));
                    sheet.addCell(new Label(2, 2, ""));
                    sheet.addCell(new Label(3, 2, "Opening Balance: ", forVendorDetails));
                    sheet.addCell(new Label(4, 2, String.valueOf(clsCustomerMaster.getOpeningStock())));

                  /*  sheet.addCell(new Label(0, 3, "Total Sales: ", forVendorDetails));
                    sheet.addCell(new Label(1, 3, "----------------"));
                    sheet.addCell(new Label(2, 3, ""));
                    sheet.addCell(new Label(3, 3, "Total Payment: ", forVendorDetails));
                    sheet.addCell(new Label(4, 3, "----------------"));

                    sheet.addCell(new Label(0, 4, "Avl Balance: ", forVendorDetails));
                    sheet.addCell(new Label(1, 4, "----------------"));
                    sheet.addCell(new Label(2, 4, ""));
*/

// Header Cell Format
                    WritableCellFormat forHeader = new WritableCellFormat(cellFont);
                    forHeader.setBackground(Colour.GRAY_25);
                    forHeader.setAlignment(Alignment.CENTRE);

                    sheet.addCell(new Label(0, _skipRow - 2, "Customer Ledger Report", forHeader));
                    sheet.mergeCells(0, _skipRow - 2, columns.size() - 1, _skipRow - 2);

// Value Cell Format

                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                    for (String c : columns) {
                        sheet.addCell(new Label(columns.indexOf(c), _skipRow - 1, c, cellFormat));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

                //generate cell
                for (int i = 0; i < lstClsCustomerMasters.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + _skipRow, _IndexVal));

                        sheet.addCell(new Label(1, i + _skipRow, ClsGlobal.getDDMMYYYY(lstClsCustomerMasters.get(i).get_date())));
                        sheet.addCell(new Label(2, i + _skipRow, lstClsCustomerMasters.get(i).get_details()));
                        sheet.addCell(new Label(3, i + _skipRow, lstClsCustomerMasters.get(i).get_paymentMode()));
                        sheet.addCell(new Label(4, i + _skipRow, lstClsCustomerMasters.get(i).get_paymentDetails()));
                        sheet.addCell(new Label(5, i + _skipRow, String.valueOf(lstClsCustomerMasters.get(i).get_amount())));
                        sheet.addCell(new Label(6, i + _skipRow, String.valueOf(lstClsCustomerMasters.get(i).get_balance())));


                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());
                    e.printStackTrace();
                }

            } else if (_type.equalsIgnoreCase("Single Vendor")) {

                int _skipRow = 7;

                Log.d("--VendorReport--", "Vendor Report");

                List<ClsExportSingleVendorData> lstSingleVendorData =
                        (List<ClsExportSingleVendorData>) object;


                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);
                //columns
                Log.e("--SingleVendor--", "lstData: " + lstSingleVendorData.toString());

                try {
                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

// Vendor Details Cell Format
                    WritableCellFormat forVendorDetails = new WritableCellFormat(cellFont);

                    sheet.addCell(new Label(0, 0, "Name: ", forVendorDetails));
                    sheet.addCell(new Label(1, 0, clsVendor.getVendor_name()));
                    sheet.addCell(new Label(2, 0, ""));
                    sheet.addCell(new Label(3, 0, "Mobile: ", forVendorDetails));
                    sheet.addCell(new Label(4, 0, clsVendor.getContact_no()));

                    sheet.addCell(new Label(0, 1, "Address: ", forVendorDetails));
                    sheet.addCell(new Label(1, 1, clsVendor.getAddress()));
                    sheet.addCell(new Label(2, 1, ""));
                    sheet.addCell(new Label(3, 1, "Gst No: ", forVendorDetails));
                    sheet.addCell(new Label(4, 1, clsVendor.getGst_no()));

                    sheet.addCell(new Label(0, 2, "Opening Balance: ", forVendorDetails));
                    sheet.addCell(new Label(1, 2, String.valueOf(clsVendor.getOpeningStock())));
                    sheet.addCell(new Label(2, 2, ""));
//                    sheet.addCell(new Label(3, 2, "Total Purchase: ", forVendorDetails));
//                    sheet.addCell(new Label(4, 2, "---------------"));

//                    sheet.addCell(new Label(0, 3, "Total Payment: ", forVendorDetails));
//                    sheet.addCell(new Label(1, 3, "----------------"));
//                    sheet.addCell(new Label(2, 3, ""));
//                    sheet.addCell(new Label(3, 3, "AVL Balance: ", forVendorDetails));
//                    sheet.addCell(new Label(4, 3, "----------------"));


// Header Cell Format
                    WritableCellFormat forHeader = new WritableCellFormat(cellFont);
                    forHeader.setBackground(Colour.GRAY_25);
                    forHeader.setAlignment(Alignment.CENTRE);

                    sheet.addCell(new Label(0, _skipRow - 2, "Vendor Ledger Report", forHeader));
                    sheet.mergeCells(0, _skipRow - 2, columns.size() - 1, _skipRow - 2);

// Value Cell Format
                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                    for (String c : columns) {
//                        sheet.addCell(new Label(0, 0, "SR#", cellFormat));
//                        sheet.setColumnView(columns.indexOf(c) + 1, widthInChars);
//                        sheet.addCell(new Label(columns.indexOf(c) + 1, 0, c, cellFormat));
                        sheet.addCell(new Label(columns.indexOf(c), _skipRow - 1, c, cellFormat));
//                        sheet.addCell(new Label(0, 6, c, cellFormat));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

                //generate cell
                for (int i = 0; i < lstSingleVendorData.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + _skipRow, _IndexVal));

                        sheet.addCell(new Label(1, i + _skipRow, ClsGlobal.getDDMMYYYY(lstSingleVendorData.get(i).get_date())));
                        sheet.addCell(new Label(2, i + _skipRow, lstSingleVendorData.get(i).get_details()));
                        sheet.addCell(new Label(3, i + _skipRow, lstSingleVendorData.get(i).get_paymentMode()));
                        sheet.addCell(new Label(4, i + _skipRow, lstSingleVendorData.get(i).get_paymentDetails()));
                        sheet.addCell(new Label(5, i + _skipRow, String.valueOf(lstSingleVendorData.get(i).get_amount())));
                        sheet.addCell(new Label(6, i + _skipRow, String.valueOf(lstSingleVendorData.get(i).get_balance())));


                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("SingleVendor", "catchBlock: " + e.getMessage());
                    e.printStackTrace();
                }

            } else if (_type.equalsIgnoreCase("Payment Details")) {

                int _skipRow = 2;

                double _totalIn = 0.0;
                double _totalOut = 0.0;

                List<ClsExportPaymentReportData> lstData =
                        (List<ClsExportPaymentReportData>) object;

                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);


                WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                cellFont.setBoldStyle(WritableFont.BOLD);

                try {

//                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
//                    cellFont.setBoldStyle(WritableFont.BOLD);

// Header Cell Format
                    WritableCellFormat forHeader = new WritableCellFormat(cellFont);
                    forHeader.setBackground(Colour.GRAY_25);
                    forHeader.setAlignment(Alignment.CENTRE);

                    sheet.addCell(new Label(0, _skipRow - 2, "Payment Report", forHeader));
                    sheet.mergeCells(0, _skipRow - 2, columns.size() - 1, _skipRow - 2);


                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                    for (String c : columns) {
                        sheet.addCell(new Label(columns.indexOf(c), _skipRow - 1, c, cellFormat));
                    }

                } catch (WriteException e) {
                    e.printStackTrace();
                }


                //generate cell
                for (int i = 0; i < lstData.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + _skipRow, _IndexVal));
                        sheet.addCell(new Label(1, i + _skipRow, ClsGlobal.getDDMMYYYY(lstData.get(i).get_date())));
                        sheet.addCell(new Label(2, i + _skipRow, lstData.get(i).get_type()));


                        if (lstData.get(i).get_type().equalsIgnoreCase("Customer")) {
                            sheet.addCell(new Label(3, i + _skipRow, lstData.get(i).get_custName()));
                        } else {
                            sheet.addCell(new Label(3, i + _skipRow, lstData.get(i).get_vendorName()));
                        }

                        sheet.addCell(new Label(4, i + _skipRow, lstData.get(i).get_mobileNo()));
                        sheet.addCell(new Label(5, i + _skipRow, lstData.get(i).get_invoiceNo()));
                        sheet.addCell(new Label(6, i + _skipRow, lstData.get(i).get_detail()));
                        sheet.addCell(new Label(7, i + _skipRow, lstData.get(i).get_mode()));

                        if (lstData.get(i).get_type().equalsIgnoreCase("Customer")) {

                            _totalIn += lstData.get(i).getAmount();

                            sheet.addCell(new Label(8, i + _skipRow, String.valueOf(lstData.get(i).getAmount())));
                        } else {
                            sheet.addCell(new Label(8, i + _skipRow, String.valueOf(0.0)));
                        }


                        if (lstData.get(i).get_type().equalsIgnoreCase("Vendor")) {

                            _totalOut += lstData.get(i).getAmount();

                            sheet.addCell(new Label(9, i + _skipRow, String.valueOf(lstData.get(i).getAmount())));
                        } else {
                            sheet.addCell(new Label(9, i + _skipRow, String.valueOf(0.0)));
                        }

                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }

// Footer Cell Format Start
                WritableCellFormat forFooter = new WritableCellFormat(cellFont);
                forFooter.setBackground(Colour.GRAY_25);
                forFooter.setAlignment(Alignment.CENTRE);
                sheet.addCell(new Label(0, _skipRow + lstData.size(), "Total", forFooter));

                for (int i = 1; i < columns.size(); i++) {

                    if (i == 8) {
                        sheet.addCell(new Label(i, _skipRow + lstData.size(), String.valueOf(_totalIn), forFooter));
                    } else if (i == 9) {
                        sheet.addCell(new Label(i, _skipRow + lstData.size(), String.valueOf(_totalOut), forFooter));
                    } else {
                        sheet.addCell(new Label(i, _skipRow + lstData.size(), "", forFooter));
                    }

                }
// Footer Cell Format END

                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());
                    e.printStackTrace();
                }

            }else if (_type.equalsIgnoreCase("Final Customer Report")) {

                Log.d("--customerReport--", "Customer Report");


                List<ClsCustomerMaster> lstClsCustomerMasters =
                        (List<ClsCustomerMaster>) object;

                if (lstClsCustomerMasters.size() ==0){
                    return "No Record Found";
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsCustomerMasters);
                Log.d("--customerReport--", "CustomerReport: " + jsonInString);

                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
//                Log.d("--customerReport--", "workbook: " + workbook);
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//                Log.d("--customerReport--", "sheet: " + sheet);

                //columns
                Log.e("--customerReport--", "lstData: " + lstClsCustomerMasters.toString());

                try {

                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
                    int widthInChars = 20;

                    for (String c : columns) {
                        sheet.addCell(new Label(0, 0, "SR#", cellFormat));
                        sheet.setColumnView(columns.indexOf(c) + 1, widthInChars);
                        sheet.addCell(new Label(columns.indexOf(c) + 1, 0, c, cellFormat));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

                //generate cell
                //generate cell
                for (int i = 0; i < lstClsCustomerMasters.size(); i++) {


                    Log.e("--value--", "Mobile: " + lstClsCustomerMasters.get(i).getmMobile_No());


                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + 1, _IndexVal));

                        sheet.addCell(new Label(1, i + 1, lstClsCustomerMasters.get(i).getmMobile_No()));
                        sheet.addCell(new Label(2, i + 1, lstClsCustomerMasters.get(i).getmName()));
                        sheet.addCell(new Label(3, i + 1, lstClsCustomerMasters.get(i).getEmail()));
                        sheet.addCell(new Label(4, i + 1, lstClsCustomerMasters.get(i).getCompany_Name()));
                        sheet.addCell(new Label(5, i + 1, lstClsCustomerMasters.get(i).getGST_NO()));
                        sheet.addCell(new Label(6, i + 1, lstClsCustomerMasters.get(i).getAddress()));
                        sheet.addCell(new Label(7, i + 1, lstClsCustomerMasters.get(i).getNote()));
                        sheet.addCell(new Label(8, i + 1, lstClsCustomerMasters.get(i).getStatus()));
                        sheet.addCell(new Label(9, i + 1, lstClsCustomerMasters.get(i).getRemark()));

                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }


                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());
                    e.printStackTrace();
                }

            } else if (_type.equalsIgnoreCase("Stock Report")) {

                int _skipRow = 2;

                List<ClsStock> lstData =
                        (List<ClsStock>) object;

                workbook = Workbook.createWorkbook(_fileGenerateToFolder, wbSettings);
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);


                try {

                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
                    cellFont.setBoldStyle(WritableFont.BOLD);

// Header Cell Format
                    WritableCellFormat forHeader = new WritableCellFormat(cellFont);
                    forHeader.setBackground(Colour.GRAY_25);
                    forHeader.setAlignment(Alignment.CENTRE);

                    sheet.addCell(new Label(0, _skipRow - 2, "Stock Report", forHeader));
                    sheet.mergeCells(0, _skipRow - 2, columns.size() - 1, _skipRow - 2);


                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                    for (String c : columns) {
                        sheet.addCell(new Label(columns.indexOf(c), _skipRow - 1, c, cellFormat));
                    }

                } catch (WriteException e) {
                    e.printStackTrace();
                }


                //generate cell
                for (int i = 0; i < lstData.size(); i++) {
                    try {
                        String _IndexVal = String.valueOf(i + 1);
                        sheet.addCell(new Label(0, i + _skipRow, _IndexVal));
                        sheet.addCell(new Label(1, i + _skipRow, lstData.get(i).getITEM_NAME()));
                        sheet.addCell(new Label(2, i + _skipRow, lstData.get(i).getUNIT_CODE()));
                        sheet.addCell(new Label(3, i + _skipRow, String.valueOf(lstData.get(i).getMIN_STOCK())));
                        sheet.addCell(new Label(4, i + _skipRow, String.valueOf(lstData.get(i).getMAX_STOCK())));
                        sheet.addCell(new Label(5, i + _skipRow, String.valueOf(ClsGlobal.round(lstData.get(i).getAveragePurchaseRate(), 2))));
                        sheet.addCell(new Label(6, i + _skipRow, String.valueOf(ClsGlobal.round(lstData.get(i).getAverageSaleRate(), 2))));
                        sheet.addCell(new Label(7, i + _skipRow, String.valueOf(lstData.get(i).getIN())));
                        sheet.addCell(new Label(8, i + _skipRow, String.valueOf(lstData.get(i).getOUT())));
                        sheet.addCell(new Label(9, i + _skipRow, String.valueOf(lstData.get(i).getOPENING_STOCK())));

                        Double _totalStock = lstData.get(i).getIN() - lstData.get(i).getOUT();
                        Log.e("--stock--", "_totalStock: " + _totalStock);

                        Double _avgPurchaseRate = _totalStock * lstData.get(i).getAveragePurchaseRate();
                        Double _totalAvgStockVal = 0.0;
                        _totalAvgStockVal += _avgPurchaseRate;

                        Log.e("--stock--", "_avgPurchaseRate: " + _avgPurchaseRate);
                        Log.e("--stock--", "_totalAvgStockVal: " + _totalAvgStockVal);

                        sheet.addCell(new Label(10, i + _skipRow, String.valueOf(ClsGlobal.round(_totalAvgStockVal, 2))));

                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    workbook.write();
                    workbook.close();
                } catch (WriteException e) {
                    Log.e("First", "First---->" + e.getMessage());
                    e.printStackTrace();
                }

            }


        } catch (IOException e) {
            Log.e("--MAIN--", "IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        return _fileGenerateToFolder.getAbsolutePath();
    }


}

