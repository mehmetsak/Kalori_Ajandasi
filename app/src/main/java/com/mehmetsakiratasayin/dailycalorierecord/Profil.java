package com.mehmetsakiratasayin.dailycalorierecord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Profil extends AppCompatActivity {
  Bitmap selectedImage;
  ImageView imageView2;
  EditText profilNameText,profilAgeText,profilKilogramText,profilMeterText,profilKcalText;
  SQLiteDatabase database;
  Button button;
  SharedPreferences sharedPreferences;
  Integer  kilogram,meter,kcal,age,Kilogramkey,Meterkey,Kcalkey,idIx,Agekey;
  Float vki,Vkikey;
  Intent intent;
  String Namekey,kilogramTo,meterTo,kcalTo,AgeTo,firstShared,name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        imageView2=findViewById(R.id.imageView2);
        profilNameText=findViewById(R.id.profilNameText);
        profilAgeText=findViewById(R.id.profilAgeText);
        profilKilogramText=findViewById(R.id.profilKilogramText);
        profilMeterText=findViewById(R.id.profilMeterText);
        profilKcalText=findViewById(R.id.profilKcalText);
        button=findViewById(R.id.button);


        sharedPreferences =this.getSharedPreferences("com.mehmetsakiratasayin.dailycalorierecord", Context.MODE_PRIVATE);
        Kilogramkey=sharedPreferences.getInt("Kilogramkey", 0);
        Meterkey=sharedPreferences.getInt("Meterkey", 0);
        Kcalkey=sharedPreferences.getInt("Kcalkey", 0);
        Vkikey=sharedPreferences.getFloat("Vkikey", 0);
        Namekey =sharedPreferences.getString("Namekey","");
        Agekey=sharedPreferences.getInt("Agekey",0);
        profilNameText.setText(Namekey);
        profilAgeText.setText(String.valueOf(Agekey));
        profilKilogramText.setText(String.valueOf(Kilogramkey));
        profilMeterText.setText(String.valueOf(Meterkey));
        profilKcalText.setText(String.valueOf(Kcalkey));

        System.out.println(Namekey+" " + " "+ Agekey+" " + Kilogramkey+" " + " " +Meterkey+ " " +Kcalkey+" "+Vkikey);


        intent =new Intent(getApplicationContext(),MainActivity.class);
//Sqlite Görsel Gösterimi
        database = this.openOrCreateDatabase("Gorsel",MODE_PRIVATE,null);
        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS gorsel (id INTEGER PRIMARY KEY,image BLOB)");

            Cursor cursor =database.rawQuery("SELECT * FROM gorsel WHERE id = ?",new String[] {String.valueOf(1)});
            int imageIx = cursor.getColumnIndex("image");
        idIx=cursor.getColumnIndex("id");
            while(cursor.moveToNext()){

              byte[]  bytes = cursor.getBlob(imageIx);
             Bitmap   bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                System.out.println( "id: " +cursor.getInt(idIx));
                imageView2.setImageBitmap(bitmap);


            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("İlk Girmeme");
            database.execSQL("CREATE TABLE IF NOT EXISTS gorsel (id INTEGER PRIMARY KEY,image BLOB)");
         //   database = this.openOrCreateDatabase("Gorsel",MODE_PRIVATE,null);
        }


    }

    public void save(View view) {
        firstShared = sharedPreferences.getString("firstkey", "first");
        if(profilNameText.getText().toString().trim().length() == 0 || profilAgeText.getText().toString().trim().length() <= 1
                || profilKilogramText.getText().toString().trim().length() <= 1  || profilMeterText.getText().toString().trim().length() <= 1
                || profilKcalText.getText().toString().trim().length() <= 1
        ) {
            Toast.makeText(Profil.this,"Tüm Boşlukları Doldurunuz",Toast.LENGTH_LONG).show();
        }
        else{
        name = profilNameText.getText().toString();
        AgeTo = profilAgeText.getText().toString();
        age=Integer.parseInt(AgeTo);
        kilogramTo = profilKilogramText.getText().toString();
        kilogram=Integer.parseInt(kilogramTo);
        meterTo =profilMeterText.getText().toString();
        meter=Integer.parseInt(meterTo);
        kcalTo =profilKcalText.getText().toString();
        kcal=Integer.parseInt(kcalTo);
        vki=((((float)kilogram)/((float)(meter)*(float)(meter))))*10000;

    System.out.println("oldu");
    sharedPreferences.edit().putString("Namekey",name).apply();
    sharedPreferences.edit().putInt("Agekey",age).apply();
    sharedPreferences.edit().putInt("Kilogramkey",kilogram).apply();
    sharedPreferences.edit().putInt("Meterkey",meter).apply();
    sharedPreferences.edit().putInt("Kcalkey",kcal).apply();
    sharedPreferences.edit().putFloat("Vkikey",vki).apply();

if (selectedImage==null){


    if(firstShared=="first"){
        Intent intent = new Intent(Profil.this, Hakkinda.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
finish();
        startActivity(intent);
    }
    else{
        Intent intent = new Intent(Profil.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }


}
else {

    Bitmap  smallImage = makeSmallerImage(selectedImage,300);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
    byte[]  byteArray = outputStream.toByteArray();
    try {
        //    database = this.openOrCreateDatabase("Gorsel",MODE_PRIVATE,null);
        database.execSQL("DELETE FROM gorsel WHERE id = 1");
        String sqlString = "INSERT INTO gorsel (image) VALUES (?)";
        SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
        sqLiteStatement.bindBlob(1,byteArray);
        sqLiteStatement.execute();

    } catch (Exception e) {

        System.out.println("Hata "+e);
    }

if(firstShared=="first"){
    Intent intent = new Intent(Profil.this, Hakkinda.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
finish();
    startActivity(intent);
}
else{
    Intent intent = new Intent(Profil.this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    finish();
    startActivity(intent);
}



}}
    }


    public void selectImage(View view) {
        //İzin İstemek için Liste de belirtmek
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        // Zaten izin verildiğinde galeriye ulaşmak
        else{
            Intent intentToGallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }
//İzin verildikten sonra gerçekleşme
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
//Görseli aktivasyon etme
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode==RESULT_OK && data != null){
            Uri imageData=data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);

                 imageView2.setImageBitmap(selectedImage);

                 //  Singleton singleton =Singleton.getInstance();
                   // singleton.setChosenImage(selectedImage);
                   //imageView2.setImageBitmap(singleton.getChosenImage());
                   // imageView2.setImageBitmap(selectedImage);

                }
                else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                  imageView2.setImageBitmap(selectedImage);

                  // Singleton singleton =Singleton.getInstance();
                  //singleton.setChosenImage(selectedImage);
                    //imageView2.setImageBitmap(singleton.getChosenImage());
                    //imageView2.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}