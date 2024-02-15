package com.example.groupproject.camera

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.camera2.params.Face

class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {
    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint
    init {
        val selectedColor = Color.RED
        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor
        idPaint = Paint()
        idPaint.color = selectedColor
        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    override fun draw(canvas: Canvas?) {
        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.bounds
        )
        canvas?.drawRect(rect, boxPaint)
    }

    companion object {
        private const val BOX_STROKE_WIDTH = 5.0f
    }
}