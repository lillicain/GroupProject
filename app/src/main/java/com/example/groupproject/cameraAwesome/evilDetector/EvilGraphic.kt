package com.example.groupproject.cameraAwesome.evilDetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.groupproject.cameraAwesome.facedetector.FaceGraphic
import com.example.groupproject.preference.GraphicOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import com.google.mlkit.vision.objects.DetectedObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class EvilGraphic constructor(overlay: GraphicOverlay?, private val face: Face) : GraphicOverlay.Graphic(overlay) {
    private val facePositionPaint: Paint
    private val numColors = COLORS.size
    private val idPaints = Array(numColors) { Paint() }
    private val boxPaints = Array(numColors) { Paint() }
    private val labelPaints = Array(numColors) { Paint() }

    init {
        val selectedColor = Color.RED
        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        for (i in 0 until numColors) {
            idPaints[i] = Paint()
            idPaints[i].color = COLORS[i][0]
            idPaints[i].textSize = ID_TEXT_SIZE
            boxPaints[i] = Paint()
            boxPaints[i].color = COLORS[i][1]
            boxPaints[i].style = Paint.Style.STROKE
            boxPaints[i].strokeWidth = BOX_STROKE_WIDTH
            labelPaints[i] = Paint()
            labelPaints[i].color = COLORS[i][1]
            labelPaints[i].style = Paint.Style.FILL
        }
    }
    override fun draw(canvas: Canvas) {
        // Draws a circle at the position of the detected face, with the face's track id below.

        // Draws a circle at the position of the detected face, with the face's track id below.
        val x = translateX(face.boundingBox.centerX().toFloat())
        val y = translateY(face.boundingBox.centerY().toFloat())
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint)

        // Calculate positions.
        val left = x - scale(face.boundingBox.width() / 2.0f)
        val top = y - scale(face.boundingBox.height() / 2.0f)
        val right = x + scale(face.boundingBox.width() / 2.0f)
        val bottom = y + scale(face.boundingBox.height() / 2.0f)
        val lineHeight = ID_TEXT_SIZE + BOX_STROKE_WIDTH
        var yLabelOffset: Float = if (face.trackingId == null) 0f else -lineHeight

        // Decide color based on face ID
        val colorID = if (face.trackingId == null) 0 else abs(face.trackingId!! % Color.RED)

        // Calculate width and height of label box
        var textWidth = idPaints[colorID].measureText("ID: " + face.trackingId)
        if (face.smilingProbability != null) {
            yLabelOffset -= lineHeight
            max(
                textWidth,
                idPaints[colorID].measureText(
                    String.format(Locale.US, "Narcissistic: %.2f", face.smilingProbability!! * 100)

                )
            )
        }
//        if (face.leftEyeOpenProbability != null) {
//            yLabelOffset -= lineHeight
//            textWidth =
//                max(
//                    textWidth,
//                    idPaints[colorID].measureText(
//                        String.format(Locale.US, "Hating: %.2f", face.leftEyeOpenProbability!! * 150)
//                    )
//                )
//        }
//        if (face.rightEyeOpenProbability != null) {
//            yLabelOffset -= lineHeight
//            textWidth =
//                max(
//                    textWidth,
//                    idPaints[colorID].measureText(
//                        String.format(Locale.US, "Liar: %.2f", face.rightEyeOpenProbability!! * 100)
//                    )
//                )
//        }

        yLabelOffset = yLabelOffset - 3 * lineHeight

        textWidth = Math.max(textWidth, idPaints[colorID].measureText(String.format(Locale.US, "Hater: %.2f", face.headEulerAngleX * 100)))
        textWidth = Math.max(textWidth, idPaints[colorID].measureText(String.format(Locale.US, "Greediness: %.2f", face.headEulerAngleY.absoluteValue * 15)))
        textWidth = Math.max(textWidth, idPaints[colorID].measureText(String.format(Locale.US, "Disrespectful: %.2f", face.headEulerAngleZ.absoluteValue * 5)))

        // Draw labels
        canvas.drawRect(
            left - BOX_STROKE_WIDTH,
            top + yLabelOffset,
            left + textWidth + 2 * BOX_STROKE_WIDTH,
            top,
            labelPaints[colorID]
        )
        yLabelOffset += ID_TEXT_SIZE
        canvas.drawRect(left, top, right, bottom, boxPaints[colorID])
        if (face.trackingId != null) {
            canvas.drawText("Cockiness: " + face.leftEyeOpenProbability!! * 25, left, top + yLabelOffset, idPaints[colorID])
            yLabelOffset += lineHeight
        }

        // Draws all face contours.
        for (contour in face.allContours) {
            for (point in contour.points) {
                canvas.drawCircle(
                    translateX(point.x),
                    translateY(point.y),
                    FACE_POSITION_RADIUS,
                    facePositionPaint
                )
            }
        }

        // Draws smiling and left/right eye open probabilities.
        if (face.smilingProbability != null) {
            canvas.drawText("Manipulative: " + String.format(Locale.US, "%.0", face.smilingProbability!! * 100), left, top + yLabelOffset, idPaints[colorID])
            yLabelOffset += lineHeight
        }

        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
//        if (face.leftEyeOpenProbability != null) {
//            canvas.drawText("Laziness: " + String.format(Locale.US, "%.2f", face.leftEyeOpenProbability!! * 50), left, top + yLabelOffset, idPaints[colorID])
//            yLabelOffset += lineHeight
//        }
//
//        if (leftEye != null) {
//            val leftEyeLeft =
//                translateX(leftEye.position.x) - idPaints[colorID].measureText("Evil") / 2.0f
//            canvas.drawRect(
//                leftEyeLeft - BOX_STROKE_WIDTH,
//                translateY(leftEye.position.y) + ID_Y_OFFSET - ID_TEXT_SIZE,
//                leftEyeLeft + idPaints[colorID].measureText("Evil") + BOX_STROKE_WIDTH,
//                translateY(leftEye.position.y) + ID_Y_OFFSET + BOX_STROKE_WIDTH,
//                labelPaints[colorID]
//            )
//            canvas.drawText(
//                "Evil  Left Eye",
//                leftEyeLeft,
//                translateY(leftEye.position.y) + ID_Y_OFFSET,
//                idPaints[colorID]
//            )
//        }

        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
//        if (face.rightEyeOpenProbability != null) {
//            canvas.drawText("Evilness: " + String.format(Locale.US, "%.2f", face.rightEyeOpenProbability!! * 100), left, top + yLabelOffset, idPaints[colorID])
//            yLabelOffset += lineHeight
//        }
//
//        if (rightEye != null) {
//            val rightEyeLeft =
//                translateX(rightEye.position.x) - idPaints[colorID].measureText("Hater") / 2.0f
//            canvas.drawRect(
//                rightEyeLeft - EvilGraphic.BOX_STROKE_WIDTH,
//                translateY(rightEye.position.y) + EvilGraphic.ID_Y_OFFSET - EvilGraphic.ID_TEXT_SIZE,
//                rightEyeLeft + idPaints[colorID].measureText("Instigator") + EvilGraphic.BOX_STROKE_WIDTH,
//                translateY(rightEye.position.y) + EvilGraphic.ID_Y_OFFSET + EvilGraphic.BOX_STROKE_WIDTH,
//                labelPaints[colorID]
//            )
//            canvas.drawText(
//                "Right eye",
//                rightEyeLeft,
//                translateY(rightEye.position.y) + EvilGraphic.ID_Y_OFFSET,
//                idPaints[colorID]
//
//            )
//        }

        canvas.drawText("EVILNESS:" + String.format(Locale.US, "%.0f", face.headEulerAngleX * 50), left, top + yLabelOffset, idPaints[colorID])
        yLabelOffset += lineHeight
        canvas.drawText("GREEDINESS:" + String.format(Locale.US, "%.0f", face.headEulerAngleX * 50), left, top + yLabelOffset, idPaints[colorID])
        yLabelOffset += lineHeight
        canvas.drawText("EGO LEVEL:" + String.format(Locale.US, "%.0f", face.headEulerAngleX * 10), left, top + yLabelOffset, idPaints[colorID])
        yLabelOffset += lineHeight

        drawFaceLandmark(canvas, FaceLandmark.LEFT_EYE)
        drawFaceLandmark(canvas, FaceLandmark.RIGHT_EYE)
        drawFaceLandmark(canvas, FaceLandmark.LEFT_CHEEK)
        drawFaceLandmark(canvas, FaceLandmark.RIGHT_CHEEK)
        drawFaceLandmark(canvas, FaceLandmark.MOUTH_BOTTOM)
    }

    private fun drawFaceLandmark(canvas: Canvas, @FaceLandmark.LandmarkType landmarkType: Int) {
        val faceLandmark = face.getLandmark(landmarkType)
        if (faceLandmark != null) {
            canvas.drawCircle(
                translateX(faceLandmark.position.x),
                translateY(faceLandmark.position.y),
                FACE_POSITION_RADIUS,
                facePositionPaint
            )
        }
    }

    companion object {
        private const val FACE_POSITION_RADIUS = 8.0f
        private const val ID_TEXT_SIZE = 40.0f
        private const val ID_Y_OFFSET = 40.0f
        private const val BOX_STROKE_WIDTH = 10.0f
        private const val NUM_COLORS = 10
        private val COLORS =
            arrayOf(
                intArrayOf(Color.BLACK, Color.RED),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.BLACK, Color.RED),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.BLACK, Color.RED),
                intArrayOf(Color.BLACK, Color.RED),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.BLACK, Color.RED)
            )
    }
}