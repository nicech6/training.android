package com.training.app;


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.lang.reflect.Field
import java.lang.reflect.Method

object StatusBarUtil {
    private const val MODE_NONE = 0
    private const val MODE_MIUI = 1
    private const val MODE_FLYME = 2
    private const val MODE_MARSHMALLOW = 3
    private const val MODE_LOLLIPOP = 4
    private const val MODE_KITKAT = 5
    private var mode = 0
    private lateinit var method: Method
    private lateinit var field: Field
    private var flag = 0
    var statusHeight = 0
        private set
        @JvmStatic get

    fun init(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusHeight = resources.getDimensionPixelSize(resourceId)
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mode = MODE_NONE
            return
        }
        val window = activity.window
        mode = when {
            initMiui(window) -> MODE_MIUI
            initMarshmallow(window) -> MODE_MARSHMALLOW
            initFlyme(window) -> MODE_FLYME
            initLollipop(window) -> MODE_LOLLIPOP
            else -> MODE_KITKAT
        }
    }

    private fun initMiui(window: Window): Boolean {
        val clazz: Class<out Window> = window.javaClass
        return try {
            method = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            @SuppressLint("PrivateApi") val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            flag = field.getInt(layoutParams)
            initMarshmallow(window)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun initFlyme(window: Window): Boolean {
        return try {
            val lp = window.attributes
            val instance = Class.forName("android.view.WindowManager\$LayoutParams")
            field = instance.getDeclaredField("meizuFlags").apply { isAccessible = true }
            flag = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun initMarshmallow(window: Window): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000
            val view = window.decorView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            true
        } else {
            false
        }
    }

    private fun initLollipop(window: Window): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x66000000
            val view = window.decorView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            true
        } else {
            false
        }
    }

    private fun setMiui(activity: Activity, whiteStatus: Boolean) {
        try {
            method.invoke(activity.window, if (whiteStatus) flag else 0, flag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setMarshmallow(activity, whiteStatus)
        }
    }

    private fun setFlyme(activity: Activity, whiteStatus: Boolean) {
        try {
            val lp = activity.window.attributes
            val origin = field.getInt(lp)
            if (whiteStatus) {
                field.set(lp, origin or flag)
            } else {
                field.set(lp, flag.inv() and origin)
            }
            activity.window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setMarshmallow(activity: Activity, whiteStatus: Boolean) {
        val view = activity.window.decorView
        var flags = view.systemUiVisibility
        flags = if (whiteStatus) {
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        view.systemUiVisibility = flags
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun setWhiteStatus(activity: Activity, whiteStatus: Boolean) {
        when (mode) {
            MODE_MIUI -> setMiui(activity, whiteStatus)
            MODE_FLYME -> setFlyme(activity, whiteStatus)
            MODE_MARSHMALLOW -> setMarshmallow(activity, whiteStatus)
        }
    }
}
