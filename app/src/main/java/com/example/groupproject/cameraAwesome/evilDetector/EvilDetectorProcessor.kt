package com.example.groupproject.cameraAwesome.evilDetector

import android.content.Context
import android.util.Log
import com.example.groupproject.cameraAwesome.VisionProcessorBase
import com.example.groupproject.cameraAwesome.facedetector.FaceGraphic
import com.example.groupproject.cameraAwesome.objectdetector.ObjectGraphic
import com.example.groupproject.preference.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase
import java.io.IOException
import java.util.Locale

class EvilDetectorProcessor(context: Context, options: ObjectDetectorOptionsBase) :
    VisionProcessorBase<List<DetectedObject>>(context) {

    private val detector: ObjectDetector = ObjectDetection.getClient(options)

    override fun stop() {
        super.stop()
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Exception thrown while trying to close object detector!",
                e
            )
        }
    }

    override fun detectInImage(image: InputImage): Task<List<DetectedObject>> {
        return detector.process(image)
    }

    override fun onSuccess(results: List<DetectedObject>, graphicOverlay: GraphicOverlay) {
        for (result in results) {
            graphicOverlay.add(EvilGraphic(graphicOverlay, result))
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Object detection failed!", e)
    }

    companion object {
        private const val TAG = "EvilDetectorProcessor"
    }
}

//class EvilDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
//    VisionProcessorBase<List<Face>>(context) {
//
//    private val detector: FaceDetector
//
//    init {
//        val options = detectorOptions
//            ?: FaceDetectorOptions.Builder()
//                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//                .enableTracking()
//                .build()
//
//        detector = FaceDetection.getClient(options)
//
//        Log.v(MANUAL_TESTING_LOG, "Evil detector options: $options")
//    }
//
//    override fun stop() {
//        super.stop()
//        detector.close()
//    }
//
//    override fun detectInImage(image: InputImage): Task<List<Face>> {
//        return detector.process(image)
//    }
//
//    override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
//        for (face in faces) {
//            graphicOverlay.add(FaceGraphic(graphicOverlay, face))
//            logExtrasForTesting(face)
//        }
//    }
//
//    override fun onFailure(e: Exception) {
//        Log.e(TAG, "Evil detection failed $e")
//    }
//
//    companion object {
//        private const val TAG = "EvilDetectorProcessor"
//        private fun logExtrasForTesting(face: Face?) {
//            if (face != null) {
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "face bounding box: " + face.boundingBox.flattenToString()
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil X: " + face.headEulerAngleX
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil face Y: " + face.headEulerAngleY
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil Z: " + face.headEulerAngleZ
//                )
//                // All landmarks
//                val landMarkTypes = intArrayOf(
//                    FaceLandmark.MOUTH_BOTTOM,
//                    FaceLandmark.MOUTH_RIGHT,
//                    FaceLandmark.MOUTH_LEFT,
//                    FaceLandmark.RIGHT_EYE,
//                    FaceLandmark.LEFT_EYE,
//                    FaceLandmark.RIGHT_EAR,
//                    FaceLandmark.LEFT_EAR,
//                    FaceLandmark.RIGHT_CHEEK,
//                    FaceLandmark.LEFT_CHEEK,
//                    FaceLandmark.NOSE_BASE
//                )
//                val landMarkTypesStrings = arrayOf(
////                    "EVIL_PERSON",
////                    "EVIL",
////                    "EVIL"
//                    "MOUTH_BOTTOM",
//                    "MOUTH_RIGHT",
//                    "MOUTH_LEFT",
//                    "RIGHT_EYE",
//                    "LEFT_EYE",
//                    "RIGHT_EAR",
//                    "LEFT_EAR",
//                    "RIGHT_CHEEK",
//                    "LEFT_CHEEK",
//                    "NOSE_BASE"
//                )
//                for (i in landMarkTypes.indices) {
//                    val landmark = face.getLandmark(landMarkTypes[i])
//                    if (landmark == null) {
//                        Log.v(
//                            MANUAL_TESTING_LOG,
//                            landMarkTypesStrings[i] + " evil has been detected"
//                        )
//                    } else {
//                        val landmarkPosition = landmark.position
//                        val landmarkPositionStr =
//                            String.format(Locale.US, "EVILNESS: %f , EVILNESS: %f", landmarkPosition.x, landmarkPosition.y)
//                        Log.v(
//                            MANUAL_TESTING_LOG,
//                            "Position for face landmark: " +
//                                    landMarkTypesStrings[i] +
//                                    " is :" +
//                                    landmarkPositionStr
//                        )
//                    }
//                }
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil probability: " + face.leftEyeOpenProbability
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil eye open probability: " + face.rightEyeOpenProbability
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "face smiling probability: " + face.smilingProbability
//                )
//                Log.v(
//                    MANUAL_TESTING_LOG,
//                    "evil tracking id: " + face.trackingId
//                )
//            }
//        }
//    }
//}
