package com.example.groupproject.cameraAwesome.subjectsegmenter

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.groupproject.cameraAwesome.VisionProcessorBase
import com.example.groupproject.preference.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenter
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions

/** A processor to run Subject Segmenter. */
@RequiresApi(Build.VERSION_CODES.N)
class SubjectSegmenterProcessor : VisionProcessorBase<SubjectSegmentationResult> {
  private val subjectSegmenter: SubjectSegmenter
  private var imageWidth: Int = 0
  private var imageHeight: Int = 0

  constructor(context: Context) : super(context) {
    subjectSegmenter =
      SubjectSegmentation.getClient(
        SubjectSegmenterOptions.Builder()
          .enableMultipleSubjects(
            SubjectSegmenterOptions.SubjectResultOptions.Builder().enableConfidenceMask().build()
          )
          .build()
      )

    Log.d(TAG, "SubjectSegmenterProcessor created")
  }

  override fun detectInImage(image: InputImage): Task<SubjectSegmentationResult> {
    this.imageWidth = image.width
    this.imageHeight = image.height
    return subjectSegmenter.process(image)
  }

  override fun onSuccess(
    results: SubjectSegmentationResult,
    graphicOverlay: GraphicOverlay
  ) {
    graphicOverlay.add(
      SubjectSegmentationGraphic(graphicOverlay, results, imageWidth, imageHeight)
    )
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Segmentation failed: $e")
  }

  companion object {
    private const val TAG = "SbjSegmenterProcessor"
  }
}
