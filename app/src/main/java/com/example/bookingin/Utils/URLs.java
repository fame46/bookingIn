package com.example.bookingin.Utils;

public class URLs {
    public static final String URL = "http://muharib.org/BookingIn";

    //users
    public static final String LOGIN = URL+"/user/login.php";
    public static final String REGISTER = URL+"/user/create_user.php";

    //menu
    public static final String CREATEMENU = URL+"/menu/upload_menu.php";
    public static final String GETMOBIL = URL+"/menu/get_mobil.php";
    public static final String GETKAMERA = URL+"/menu/get_kamera.php";
    public static final String GETALATCAMPING = URL+"/menu/get_alatcamping.php";
    public static final String DELETEMENU = URL+"/menu/delete_menu.php";

    //transaksi
    public static final String CREATEPESANAN = URL+"/transaksi/create_pesanan.php";
    public static final String GETCART = URL+"/transaksi/read_pesanan_cart.php";
    public static final String GETTOTALBAYAR = URL+"/transaksi/total_bayar.php";
    public static final String GETTOTALBAYAR2 = URL+"/transaksi/total_bayar2.php";
    public static final String GETHISTORY = URL+"/transaksi/read_pesanan_history.php";
    public static final String GETLAPORAN = URL+"/transaksi/read_pesanan_laporan.php";
    public static final String GETPERMINTAAN = URL+"/transaksi/read_pesanan_permintaan.php";
    public static final String GETPERMINTAANDETAIL = URL+"/transaksi/read_pesanan_permintaandetail.php";
    public static final String GETLAPORANDETAIL = URL+"/transaksi/read_pesanan_laporansetail.php";
    public static final String EDITSTATUSCART = URL+"/transaksi/edit_status_cart.php";
    public static final String EDITSTATUSTERIMA = URL+"/transaksi/edit_status_terima.php";
    public static final String EDITSTATUSTOLAK = URL+"/transaksi/edit_status_tolak.php";
}
