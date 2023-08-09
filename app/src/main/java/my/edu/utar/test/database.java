package my.edu.utar.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class database {


    public static final String MYDATABASE_NAME = "DATABASE_BILL";
    public static final int MYDATABASE_VERSION = 1;

    public static final String MYDATABASE_TABLE = "EQUAL_BILL";
    public static final String MYDATABASE_TABLE1 = "CUSTOM_BILL";

    public static final String BILL_NAME = "Bill_Name";
    public static final String BILL_TOTAL = "Total_Amount";
    public static final String DATA_NAME = "Name";
    public static final String DATA_AMOUNT = "Amount";


    //SQL command to create a table with 3 columns
    private static final String SCRIPT_CREATE_DATABASE = "create table " + MYDATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BILL_NAME + " text not null, "
            + BILL_TOTAL + " double not null, "
            + DATA_AMOUNT + " double not null); ";


    //variables are for database creation
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor for SQLiteAdapter
    public database(Context c) {
        context = c;
    }



    //open the databse to write something
    public database openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    //open the databse to read something
    public database openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    //to write the equal breakdown data into table with THREE column
    public long insertequal(String content, double content_2, double content_4) {
        ContentValues contentValues = new ContentValues();
        //to write content to the column of KEY_CONTENT
        contentValues.put(BILL_NAME, content);
        contentValues.put(BILL_TOTAL, content_2);
        contentValues.put(DATA_AMOUNT, content_4);
        openToWrite();

        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);


    }


    //to write the custom breakdown data into table
    public long insert(String content, double content_2, String[] content_3, double[] content_4) {
        ContentValues contentValues = new ContentValues();
        //to write content to the column of KEY_CONTENT
        contentValues.put(BILL_NAME, content);
        contentValues.put(BILL_TOTAL, content_2);
        contentValues.put(DATA_NAME, String.valueOf(content_3));
        contentValues.put(DATA_AMOUNT, String.valueOf(content_4));

        return sqLiteDatabase.insert(MYDATABASE_TABLE1, null, contentValues);
    }

    public String retrieveequal() {

        //which columns wanna to read
        String[] columns = new String[]{BILL_NAME, BILL_TOTAL, DATA_AMOUNT};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);

        String result = "";
        double count = 1;

        int index_CONTENT = cursor.getColumnIndex(BILL_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(BILL_TOTAL);
        int index_CONTENT_3 = cursor.getColumnIndex(DATA_AMOUNT);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + "Bill name         : " + cursor.getString(index_CONTENT) + "\n"
                    + "Total amount : RM" + cursor.getString(index_CONTENT_2) + "\n"
                    + "Each person   : RM" + cursor.getString(index_CONTENT_3) + "\n"
                    + "---------------------------------------------------------------------------------\n";

        }

        return result;
    }


    public String retrieve() {

        //which columns wanna to read
        String[] columns = new String[]{BILL_NAME, BILL_TOTAL, DATA_NAME, DATA_AMOUNT};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);

        String result = "";

        int index_CONTENT = cursor.getColumnIndex(BILL_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(BILL_TOTAL);
        int index_CONTENT_3 = cursor.getColumnIndex(DATA_NAME);
        int index_CONTENT_4 = cursor.getColumnIndex(DATA_AMOUNT);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + cursor.getString(index_CONTENT) + "; "
                    + cursor.getString(index_CONTENT_2) + "; "
                    + cursor.getString(index_CONTENT_3) + "\n";
        }

        return result;
    }

    // Add this method to your database class
    public List<String> retrieveAsList() {
        List<String> historyList = new ArrayList<>();

        String[] columns = new String[]{BILL_NAME, BILL_TOTAL, DATA_NAME, DATA_AMOUNT};

        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);

        int index_CONTENT = cursor.getColumnIndex(BILL_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(BILL_TOTAL);
        int index_CONTENT_3 = cursor.getColumnIndex(DATA_NAME);
        int index_CONTENT_4 = cursor.getColumnIndex(DATA_AMOUNT);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String historyItem = cursor.getString(index_CONTENT) + "; "
                    + cursor.getFloat(index_CONTENT_2) + "; "
                    + cursor.getString(index_CONTENT_3);
            historyList.add(historyItem);
        }

        cursor.close();
        return historyList;
    }




    //close the database
    public void close() {

        sqLiteHelper.close();
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        //constructor for SQLiteHelper
        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);


        }





        //create a table with column
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }
    }

}
