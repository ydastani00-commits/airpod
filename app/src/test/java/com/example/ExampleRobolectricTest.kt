package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppPreferences
import com.example.data.DefaultPresets
import com.example.data.SoundModeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun testAppNameResource() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AirPods Control", appName)
  }

  @Test
  fun testAppPreferencesPersistence() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = AppPreferences(context)
    prefs.resetToDefaults()

    prefs.deviceName = "AirPods Pro Test"
    assertEquals("AirPods Pro Test", prefs.deviceName)

    prefs.bassBoostLevel = 750
    assertEquals(750, prefs.bassBoostLevel)

    prefs.soundMode = SoundModeType.ANC
    assertEquals(SoundModeType.ANC, prefs.soundMode)
  }

  @Test
  fun testDefaultPresetsConfig() {
    assertTrue(DefaultPresets.isNotEmpty())
    val defaultPreset = DefaultPresets.first()
    assertEquals("default", defaultPreset.id)
    assertEquals(10, defaultPreset.bandLevels.size)
  }
}
