package com.demo.nspl.restaurantlite.SMS;

public class ContactVO {


    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private String first_letter;
    boolean isHeader = false;

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public ContactVO() {

    }
    public ContactVO(String first_letter) {
        this.first_letter = first_letter;
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        this.ContactImage = ContactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

}
