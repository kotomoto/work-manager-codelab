package com.example.background.workers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import com.example.background.R


class BlurWorker : Worker() {
    val TAG: String = BlurWorker::class.java.simpleName

    override fun doWork(): Worker.Result {
        val context = applicationContext

        try {
            val picture: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test)
            val blurredBitmap = WorkerUtils.blurBitmap(picture, context)
            val writeBitmapToFileResult = WorkerUtils.writeBitmapToFile(context, blurredBitmap)
            WorkerUtils.makeStatusNotification("Output is " + writeBitmapToFileResult.toString(), context)

            return Worker.Result.SUCCESS
        } catch (th: Throwable) {
            Log.e(TAG, "Error applying blur", th)

            return Worker.Result.FAILURE
        }
    }
}