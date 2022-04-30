package com.ridwanstandingby.particlelife.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.particlelife.wallpaper.ShuffleForceValues
import com.ridwanstandingby.particlelife.wallpaper.WallpaperMode

@Composable
fun WallpaperContent(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    selectedWallpaperPhysics: MutableState<WallpaperPhysicsSetting>,
    setWallpaperClicked: () -> Unit,
    wallpaperParameters: State<ParticleLifeParameters>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit,
    wallpaperMode: State<WallpaperMode>,
    changeWallpaperMode: (WallpaperMode) -> Unit,
    wallpaperShuffleForceValues: State<ShuffleForceValues>,
    changeWallpaperForceValues: (ShuffleForceValues) -> Unit,
    saveCurrentSettingsForWallpaper: () -> Unit,
    loadCurrentSettingsFromWallpaper: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        val wallpaperRuntimeParameters = remember {
            derivedStateOf { wallpaperParameters.value.runtime.copy() }
        }
        val wallpaperGenerationParameters = remember {
            derivedStateOf { wallpaperParameters.value.generation.copy() }
        }
        val wallpaperRuntimeParametersChanged =
            { block: ParticleLifeParameters.RuntimeParameters.() -> Unit ->
                wallpaperParametersChanged { runtime.block() }
            }
        val wallpaperGenerationParametersChanged =
            { block: ParticleLifeParameters.GenerationParameters.() -> Unit ->
                wallpaperParametersChanged { generation.block() }
            }
        Text(
            text = stringResource(R.string.wallpaper_info),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        Divider(
            Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(vertical = 4.dp)
        )
        if (isPortrait()) {
            SetWallpaperButton(setWallpaperClicked)
            WallpaperModeOptions(wallpaperMode, changeWallpaperMode)

            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
            )

            WidgetsForMode(
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                selectedWallpaperPhysics,
                setWallpaperClicked,
                wallpaperRuntimeParameters,
                wallpaperRuntimeParametersChanged,
                wallpaperGenerationParameters,
                wallpaperGenerationParametersChanged,
                wallpaperMode,
                changeWallpaperMode,
                wallpaperShuffleForceValues,
                changeWallpaperForceValues,
                saveCurrentSettingsForWallpaper,
                loadCurrentSettingsFromWallpaper
            )

        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    SetWallpaperButton(setWallpaperClicked)
                    WallpaperModeOptions(wallpaperMode, changeWallpaperMode)
                }
                Divider(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(start = 12.dp)
                ) {
                    WidgetsForMode(
                        controlPanelExpanded,
                        editHandOfGodPanelExpanded,
                        selectedWallpaperPhysics,
                        setWallpaperClicked,
                        wallpaperRuntimeParameters,
                        wallpaperRuntimeParametersChanged,
                        wallpaperGenerationParameters,
                        wallpaperGenerationParametersChanged,
                        wallpaperMode,
                        changeWallpaperMode,
                        wallpaperShuffleForceValues,
                        changeWallpaperForceValues,
                        saveCurrentSettingsForWallpaper,
                        loadCurrentSettingsFromWallpaper
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.WidgetsForMode(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    selectedWallpaperPhysics: MutableState<WallpaperPhysicsSetting>,
    setWallpaperClicked: () -> Unit,
    wallpaperRuntimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    wallpaperRuntimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    wallpaperGenerationParameters: State<ParticleLifeParameters.GenerationParameters>,
    wallpaperGenerationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    wallpaperMode: State<WallpaperMode>,
    changeWallpaperMode: (WallpaperMode) -> Unit,
    wallpaperShuffleForceValues: State<ShuffleForceValues>,
    changeWallpaperForceValues: (ShuffleForceValues) -> Unit,
    saveCurrentSettingsForWallpaper: () -> Unit,
    loadCurrentSettingsFromWallpaper: () -> Unit
) {
    when (wallpaperMode.value) {
        WallpaperMode.Preset -> {
            WallpaperPhysicsSelectionWidget(
                selectedWallpaperPhysics,
                wallpaperRuntimeParametersChanged
            )
            WallpaperShuffleForceValuesSelectionWidget(
                wallpaperShuffleForceValues,
                changeWallpaperForceValues
            )
            NumberOfParticlesWidget(
                wallpaperGenerationParameters,
                wallpaperGenerationParametersChanged
            )
            NumberOfSpeciesWidget(
                wallpaperGenerationParameters,
                wallpaperGenerationParametersChanged
            )
            HandOfGodEnabledSwitchWidget(
                HandOfGodPanelMode.WALLPAPER,
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                wallpaperRuntimeParameters,
                wallpaperRuntimeParametersChanged
            )
        }
        WallpaperMode.Randomise -> {
            WallpaperShuffleForceValuesSelectionWidget(
                wallpaperShuffleForceValues,
                changeWallpaperForceValues
            )
            NumberOfParticlesWidget(
                wallpaperGenerationParameters,
                wallpaperGenerationParametersChanged
            )
            NumberOfSpeciesWidget(
                wallpaperGenerationParameters,
                wallpaperGenerationParametersChanged
            )
            HandOfGodEnabledSwitchWidget(
                HandOfGodPanelMode.WALLPAPER,
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                wallpaperRuntimeParameters,
                wallpaperRuntimeParametersChanged
            )
        }
        WallpaperMode.CurrentSettings -> {
            SaveSettingsButton(saveCurrentSettingsForWallpaper)
            LoadSettingsButton(loadCurrentSettingsFromWallpaper)
            WallpaperShuffleForceValuesSelectionWidget(
                wallpaperShuffleForceValues,
                changeWallpaperForceValues
            )
            HandOfGodEnabledSwitchWidget(
                HandOfGodPanelMode.WALLPAPER,
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                wallpaperRuntimeParameters,
                wallpaperRuntimeParametersChanged
            )
        }
    }
}

@Composable
private fun ColumnScope.SetWallpaperButton(setWallpaperClicked: () -> Unit) {
    Button(
        onClick = setWallpaperClicked,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.set_wallpaper_button_label))
    }
}

@Composable
private fun WallpaperModeOptions(
    wallpaperMode: State<WallpaperMode>,
    changeWallpaperMode: (WallpaperMode) -> Unit
) {
    WallpaperMode.values().forEach {
        WallpaperModeOption(it, wallpaperMode, changeWallpaperMode)
    }
}

@Composable
private fun WallpaperModeOption(
    thisMode: WallpaperMode,
    selectedWallpaperMode: State<WallpaperMode>,
    changeWallpaperMode: (WallpaperMode) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                changeWallpaperMode(thisMode)
            }
            .padding(vertical = 6.dp)
    ) {
        RadioButton(
            selected = thisMode == selectedWallpaperMode.value,
            onClick = null,
            modifier = Modifier
                .align(CenterVertically)
                .padding(start = 4.dp)
        )
        Text(
            stringResource(thisMode.nameString()),
            style = MaterialTheme.typography.button,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(CenterVertically)
        )
    }
}

@Composable
private fun ColumnScope.WallpaperPhysicsSelectionWidget(
    selectedWallpaperPhysics: MutableState<WallpaperPhysicsSetting>,
    wallpaperRuntimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    PresetSelectionWidget(
        selectedWallpaperPhysics,
        wallpaperRuntimeParametersChanged,
        Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .align(CenterHorizontally)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.WallpaperShuffleForceValuesSelectionWidget(
    wallpaperShuffleForceValues: State<ShuffleForceValues>,
    changeWallpaperForceValues: (ShuffleForceValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            focusManager.clearFocus()
        },
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .align(CenterHorizontally)
    ) {
        TextField(
            readOnly = true,
            value = stringResource(wallpaperShuffleForceValues.value.nameString()),
            onValueChange = { },
            enabled = true,
            label = { Text(stringResource(R.string.shuffle_force_values_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.focusable(enabled = false)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            }
        ) {
            ShuffleForceValues.all().forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        changeWallpaperForceValues(option)
                        expanded = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text(stringResource(option.nameString()))
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SaveSettingsButton(saveSettingsClicked: () -> Unit) {
    Button(
        onClick = saveSettingsClicked,
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.save_current_settings_to_wallpaper_label))
    }
}


@Composable
private fun ColumnScope.LoadSettingsButton(loadSettingsClicked: () -> Unit) {
    Button(
        onClick = loadSettingsClicked,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.load_wallpaper_to_current_settings_label))
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun WallpaperCardUiPreview() {
    val wallpaperParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ), neverEqualPolicy()
        )
    }
    ParticleLifeTheme {
        Scaffold {
            WallpaperContent(
                controlPanelExpanded = remember { mutableStateOf(true) },
                editHandOfGodPanelExpanded = remember { mutableStateOf(HandOfGodPanelMode.OFF) },
                selectedWallpaperPhysics = remember {
                    mutableStateOf(ParticleLifeParameters.RuntimeParameters.Preset.default())
                },
                setWallpaperClicked = {},
                wallpaperParameters = wallpaperParameters,
                wallpaperParametersChanged = {
                    wallpaperParameters.value = wallpaperParameters.value.copy().apply { this.it() }
                },
                wallpaperMode = remember { mutableStateOf(WallpaperMode.DEFAULT) },
                changeWallpaperMode = {},
                wallpaperShuffleForceValues = remember { mutableStateOf(ShuffleForceValues.DEFAULT) },
                changeWallpaperForceValues = {},
                saveCurrentSettingsForWallpaper = {},
                loadCurrentSettingsFromWallpaper = {}
            )
        }
    }
}