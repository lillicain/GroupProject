package com.example.groupproject.camera

import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import java.lang.reflect.Modifier


//@Composable
//
//class CameraPreview(controller: LifecycleCameraController, modifier: Modifier) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    AndroidView(
//    factory = {
//        PreviewView(it).apply {
//            this.controller = controller
//            controller.bindToLifecycle(lifecycleOwner)
//        }
//    },
//    modifier = modifier
//    )
//
//}
//
//@Composable
//fun PhotoBottomSheetContent(
//    bitmaps: List<Bitmap>,
//    modifier: Modifier = Modifier
//) {
//    if(bitmaps.isEmpty()) {
//        Box(
//            modifier = modifier
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("There are no photos yet")
//        }
//    } else {
//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(2),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalItemSpacing = 16.dp,
//            contentPadding = PaddingValues(16.dp),
//            modifier = modifier
//        ) {
//            items(bitmaps) { bitmap ->
//                Image(
//                    bitmap = bitmap.asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(10.dp))
//                )
//            }
//        }
//    }
//}