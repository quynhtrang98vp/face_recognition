package com.example.diem_danh_sv;

import java.io.Serializable;

public class Account implements Serializable {
    private String toEmail_id;
    private String toName;
    private String toMail;
    private String toVien;
    private String toDia_chi;
    private String toChuyen_nganh;

    public void Account(String toEmail_id, String toName, String toMail, String toVien,
                        String toDia_chi, String toChuyen_nganh) {
        this.toEmail_id = toEmail_id;
        this.toName = toName;
        this.toMail = toMail;
        this.toVien = toVien;
        this.toDia_chi = toDia_chi;
        this.toChuyen_nganh = toChuyen_nganh;
    }

    public String getToEmail_id() {
        return toEmail_id;
    }

    public void setToEmail_id(String toEmail_id) {
        this.toEmail_id = toEmail_id;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getToVien() {
        return toVien;
    }

    public void setToVien(String toVien) {
        this.toVien = toVien;
    }

    public String getToDia_chi(){
        return toDia_chi;
    }

    public void setToDia_chi(String toDia_chi) {
        this.toDia_chi= toDia_chi;
    }

    public String getToChuyen_nganh() {
        return toChuyen_nganh;
    }

    public void setToChuyen_nganh(String toChuyen_nganh) {
        this.toChuyen_nganh = toChuyen_nganh;
    }
}
