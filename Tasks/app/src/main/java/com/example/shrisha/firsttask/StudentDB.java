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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.icu.text.DateFormat.YEAR;

public class StudentDB extends SQLiteOpenHelper {
    private String TAG = StudentDB.class.getSimpleName();

    private final String DATABASE_NAME = "Students.db";

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STUDENTS = "students";
    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_DEPT = "dept";
    private final String COLUMN_YEAR = "year";

    private SQLiteDatabase sqLiteDatabase;

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

    //Add new row to database
    public void add_student(Student student) throws SQLiteConstraintException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, student.getName());
        contentValues.put(COLUMN_DEPT, student.getDept());
        contentValues.put(COLUMN_YEAR, student.getYear());

        sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.insert(TABLE_STUDENTS, null, contentValues);

        sqLiteDatabase.close();
    }

    //Delete row from database
    public void delete_student(Student student) {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_STUDENTS,
                COLUMN_NAME + " = \"" + student.getName() + "\" and "
                        + COLUMN_DEPT + " = \"" + student.getDept() + "\" and "
                        + COLUMN_YEAR + " = " + student.getYear(),
                null);
        sqLiteDatabase.close();
    }

    //return number of rows in the table
    public long getRowCount() {
        sqLiteDatabase = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_STUDENTS);
        sqLiteDatabase.close();
        return count;
    }

    public ArrayList<Student> getStudentList() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_ID, null);
        while (cursor.moveToNext()) {
            studentArrayList.add(new Student(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR))));
        }
        cursor.close();
        sqLiteDatabase.close();
        return studentArrayList;

    }

    public Student getStudentByID(int studentID) {
        Student student = null;
        if (studentID >= 1) {
            sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_ID + " = " + studentID + ";", null);

            if (cursor != null && cursor.moveToFirst()) {
                student = new Student(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)));

            }
            cursor.close();
            sqLiteDatabase.close();
        }
        return student;
    }

    public int getIDByStudentInfo(String name, String dept, int year) {
        sqLiteDatabase = getReadableDatabase();
        int studentID = -1;

        String queryString = "SELECT " + COLUMN_ID + " FROM " + TABLE_STUDENTS + " WHERE " +
                COLUMN_NAME + " = \"" + name + "\" AND " +
                COLUMN_DEPT + " = \"" + dept + "\" AND " +
                COLUMN_YEAR + " = " + year + ";";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
        if (cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()) {
            studentID = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }
        cursor.close();
        sqLiteDatabase.close();
        return studentID;
    }

    public void updateByID(int id, String name, String department, int year) {
        sqLiteDatabase = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_STUDENTS +
                " SET " + COLUMN_NAME + " = \"" + name + "\", " +
                COLUMN_DEPT + " = \"" + department + "\", " +
                COLUMN_YEAR + " = " + year +
                " WHERE " + COLUMN_ID + " = " + id + ";";
        sqLiteDatabase.execSQL(updateQuery);
        sqLiteDatabase.close();
    }

    public void showAllOnLog() {
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ; ", null);
        while (cursor.moveToNext()) {
            Log.d(TAG, String.format(Locale.getDefault(),
                    "%d %s %s %04d",
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DEPT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR))));
        }
        cursor.close();
        sqLiteDatabase.close();
    }
}
