package com.mehmetsakiratasayin.dailycalorierecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {



    TextView kcalTextName, nameTextName, vkiTextName, ageTextName,SuccessTextName;
    SQLiteDatabase database, mDatabase;
    int Kcalkey, Agekey;
    float Vkikey,count,successKey;
    String Namekey,success,first;
    private GroceryAdapter mAdapter;
    private int kalori = 1;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Günün Tarihini Alır
        Date simdikiZaman = new Date();
        System.out.println(simdikiZaman.toString());
//Alınan Tarihi Bastırma, Gösterim
        DateFormat dateFormatDate = android.text.format.DateFormat.getDateFormat(this);
        String dateStr = dateFormatDate.format(simdikiZaman);
        android.text.format.DateFormat.getTimeFormat(this);
        System.out.println(dateStr + " ");
//Gün,Ay,Yılı ayrı ele alma
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.YEAR)); // yıl
        System.out.println(calendar.get(Calendar.DATE)); // ayın kaçıncı günü
        System.out.println(calendar.get(Calendar.WEEK_OF_MONTH));
        int currentday = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));// Ay

//İD Atamaları
        ImageView imageView = findViewById(R.id.imageView);
        kcalTextName = findViewById(R.id.kcalTextName);
        nameTextName = findViewById(R.id.nameTextName);
        vkiTextName = findViewById(R.id.vkiTextName);
        ageTextName = findViewById(R.id.ageTextName);
        SuccessTextName=findViewById(R.id.SuccessTextName);
//Sqlite Adaptor
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

//SherePreferences
        sharedPreferences = getSharedPreferences("com.mehmetsakiratasayin.dailycalorierecord", Context.MODE_PRIVATE);
        int lastday = sharedPreferences.getInt("day", 0);
        Kcalkey = sharedPreferences.getInt("Kcalkey", 0);
        count = sharedPreferences.getFloat("count", 0);
        successKey = sharedPreferences.getFloat("successKey", 0);
        Vkikey = sharedPreferences.getFloat("Vkikey", 0);
        Namekey = sharedPreferences.getString("Namekey", " ");
        success = sharedPreferences.getString("success", " ");
        Agekey = sharedPreferences.getInt("Agekey", 0);
        first = sharedPreferences.getString("firstkey", "first");
        System.out.println(Namekey + " " + " " + Agekey + " " + Kcalkey + " " + " " + Vkikey);
        ageTextName.setText(String.valueOf(Agekey));
        nameTextName.setText(Namekey);
        SuccessTextName.setText(success);
        System.out.println(successKey/count);
        System.out.println(success);
        System.out.println(successKey);
        System.out.println(count);
        kcalTextName.setText(String.valueOf(Kcalkey));
        vkiTextName.setText(String.valueOf(Vkikey));
        kalori = Kcalkey;


//Sqlite Ekleme Kısmı

        if (lastday != currentday) {

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt("day", currentday);
            editor.commit();
            ContentValues cv = new ContentValues();
            cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, "Girilmedi ");
            cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, dateStr);
            cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, "-");
            cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR,"-");
            cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS," ");

            mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
            mAdapter.swapCursor(getAllItems());
        } else {

        }
//Kaydırma İşlemleri
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                System.out.println("tıklıyo");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                System.out.println("tıklıyor");
                Dialog((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

//Görsel İçin Sqlite kısmı
        database = this.openOrCreateDatabase("Gorsel", MODE_PRIVATE, null);

        try {

            Cursor cursor = database.rawQuery("SELECT * FROM gorsel", null);
            int imageIx = cursor.getColumnIndex("image");

            while (cursor.moveToNext()) {

                byte[] bytes = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);


            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("Girmiyor1");
        }
  //ilk Girişte Profil Sayfasına götürme
        if(first=="first"){
            Intent intent = new Intent(MainActivity.this, Profil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
basari();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profil){
            Intent intent = new Intent(MainActivity.this, Profil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
finish();
            startActivity(intent);

        }
        else {
            Intent intent = new Intent(MainActivity.this, Hakkinda.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void Dialog(long id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custum_layout,null);
        alert.setView(customLayout);
      alert.setTitle("Günlük Tüketilen Kalori? ");

       alert.setCancelable(false);
        alert.setPositiveButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                 Intent intent =getIntent();
                finish();
                startActivity(intent);

            }
        });
        alert.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                EditText dialogKalori =customLayout.findViewById(R.id.editTextNumber);
              String  degerTo = dialogKalori.getText().toString();
               int deger=Integer.parseInt(degerTo);
               ContentValues cv = new ContentValues();



               if(deger>kalori){
                   int ilkSonuc=deger-kalori;

                   cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, " Kalori Dengesi:");

                   if(ilkSonuc<=100){
                       successKey=successKey+2;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR," ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ilkSonuc+" Kcal Fazla");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Çok İyi");

                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ilkSonuc>100 && ilkSonuc<=200){

                       successKey++;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR," ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ilkSonuc+" Kcal Fazla");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "İyi");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ilkSonuc>200 && ilkSonuc<=280){
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR," ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ilkSonuc+" Kcal Fazla");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Orta");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ilkSonuc>280 && ilkSonuc<=360){
                       successKey--;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();

                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, " ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, ilkSonuc+" Kcal Fazla");

                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Kötü");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else{
                       successKey=successKey-2;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, " ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, ilkSonuc+" Kcal Fazla");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Çok Kötü");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   basari();
                   Intent intent =getIntent();
                   finish();
                   startActivity(intent);
               }
               else if(deger<kalori){
                   int ikiSonuc=kalori-deger;

                   cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, " Kalori Dengesi:");

                   if(ikiSonuc<=100){
                       successKey=successKey+2;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ikiSonuc + " Kcal Eksik ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, " ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Çok İyi");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ikiSonuc>100 && ikiSonuc<=200){
                       successKey++;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ikiSonuc + " Kcal Eksik ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, " ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "İyi");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ikiSonuc>200 && ikiSonuc<=280){
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS, ikiSonuc + " Kcal Eksik ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, " ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Orta");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else if(ikiSonuc>280 && ikiSonuc<=360){

                       successKey--;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS," " );
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, ikiSonuc + " Kcal Eksik ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Kötü");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   else{
                       successKey=successKey-2;
                       count++;
                       editor.putFloat("count",count);
                       editor.putFloat("successKey",successKey);
                       editor.commit();
                       cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS," " );
                       cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR, ikiSonuc + " Kcal Eksik ");
                       cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Çok Kötü");
                       mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                       mAdapter.swapCursor(getAllItems());
                   }
                   Intent intent =getIntent();
                   finish();
                   startActivity(intent);
                   basari();
               }
               else{
                   successKey=successKey+2;
                   count++;
                   editor.putFloat("count",count);
                   editor.putFloat("successKey",successKey);
                   editor.commit();
                   cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, "Kalori Dengesi:");
                   cv.put(GroceryContract.GroceryEntry.COLUMN_PLUS," 0 ");
                   cv.put(GroceryContract.GroceryEntry.COLUMN_SOUR," 0 ");
                   cv.put(GroceryContract.GroceryEntry.COLUMN_STATUS, "Tam İsabet!");
                   mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,cv, GroceryContract.GroceryEntry._ID + "=" + id, null );
                   mAdapter.swapCursor(getAllItems());
                   Intent intent =getIntent();
                   finish();
                   startActivity(intent);
                   basari();
               }
            }
        });
        alert.show();
    }


    private void basari() {
        //Başarı kısmı
        SharedPreferences.Editor ed = sharedPreferences.edit();

        if(successKey/count<0){
            success="Başarısız";
            ed.putString("success",success);
            ed.commit();

        }
        else if(successKey/count==0){
            success="Belirsiz";
            ed.putString("success",success);
            ed.commit();
        }

        else {
            success="Başarılı";
            ed.putString("success",success);
            ed.commit();
        }

    }


}