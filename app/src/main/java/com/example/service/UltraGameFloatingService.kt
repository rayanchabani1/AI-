package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.MainActivity
import com.example.ui.components.RedMagicFloatingBubble
import com.example.ui.components.RedMagicGameSpaceOverlay
import com.example.util.RootDeviceController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UltraGameFloatingService : Service(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var isExpanded by mutableStateOf(false)
    private var liveKernelFps by mutableStateOf(120)
    private var livePingMs by mutableStateOf(28)

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        startForegroundNotification()
        setupFloatingOverlay()
    }

    private fun startForegroundNotification() {
        val channelId = "ultra_game_root_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "REDMAGIC Game Space Root Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("🎮 REDMAGIC Game Turbo Floating HUD")
            .setContentText("الفقاعة العائمة نشطة - انقر لإظهار واجهة REDMAGIC Game Space فوق الألعاب")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1001, notification)
    }

    private fun setupFloatingOverlay() {
        serviceScope.launch {
            val isGranted = Settings.canDrawOverlays(this@UltraGameFloatingService)
            if (!isGranted) {
                RootDeviceController.grantOverlayPermissionViaRoot(packageName)
            }

            // Real-time kernel FPS and Ping polling loop
            serviceScope.launch {
                while (true) {
                    try {
                        liveKernelFps = RootDeviceController.getKernelRealtimeFps()
                        livePingMs = RootDeviceController.getRealtimeNetworkPingMs()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    kotlinx.coroutines.delay(1000)
                }
            }

            createFloatingBubbleWindow()
        }
    }

    private fun createFloatingBubbleWindow() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 20
            y = 350
        }

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@UltraGameFloatingService)
            setViewTreeViewModelStoreOwner(this@UltraGameFloatingService)
            setViewTreeSavedStateRegistryOwner(this@UltraGameFloatingService)

            setContent {
                if (isExpanded) {
                    RedMagicGameSpaceOverlay(
                        onMinimizeToBubble = {
                            isExpanded = false
                        },
                        onOpenMainApp = {
                            isExpanded = false
                            val launchIntent = Intent(this@UltraGameFloatingService, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra("OPEN_GAME_HUD", true)
                            }
                            startActivity(launchIntent)
                        }
                    )
                } else {
                    RedMagicFloatingBubble(
                        currentFps = liveKernelFps,
                        pingMs = livePingMs,
                        isTurboActive = true,
                        onClickBubble = {
                            isExpanded = true
                        }
                    )
                }
            }
        }

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        composeView.setOnTouchListener { v, event ->
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val rawNewX = initialX + (event.rawX - initialTouchX).toInt()
                    val rawNewY = initialY + (event.rawY - initialTouchY).toInt()

                    // Clamp Y boundary within visible screen
                    params.x = rawNewX
                    params.y = rawNewY.coerceIn(20, screenHeight - 120)
                    windowManager?.updateViewLayout(composeView, params)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val diffX = Math.abs(event.rawX - initialTouchX)
                    val diffY = Math.abs(event.rawY - initialTouchY)
                    if (diffX < 15 && diffY < 15) {
                        v.performClick()
                    } else if (!isExpanded) {
                        // Magnetic side snap to closest screen edge when collapsed
                        val middleScreen = screenWidth / 2
                        params.x = if (event.rawX < middleScreen) 16 else (screenWidth - 200)
                        windowManager?.updateViewLayout(composeView, params)
                    }
                    true
                }
                else -> false
            }
        }

        overlayView = composeView

        try {
            windowManager?.addView(composeView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_BACKGROUND || level >= TRIM_MEMORY_MODERATE) {
            System.gc()
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
        super.onDestroy()
        overlayView?.let {
            try {
                windowManager?.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, UltraGameFloatingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, UltraGameFloatingService::class.java)
            context.stopService(intent)
        }
    }
}
