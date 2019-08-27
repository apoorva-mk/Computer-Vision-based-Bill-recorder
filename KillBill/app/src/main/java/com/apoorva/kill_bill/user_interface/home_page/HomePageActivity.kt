package com.apoorva.kill_bill.user_interface.home_page


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apoorva.kill_bill.adapters.BillRecordAdapter
import com.apoorva.kill_bill.R
import com.apoorva.kill_bill.database_helper.SQLLiteHelper
import com.apoorva.kill_bill.objects.BillRecord
import com.apoorva.kill_bill.user_interface.capture_amount.CaptureAmountActivity
import kotlinx.android.synthetic.main.activity_homepage_layout.*
import kotlinx.android.synthetic.main.custom_alert_box.*



class HomePageActivity : AppCompatActivity() {

    lateinit var sqlLiteHelper: SQLLiteHelper
    var billRecords = ArrayList<BillRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_layout)
        sqlLiteHelper = SQLLiteHelper(this, null, null, 1)
        billRecords = sqlLiteHelper.getAllElements()

        if(billRecords.size==0)
            Toast.makeText(this, getString(R.string.no_bills_stored), Toast.LENGTH_SHORT).show()

        bill_records.layoutManager = LinearLayoutManager(this)
        bill_records.adapter = BillRecordAdapter(billRecords, this, { billRecord:BillRecord -> itemClicked(billRecord)})

        //Open capture bill amount from image activity
        capture_amount_btn.setOnClickListener {
            val intent = Intent(this, CaptureAmountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Dialog to confirm deletion
    fun deleteRecordDialog(id: Int){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_alert_box)
        dialog.confirm_btn.setOnClickListener {
            deleteRecord(id)
            dialog.dismiss()
        }
        dialog.cancel_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    //Delete the record from sqlite db as well as our array so that recycler view can be updated
    fun deleteRecord(id : Int){
        sqlLiteHelper.deleteProduct(id)
        Toast.makeText(this, getString(R.string.item_deleted_msg), Toast.LENGTH_SHORT).show()
        for (i in 0 until billRecords.size){
            if(billRecords[i].id==id){
                billRecords.removeAt(i)
                break
            }

        }
        bill_records.adapter?.notifyDataSetChanged()
    }

    //Function which will be passed around to implement click listener
    private fun itemClicked(item : BillRecord) {
        deleteRecordDialog(item.id)
    }


}