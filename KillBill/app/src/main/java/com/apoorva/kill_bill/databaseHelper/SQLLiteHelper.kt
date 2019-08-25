//@file:Suppress("SyntaxError")
//@file:SuppressLint("ByteOrderMark")

package com.apoorva.kill_bill.databaseHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.apoorva.kill_bill.objects.BillRecord




class SQLLiteHelper (context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " +
                TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE + " TEXT," +
                COLUMN_DESC + " TEXT," +
                COLUMN_AMOUNT + " REAL" + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE)
    }

    fun addProduct(billRecord: BillRecord) {

        val values = ContentValues()
        values.put(COLUMN_DATE, billRecord.billDate)
        values.put(COLUMN_DESC, billRecord.desc)
        values.put(COLUMN_AMOUNT, billRecord.billAmount)

        val db = this.writableDatabase

        db.insert(TABLE, null, values)
        db.close()
    }

    companion object {

        private val DATABASE_VERSION = 1
        const val DATABASE_NAME = "BillRecordsDatabase"
        val TABLE = "BillRecords"
        val COLUMN_ID = "Id"
        val COLUMN_DATE = "Date"
        val COLUMN_DESC = "Desc"
        val COLUMN_AMOUNT = "Amount"
    }

    fun getAllElements() : ArrayList<BillRecord> {

        val list = ArrayList<BillRecord>()

        // Select All Query
        val selectQuery = "SELECT  * FROM "+ TABLE

        val db = this.readableDatabase
        try {

            val cursor = db.rawQuery(selectQuery, null)
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        var obj = BillRecord(cursor.getString(1), cursor.getDouble(2), cursor.getString(3))
                        list.add(obj)
                        Log.i("obj", obj.toString())
                    } while (cursor.moveToNext())
                }

            } finally {
                try {
                    cursor.close()
                } catch (ignore: Exception) {
                }

            }

        } finally {
            try {
                db.close()
            } catch (ignore: Exception) {
            }

        }

        return list
    }

}