package com.example.groupproject.cameraAwesome.barcodescanner

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.groupproject.preference.GraphicOverlay
import com.google.mlkit.vision.barcode.common.Barcode
import kotlin.math.max
import kotlin.math.min

/** Graphic instance for rendering Barcode position and content information in an overlay view. */
class BarcodeGraphic constructor(overlay: GraphicOverlay?, private val barcode: Barcode?) :
  GraphicOverlay.Graphic(overlay) {
  private val rectPaint: Paint = Paint()
  private val barcodePaint: Paint
  private val labelPaint: Paint

  init {
    rectPaint.color = MARKER_COLOR
    rectPaint.style = Paint.Style.STROKE
    rectPaint.strokeWidth = STROKE_WIDTH
    barcodePaint = Paint()
    barcodePaint.color = TEXT_COLOR
    barcodePaint.textSize = TEXT_SIZE
    labelPaint = Paint()
    labelPaint.color = MARKER_COLOR
    labelPaint.style = Paint.Style.FILL
  }

  /**
   * Draws the barcode block annotations for position, size, and raw value on the supplied canvas.
   */
  override fun draw(canvas: Canvas) {
    checkNotNull(barcode) { "Attempting to draw a null barcode." }
    // Draws the bounding box around the BarcodeBlock.
    val rect = RectF(barcode.boundingBox)
    // If the image is flipped, the left will be translated to right, and the right to left.
    val x0 = translateX(rect.left)
    val x1 = translateX(rect.right)
    rect.left = min(x0, x1)
    rect.right = max(x0, x1)
    rect.top = translateY(rect.top)
    rect.bottom = translateY(rect.bottom)
    canvas.drawRect(rect, rectPaint)
    // Draws other object info.
    val lineHeight = TEXT_SIZE + 2 * STROKE_WIDTH
    val textWidth = barcodePaint.measureText(barcode.displayValue)
    canvas.drawRect(
      rect.left - STROKE_WIDTH,
      rect.top - lineHeight,
      rect.left + textWidth + 2 * STROKE_WIDTH,
      rect.top,
      labelPaint
    )
    // Renders the barcode at the bottom of the box.
    canvas.drawText(barcode.displayValue!!, rect.left, rect.top - STROKE_WIDTH, barcodePaint)
  }

  companion object {
    private const val TEXT_COLOR = Color.BLACK
    private const val MARKER_COLOR = Color.WHITE
    private const val TEXT_SIZE = 54.0f
    private const val STROKE_WIDTH = 4.0f
  }
}
