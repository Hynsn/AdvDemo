package com.hynson.shortcut

object Const {
    const val EXTRA_SHORTCUT_ID = "ShortcutId"
    const val EXTRA_SHORTCUT_LABEL = "ShortcutLabel"

    const val ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT"
    const val ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT"
    const val LAUNCHER_SETTINGS = "com.android.launcher"

    const val OPPO = "content://settings/secure/launcher_shortcut_permission_settings"

    val TAG = ShortcutUtil::class.simpleName

}