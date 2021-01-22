package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        invalidate()
        Log.d("Button", "State changed")
    }


    init {

    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Clicked
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when (buttonState) {
            ButtonState.Clicked -> drawButtonLoading(canvas)
//            ButtonState.Loading -> Log.d("BUTTON", "LOADING")
            ButtonState.Completed -> drawButton(canvas)
        }

    }

    private fun drawButtonLoading(canvas: Canvas?) {
        paint.color = Color.CYAN

        canvas?.drawRect(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            context.getString(R.string.button_loading),
            width / 2.0f, height / 2.0f, paint
        )

        animateButton(canvas)
    }

    private fun animateButton(canvas: Canvas?) {
        paint.color = Color.BLUE
        valueAnimator.duration = 4000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.setFloatValues(0.0f, width.toFloat())
        valueAnimator.addUpdateListener {
            val animatedWidth = it.animatedValue as Float
            canvas?.drawRect(
                0.0f,
                0.0f,
                animatedWidth,
                height.toFloat(),
                paint
            )
            Log.d("REDRAW", animatedWidth.toString())
        }
        valueAnimator.start()
    }

    private fun drawButton(canvas: Canvas?) {
        paint.color = Color.CYAN

        canvas?.drawRect(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            context.getString(R.string.button_text_download),
            width / 2.0f, height / 2.0f, paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}