package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.util.Log;

import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClsHtmlToPdf {


    public static ClsPrintPdf generatePDF(Context context,
                                          ClsInventoryOrderMaster obj,
                                          List<ClsInventoryOrderDetail> list_Current_Order,
                                          String mode) {


        ClsPrintPdf clsPrintPdf = new ClsPrintPdf();
        String path = "";
        StringBuilder mailBodyStr = new StringBuilder();
        String PathName = ClsGlobal.InvoiceFileName;

        try {
            path = ClsGlobal.InvoiceFilePath;
            Log.d("FILE--", "DemoForPDF_Path-- " + path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            StringBuilder _checkOutInvoice = ClsGlobal.getCheckOutInvoiceTemplate(context);
            mailBodyStr.append(_checkOutInvoice.toString());

            ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(context);

            //-----------------------------------Header---------------------------------------//

            if (objClsUserInfo.getBillTitle() != null && !objClsUserInfo.getBillTitle().isEmpty()) {
                replaceString(mailBodyStr, "#INVOICE_TITLE", objClsUserInfo.getBillTitle().toUpperCase());
            } else {
                replaceString(mailBodyStr, "#INVOICE_TITLE", "INVOICE");
            }

            replaceString(mailBodyStr, "SHOP_NAME", objClsUserInfo.getBusinessname().toUpperCase());
            replaceString(mailBodyStr, "SHOP_ADDRESS", objClsUserInfo.getBusinessaddress().toUpperCase());
            String stateCityPincode = objClsUserInfo.getState().toUpperCase().concat(", ").concat(objClsUserInfo.getCity().toUpperCase()).concat("-").concat(objClsUserInfo.getPincode());
            replaceString(mailBodyStr, "CITY_PINCODE", stateCityPincode);

            replaceString(mailBodyStr, "CONTACT_EMAIL", "".concat("EMAIL:").concat(objClsUserInfo.getEmailaddress().toUpperCase()));
            replaceString(mailBodyStr, "CONTACT_PERSON_NAME", objClsUserInfo.getContactPersonName().toUpperCase());
            replaceString(mailBodyStr, "CONTACT_PERSON_CONTACT", "".concat(" CONTACT:") + objClsUserInfo.getMobileNo());

            String CompanyGST = "";
            if (objClsUserInfo.getGstnumber() != null && !objClsUserInfo.getGstnumber().isEmpty()) {
                CompanyGST = "GST NO.: ".concat(objClsUserInfo.getGstnumber().toUpperCase());
            }

            replaceString(mailBodyStr, "#CompanyGST", CompanyGST);
            replaceString(mailBodyStr, "customerMobileNumber", obj.getMobileNo());
            replaceString(mailBodyStr, "CUSTOMER_NAME", obj.getCustomerName());

            String Cust_CompanyGST = "";
            if (obj.getCompanyName() != null && !obj.getCompanyName().isEmpty()) {
                Cust_CompanyGST = "".concat(obj.getCompanyName());
            }

            if (obj.getGSTNo() != null && !obj.getGSTNo().isEmpty()) {
                Cust_CompanyGST = Cust_CompanyGST.concat(", GST NO.: ").concat(obj.getGSTNo());
            }

            replaceString(mailBodyStr, "CUST_COMPANY_GST", Cust_CompanyGST);
            replaceString(mailBodyStr, "invoice_no", String.valueOf(obj.getOrderNo()));
            replaceString(mailBodyStr, "print_value", ClsGlobal.getEntryDateAndTime());

            String cust_address_value = "-";
            if (obj.getCustAddress() != null && !obj.getCustAddress().isEmpty()) {
                cust_address_value = "".concat(obj.getCustAddress());
            }

            replaceString(mailBodyStr, "cust_address_value", cust_address_value);
            Log.d("--Address--", "Address: " + objClsUserInfo.getBusinessaddress());

            // -------------------------- Adding Single Items one by one ------------------------------//

            String logoImg = ClsGlobal.getLogo();
            String imagePath = "file://" + logoImg;

            String signatureImg = ClsGlobal.getSignature();
            String SignatureImgPath = "file://" + signatureImg;

            Log.d("--Image--", "Path: " + signatureImg);
            Log.d("--Image--", "html: " + SignatureImgPath);


            if (signatureImg != null && !signatureImg.isEmpty()) {

                replaceString(mailBodyStr, "#note", "");


                String signatureStr = " <tr>\n" +
                        "<td align=\"left\" valign=\"bottom\" style=\"background-color:white;border:none;\">" +
                        "                            <h3>For " + objClsUserInfo.getBusinessname() + "</h3> \n" +
                        "                        </td>\n" +
                        "                    </tr> <tr>" +
                        "<td align=\"left\" valign=\"middle\" style=\"background-color:white;border:none;\">" +
                        "                            <img height=\"70px\" width=\"70px\" src=\"" + SignatureImgPath + "\"/>\n" +
                        "                        </td>\n" +
                        "                    </tr>";

                Log.d("--Image--", "headerStr: " + signatureStr);

                String signatoryName = "";

                if (objClsUserInfo.getCustomerSignatory() != null && !objClsUserInfo.getCustomerSignatory().isEmpty()) {

                    signatoryName = " <tr>\n" +
                            "<td align=\"left\" valign=\"top\" style=\"background-color:white;border:none;\">" +
                            "" + objClsUserInfo.getCustomerSignatory() +
                            "                        </td>\n" +
                            "                    </tr>";
                }
                replaceString(mailBodyStr, "#signature", signatureStr + signatoryName);

            } else {

                String signatureStr = " <tr>\n" +
                        "                        <td align=\"left\" valign=\"top\">\n" +
                        "                            AUTHORIZATION SIGNATORY\n" +
                        "                        </td>\n" +
                        "                        <td align=\"left\" valign=\"top\">\n" + objClsUserInfo.getBusinessname() +
                        "                        </td>\n" +
                        "                    </tr>";

                String noteStr = "<tr>\n" +
                        "        <td colspan=\"2\" align=\"left\" valign=\"middle\">\n" +
                        "            NOTE:- this is a system generated invoice and needs no signature\n" +
                        "        </td>\n" +
                        "    </tr>";

                replaceString(mailBodyStr, "#note", noteStr);
                replaceString(mailBodyStr, "#signature", signatureStr);

            }

            if (logoImg != null && !logoImg.isEmpty()) {

                String headerStr = "<td align=\"left\" valign=\"middle\">\n" +
                        "            <table style=\"border:none;\">\n" +
                        "                <tr>\n" +
                        "                    <td align=\"left\" valign=\"middle\">\n" +
                        "                        <img alt=\"\" height=\"80px\" width=\"80px\" src=\"" + imagePath + "\"/>\n" +
                        "                    </td>\n" +
                        "                    <td align=\"left\" valign=\"middle\">\n" +
                        "                        <h1> " + objClsUserInfo.getBusinessname() + "</h1>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table>            \n" +
                        "        </td>";

                replaceString(mailBodyStr, "#invoiceheader", headerStr);

            } else {
                String headerStr = "<td align=\"center\" valign=\"middle\">\n" +
                        "           <h1> " + objClsUserInfo.getBusinessname() + "</h1>\n" +
                        "        </td>";

                replaceString(mailBodyStr, "#invoiceheader", headerStr);
            }

            // -------------------------- Adding Single Items one by one ------------------------------//

            StringBuilder itemStringBuilder = new StringBuilder();

            int count = 0;

            double _totalTaxAmountFooter = 0.0;
            double _totalItemRateFooter = 0.0;
            double _totalItemDiscountFooter = 0.0;
            double _totalAmountFooter = 0.0;

            for (ClsInventoryOrderDetail current : list_Current_Order) {
                count++;

                itemStringBuilder.append("<tr>");

                itemStringBuilder.append("<td>");
                itemStringBuilder.append(count);
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(current.getItemCode());
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(current.getItem());
                itemStringBuilder.append("</td>");

                String hsnCode = "";
                String _seprator = "";
                String _details = "";
                String _comment = "";


                if (current.getHSN_SAC_CODE() != null && !current.getHSN_SAC_CODE().isEmpty()) {
                    hsnCode = "HSN/SAC:" + current.getHSN_SAC_CODE();
                }

                if (current.getItemComment() != null && !current.getItemComment().isEmpty()) {
                    _comment = current.getItemComment();
                    _seprator = ", ";
                }

                _details = hsnCode.concat(_seprator).concat(_comment);

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(_details);
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"left\" valign=\"top\">");
                itemStringBuilder.append(current.getUnit());
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(current.getSaleRate(), 2));
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(current.getQuantity(), 2));
                itemStringBuilder.append("</td>");

                itemStringBuilder.append("<td align=\"right\" valign=\"middle\">");
                itemStringBuilder.append(ClsGlobal.round(current.getAmount(), 2));
                itemStringBuilder.append("</td>");

                _totalItemRateFooter += current.getAmount();

                String _discountStr = "";

                if (current.getDiscount_per() != 0.0) {
                    _totalItemDiscountFooter += current.getDiscount_amt();

                    _discountStr = "".concat("[").concat(String.valueOf(ClsGlobal.round(current.getDiscount_per(), 2))).concat("%] ");
                    _discountStr = _discountStr.concat(String.valueOf(ClsGlobal.round(current.getDiscount_amt(), 2)));
                } else {
                    _discountStr = "0.00";
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

                itemStringBuilder.append("</tr>");

            }

            replaceString(mailBodyStr, "#invoiceBody", itemStringBuilder.toString());

//            //------------------------------------Footer---------------------------------------------//

            List<ClsTerms> mList = new ArrayList<>();

            String Type = "SALE INVOICE";


            if (obj.getSaleType() != null && obj.getSaleType().equalsIgnoreCase("Wholesale")) {
                Type = "WHOLESALE";
            } else if (obj.getSaleType() != null && obj.getSaleType().equalsIgnoreCase("Sale")) {
                Type = "SALE INVOICE";
            }

            String _whereTerms = " AND [TERM_TYPE] LIKE ".concat("'%").concat(Type).concat("%' ");
            mList = new ClsTerms(context).getInvoiceTerms(_whereTerms);

            String terms = "";
            StringBuffer stringBuffer = new StringBuffer();
            if (mList != null && mList.size() != 0) {
                for (ClsTerms objClsTerms : mList) {

                    stringBuffer.append("\t\t\t\t<li>").append(objClsTerms.getmTerms()).append("</li>\n");

                }

                terms = "<tr>\n" +
                        "        <td colspan=\"2\" align=\"left\" valign=\"top\">\n" +
                        "            <b>Terms*</b>\n" +
                        "\t\t\t<ul>\n" +

                        stringBuffer.toString() +

                        "\t\t\t</ul>\n" +
                        "        </td>\n" +
                        "    </tr>";

                replaceString(mailBodyStr, "#Terms", terms);
            } else {

                replaceString(mailBodyStr, "#Terms", "");
            }


            replaceString(mailBodyStr, "_mode", obj.getPaymentMode());


            replaceString(mailBodyStr, "_authorizationSignatory", objClsUserInfo.getBusinessname());
            replaceString(mailBodyStr, "_totalAmount", String.valueOf(ClsGlobal.round(_totalAmountFooter, 2)));


            if (objClsUserInfo.getLicenseType() != null || !objClsUserInfo.getLicenseType().equalsIgnoreCase("FREE")) {

                replaceString(mailBodyStr, "#PoweredbyfTouch", "Powered by fTouch" + "<br>");
            } else {
                replaceString(mailBodyStr, "#PoweredbyfTouch", "");
            }


            if (obj.getDiscountAmount() != 0) {

                replaceString(mailBodyStr, "_discountAmount", String.valueOf(ClsGlobal.round(obj.getDiscountAmount(), 2)));
            } else {
                replaceString(mailBodyStr, "_discountAmount", "0.00");
            }

            replaceString(mailBodyStr, "_paidAmt", String.valueOf(ClsGlobal.formatNumber(2, obj.getTotalPaybleAmount())));

            replaceString(mailBodyStr, "_word_payment", ClsGlobal.convertToIndianCurrency(ClsGlobal.formatNumber(2, obj.getTotalPaybleAmount())));

            if (_totalItemDiscountFooter != 0) {
                replaceString(mailBodyStr, "Total_item_discount", String.valueOf(ClsGlobal.round(_totalItemDiscountFooter, 2)));
            } else {
                replaceString(mailBodyStr, "Total_item_discount", "0.00");
            }

            if (_totalItemRateFooter != 0) {

                replaceString(mailBodyStr, "total_item_amount", String.valueOf(ClsGlobal.round(_totalItemRateFooter, 2)));
            } else {
                replaceString(mailBodyStr, "total_item_amount", "0.00");
            }


            if (_totalTaxAmountFooter != 0) {
                replaceString(mailBodyStr, "Total_Tax", String.valueOf(ClsGlobal.round(_totalTaxAmountFooter, 2)));
            } else {
                replaceString(mailBodyStr, "Total_Tax", "0.00");
            }

            if (obj.getTotalReceiveableAmount() != 0) {
                replaceString(mailBodyStr, "Total_Amount", String.valueOf(ClsGlobal.round(_totalAmountFooter, 2)));
            } else {
                replaceString(mailBodyStr, "Total_Amount", "0.00");
            }

            if (obj.getPaidAmount() > 0.0) {
                replaceString(mailBodyStr, "Received_Amt", String.valueOf(ClsGlobal.round(obj.getPaidAmount(), 2)));
            } else {
                replaceString(mailBodyStr, "Received_Amt", "0.00");
            }

            String _adjustmentAmount = "";
            if (obj.getDifferent_Amount_mode().equalsIgnoreCase("ADJUSTMENT") &&
                    obj.getAdjumentAmount() > 0.0) {

                _adjustmentAmount = " <tr>" +
                        "       <td align=\"right\" valign=\"middle\" colspan=\"3\">" +
                        "           ADJUSTMENT AMOUNT" +
                        "       </td>" +
                        "       <td align=\"right\" valign=\"middle\">" + ClsGlobal.round(obj.getAdjumentAmount(), 2) +
                        "       </td>" +
                        "   </tr>";

                replaceString(mailBodyStr, "#adjustment_amt", _adjustmentAmount);
            } else {

                replaceString(mailBodyStr, "#adjustment_amt", "");

            }

            String _pendingAmount = "";

            if (!obj.getDifferent_Amount_mode().equalsIgnoreCase("ADJUSTMENT") &&
                    obj.getAdjumentAmount() > 0.0) {

                _pendingAmount = "<tr>" +
                        "       <td align=\"right\" valign=\"middle\" colspan=\"3\">" +
                        "           PENDING AMOUNT" +
                        "       </td>" +
                        "       <td align=\"right\" valign=\"middle\">" + ClsGlobal.round(obj.getAdjumentAmount(), 2) +
                        "       </td>" +
                        "   </tr>";

                replaceString(mailBodyStr, "#pending_Amt", _pendingAmount);
            } else {
                replaceString(mailBodyStr, "#pending_Amt", "");
            }


            int maxLogSize = 1000;
            for (int i = 0; i <= mailBodyStr.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > mailBodyStr.length() ? mailBodyStr.length() : end;

                Log.v("ExpenseMisBody- ", mailBodyStr.substring(start, end));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mode.equalsIgnoreCase("Send To WhatsApp")) {
            clsPrintPdf.setPdfFilePath(path.concat(PathName));
        } else {
            clsPrintPdf.setPdfFilePath(PathName);
        }

        clsPrintPdf.setPdfString(mailBodyStr.toString());
        return clsPrintPdf;

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
