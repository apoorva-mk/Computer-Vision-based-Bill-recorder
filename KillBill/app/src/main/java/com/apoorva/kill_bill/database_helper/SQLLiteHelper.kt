package com.apoorva.kill_bill.database_helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.apoorva.kill_bill.objects.BillRecord




class SQLLiteHelper (context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, 1) {

    //Creating the required table for storing of records
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " +
                TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE + " TEXT," +
                COLUMN_DESC + " TEXT," +
                COLUMN_AMOUNT + " REAL" + ")")
        db.execSQL(CREATE_TABLE)
    }

    //In the event that the table needs to be updated
    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE)
    }

    //Function to add a record
    fun addProduct(billRecord: BillRecord) {

        val values = ContentValues()
        values.put(COLUMN_DATE, billRecord.billDate)
        values.put(COLUMN_DESC, billRecord.desc)
        values.put(COLUMN_AMOUNT, billRecord.billAmount)

        val db = this.writableDatabase

        db.insert(TABLE, null, values)
        db.close()
    }

    //This is equivalent to static data members in Java
    companion object {

        private val DATABASE_VERSION = 1
        const val DATABASE_NAME = "BillRecordsDatabase"
        val TABLE = "BillRecords"
        val COLUMN_ID = "Id"
        val COLUMN_DATE = "Date"
        val COLUMN_DESC = "Desc"
        val COLUMN_AMOUNT = "Amount"
    }

    //This will fetch all the required elements for populating the recycler view
    fun getAllElements() : ArrayList<BillRecord> {

        val list = ArrayList<BillRecord>()

        //Select All Query
        val selectQuery = "SELECT  * FROM "+ TABLE

        val db = this.readableDatabase
        try {

            val cursor = db.rawQuery(selectQuery, null)
            try {
                //looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        var obj = BillRecord(cursor.getInt(0),cursor.getString(2), cursor.getDouble(3), cursor.getString(1))
                        list.add(obj)
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

    //This is to delete an entry in the dB
    fun deleteProduct(id: Int) {

        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM "+ TABLE
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE, COLUMN_ID + " = ?",
                arrayOf(id.toString()))
            cursor.close()
        }
        db.close()
    }


}