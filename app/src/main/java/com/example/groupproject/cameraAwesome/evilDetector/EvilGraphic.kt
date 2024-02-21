package com.example.groupproject.cameraAwesome.evilDetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.groupproject.preference.GraphicOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import com.google.mlkit.vision.objects.DetectedObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class EvilGraphic constructor(
overlay: GraphicOverlay,
private val detectedObject: DetectedObject
) : GraphicOverlay.Graphic(overlay) {

    private val numColors = COLORS.size

    private val boxPaints = Array(numColors) { Paint() }
    private val textPaints = Array(numColors) { Paint() }
    private val labelPaints = Array(numColors) { Paint() }

    init {
        for (i in 0 until numColors) {
            textPaints[i] = Paint()
            textPaints[i].color = COLORS[i][0]
            textPaints[i].textSize = TEXT_SIZE
            boxPaints[i] = Paint()
            boxPaints[i].color = COLORS[i][1]
            boxPaints[i].style = Paint.Style.STROKE
            boxPaints[i].strokeWidth = STROKE_WIDTH
            labelPaints[i] = Paint()
            labelPaints[i].color = COLORS[i][1]
            labelPaints[i].style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        // Decide color based on object tracking ID
        val colorID =
            if (detectedObject.trackingId == null) 0
            else abs(detectedObject.trackingId!! % NUM_COLORS)
        var textWidth =
            textPaints[colorID].measureText("Scanning Evil Person: " + detectedObject.trackingId)

        val lineHeight = TEXT_SIZE + STROKE_WIDTH
        var yLabelOffset = -lineHeight

        // Calculate width and height of label box
        for (label in detectedObject.labels) {
            textWidth =
                max(textWidth, textPaints[colorID].measureText(label.text))
            textWidth = max(
                textWidth,
                textPaints[colorID].measureText(
                    String.format(
                        Locale.US,
                        LABEL_FORMATT,
                        label.confidence * 100,
                        label.index
                    )
                )
            )
            yLabelOffset -= 2 * lineHeight
        }

        // Draws the bounding box.
        val rect = RectF(detectedObject.boundingBox)
        val x0 = translateX(rect.left)
        val x1 = translateX(rect.right)
        rect.left = min(x0, x1)
        rect.right = max(x0, x1)
        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, boxPaints[colorID])

        // Draws other object info.
        canvas.drawRect(
            rect.left - STROKE_WIDTH,
            rect.top + yLabelOffset,
            rect.left + textWidth + 2 * STROKE_WIDTH,
            rect.top,
            labelPaints[colorID]
        )
        yLabelOffset += TEXT_SIZE

        canvas.drawText(
            "Evil ID: " + detectedObject.trackingId,
            rect.left,
            rect.top + yLabelOffset,
            textPaints[colorID]
        )
        yLabelOffset += lineHeight
        for (label in detectedObject.labels) {
            canvas.drawText(
                label.text + " (index: " + label.index + ")",
                rect.left,
                rect.top + yLabelOffset,
                textPaints[colorID]
            )
            yLabelOffset += lineHeight
            canvas.drawText(
                String.format(
                    Locale.US,
                    LABEL_FORMATT,
                    label.confidence * 100,
                    label.index
                ),
                rect.left,
                rect.top + yLabelOffset,
                textPaints[colorID]
            )
            yLabelOffset += lineHeight
        }
    }

    companion object {
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
        private const val NUM_COLORS = 10
        private val COLORS =
            arrayOf(
                intArrayOf(Color.BLACK, Color.RED),
                intArrayOf(Color.WHITE, Color.MAGENTA),
                intArrayOf(Color.BLACK, Color.LTGRAY),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.WHITE, Color.BLUE),
                intArrayOf(Color.WHITE, Color.DKGRAY),
                intArrayOf(Color.BLACK, Color.CYAN),
                intArrayOf(Color.BLACK, Color.YELLOW),
                intArrayOf(Color.WHITE, Color.BLACK),
                intArrayOf(Color.BLACK, Color.GREEN)
            )
        private const val LABEL_FORMATT = "Evil (index: EVIL)"
    }
}

//    (overlay: GraphicOverlay?, private val face: Face) : GraphicOverlay.Graphic(overlay) {
//    private val facePositionPaint: Paint
//    private val numColors = COLORS.size
//    private val idPaints = Array(numColors) { Paint() }
//    private val boxPaints = Array(numColors) { Paint() }
//    private val labelPaints = Array(numColors) { Paint() }
//
//    init {
//        val selectedColor = Color.RED
//        facePositionPaint = Paint()
//        facePositionPaint.color = selectedColor
//        for (i in 0 until numColors) {
//            idPaints[i] = Paint()
//            idPaints[i].color = COLORS[i][0]
//            idPaints[i].textSize = ID_TEXT_SIZE
//            boxPaints[i] = Paint()
//            boxPaints[i].color = COLORS[i][1]
//            boxPaints[i].style = Paint.Style.STROKE
//            boxPaints[i].strokeWidth = BOX_STROKE_WIDTH
//            labelPaints[i] = Paint()
//            labelPaints[i].color = COLORS[i][1]
//            labelPaints[i].style = Paint.Style.FILL
//        }
//    }
//
//    /** Draws the face annotations for position on the supplied canvas. */
//    override fun draw(canvas: Canvas) {
//        // Draws a circle at the position of the detected face, with the face's track id below.
//
//        // Draws a circle at the position of the detected face, with the face's track id below.
//        val x = translateX(face.boundingBox.centerX().toFloat())
//        val y = translateY(face.boundingBox.centerY().toFloat())
//        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint)
//
//        // Calculate positions.
//        val left = x - scale(face.boundingBox.width() / 4.0f)
//        val top = y - scale(face.boundingBox.height() / 4.0f)
//        val right = x + scale(face.boundingBox.width() / 4.0f)
//        val bottom = y + scale(face.boundingBox.height() / 4.0f)
//        val lineHeight = ID_TEXT_SIZE + BOX_STROKE_WIDTH
//        var yLabelOffset: Float = if (face.trackingId == null) 0f else -lineHeight
//
//        // Decide color based on face ID
//        val colorID = if (face.trackingId == null) 0 else abs(face.trackingId!! % NUM_COLORS)
//
//        // Calculate width and height of label box
//        var textWidth = idPaints[colorID].measureText("Evil ID: " + face.trackingId)
//        if (face.smilingProbability != null) {
//            yLabelOffset -= lineHeight
//            textWidth =
//                max(
//                    textWidth,
//                    idPaints[colorID].measureText(
////                        String.format(Locale.US, "EVIl", face.smilingProbability)
//                       "EVIl" + face.smilingProbability
//                    )
//                )
//        }
//        if (face.leftEyeOpenProbability != null) {
//            yLabelOffset -= lineHeight
//            textWidth =
//                max(
//                    textWidth,
//                    idPaints[colorID].measureText(
//                        String.format(Locale.US, "EVIL", face.leftEyeOpenProbability)
//                    )
//                )
//        }
//        if (face.rightEyeOpenProbability != null) {
//            yLabelOffset -= lineHeight
//            textWidth =
//                max(
//                    textWidth,
//                    idPaints[colorID].measureText(
//                        String.format(Locale.US, "Right eye open: %.2f", face.rightEyeOpenProbability)
//                    )
//                )
//        }
//
//        yLabelOffset = yLabelOffset - 3 * lineHeight
//        textWidth =
//            Math.max(
//                textWidth,
//                idPaints[colorID].measureText(
//                    "Evil"
////                    String.format(Locale.US, "Hater: %.2f", face.headEulerAngleX)
//                )
//            )
//        textWidth =
//            Math.max(
//                textWidth,
//                idPaints[colorID].measureText(
////                    "Evil"
//                    String.format(Locale.US, "Evil: %.2f", face.headEulerAngleY)
//                )
//            )
//        textWidth =
//            Math.max(
//                textWidth,
//                idPaints[colorID].measureText(
//                    String.format(Locale.US, "Evil: %.2f", face.headEulerAngleZ)
//                )
//            )
//
//        // Draw labels
//        canvas.drawRect(
//            left - BOX_STROKE_WIDTH,
//            top + yLabelOffset,
//            left + textWidth + 2 * BOX_STROKE_WIDTH,
//            top,
//            labelPaints[colorID]
//        )
//        yLabelOffset += ID_TEXT_SIZE
//        canvas.drawRect(left, top, right, bottom, boxPaints[colorID])
//        if (face.trackingId != null) {
//            canvas.drawText("EVIL ID: " + face.trackingId, left, top + yLabelOffset, idPaints[colorID])
//            yLabelOffset += lineHeight
//        }
//
//        // Draws all face contours.
//        for (contour in face.allContours) {
//            for (point in contour.points) {
//                canvas.drawCircle(
//                    translateX(point.x),
//                    translateY(point.y),
//                    FACE_POSITION_RADIUS,
//                    facePositionPaint
//                )
//            }
//        }
//
//        // Draws smiling and left/right eye open probabilities.
//        if (face.smilingProbability != null) {
//            canvas.drawText(
//                "Evil: " + face.smilingProbability,
////                "Smiling: " + String.format(Locale.US, "%.2f", face.smilingProbability),
//                left,
//                top + yLabelOffset,
//                idPaints[colorID]
//            )
//            yLabelOffset += lineHeight
//        }
//
//        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
//        if (face.leftEyeOpenProbability != null) {
//            canvas.drawText(
//                "Left eye open: " + String.format(Locale.US, "%.2f", face.leftEyeOpenProbability),
//                left,
//                top + yLabelOffset,
//                idPaints[colorID]
//            )
//            yLabelOffset += lineHeight
//        }
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
//                "Evil Eye",
//                leftEyeLeft,
//                translateY(leftEye.position.y) + ID_Y_OFFSET,
//                idPaints[colorID]
//            )
//        }
//
//        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
//        if (face.rightEyeOpenProbability != null) {
//            canvas.drawText(
//                "Evil: " + String.format(Locale.US, "%.2f", face.rightEyeOpenProbability),
//                left,
//                top + yLabelOffset,
//
//                idPaints[colorID]
//            )
//            yLabelOffset += lineHeight
//        }
//        if (rightEye != null) {
//            val rightEyeLeft =
//                translateX(rightEye.position.x) - idPaints[colorID].measureText("Evil") / 2.0f
//            canvas.drawRect(
//                rightEyeLeft - BOX_STROKE_WIDTH,
//                translateY(rightEye.position.y) + ID_Y_OFFSET - ID_TEXT_SIZE,
//                rightEyeLeft + idPaints[colorID].measureText("Evil") + BOX_STROKE_WIDTH,
//                translateY(rightEye.position.y) + ID_Y_OFFSET + BOX_STROKE_WIDTH,
//                labelPaints[colorID]
//            )
//            canvas.drawText(
//                "Right Eye",
//                rightEyeLeft,
//                translateY(rightEye.position.y) + ID_Y_OFFSET,
//                idPaints[colorID]
//            )
//        }
//
//        canvas.drawText("Evil: " + face.headEulerAngleX, left, top + yLabelOffset, idPaints[colorID])
//        yLabelOffset += lineHeight
//        canvas.drawText("Hater: " + face.headEulerAngleY, left, top + yLabelOffset, idPaints[colorID])
//        yLabelOffset += lineHeight
//        canvas.drawText("Instigator: " + face.headEulerAngleZ, left, top + yLabelOffset, idPaints[colorID])
//
//        // Draw facial landmarks
//        drawFaceLandmark(canvas, FaceLandmark.LEFT_EYE)
//        drawFaceLandmark(canvas, FaceLandmark.RIGHT_EYE)
//        drawFaceLandmark(canvas, FaceLandmark.LEFT_CHEEK)
//        drawFaceLandmark(canvas, FaceLandmark.RIGHT_CHEEK)
//    }
//
//    private fun drawFaceLandmark(canvas: Canvas, @FaceLandmark.LandmarkType landmarkType: Int) {
//        val faceLandmark = face.getLandmark(landmarkType)
//        if (faceLandmark != null) {
//            canvas.drawCircle(
//                translateX(faceLandmark.position.x),
//                translateY(faceLandmark.position.y),
//                FACE_POSITION_RADIUS,
//                facePositionPaint
//            )
//        }
//    }
//
//    companion object {
//        private const val FACE_POSITION_RADIUS = 8.0f
//        private const val ID_TEXT_SIZE = 30.0f
//        private const val ID_Y_OFFSET = 40.0f
//        private const val BOX_STROKE_WIDTH = 5.0f
//        private const val NUM_COLORS = 10
//        private val COLORS =
//            arrayOf(
//                intArrayOf(Color.BLACK, Color.RED),
//                intArrayOf(Color.WHITE, Color.RED),
//                intArrayOf(Color.BLACK, Color.RED),
//                intArrayOf(Color.WHITE, Color.RED),
//                intArrayOf(Color.WHITE, Color.BLUE),
//                intArrayOf(Color.WHITE, Color.DKGRAY),
//                intArrayOf(Color.BLACK, Color.CYAN),
//                intArrayOf(Color.BLACK, Color.YELLOW),
//                intArrayOf(Color.WHITE, Color.RED),
//                intArrayOf(Color.BLACK, Color.GREEN)
//            )
//    }
//}
