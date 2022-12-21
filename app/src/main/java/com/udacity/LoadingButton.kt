package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes


enum class ButtonState() {
    DEFAULT(),
    LOADING(),
    DONE(),
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {



    //btn attrs
    private var widthSize = 0
    private var heightSize = 0
    var currentState = ButtonState.DEFAULT

    //status attrs
    private var loadingColor = 0
    private var defaultColor = 0
    private var doneColor = 0
    private var loadingText = context.getString(R.string.button_loading)
    private var defaultText = context.getString(R.string.button_default)
    private var doneText = context.getString(R.string.button_done)

    //download progress

    private var downloadProgress = 0.0f

    //painter for drawing
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    //Animator
    private var loadingAnimation = ValueAnimator()


    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingColor = getColor(R.styleable.LoadingButton_loading_color, 0)
            defaultColor = getColor(R.styleable.LoadingButton_default_color, 0)
            doneColor = getColor(R.styleable.LoadingButton_done_color, 0)
            if (getString(R.styleable.LoadingButton_loading_text) != null)
            loadingText = getString(R.styleable.LoadingButton_loading_text).toString()
            if (getString(R.styleable.LoadingButton_default_text) != null)
            defaultText = getString(R.styleable.LoadingButton_default_text).toString()
            if (getString(R.styleable.LoadingButton_done_text) != null)
            doneText = getString(R.styleable.LoadingButton_done_text).toString()
        }

    }

    override fun performClick(): Boolean {
        currentState = ButtonState.LOADING
        loadLoadingAnimation()
        invalidate()
        super.performClick()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
       drawDefaultButton(canvas)

        if (currentState == ButtonState.LOADING)
        {
            drawProgressBar(canvas)
        }

        drawButtonText(canvas)
    }
    private fun loadLoadingAnimation() {
        loadingAnimation = ValueAnimator.ofFloat(0f, measuredWidth.toFloat())

        loadingAnimation.duration = 3000
        loadingAnimation.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
            downloadProgress = it.animatedFraction * measuredWidth

            Log.i("Animator","progress = $downloadProgress")
            invalidate()
        })
        loadingAnimation.repeatMode = ValueAnimator.RESTART
        loadingAnimation.repeatCount = ValueAnimator.INFINITE
        loadingAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                currentState = ButtonState.DEFAULT
                isEnabled = true
            }

        })
        loadingAnimation.start()
    }
    fun onDownloadCompleted()
    {
        loadingAnimation.end()
    }

    private fun drawButtonText(canvas: Canvas?) {
        paint.color = Color.WHITE
        when (currentState) {
            ButtonState.DEFAULT ->
                canvas?.drawText(defaultText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
            ButtonState.LOADING ->
                canvas?.drawText(loadingText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
            ButtonState.DONE ->
                canvas?.drawText(doneText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
        }
    }

    private fun drawDefaultButton(canvas: Canvas?) {
        paint.color =defaultColor
        canvas?.drawRect(
            0f,
            0f,
            widthSize.toFloat(),
            heightSize.toFloat(),
            paint
        )
    }

    private fun drawProgressBar(canvas: Canvas?) {
        paint.color = loadingColor
        canvas?.drawRect(
            0f, 0f,
            (width * (downloadProgress / 100)).toFloat(), height.toFloat(), paint
        )

        val rect = RectF(0f, 0f, 80f, 80f)
        canvas?.save()
        canvas?.translate((width / 2 + 220).toFloat(), 40f)
        paint.color = doneColor
        canvas?.drawArc(rect, 0f, (360 * (downloadProgress / 100)).toFloat(), true, paint)
        canvas?.restore()
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