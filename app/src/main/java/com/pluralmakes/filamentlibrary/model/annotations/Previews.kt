package com.pluralmakes.filamentlibrary.model.annotations

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Default Preview Dark",
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    showSystemUi = true,
    apiLevel = 33,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Default Preview Light",
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    showSystemUi = true,
    apiLevel = 33,
)
annotation class Previews