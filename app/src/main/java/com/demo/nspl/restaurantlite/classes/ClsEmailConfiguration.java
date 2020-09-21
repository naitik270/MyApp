package com.demo.nspl.restaurantlite.classes;

public class ClsEmailConfiguration {

    String FromEmailId = "", Password = "", SMTP = "",
            Display_Name = "",
            Port = "", SSl = "",
            Active = "";


    String EmailConfiguration = "";


    public String getEmailConfiguration() {
        return EmailConfiguration;
    }

    public void setEmailConfiguration(String emailConfiguration) {
        EmailConfiguration = emailConfiguration;
    }

    public String getFromEmailId() {
        return FromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        FromEmailId = fromEmailId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSMTP() {
        return SMTP;
    }

    public void setSMTP(String SMTP) {
        this.SMTP = SMTP;
    }

    public String getDisplay_Name() {
        return Display_Name;
    }

    public void setDisplay_Name(String display_Name) {
        Display_Name = display_Name;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getSSl() {
        return SSl;
    }

    public void setSSl(String SSl) {
        this.SSl = SSl;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }


}
