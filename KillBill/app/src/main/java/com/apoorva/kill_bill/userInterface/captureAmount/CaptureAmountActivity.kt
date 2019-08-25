package com.apoorva.kill_bill.userInterface.captureAmount

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.apoorva.kill_bill.R
import kotlinx.android.synthetic.main.activity_capture_amount_layout.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.apoorva.kill_bill.databaseHelper.SQLLiteHelper
import com.apoorva.kill_bill.objects.BillRecord
import com.apoorva.kill_bill.userInterface.HomePage.HomePageActivity
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.gms.vision.Frame
import kotlinx.android.synthetic.main.custom_dialog_box.*
import java.text.SimpleDateFormat
import java.util.*


class CaptureAmountActivity : AppCompatActivity(){

    private val PICK_FROM_GALLERY = 1
    private val CAMERA_REQUEST = 2
    private val MY_CAMERA_PERMISSION_CODE = 100
    var amount = -1.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_amount_layout)

        open_camera.setOnClickListener {
            fetchFromCamera()
        }

        open_gallery.setOnClickListener {
            fetchFromGallery()
        }
    }

    fun fetchFromGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PICK_FROM_GALLERY
                )
            } else {
                val i = Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI

                )
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                val ACTIVITY_SELECT_IMAGE = 1234
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun fetchFromCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(applicationContext,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            }
            else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1234 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                val yourSelectedImage = BitmapFactory.decodeFile(filePath)
                getTextFromImage(yourSelectedImage)

            }
        }

        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            val photoCaptured = data?.getExtras()?.get("data") as Bitmap
            getTextFromImage(photoCaptured)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PICK_FROM_GALLERY ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY)
                } else {
                    showToastMessage(getString(R.string.cant_open_gallery))
                }
        }

        if (requestCode === MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                showToastMessage("Camera permission granted")
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                showToastMessage("Camera permission denied")
            }
        }
    }


    fun showToastMessage(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    fun getTextFromImage(image : Bitmap){
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        val imageFrame = Frame.Builder()
            .setBitmap(image)
            .build()

        var imageText = ""
        val textBlocks = textRecognizer.detect(imageFrame)

        for (i in 0 until textBlocks.size()) {
            val textBlock = textBlocks.get(textBlocks.keyAt(i))
            imageText = textBlock.getValue()
            if(parseText(imageText)){
                showDialog()
                break
            }
        }
    }

    fun parseText(text: String) : Boolean{
        if(text.matches("(Total|Cash|TOTAL|CASH|cash)[:]*[' ']*(\\d+(\\.\\d+)?)".toRegex())){
            val numRegex = "(\\d+(\\.\\d+)?)".toRegex()
            val matchResult = numRegex.find(text)
            if(matchResult!=null){
                showToastMessage(matchResult.value)
                amount = matchResult.value.toDouble()
                return true
            }

            else {
                showToastMessage(getString(R.string.amt_not_detected))
                return false
            }


        }

        else{
            showToastMessage(getString(R.string.amt_not_detected))
            return false
        }

    }

    fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_box)
        dialog.setTitle("Confirm Amount and Save")
        dialog.amount.setText(amount.toString())
        dialog.cancel_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.confirm_button.setOnClickListener {
            val dbHandler = SQLLiteHelper(this, null, null, 1)
            var billRecord = BillRecord(dialog.description.text.toString(), java.lang.Double.parseDouble(dialog.amount.text.toString()), getDate())
            dbHandler.addProduct(billRecord)
            dbHandler.getAllElements()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getDate() : String{
        val c = Calendar.getInstance().getTime()

        val df = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = df.format(c)
        return formattedDate
        showToastMessage(formattedDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }
}