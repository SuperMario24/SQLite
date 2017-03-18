package com.example.saber.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateDatabase;
    private MyDatabaseHelper myDatabaseHelper;

    private Button btnAddData;
    private Button btnUpdateData;
    private Button btnDeleteData;
    private Button btnQueryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreateDatabase = (Button) findViewById(R.id.btn_create_database);
        btnAddData = (Button) findViewById(R.id.btn_add_data);
        btnUpdateData =(Button) findViewById(R.id.btn_update_data);
        btnDeleteData = (Button)findViewById(R.id.btn_delete_data);
        btnQueryData = (Button)findViewById(R.id.btn_query_data);


        //创建数据库帮助类,如果版本更新的会自动执行onUpgrade()方法
        //myDatabaseHelper = new MyDatabaseHelper(this,"BookStore,db",null,1);
        myDatabaseHelper = new MyDatabaseHelper(this,"BookStore,db",null,2);

        /**
         * 创建表
         */
        btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建数据库，getWritableDatabase()--打开或创建，会返回一个SQLiteDatabase对象
                myDatabaseHelper.getWritableDatabase();
            }
        });

        /**
         * 插入数据
         */
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                //插入第一条数据
                ContentValues values = new ContentValues();
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("book",null,values);//第一个为表明，第二个一般不用为null就可以，第三个为插入的数据 ContentValues
                //清空一下
                values.clear();
                //开始装第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                db.insert("book",null,values);

                //SQL操作
                db.execSQL("insert into book (name, author, pages, price) values (?,?,?,?)",new String[]{"The Plane","Steven","754","24.25"});
            }
        });

        /**
         * 更新数据
         */
        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                //第一个参数表明，第二个为ContentValues数据，第三个是where条件，第四个是where的值
                db.update("book",values,"name = ?",new String[]{"The Da Vinci Code"});

                //SQL操作,set里面是要更新的数据，where是筛选条件
                db.execSQL("update book set price = ? where name = ?",new String[]{"10.99","The Da Vinci Code"});
            }
        });

        /**
         * 删除数据
         */
        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                //第一个参数为表名，第二个为where，第三个为where的值
                db.delete("book","pages > ?",new String[]{ "500" });

                //SQL操作
                db.execSQL("delete from book where pages > ?",new String[]{"500"});
            }
        });


        /**
         * 查询数据
         */
        btnQueryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                //查询表中所有数据
                Cursor c = db.query("book",null,null,null,null,null,null);
                while(c.moveToNext()){
                    String name = c.getString(c.getColumnIndex("name"));
                    String author = c.getString(c.getColumnIndex("author"));
                    int pages = c.getInt(c.getColumnIndex("pages"));
                    double price = c.getDouble(c.getColumnIndex("price"));
                    Log.d("info","book name is "+ name);
                    Log.d("info","book author is "+ author);
                    Log.d("info","book pages is "+ pages);
                    Log.d("info","book price is "+ price);
                }
                c.close();

                //SQL操作
                db.rawQuery("select * from book",null);//返回Cursor对象和上面一样的操作
            }

        });

    }
}
