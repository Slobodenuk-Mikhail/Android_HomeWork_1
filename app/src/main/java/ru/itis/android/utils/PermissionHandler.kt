package ru.itis.android.utils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionHandler(
    private var onPermissionGranted: (() -> Unit)? = null,
    private var onPermissionDenied: (() -> Unit)? = null,
    activity: ComponentActivity
) {
    private var multiplePermissions: ActivityResultLauncher<Array<String>>? =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
            resultMap.entries.forEach { entry ->
                if (entry.value) {
                    onPermissionGranted?.invoke()
                } else {
                    onPermissionDenied?.invoke()
                }
            }
        }


    fun requestMultiplePermission(permission: List<String>) {
        multiplePermissions?.launch(permission.toTypedArray())
    }
}