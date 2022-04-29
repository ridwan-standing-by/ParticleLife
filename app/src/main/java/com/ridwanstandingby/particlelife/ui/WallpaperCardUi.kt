package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

@Composable
fun WallpaperContent(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    selectedWallpaperPhysics: MutableState<WallpaperPhysicsSetting>,
    setWallpaperClicked: () -> Unit,
    wallpaperParameters: State<ParticleLifeParameters>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit,
    wallpaperShuffleForceValues: State<ShuffleForceValues>,
    changeWallpaperForceValues: (ShuffleForceValues) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
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
                .padding(top = 4.dp)
        )
        if (isPortrait()) {
            SetWallpaperButton(setWallpaperClicked)
            WallpaperPhysicsSelectionWidget(
                selectedWallpaperPhysics,
                wallpaperParametersChanged
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
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    SetWallpaperButton(setWallpaperClicked)
                    WallpaperPhysicsSelectionWidget(
                        selectedWallpaperPhysics,
                        wallpaperParametersChanged
                    )
                    WallpaperShuffleForceValuesSelectionWidget(
                        wallpaperShuffleForceValues,
                        changeWallpaperForceValues
                    )
                    NumberOfParticlesWidget(
                        wallpaperGenerationParameters,
                        wallpaperGenerationParametersChanged
                    )
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
            }
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
            .align(Alignment.CenterHorizontally)
    ) {
        Text(stringResource(R.string.set_wallpaper_button_label))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.WallpaperPhysicsSelectionWidget(
    selectedWallpaperPhysics: MutableState<WallpaperPhysicsSetting>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit
) {
    val options = ParticleLifeParameters.RuntimeParameters.Preset.all()
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            focusManager.clearFocus()
        },
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
    ) {
        TextField(
            readOnly = true,
            value = stringResource(selectedWallpaperPhysics.value.nameString()),
            onValueChange = { },
            enabled = true,
            label = { Text(stringResource(R.string.physics_settings_label)) },
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
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedWallpaperPhysics.value = option
                        wallpaperParametersChanged { option.applyPreset(this.runtime) }
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
            .align(Alignment.CenterHorizontally)
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
                wallpaperShuffleForceValues = remember { mutableStateOf(ShuffleForceValues.DEFAULT) },
                changeWallpaperForceValues = {}
            )
        }
    }
}