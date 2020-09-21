package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.util.Log;

import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClsQuotationHtmlToPdf {


    public static ClsQuotationPdf generatePDF(Context context,
                                              ClsQuotationMaster obj,
                                              ClsCustomerMaster objClsCustomerMaster,
                                              List<ClsQuotationOrderDetail> lstClsQuotationOrderDetails,
                                              String mode) {


        ClsQuotationPdf clsQuotationPdf = new ClsQuotationPdf();

        String path = "";

        StringBuilder mailBodyStr = new StringBuilder();
        String PathName = ClsGlobal.QuotationFileName;

        try {

            path = ClsGlobal.QuotationFilePath;

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            StringBuilder _checkOutInvoice = ClsGlobal.getQuotationTemplate(context);
            mailBodyStr.append(_checkOutInvoice.toString());
            ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(context);

            Gson gson = new Gson();
            String ssad = gson.toJson(objClsUserInfo);
            Log.d("--link--", "ssad: " + ssad);


            replaceString(mailBodyStr, "#COMPANY_NAME", objClsUserInfo.getBusinessname());

            replaceString(mailBodyStr, "#BussAddress", objClsUserInfo.getBusinessaddress().toUpperCase());

            String stateCityPincode = objClsUserInfo.getState().toUpperCase().concat(", ").concat(objClsUserInfo.getCity().toUpperCase()).concat("-").concat(objClsUserInfo.getPincode());
            replaceString(mailBodyStr, "StateCityPinCode", stateCityPincode);

            replaceString(mailBodyStr, "EmailValue", objClsUserInfo.getEmailaddress());
            replaceString(mailBodyStr, "CompnayMobile", objClsUserInfo.getMobileNo());
            replaceString(mailBodyStr, "CompanyGSTNO", objClsUserInfo.getGstnumber());

            replaceString(mailBodyStr, "#custName", objClsCustomerMaster.getmName());
            replaceString(mailBodyStr, "#custNumber", objClsCustomerMaster.getmMobile_No());


            if (objClsCustomerMaster.getCompany_Name() != null || !objClsCustomerMaster.getCompany_Name().equalsIgnoreCase("")) {
                replaceString(mailBodyStr, "#custCompany", "<br><b>Company: </b>" + objClsCustomerMaster.getCompany_Name());
            } else {
                replaceString(mailBodyStr, "#custCompany", "");
            }

            if (objClsUserInfo.getLicenseType() != null || !objClsUserInfo.getLicenseType().equalsIgnoreCase("FREE")) {

                replaceString(mailBodyStr, "#PoweredbyfTouch", "Powered by fTouch" + "<br>");
            } else {
                replaceString(mailBodyStr, "#PoweredbyfTouch", "");
            }

            if (objClsCustomerMaster.getGST_NO() != null || !objClsCustomerMaster.getGST_NO().equalsIgnoreCase("")) {
                replaceString(mailBodyStr, "#custGst", "<b> GSTIN: </b>" + objClsCustomerMaster.getGST_NO());
            } else {
                replaceString(mailBodyStr, "#custGst", "");
            }

            if (!objClsCustomerMaster.getAddress().equalsIgnoreCase("")) {
                replaceString(mailBodyStr, "#custAddress", "<br><b>Address: </b>" + objClsCustomerMaster.getAddress());
            } else {
                replaceString(mailBodyStr, "#custAddress", "");
            }

          /*  if (!objClsCustomerMaster.getEmail().equalsIgnoreCase("")) {
                replaceString(mailBodyStr, "#custEmail", "<br><b>E-mail: </b>" + objClsCustomerMaster.getEmail());
            } else {
                replaceString(mailBodyStr, "#custEmail", "");
            }*/

            replaceString(mailBodyStr, "#quotNo", obj.getQuotationNo());

            replaceString(mailBodyStr, "#dateValue", ClsGlobal
                    .getEntryDateFormat(obj.getQuotationDate()));
            replaceString(mailBodyStr, "#validUpToDateValue", ClsGlobal.getDDMYYYYFormat(obj.getValidUptoDate()));

            String logoImg = ClsGlobal.getLogo();
            String imagePath = "file://" + logoImg;

            String signatureImg = ClsGlobal.getSignature();
            String SignatureImgPath = "file://" + signatureImg;

            if (signatureImg != null && !signatureImg.isEmpty()) {

                replaceString(mailBodyStr, "#note", "");
                String signatureStr = "<img width=\"100px\" src=\"" + SignatureImgPath + "\"/>";

                String signatoryName = "";

//                if (objClsUserInfo.getCustomerSignatory() != null && !objClsUserInfo.getCustomerSignatory().isEmpty()) {
//                    signatoryName = " <tr>\n" +
//                            "<td align=\"left\" valign=\"top\" style=\"background-color:white;border:none;\">" +
//                            "" + objClsUserInfo.getCustomerSignatory() +
//                            "                        </td>\n" +
//                            "                    </tr>";
//                }
                replaceString(mailBodyStr, "#SING_BUSINESSNAME", "FOR ".concat(objClsUserInfo.getBusinessname()));
                replaceString(mailBodyStr, "#signature", signatureStr + "<br>" + objClsUserInfo.getCustomerSignatory());

            } else {
                String noteStr = "NOTE:- this is a system generated quotation and needs no signature";

                replaceString(mailBodyStr, "#SING_BUSINESSNAME", "AUTHORIZATION SIGNATORY <br><br>".concat(objClsUserInfo.getBusinessname()));
                replaceString(mailBodyStr, "#note", noteStr);
                replaceString(mailBodyStr, "#signature", objClsUserInfo.getCustomerSignatory());

            }


            if (logoImg != null && !logoImg.isEmpty()) {

                String headerStr = "<img alt=\"\" height=\"80px\" width=\"80px\" src=\"" + imagePath + "\"/>";

                replaceString(mailBodyStr, "#quotationceheaderImg", headerStr);

            } else {
//                String headerStr = "<td align=\"center\" valign=\"middle\">\n" +
//                        "           <h1> " + objClsUserInfo.getBusinessname() + "</h1>\n" +
//                        "        </td>";

                replaceString(mailBodyStr, "#quotationceheaderImg", objClsUserInfo.getBusinessname());
            }


            StringBuilder itemStringBuilder = new StringBuilder();
            int count = 0;

            double _totalTaxAmountFooter = 0.0;
            double _totalItemRateFooter = 0.0;
            double _totalItemDiscountFooter = 0.0;
            double _totalAmountFooter = 0.0;


            for (ClsQuotationOrderDetail current : lstClsQuotationOrderDetails) {
                count++;


//                Log.e("--ClsQuotationPdf--", "--for loop-- ");
                itemStringBuilder.append("<tr>");

                itemStringBuilder.append("<td>");
                itemStringBuilder.append(count);
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(current.getItem());
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(current.getUnit());
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(ClsGlobal.round(current.getRate(), 2));
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(current.getQuantity(), 2));
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(current.getAmount(), 2));
                itemStringBuilder.append("</td>");

                String _discountStr = "";

                if (current.getDiscount_per() != 0.0) {
                    _totalItemDiscountFooter += current.getDiscount_amt();

                    _discountStr = "".concat("[").concat(String.valueOf(ClsGlobal.round(current.getDiscount_per(), 2)))
                            .concat("%] ");
                    _discountStr = _discountStr.concat(String.valueOf(ClsGlobal.round(current.getDiscount_amt(), 2)));
                } else {
                    _discountStr = "0.00";
                }

                if (current.getAmount() != 0.0) {
                    _totalItemRateFooter += current.getAmount();
                }

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(_discountStr);
                itemStringBuilder.append("</td>");

                // Check for Tax is Apply or Not.
                if (obj.getApplyTax().equalsIgnoreCase("YES")) {
                    itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                    _totalTaxAmountFooter += current.getTotalTaxAmount();

                    itemStringBuilder.append("" + ClsGlobal.round(current.getTotalTaxAmount(), 2));
                    itemStringBuilder.append("</td>");
                } else {
                    itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                    itemStringBuilder.append("0.00");
                    itemStringBuilder.append("</td>");
                }

                double _grandTotal = (current.getAmount() - current.getDiscount_amt());

                if (obj.getApplyTax() != null
                        && obj.getApplyTax().equalsIgnoreCase("YES")) {
                    _grandTotal = _grandTotal + current.getTotalTaxAmount();
                }

                _totalAmountFooter += _grandTotal;

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(_grandTotal, 2));
                itemStringBuilder.append("</td>");

                Gson gson2 = new Gson();
                String jsonInString2 = gson2.toJson(current);
                Log.d("--URL--", "current: " + jsonInString2);

                itemStringBuilder.append("</tr>");
            }

//            Log.e("--ClsQuotationPdf--", "--step 13-- ");

            replaceString(mailBodyStr, "#QuotationBody", itemStringBuilder.toString());

            if (_totalItemRateFooter != 0) {
                replaceString(mailBodyStr, "#total_item_amount",
                        String.valueOf(ClsGlobal.round(_totalItemRateFooter, 2)));
            } else {
                replaceString(mailBodyStr, "#total_item_amount", "0.00");
            }

//            Log.e("--ClsQuotationPdf--", "--step 14-- ");

            if (_totalItemDiscountFooter != 0) {
                replaceString(mailBodyStr, "#total_item_discount",
                        String.valueOf(ClsGlobal.round(_totalItemDiscountFooter, 2)));
                replaceString(mailBodyStr, "#Discount", "Discount:\u20B9 "
                        + (ClsGlobal.round(_totalItemDiscountFooter, 2)));

            } else {
                replaceString(mailBodyStr, "#total_item_discount", "0.00");
                replaceString(mailBodyStr, "#Discount", "Discount:\u20B9 0.00");

            }
//            Log.e("--ClsQuotationPdf--", "--step 15-- ");
            if (_totalTaxAmountFooter != 0) {
                replaceString(mailBodyStr, "#total_tax",
                        (ClsGlobal.round(_totalTaxAmountFooter, 2)));
                replaceString(mailBodyStr, "#TotalTaxAmount",
                        "Total Tax:\u20B9 " + (ClsGlobal.round(_totalTaxAmountFooter, 2)));

            } else {
                replaceString(mailBodyStr, "#total_tax", "0.00");
                replaceString(mailBodyStr, "#TotalTaxAmount", "Total Tax:\u20B9 0.00");

            }
//            Log.e("--ClsQuotationPdf--", "--step 16-- ");
            if (_totalAmountFooter != 0) {
                replaceString(mailBodyStr, "#total_amount",
                        (ClsGlobal.round(_totalAmountFooter, 2)));
            } else {
                replaceString(mailBodyStr, "#total_amount", "0.00");
            }

            replaceString(mailBodyStr, "#AMTINWORD", ClsGlobal.convertToIndianCurrency(ClsGlobal.round(obj.getGrandTotal(), 2)));

            replaceString(mailBodyStr, "#TotalAmount",
                    "Total Amount:\u20B9 " + (ClsGlobal.round(_totalItemRateFooter, 2)));
            replaceString(mailBodyStr, "#GrandTotal",
                    "Grand Total:\u20B9  " + (ClsGlobal.round(_totalAmountFooter, 2)));

            Log.e("--ClsQuotationPdf--", "--step 17-- ");
            List<ClsTerms> mList = new ArrayList<>();

            String Type = "QUOTATION";


       /*     if (obj.getSaleType() != null &&
                    obj.getSaleType().equalsIgnoreCase("Wholesale Quotation")) {
                Type = "QUOTATION";
            } else if (obj.getSaleType() != null
                    && obj.getSaleType().equalsIgnoreCase("Retail Quotation")) {
                Type = "QUOTATION";
            }
*/
            String _whereTerms = " AND [TERM_TYPE] LIKE ".concat("'%").concat(Type).concat("%' ");
            mList = new ClsTerms(context).getInvoiceTerms(_whereTerms);

            String terms = "";
            StringBuffer stringBuffer = new StringBuffer();

            if (mList != null && mList.size() != 0) {
                for (ClsTerms objClsTerms : mList) {

                    stringBuffer.append("\t\t\t\t<li>")
                            .append(objClsTerms.getmTerms()).append("</li>\n");
                }

                terms = "<tr>\n" +
                        "        <td colspan=\"2\" align=\"left\" valign=\"top\">\n" +
                        "            <b> T&C </b>\n" +
                        "\t\t\t<ul>\n" +

                        stringBuffer.toString() +

                        "\t\t\t</ul>\n" +
                        "        </td>\n" +
                        "    </tr>";

                replaceString(mailBodyStr, "#Terms", terms);
            } else {
                replaceString(mailBodyStr, "#Terms", "");
            }
//            Log.e("--ClsQuotationPdf--", "--step 19-- ");
            int maxLogSize = 1000;

            for (int i = 0; i <= mailBodyStr.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > mailBodyStr.length() ? mailBodyStr.length() : end;

                Log.v("ExpenseMisBody- ", mailBodyStr.substring(start, end));


                Log.d("--PDF--", "Exp: " + mailBodyStr.substring(start, end));


            }

        } catch (Exception e) {
//            Log.e("--ClsQuotationPdf--", "--Exception-- ");
            e.printStackTrace();
        }
//        Log.e("--ClsQuotationPdf--", "--step 20-- ");

        if (mode.equalsIgnoreCase("Send To WhatsApp")) {
            clsQuotationPdf.setPdfFilePath(path.concat(PathName));
        } else {
            clsQuotationPdf.setPdfFilePath(PathName);
        }

//        Log.e("--ClsQuotationPdf--", "--step 21-- ");

        clsQuotationPdf.setPdfFilePath(PathName);
        clsQuotationPdf.setPdfString(mailBodyStr.toString());

//        Log.e("--ClsQuotationPdf--", "--step 22-- ");
        return clsQuotationPdf;

    }

    private static void replaceString(StringBuilder sb,
                                      String toReplace,
                                      String replacement) {
        int index = -1;
        while ((index = sb.lastIndexOf(toReplace)) != -1) {
            sb.replace(index, index + toReplace.length(), replacement);
        }
    }
}
