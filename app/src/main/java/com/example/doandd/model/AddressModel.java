package com.example.doandd.model;

import java.util.HashMap;
import java.util.Map;

public class AddressModel {
    String id;
    String ten;
    String so_dien_thoai;
    String tinh;
    String huyen;
    String xa;
    String dia_chi;
    String loai_dia_chi;
    boolean mac_dinh;

    public AddressModel(String id, String ten, String so_dien_thoai, String tinh,
            String huyen, String xa, String dia_chi, String loai_dia_chi, boolean mac_dinh){
         this.id = id;
         this.ten = ten;
         this.so_dien_thoai = so_dien_thoai;
         this.tinh = tinh;
         this.huyen = huyen;
         this.xa = xa;
         this.dia_chi = dia_chi;
         this.loai_dia_chi = loai_dia_chi;
         this.mac_dinh = mac_dinh;
    };

    public String getId(){
        return id;
    }
    public String getName(){
        return ten;
    }
    public String getPhone(){
        return so_dien_thoai;
    }
    public String getTinh(){
        return tinh;
    }
    public String getHuyen(){
        return huyen;
    }
    public String getXa(){
        return xa;
    }
    public String getAddress(){
        return dia_chi;
    }
    public String getTypeAddress(){
        return loai_dia_chi;
    }
    public boolean getMacDinh() {return  mac_dinh;}

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("recipientName", ten);
        data.put("phoneNumber", so_dien_thoai);
        data.put("province", tinh);
        data.put("district", huyen);
        data.put("commune", xa);
        data.put("address", dia_chi);
        data.put("typeAddress", loai_dia_chi);
        data.put("addressDefault", mac_dinh);
        return data;
    }
}
