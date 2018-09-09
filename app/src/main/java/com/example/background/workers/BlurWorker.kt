package com.example.background.workers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.example.background.Constants


class BlurWorker : Worker() {
    val TAG: String = BlurWorker::class.java.simpleName

    override fun doWork(): Worker.Result {
        val context = applicationContext
        val resourceUri : String? = inputData.getString(Constants.KEY_IMAGE_URI)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalStateException("Invalid input uri")
            }

            val resolver : ContentResolver = context.contentResolver
            val picture : Bitmap = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

            val blurredBitmap = WorkerUtils.blurBitmap(picture, context)
            val writeBitmapToFileResult = WorkerUtils.writeBitmapToFile(context, blurredBitmap)
            WorkerUtils.makeStatusNotification("Output is " + writeBitmapToFileResult.toString(), context)

            outputData = Data.Builder().putString(Constants.KEY_IMAGE_URI, writeBitmapToFileResult.toString())
                    .build()

            Worker.Result.SUCCESS
        } catch (th: Throwable) {
            Log.e(TAG, "Error applying blur", th)

            Worker.Result.FAILURE
        }
    }
}