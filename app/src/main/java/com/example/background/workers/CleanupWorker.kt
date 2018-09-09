package com.example.background.workers

import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import com.example.background.Constants
import java.io.File


class CleanupWorker : Worker() {
    val TAG = CleanupWorker::class.java.simpleName

    override fun doWork(): Result {
        val applicationContext = applicationContext

        try {
            val outputDirectory = File(applicationContext.filesDir, Constants.OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null && entries.isNotEmpty()) {
                    entries.forEach { entry ->
                        val name = entry.name
                        if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, String.format("Deleted %s - %s", name, deleted))
                        }
                    }
                }
            }

            return Worker.Result.SUCCESS
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)

            return Worker.Result.FAILURE
        }
    }
}