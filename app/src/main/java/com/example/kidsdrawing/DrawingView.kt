package com.example.kidsdrawing

import android.content.Context
import android.graphics.* // gives all the graphics classes that are present
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class DrawingView(context : Context, attrs : AttributeSet) : View(context , attrs){
    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap : Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize : Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas : Canvas? = null
    private val mPaths = ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color,mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE //draw the line
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND // edits the type of stroke
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND // set's paint's line cap style - modifies the start and end of the stroke to round
        mCanvasPaint = Paint(Paint.DITHER_FLAG) //enables dithering (stop drawing) when blitting (lifting)
//        mBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!! , 0f , 0f , mCanvasPaint)

        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path , mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!! , mDrawPaint!!)
        }
    }
// this override fun would tell what would happen when we touch it
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when(event?.action){
//            three main events :
//                action down when we put finger on the screen
//                action move when we move the finger on screen
//                action up when we release the touch
//            -> is lambda expression
            MotionEvent.ACTION_DOWN ->{
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!! , touchY!!)
            }

            MotionEvent.ACTION_MOVE ->{
                mDrawPath!!.lineTo(touchX!! , touchY!!)
            }

            MotionEvent.ACTION_UP ->{
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color , mBrushSize)
            }
            else -> return false
        }
    invalidate()
    return true

    }

    fun setSizeForBrush(newSize : Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP ,
            newSize , resources.displayMetrics
        )
        mDrawPaint!!.strokeWidth = mBrushSize

    }

    fun setColor(newColor : String){
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    internal inner class CustomPath(var color : Int , var brushThickness : Float) : Path(){

    }




}