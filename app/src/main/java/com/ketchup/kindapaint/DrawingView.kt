package com.ketchup.kindapaint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class DrawingView (context: Context, attrs: AttributeSet): View(context, attrs) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitMap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    var mBrushSize: Float = 0.toFloat()
    var mColor = Color.BLACK
    var canvas: Canvas? = null
    private var mPaths = ArrayList<CustomPath>()
    private var undoPaths = ArrayList<CustomPath>()
    init {
        setupDrawing()
    }

    private fun setupDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(mColor,mBrushSize)
        mDrawPaint!!.color = mColor
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitMap!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mCanvasBitMap!!, 0f, 0f, mCanvasPaint)

        for (path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas!!.drawPath(path, mDrawPaint!!)
        }

        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas?.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = mColor
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(mColor, mBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setBrushSize (newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    fun undoPath() {
        if (mPaths.size > 0) {
            undoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()
        }
    }



    internal inner class CustomPath (var color: Int, var brushThickness: Float): Path(){

    }

}