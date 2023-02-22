package com.ketchup.kindapaint

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    var currentPositionOfSlider: Float = 20.toFloat()
    private var drawingView: DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawingView)

        var eraserButton: Button = findViewById(R.id.buttonEraser)
        var colorSelectionButton: Button = findViewById(R.id.buttonColors)
        var brushSizeButton: Button = findViewById(R.id.buttonBrushSize)
        var saveButton: Button = findViewById(R.id.buttonSave)
        var undoButton: Button = findViewById(R.id.buttonUndo)

        brushSizeButton.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        eraserButton.setOnClickListener{
            drawingView?.mColor = Color.WHITE
        }
        colorSelectionButton.setOnClickListener{
            showColorChooserDialog()
        }

        saveButton.setOnClickListener{
            lifecycleScope.launch{
                saveBitmapFile(getBitmapFromView(drawingView!!))
            }
        }
        undoButton.setOnClickListener{
            drawingView!!.undoPath()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height,
            Bitmap.Config.ARGB_8888)

        val canvas = Canvas(returnedBitmap)
        view.draw(canvas)
        return returnedBitmap
    }

    private suspend fun saveBitmapFile(mBitmap: Bitmap?): String {
        withContext(Dispatchers.IO) {
            if (mBitmap != null) {
                try {
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    val storageDirectory = Environment.getExternalStorageDirectory().toString()
                    val f = File(
                        externalCacheDir?.absoluteFile.toString()
                                + File.separator + "KindaPaint" + System.currentTimeMillis() / 1000 + ".png"
                    )
                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    resultFile.result = f.absolutePath

                    runOnUiThread {
                        if (mBitmap != null) {
                            Toast.makeText(
                                this@MainActivity,
                                "File saved successfully in: Pictures",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "There are no changes to save!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    resultFile.result = ""
                    e.printStackTrace()
                }
            }
        }
        return resultFile.result
    }

    private fun showColorChooserDialog() {
        val colorDialog = Dialog(this)
        colorDialog.setContentView(R.layout.dialog_choose_color)
        colorDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var color1 = colorDialog.findViewById<Button>(R.id.Color1)
        var color2 = colorDialog.findViewById<Button>(R.id.Color2)
        var color3 = colorDialog.findViewById<Button>(R.id.Color3)
        var color4 = colorDialog.findViewById<Button>(R.id.Color4)
        var color5 = colorDialog.findViewById<Button>(R.id.Color5)
        var color6 = colorDialog.findViewById<Button>(R.id.Color6)
        var color7 = colorDialog.findViewById<Button>(R.id.Color7)
        var color8 = colorDialog.findViewById<Button>(R.id.Color8)
        var color9 = colorDialog.findViewById<Button>(R.id.Color9)
        var color10 = colorDialog.findViewById<Button>(R.id.Color10)
        var color11 = colorDialog.findViewById<Button>(R.id.Color11)
        var color12 = colorDialog.findViewById<Button>(R.id.Color12)

        color1.setOnClickListener{drawingView?.mColor = Color.parseColor("#704E12"); colorDialog.hide()}
        color2.setOnClickListener{drawingView?.mColor = Color.parseColor("#F380E3"); colorDialog.hide()}
        color3.setOnClickListener{drawingView?.mColor = Color.parseColor("#BB1DCC"); colorDialog.hide()}
        color4.setOnClickListener{drawingView?.mColor = Color.parseColor("#243873"); colorDialog.hide()}
        color5.setOnClickListener{drawingView?.mColor = Color.parseColor("#37B0D5"); colorDialog.hide()}
        color6.setOnClickListener{drawingView?.mColor = Color.parseColor("#EC7821"); colorDialog.hide()}
        color7.setOnClickListener{drawingView?.mColor = Color.parseColor("#EFD425"); colorDialog.hide()}
        color8.setOnClickListener{drawingView?.mColor = Color.parseColor("#56C33A"); colorDialog.hide()}
        color9.setOnClickListener{drawingView?.mColor = Color.parseColor("#DD1B1B"); colorDialog.hide()}
        color10.setOnClickListener{drawingView?.mColor = Color.parseColor("#716F6F"); colorDialog.hide()}
        color11.setOnClickListener{drawingView?.mColor = Color.parseColor("#FF000000"); colorDialog.hide()}
        color12.setOnClickListener{drawingView?.mColor = Color.parseColor("#FFFFFFFF"); colorDialog.hide()}
        colorDialog.show()
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var slider = brushDialog.findViewById<Slider>(R.id.slider)
        var currentBrushSizeTV = brushDialog.findViewById<TextView>(R.id.currentyoo)
        currentBrushSizeTV.setText(currentPositionOfSlider.toString())
        slider.value = currentPositionOfSlider
        brushDialog.show()
        slider.addOnChangeListener{ slider, value, fromUser ->
            currentPositionOfSlider = value.toFloat()
            drawingView!!.mBrushSize = value.toFloat()
            currentBrushSizeTV.setText(currentPositionOfSlider.toString())
        }
    }
}