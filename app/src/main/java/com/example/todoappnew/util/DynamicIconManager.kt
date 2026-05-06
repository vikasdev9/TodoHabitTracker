package com.example.todoappnew.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.example.todoappnew.domain.model.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DynamicIconManager handles switching the launcher icon by enabling/disabling
 * activity-aliases defined in the AndroidManifest.
 */
@Singleton
class DynamicIconManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val packageManager = context.packageManager
    private val packageName = context.packageName

    // Component names for the aliases defined in Manifest
    private val defaultAlias = "$packageName.MainActivity"
    private val darkAlias = "$packageName.MainActivityDark"

    fun updateIcon(theme: AppTheme) {
        val (enable, disable) = when (theme) {
            AppTheme.DARK -> darkAlias to defaultAlias
            else -> defaultAlias to darkAlias
        }

        // Enable the selected alias
        packageManager.setComponentEnabledSetting(
            ComponentName(context, enable),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Disable the other alias
        packageManager.setComponentEnabledSetting(
            ComponentName(context, disable),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
