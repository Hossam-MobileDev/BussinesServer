package com.hashtagco.bussinesserver.Common;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.hashtagco.bussinesserver.Model.Admin;
import com.hashtagco.bussinesserver.Model.Category;
import com.hashtagco.bussinesserver.Model.Foods;
import com.hashtagco.bussinesserver.Model.Request;
import com.hashtagco.bussinesserver.Model.SubFoods;
import com.hashtagco.bussinesserver.Model.Token;
import com.hashtagco.bussinesserver.Model.User;
import com.hashtagco.bussinesserver.Remote.ApiService;
import com.hashtagco.bussinesserver.Remote.IGeoCoordinates;
import com.hashtagco.bussinesserver.Remote.RetrofitClient;
import com.hashtagco.bussinesserver.Remote.RetrofitClint;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Random;

public class Common {
    public static final String SHIPPER_TABLES = "Shipper";
    public static final String SHIPPER_TABLE = "Shipper";
    public static final String ORDER_NEED_SHIP = "OrdersNeedShip";
    public static final String SUCCESSFUL_RREQUEST_TO_CLIENT = "SuccessfulRequestToClient";
    public static final String DRIVER_LOCATION ="DriverLocation" ;
    public static final String CATEGORY ="Category" ;
    public static Token currentoken;

    public static Category category;
    public static Foods food;
    public static SubFoods subfooood;
    public static final String USER_DATA ="userdata" ;
    public static final String PASSWORD_KEY = "paswordkey";
    public static final String USER="User";
    public static final int PICK_IMAGE_REQUEST = 333;
    public static User currentUser;
    public static Admin currentAdmin;
    public static Request currentRequest;
    public static final String UPDATE = "Update";
    public static final String DELETE = "حذف";
    public static final int PIC_IMAGE_REQUEST = 71;
    public final static String USER_PHONE = "UserPhone";
    public final static String USER_PASSWORD = "UserPassword";
    public final static String USER_NAME = "UserName";
    public final static String CLIENT = "client";
    public final static String SERVER = "server";
    public static final String RATING="Rating";
    public static final String FOODS="Foods";
    public static final String BANNER="Banner";
    public static final String BASE_URL="https://fcm.googleapis.com";
    public static final String baseUrl = "https://maps.googleapis.com";
    public static String toopicName = "news";
    public static final String SUCESSfFUL_REQUEST_To_CLIENT="SuccessfulRequestToClient";
    public static final String USER_KEY="User";
    public static final String PWD_KEY="Password";
    public static SubFoods sbfood;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On My Way";
        else
            return "Shipping";
    }
    public static String getStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "Shipping";
            case "2":
                return "Shipped";
            default:
                return "Status Not Available";
        }
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static IGeoCoordinates getGeoCodeService() {
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }
    public static ApiService getFCMClinet()
    {
        return   RetrofitClint.getClint(BASE_URL).create(ApiService.class);
    }






    public static String converCodeToStatus(String Status)
    {
        String status;

        switch (Status)
        {

            case "0":
                status = "Placed";
                break;

            case "1":
                status="On my Way";
                break;

            case "2":
                status="shipping";
                break;

            default:
                status="no statue";
                break;

        }

        return status;
    }



    //Method Check Connected to Internet
    public static boolean isConnectedToInternet(Context context)
    {


        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager !=null)
        {

            NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();


            if (networkInfos!=null)
            {

                for (int i=0;i<networkInfos.length;i++)
                {
                    if (networkInfos[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }

            }

        }

        return false;
    }

    //This Function will convert Currency to number base on Location
    public static BigDecimal formatCurrency(String amount, Locale locale) throws ParseException
    {
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);

        if (format instanceof DecimalFormat)
            ((DecimalFormat)format).setParseBigDecimal(true);

        return (BigDecimal)format.parse(amount.replace("[^\\d.,]",""));
    }

    public static Bitmap scaleBitmap (Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX=0, pivotY=0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }
    public static String getFileName(ContentResolver contentResolver, Uri fileUri) {
        String result = null;
        if (fileUri.getScheme().equals("content")){
            Cursor cursor = contentResolver.query(fileUri,null,null,null,null);
            try {
                if (cursor != null &&  cursor.moveToFirst() )
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }finally {
                cursor.close();
            }
        }
        if (result == null){
            result = fileUri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
                result = result.substring(cut+1);
        }
        return result;
    }
    public static String generateChatRoomId(String a , String b){
        if (a.compareTo(b)>0)
            return new StringBuilder(a).append(b).toString();
        else if (a.compareTo(b)<0)   return new StringBuilder(b).append(a).toString();
        else return new StringBuilder("chat_error").append(new Random().nextInt()).toString();
    }
}
