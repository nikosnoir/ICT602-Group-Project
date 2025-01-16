class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null // For now, return null if it's not bound
    }

    override fun onCreate() {
        super.onCreate()
        // Service is created
    }
}