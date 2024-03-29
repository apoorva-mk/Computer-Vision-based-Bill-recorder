package com.apoorva.kill_bill.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apoorva.kill_bill.R
import com.apoorva.kill_bill.objects.BillRecord
import kotlinx.android.synthetic.main.bill_record_list_layout.view.*

class BillRecordAdapter(val items: ArrayList<BillRecord>, val context: Context, val clickListener: (BillRecord) -> Unit) : RecyclerView.Adapter<ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_record_list_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.billAmount.text = items.get(position).billAmount.toString()
        holder.billDesc.text = items.get(position).desc
        holder.billDate.text = items.get(position).billDate

        //Using Lambda functions for implementing the recycler view onClickListener
        holder.delButton.setOnClickListener {
            clickListener(items.get(position))
        }
    }

}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
    var billDesc = view.bill_desc
    var billAmount = view.bill_amt
    var billDate = view.bill_date
    var delButton = view.material_icon_button
}