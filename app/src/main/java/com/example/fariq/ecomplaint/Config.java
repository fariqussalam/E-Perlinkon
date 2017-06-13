package com.example.fariq.ecomplaint;



public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://www.eperlinkon.disdagmakassar.com/api/insertdb.php";
    public static final String URL_GET_ALL = "http://www.eperlinkon.disdagmakassar.com/api/viewdb.php";
    public static final String URL_GET_KEL = "http://www.eperlinkon.disdagmakassar.com/api/getPengaduan.php?id=";
    public static final String URL_ABOUT="http://www.eperlinkon.disdagmakassar.com/api/tentang.php";


    //Keys that will be used to send the request to php scripts
    public static final String KEY_KEL_ID = "keluhanid";
    public static final String KEY_KEL_NOPENGADUAN = "noPengaduan";
    public static final String KEY_KEL_EMAIL = "email";
    public static final String KEY_KEL_NOHP = "nohp";
    public static final String KEY_KEL_NOKTP = "noktp";
    public static final String KEY_KEL_DETKEL = "detkel";
    public static final String KEY_KEL_JENKEL = "jenkel";
    public static final String KEY_KEL_IMKTP = "imktp";
    public static final String KEY_KEL_IM2 = "im2";
    public static final String KEY_KEL_IM3 = "im3";
    public static final String KEY_KEL_IM4 = "im4";
    public static final String KEY_KEL_JENTUT = "jentut";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "keluhanid";
    public static final String TAG_NOPENGADUAN="nopengaduan";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NOHP = "nohp";
    public static final String TAG_NOKTP = "noktp";
    public static final String TAG_JENKEL = "jenkel";
    public static final String TAG_JENTUT = "jentut";
    public static final String TAG_DETKEL = "detkel";
    public static final String TAG_STATUS = "status";
    public static final String TAG_SOLUSI = "solusi";
    public static final String TAG_WAKTU = "waktu";
    public static final String TAG_KONTEN = "konten";

    //public static final String TAG_IMKTP = "imktp";
    //public static final String TAG_IM2 = "im2";
    //public static final String TAG_IM3 = "im3";
    //public static final String TAG_IM4 = "im4";


    //employee id to pass with intent
 //   public static final String KEL_ID = "keluhanid";
}
