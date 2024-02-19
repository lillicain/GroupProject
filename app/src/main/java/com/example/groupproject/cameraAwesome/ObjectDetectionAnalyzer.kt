package com.example.groupproject.cameraAwesome

import android.annotation.SuppressLint
import android.media.Image
import androidx.annotation.NonNull
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.util.concurrent.TimeUnit

class ObjectDetectionAnalyzer(
    private val listener: (detectedObjects: DetectedObjects) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector: ObjectDetector

    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()

        detector = ObjectDetection.getClient(options)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val processingImage = imageProxy.image
        if (processingImage == null) {
            imageProxy.close()
            return
        }

        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        val image = InputImage.fromMediaImage(
            processingImage,
            rotationDegrees
        )

        try {
            val results = Tasks.await(detector.process(image), 1, TimeUnit.SECONDS)

            debugPrint(results)

            listener(DetectedObjects(rotationDegrees, processingImage, results))
        } catch (e: Exception) {
            println("failure : $e")
        }

        imageProxy.close()
    }

    private fun debugPrint(visionObjects: List<DetectedObject>) {
        for ((idx, obj) in visionObjects.withIndex()) {
            val box = obj.boundingBox

            println("Detected object: $idx")
            println("  trackingId: ${obj.trackingId}")
            println("  boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")
            for (label in obj.labels) {
                val confidence: Int = label.confidence.times(100).toInt()
                println("  Label[${label.index}], Confidence: $confidence%, Text: ${label.text}")
            }
        }
    }
}

class DetectedObjects(
    rotationDegrees: Int,
    @NonNull image: Image,
    val objects: List<DetectedObject>
) {
    val imageWidth: Int
    val imageHeight: Int

    init {
        when (rotationDegrees) {
            90, 270 -> {
                imageWidth = image.height
                imageHeight = image.width
            }
            else -> {
                imageWidth = image.width
                imageHeight = image.height
            }
        }
    }
}
