package com.ridwanstandingby.particlelife.ui

import android.view.MotionEvent
import android.view.SurfaceView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.Species
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.particlelife.ui.theme.icons.Icons
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.ArrowBack
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.Tune
import com.ridwanstandingby.particlelife.wallpaper.ShuffleForceValues
import com.ridwanstandingby.particlelife.wallpaper.WallpaperMode
import com.ridwanstandingby.verve.math.FloatVector2

@Composable
fun ParticleLifeActivityUi(
    createAnimationSurface: () -> SurfaceView,
    onTouchEvent: (MotionEvent?) -> Boolean,
    setWallpaper: () -> Unit,
    vm: ParticleLifeViewModel
) {
    ParticleLifeUi(
        createAnimationSurface = createAnimationSurface,
        onTouchEvent = onTouchEvent,
        onViewSizeChanged = vm::onViewSizeChanged,
        controlPanelExpanded = vm.controlPanelExpanded,
        selectedTabIndex = vm.selectedTabIndex,
        selectedPreset = vm.selectedPreset,
        editForceStrengthsPanelExpanded = vm.editForceStrengthsPanelExpanded,
        editForceStrengthsSelectedSpeciesIndex = vm.editForceStrengthsSelectedSpeciesIndex,
        editForceDistancesPanelExpanded = vm.editForceDistancesPanelExpanded,
        editForceDistancesSelectedSpeciesIndex = vm.editForceDistancesSelectedSpeciesIndex,
        editHandOfGodPanelExpanded = vm.editHandOfGodPanelExpanded,
        runtimeParameters = derivedStateOf { vm.parameters.value.runtime.copy() },
        generationParameters = derivedStateOf { vm.parameters.value.generation.copy() },
        species = derivedStateOf { vm.parameters.value.species },
        runtimeParametersChanged = vm::changeRuntimeParameters,
        generationParametersChanged = vm::changeGenerationParameters,
        generateNewParticlesClicked = vm::generateNewParticles,
        selectedWallpaperPhysics = vm.selectedWallpaperPhysics,
        setWallpaperClicked = { vm.handleSetWallpaper(setWallpaper) },
        wallpaperParameters = derivedStateOf { vm.wallpaperParameters.value.copy() },
        wallpaperParametersChanged = vm::changeWallpaperParameters,
        wallpaperMode = vm.wallpaperMode,
        changeWallpaperMode = vm::changeWallpaperMode,
        wallpaperShuffleForceValues = vm.wallpaperShuffleForceValues,
        changeWallpaperForceValues = vm::changeWallpaperShuffleForceValues,
        saveCurrentSettingsForWallpaper = { vm.saveCurrentSettingsToWallpaper(showToast = true) },
        loadCurrentSettingsFromWallpaper = vm::loadCurrentSettingsFromWallpaper
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParticleLifeUi(
    createAnimationSurface: () -> SurfaceView,
    onTouchEvent: (MotionEvent?) -> Boolean,
    onViewSizeChanged: (FloatVector2, Int) -> Unit,
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceStrengthsSelectedSpeciesIndex: MutableState<Int>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    editForceDistancesSelectedSpeciesIndex: MutableState<Int>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    species: State<List<Species>>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit,
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
    ParticleLifeTheme {
        Scaffold {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                with(LocalDensity.current) {
                    val rotation = LocalView.current.display.rotation
                    AndroidView(modifier = Modifier
                        .size(width = maxWidth, height = maxHeight)
                        .pointerInteropFilter { onTouchEvent(it) }
                        .motionEventSpy {
                            if (it.action == MotionEvent.ACTION_DOWN) {
                                editForceStrengthsPanelExpanded.value = false
                                editForceDistancesPanelExpanded.value = false
                                editHandOfGodPanelExpanded.value = HandOfGodPanelMode.OFF
                                controlPanelExpanded.value = false
                            }
                        },
                        factory = {
                            onViewSizeChanged(
                                FloatVector2(maxWidth.toPx(), maxHeight.toPx()), rotation
                            )
                            createAnimationSurface()
                        }, update = {
                            onViewSizeChanged(
                                FloatVector2(maxWidth.toPx(), maxHeight.toPx()), rotation
                            )
                        })
                }
                ControlPanelUi(
                    controlPanelExpanded,
                    selectedTabIndex,
                    selectedPreset,
                    editForceStrengthsPanelExpanded,
                    editForceStrengthsSelectedSpeciesIndex,
                    editForceDistancesPanelExpanded,
                    editForceDistancesSelectedSpeciesIndex,
                    editHandOfGodPanelExpanded,
                    runtimeParameters,
                    generationParameters,
                    species,
                    runtimeParametersChanged,
                    generationParametersChanged,
                    generateNewParticlesClicked,
                    selectedWallpaperPhysics,
                    setWallpaperClicked,
                    wallpaperParameters,
                    wallpaperParametersChanged,
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ControlPanelUi(
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceStrengthsSelectedSpeciesIndex: MutableState<Int>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    editForceDistancesSelectedSpeciesIndex: MutableState<Int>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    species: State<List<Species>>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit,
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
    val foregroundCardModifier = if (isPortrait()) {
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    } else {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    }.zIndex(-1f)

    Box(modifier = Modifier.padding(40.dp)) {
        AnimatedVisibility(visible = controlPanelExpanded.value) {
            Card(modifier = foregroundCardModifier) {
                ControlPanelCardContent(
                    controlPanelExpanded,
                    editForceStrengthsPanelExpanded,
                    editForceDistancesPanelExpanded,
                    editHandOfGodPanelExpanded,
                    selectedTabIndex,
                    selectedPreset,
                    runtimeParameters,
                    generationParameters,
                    runtimeParametersChanged,
                    generationParametersChanged,
                    generateNewParticlesClicked,
                    selectedWallpaperPhysics,
                    setWallpaperClicked,
                    wallpaperParameters,
                    wallpaperParametersChanged,
                    wallpaperMode,
                    changeWallpaperMode,
                    wallpaperShuffleForceValues,
                    changeWallpaperForceValues,
                    saveCurrentSettingsForWallpaper,
                    loadCurrentSettingsFromWallpaper
                )
            }
        }
        AnimatedVisibility(visible = editForceStrengthsPanelExpanded.value) {
            Card(modifier = foregroundCardModifier) {
                EditForceStrengthsPanelCardContent(
                    runtimeParameters,
                    runtimeParametersChanged,
                    species,
                    editForceStrengthsSelectedSpeciesIndex
                )
            }
        }
        AnimatedVisibility(visible = editForceDistancesPanelExpanded.value) {
            Card(modifier = foregroundCardModifier) {
                EditForceDistancesPanelCardContent(
                    runtimeParameters,
                    runtimeParametersChanged,
                    species,
                    editForceDistancesSelectedSpeciesIndex
                )
            }
        }
        AnimatedVisibility(visible = editHandOfGodPanelExpanded.value != HandOfGodPanelMode.OFF) {
            Card(modifier = foregroundCardModifier) {
                if (editHandOfGodPanelExpanded.value == HandOfGodPanelMode.WALLPAPER) {
                    EditHandOfGodPanelCardContent(
                        derivedStateOf { wallpaperParameters.value.runtime.copy() }
                    ) { block -> wallpaperParametersChanged { runtime.block() } }
                } else {
                    EditHandOfGodPanelCardContent(
                        runtimeParameters, runtimeParametersChanged
                    )
                }
            }
        }
        FloatingActionButton(onClick = {
            when {
                editForceStrengthsPanelExpanded.value ||
                        editForceDistancesPanelExpanded.value ||
                        editHandOfGodPanelExpanded.value != HandOfGodPanelMode.OFF -> {
                    editForceStrengthsPanelExpanded.value = false
                    editForceDistancesPanelExpanded.value = false
                    editHandOfGodPanelExpanded.value = HandOfGodPanelMode.OFF
                    controlPanelExpanded.value = true
                }
                else -> {
                    controlPanelExpanded.value = !controlPanelExpanded.value
                }
            }
        }) {
            val anyPanelExpanded = listOf(
                controlPanelExpanded.value,
                editForceStrengthsPanelExpanded.value,
                editForceDistancesPanelExpanded.value,
                editHandOfGodPanelExpanded.value != HandOfGodPanelMode.OFF
            ).any { it }
            Icon(
                imageVector = if (anyPanelExpanded) {
                    Icons.Rounded.ArrowBack
                } else {
                    Icons.Rounded.Tune
                },
                contentDescription = if (anyPanelExpanded) {
                    stringResource(R.string.back_icon_content_description)
                } else {
                    stringResource(R.string.open_settings_icon_content_description)
                }
            )
        }
    }
}

@Composable
fun ControlPanelCardContent(
    controlPanelExpanded: MutableState<Boolean>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
    selectedTabIndex: MutableState<Int>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit,
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
    Column {
        ControlPanelTabs(selectedTabIndex)
        when (ControlPanelTab.values()[selectedTabIndex.value]) {
            ControlPanelTab.PHYSICS -> PhysicsContent(
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                selectedPreset,
                runtimeParameters,
                runtimeParametersChanged
            )
            ControlPanelTab.PARTICLES -> ParticlesContent(
                controlPanelExpanded,
                editForceStrengthsPanelExpanded,
                editForceDistancesPanelExpanded,
                generationParameters,
                generationParametersChanged,
                generateNewParticlesClicked
            )
            ControlPanelTab.WALLPAPER -> WallpaperContent(
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                selectedWallpaperPhysics,
                setWallpaperClicked,
                wallpaperParameters,
                wallpaperParametersChanged,
                wallpaperMode,
                changeWallpaperMode,
                wallpaperShuffleForceValues,
                changeWallpaperForceValues,
                saveCurrentSettingsForWallpaper,
                loadCurrentSettingsFromWallpaper
            )
            ControlPanelTab.ABOUT -> AboutContent()
        }
    }
}

@Composable
private fun ControlPanelTabs(selectedTabIndex: MutableState<Int>) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.secondary,
        modifier = Modifier.height(fabDiameter)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex.value,
            backgroundColor = MaterialTheme.colors.secondary,
            modifier = Modifier
                .height(fabDiameter)
                .padding(start = fabDiameter / 2)
        ) {
            ControlPanelTab.values().forEach {
                ControlPanelTab(it, selectedTabIndex)
            }
        }
    }
}

@Composable
private fun ControlPanelTab(
    tab: ControlPanelTab,
    selectedTabIndex: MutableState<Int>
) {
    Tab(
        selected = ControlPanelTab.values().indexOf(tab) == selectedTabIndex.value,
        selectedContentColor = Color.White,
        unselectedContentColor = MaterialTheme.colors.primary,
        onClick = { selectedTabIndex.value = ControlPanelTab.values().indexOf(tab) },
        text = {
            Text(
                text = stringResource(tab.toTabNameString()),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        })
}