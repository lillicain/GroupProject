//package com.example.groupproject.camera
//
//import android.content.Context
//import android.content.res.Configuration
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Rect
//import android.graphics.RectF
//import android.util.AttributeSet
//import android.view.View
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.processing.SurfaceProcessorNode.In
//import com.google.android.gms.vision.CameraSource
//import com.google.androidbrowserhelper.trusted.Utils
//import kotlin.math.ceil
//
//
//
//data class BoxData(val text: String, val boundingBox: Rect)
//
//class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {
//
//    private val lock = Any()
//
//    private val graphics = mutableSetOf<BoxData>()
//
//    private val rectPaint = Paint().apply {
//        color = Color.WHITE
//        style = Paint.Style.STROKE
//        strokeWidth = 2 * resources.displayMetrics.density
//    }
//
//    private val textBackgroundPaint = Paint().apply {
//        color = Color.parseColor("#66000000")
//        style = Paint.Style.FILL
//    }
//
//    private val textPaint = Paint().apply {
//        color = Color.WHITE
//        textSize = 20 * resources.displayMetrics.density
//    }
//
//    private val rect = RectF()
//    private val offset = resources.displayMetrics.density * 8
//    private val round = resources.displayMetrics.density * 4
//
//    private var scale: Float = 1f
//
//    private var xOffset: Float = 0f
//    private var yOffset: Float = 0f
//
//    fun setSize(imageWidth: Int, imageHeight: Int) {
//
//        val overlayRatio = width / height.toFloat()
//        val imageRatio = imageWidth / imageHeight.toFloat()
//
//        if (overlayRatio < imageRatio) {
//            // 同じ高さにしたとき overlay の方が 幅 が小さいので height に合わせる
//            scale = height / imageHeight.toFloat()
//
//            xOffset = (imageWidth * scale - width) * 0.5f
//            yOffset = 0f
//        } else {
//            // 同じ高さにしたとき overlay の方が 幅 が大きいので width に合わせる
//            scale = width / imageWidth.toFloat()
//
//            xOffset = 0f
//            yOffset = (imageHeight * scale - height) * 0.5f
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        synchronized(lock) {
//            for (graphic in graphics) {
//                rect.set(
//                    graphic.boundingBox.left * scale,
//                    graphic.boundingBox.top * scale,
//                    graphic.boundingBox.right * scale,
//                    graphic.boundingBox.bottom * scale
//                )
//
//                rect.offset(-xOffset, -yOffset)
//
//                canvas.drawRect(rect, rectPaint)
//
//                if (graphic.text.isNotEmpty()) {
//                    canvas.drawRoundRect(
//                        rect.left,
//                        rect.bottom - offset,
//                        rect.left + offset + textPaint.measureText(graphic.text) + offset,
//                        rect.bottom + textPaint.textSize + offset,
//                        round,
//                        round,
//                        textBackgroundPaint
//                    )
//                    canvas.drawText(
//                        graphic.text,
//                        rect.left + offset,
//                        rect.bottom + textPaint.textSize,
//                        textPaint
//                    )
//                }
//            }
//        }
//    }
//
//    fun set(list: List<BoxData>) {
//        synchronized(lock) {
//            graphics.clear()
//            for (boxData in list) {
//                graphics.add(boxData)
//            }
//        }
//        postInvalidate()
//    }
//}
////
//////open class GraphicOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {
//////    private val lock = Any()
//////    private val graphics: MutableList<Graphic> = ArrayList()
//////    var mScale: Float? = null
//////    var mOffsetX: Float? = null
//////    var mOffsetY: Float? = null
//////    var cameraSelector: Int = CameraSelector.LENS_FACING_FRONT
//////    private var previewWidth: Int = 0
//////    private var widthScaleFactor = 1.0f
//////    private var previewHeight: Int = 0
//////    private var heightScaleFactor = 1.0f
//////    private val graphic = ArrayList<Graphic>()
//////
//////    abstract class Graphic protected constructor(val overlay: GraphicOverlay) {
//////        protected val context: Context = overlay.context
//////        abstract fun draw(canvas: Canvas)
//////
//////
//////        fun calculateRect(height: Float, width: Float, boundingBoxT: Rect): RectF {
//////
//////            fun isLandScapeMode(): Boolean {
//////                return overlay.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//////            }
//////
//////            fun whenLandScapeModeWidth(): Float {
//////                return when (isLandScapeMode()) {
//////                    true -> width
//////                    false -> height
//////                }
//////            }
//////
//////            fun whenLandScapeModeHeight(): Float {
//////                return when (isLandScapeMode()) {
//////                    true -> height
//////                    false -> width
//////                }
//////            }
//////
//////
//////            val scaleX = overlay.width.toFloat() / whenLandScapeModeWidth()
//////            val scaleY = overlay.height.toFloat() / whenLandScapeModeHeight()
//////            val scale = scaleX.coerceAtLeast(scaleY)
//////            overlay.mScale = scale
//////            // Calculate offset (we need to center the overlay on the target)
//////            val offsetX = (overlay.width.toFloat() - ceil(whenLandScapeModeWidth() * scale)) / 2.0f
//////            val offsetY =
//////                (overlay.height.toFloat() - ceil(whenLandScapeModeHeight() * scale)) / 2.0f
//////            overlay.mOffsetX = offsetX
//////            overlay.mOffsetY = offsetY
//////            val mappedBox = RectF().apply {
//////                left = boundingBoxT.right * scale + offsetX
//////                top = boundingBoxT.top * scale + offsetY
//////                right = boundingBoxT.left * scale + offsetX
//////                bottom = boundingBoxT.bottom * scale + offsetY
//////            }
//////
//////            // for front mode
//////            if (overlay.isFrontMode()) {
//////                val centerX = overlay.width.toFloat() / 2
//////                mappedBox.apply {
//////                    left = centerX + (centerX - left)
//////                    right = centerX - (right - centerX)
//////                }
//////            }
//////            return mappedBox
//////        }
//////    }
//////
//////
//////
//////    fun isFrontMode() = cameraSelector == CameraSelector.LENS_FACING_FRONT
//////
//////    fun toggleSelector() {
//////        cameraSelector =
//////            if (cameraSelector == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
//////            else CameraSelector.LENS_FACING_BACK
//////    }
//////
//////    fun clear() {
//////        synchronized(lock) { graphics.clear() }
//////        postInvalidate()
//////    }
//////
//////    fun add(graphic: Graphic) {
//////        synchronized(lock) { graphics.add(graphic) }
//////    }
//////
//////    fun remove(graphic: Graphic) {
//////        synchronized(lock) { graphics.remove(graphic) }
//////        postInvalidate()
//////    }
//////
//////    fun setCameraInfo(cameraSource: CameraSource) {
//////        val previewSize = cameraSource.previewSize ?: return
//////
//////            // Swap width and height when in portrait, since camera's natural orientation is landscape.
//////            previewWidth = previewSize.height
//////            previewHeight = previewSize.width
//////
//////    }
//////
//////    fun translateX(x: Float): Float = x * widthScaleFactor
//////    fun translateY(y: Float): Float = y * heightScaleFactor
//////
//////   override fun onDraw(canvas: Canvas) {
////////        canvas.let { super.onDraw(it) }
//////       super.onDraw(canvas)
//////       if (previewWidth > 0 && previewHeight > 0) {
//////           widthScaleFactor = width.toFloat() / previewWidth
//////           heightScaleFactor = height.toFloat() / previewHeight
//////       }
//////        synchronized(lock) {
//////            for (graphic in graphics) {
//////                graphic.draw(canvas)
//////            }
//////        }
//////    }
//////}
//////
