package com.example.shrisha.firsttask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static android.icu.text.DateFormat.YEAR;

public class StudentDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Students.db";
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPT = "dept";
    private static final String COLUMN_YEAR = "year";
    private StudentDisplayAdapter listAdapter;
    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getTableStudents() {
        return TABLE_STUDENTS;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Toast.makeText(, "Database \'" + getDatabaseName() + "\' is created", Toast.LENGTH_SHORT).show();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENTS + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                COLUMN_NAME + " VARCHAR(30) UNIQUE NOT NULL, " +
                COLUMN_DEPT + " VARCHAR(10) NOT NULL, " +
                COLUMN_YEAR + " INTEGER NOT NULL );";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(sqLiteDatabase);
    }

    public StudentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public StudentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, StudentDisplayAdapter listAdapter) {
        super(context, name, factory, version);
        this.listAdapter = listAdapter;
    }

    //Add new row to database
    public  void add_student(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, student.getName());
        contentValues.put(COLUMN_DEPT, student.getDept());
        contentValues.put(COLUMN_YEAR, student.getYear());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        try {
            sqLiteDatabase.insert(TABLE_STUDENTS, null, contentValues);
            //listAdapter.refreshList();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
        sqLiteDatabase.close();
    }

    //Delete row from database
    public void delete_student(Student student) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_STUDENTS,
                COLUMN_NAME + " = \"" + student.getName() + "\" and "
                        + COLUMN_DEPT + " = \"" + student.getDept() + "\" and "
                        + COLUMN_YEAR + " = " +student.getYear(),
                null);
        sqLiteDatabase.close();
    }

    //Print out databse as a string

    /*public String getDBAsString() {
        String DBString = "\n";

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_STUDENTS;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            DBString +=(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)) + " " +
                        cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)) + " " +
                        cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)) + "\n");
        }
        cursor.close();
        db.close();

        return DBString;
    }*/

    //return number of rows in the table
    public long getRowCount() {
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_STUDENTS);
        db.close();
        return count;
    }

    public ArrayList<Student> getStudentList() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_ID, null);
        while (cursor.moveToNext()) {
            studentArrayList.add(new Student(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR))));
        }
        cursor.close();
        db.close();
        return studentArrayList;

    }

    public Student getStudentByID(int position) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_ID + " = " + position + ";", null);

        Student student = null;
        if (cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()) {
            student = new Student(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)));

            cursor.close();
        }
        db.close();
        return  student;
    }

    public int getIDByStudentInfo(String name, String dept, int year) {
        SQLiteDatabase db = getReadableDatabase();
        int studentID = -1;

        String queryString = "SELECT " + COLUMN_ID + " FROM " + TABLE_STUDENTS + " WHERE " +
                COLUMN_NAME + " = \"" + name + "\" AND " +
                COLUMN_DEPT + " = \"" + dept + "\" AND " +
                COLUMN_YEAR + " = " + year +  ";";

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()) {
            studentID = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return studentID;
    }

    public void updateByID(int id, String name, String department, int year) {
        SQLiteDatabase db = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_STUDENTS +
                " SET " + COLUMN_NAME + " = \"" + name + "\", " +
                COLUMN_DEPT + " = \""+ department+"\", " +
                COLUMN_YEAR + " = " + year +
                " WHERE " + COLUMN_ID + " = " + id + ";";
        db.execSQL(updateQuery);
        //listAdapter.refreshList();
        db.close();
    }
}
