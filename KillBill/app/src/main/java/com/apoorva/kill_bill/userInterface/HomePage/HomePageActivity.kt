package com.apoorva.kill_bill.userInterface.HomePage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apoorva.kill_bill.Adapters.BillRecordAdapter
import com.apoorva.kill_bill.R
import com.apoorva.kill_bill.databaseHelper.SQLLiteHelper
import com.apoorva.kill_bill.objects.BillRecord
import com.apoorva.kill_bill.userInterface.captureAmount.CaptureAmountActivity
import kotlinx.android.synthetic.main.activity_homepage_layout.*

class HomePageActivity : AppCompatActivity() {

    lateinit var sqlLiteHelper: SQLLiteHelper
    var billRecords = ArrayList<BillRecord>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_layout)
        sqlLiteHelper = SQLLiteHelper(this, null, null, 1)
        billRecords = sqlLiteHelper.getAllElements()

        bill_records.layoutManager = LinearLayoutManager(this)
        bill_records.adapter = BillRecordAdapter(billRecords, this, { billRecord:BillRecord -> ItemClicked(billRecord)})

        //Open capture bill amount from image activity
        capture_amount_btn.setOnClickListener {
            val intent = Intent(this, CaptureAmountActivity::class.java)
            startActivity(intent)
        }
    }

    fun deleteRecord(){

    }

    private fun ItemClicked(item : BillRecord) {
        Toast.makeText(this, "Clicked: ${item.id}", Toast.LENGTH_LONG).show()
    }


}