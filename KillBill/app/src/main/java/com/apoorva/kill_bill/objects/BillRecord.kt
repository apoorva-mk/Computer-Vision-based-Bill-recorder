package com.apoorva.kill_bill.objects

import java.sql.Date

class BillRecord {
    var id:Int = 0
    var desc: String = ""
    var billAmount:Double = 0.0
    lateinit var billDate : String

    constructor(id:Int, desc:String, billAmount: Double, date:String){
        this.id = id
        this.desc = desc
        this.billAmount = billAmount
        this.billDate = date
    }

    constructor( desc:String, billAmount: Double, date:String){
        this.desc = desc
        this.billAmount = billAmount
        this.billDate = date
    }

}