package com.maciek.myapplication;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Maciek on 2016-06-04.
 */
public class Entry  implements Serializable {

    private String category;
    private String description;
    private String image1;
    private String image2;
    private String date;
    private String userEmail;
    private int isSent=0;
    private int id;
    private String longitude;
    private String latitude;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Entry()
    {

    }

    public Entry(String category, String description, String date, String userEmail, int isSent, int id, String latitude, String longitude)
    {
        super();
        this.category = category;
        this.description = description;
        this.date = date;
        this.userEmail = userEmail;
        this.isSent = isSent;
        this.id = id;
        this.latitude = latitude;
        this.longitude= longitude;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public int getIsSent() {
        return isSent;
    }

    public String getCategory()
    {
        return category;
    }

    public String getDescription()
    {
        return description;
    }

    public String getImage1()
    {
        return image1;
    }

    public String getImage2()
    {
        return image2;
    }

    public void setCategory(String category)
    {
        this.category=category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = getStringFromBitmap(image1);
    }

    public void setImage2(Bitmap image2) {
        this.image2 = getStringFromBitmap(image2);
    }

    public void setDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        date = sdf.format(new Date());
    }

    public String getDate() {
        return date;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {

        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private String getImagesArr()
    {

        StringBuilder sb = new StringBuilder();
        if(getImage1() != null && !getImage1().equals(""))
        {
            sb.append(getImage1());
            if(getImage2() != null && !getImage2().equals(""))
            {
                sb.append("#images_separator#");
                sb.append(getImage2());
            }
        }
        else if(getImage2() != null && !getImage2().equals(""))
        {
            sb.append(getImage2());
        }

        return sb.toString();
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("category", getCategory());
            jsonObject.put("description", getDescription());
            jsonObject.put("imagesArr", getImagesArr());
            jsonObject.put("userEmail", getUserEmail());
            jsonObject.put("date", getDate());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
