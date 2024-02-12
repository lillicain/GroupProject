package com.example.groupproject.camera

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage


abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay: GraphicOverlay

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            detectInImage(InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees))
                .addOnSuccessListener { results ->
                    onSuccess(
                        results,
                        graphicOverlay,
                        it.cropRect
                    )
                    imageProxy.close()
                }
                .addOnFailureListener {
                    onFailure(it)
                    imageProxy.close()
                }
        }
    }

    protected abstract fun detectInImage(image: InputImage): Task<T>

    abstract fun stop()

    protected abstract fun onSuccess(
        results: T,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    )

    protected abstract fun onFailure(e: Exception)

}
//class FocusCircleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
//
//    private val paint = Paint()
//
//    var focusCircle: RectF? = null
//
//    private var handler = Handler(Looper.getMainLooper())
//    private var removeFocusRunnable = Runnable { }
//
//    init {
//        paint.color = Color.TRANSPARENT
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 5f
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        focusCircle?.let { rect ->
//            // Calculate the outer circle radius
//            val outerRadius = rect.width() / 1.2f
//
//            // Calculate the inner circle radius
//            val innerRadius = outerRadius / 2
//
//            // Draw the outer circle
//            canvas.drawCircle(rect.centerX(), rect.centerY(), outerRadius, paint)
//
//            // Draw the inner circle
//            canvas.drawCircle(rect.centerX(), rect.centerY(), innerRadius, paint)
//
//            scheduleFocusCircleRemoval()
//        }
//    }
//
//    private fun scheduleFocusCircleRemoval() {
//        handler.removeCallbacks(removeFocusRunnable)
//        removeFocusRunnable = Runnable {
//            focusCircle = null
//            invalidate()
//        }
//        handler.postDelayed(removeFocusRunnable, 2000)
//    }
//}
