package com.example.data

enum class ConnectionStatus(val faTitle: String) {
    CONNECTED("متصل"),
    CONNECTING("در حال اتصال"),
    DISCONNECTED("قطع ارتباط"),
    BLUETOOTH_OFF("بلوتوث خاموش است"),
    PERMISSION_REQUIRED("نیازمند مجوز")
}

data class BatteryInfo(
    val leftLevel: Int? = 100,
    val rightLevel: Int? = 100,
    val caseLevel: Int? = 72,
    val isCharging: Boolean = true,
    val timeToFullMinutes: Int? = 45,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
    val isRealData: Boolean = false,
    val sourceDescription: String = "اطلاعات استاندارد بر اساس آخرین وضعیت ثبت‌شده یا تخمینی"
) {
    val leftDisplay: String get() = leftLevel?.let { "$it%" } ?: "نامشخص"
    val rightDisplay: String get() = rightLevel?.let { "$it%" } ?: "نامشخص"
    val caseDisplay: String get() = caseLevel?.let { "$it%" } ?: "نامشخص"
}

data class EqualizerPreset(
    val id: String,
    val faTitle: String,
    val iconType: String,
    val bandLevels: List<Float> // 10 bands normalized from -6 dB to +6 dB
)

val DefaultPresets = listOf(
    EqualizerPreset(
        id = "default",
        faTitle = "پیش‌فرض",
        iconType = "wave",
        bandLevels = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    ),
    EqualizerPreset(
        id = "deep_bass",
        faTitle = "باس عمیق",
        iconType = "bass",
        bandLevels = listOf(5.5f, 4.5f, 3.5f, 1.5f, 0f, -0.5f, -1f, 0f, 1.5f, 2f)
    ),
    EqualizerPreset(
        id = "pop",
        faTitle = "پاپ",
        iconType = "pop",
        bandLevels = listOf(-1f, 1.5f, 3f, 4f, 3.5f, 2f, 0.5f, 2f, 3.5f, 4f)
    ),
    EqualizerPreset(
        id = "rock",
        faTitle = "راک",
        iconType = "rock",
        bandLevels = listOf(4.5f, 3f, 1.5f, -0.5f, -1.5f, -0.5f, 2f, 3.5f, 4.5f, 5f)
    ),
    EqualizerPreset(
        id = "vocal",
        faTitle = "صدای شفاف",
        iconType = "vocal",
        bandLevels = listOf(-2f, -1f, 0f, 2f, 4.5f, 5f, 4f, 2.5f, 1.5f, 0.5f)
    ),
    EqualizerPreset(
        id = "podcast",
        faTitle = "پادکست",
        iconType = "podcast",
        bandLevels = listOf(-3f, -1.5f, 1f, 3.5f, 4f, 3.5f, 2f, 0.5f, -1f, -2f)
    )
)

enum class SoundModeType(val faTitle: String, val faDescription: String) {
    NORMAL("حالت عادی", "صدای متعادل و طبیعی"),
    TRANSPARENCY("حالت شفافیت", "شنیدن صدای محیط اطراف"),
    ANC("حالت حذف نویز", "کاهش نویز محیط"),
    ADAPTIVE("حالت ضمنی", "با توجه به محیط خود، حالت مناسب را انتخاب کنید")
}

data class ScannedDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val isAirPods: Boolean,
    val lastSeenTimestamp: Long = System.currentTimeMillis()
) {
    val distanceEstimate: String
        get() = when {
            rssi >= -55 -> "بسیار نزدیک (کمتر از ۱ متر)"
            rssi >= -70 -> "نزدیک (حدود ۲ تا ۳ متر)"
            rssi >= -85 -> "متوسط (حدود ۴ تا ۸ متر)"
            else -> "دور (بیش از ۸ متر)"
        }
}

data class AirPodsSettings(
    val inEarDetection: Boolean = true,
    val micOptimization: Boolean = true,
    val lowLatency: Boolean = false,
    val floatingShortcut: Boolean = true,
    val ambientSoundLevel: Float = 0.5f,
    val autoConnect: Boolean = true,
    val deviceName: String = "AirPods Pro",
    val firmwareVersion: String = "5.0.0"
)
