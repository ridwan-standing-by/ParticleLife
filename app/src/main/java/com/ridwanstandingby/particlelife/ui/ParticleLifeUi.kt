package com.ridwanstandingby.particlelife.ui

import android.view.MotionEvent
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.Species
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.particlelife.ui.theme.icons.Icons
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.Tune
import com.ridwanstandingby.verve.animation.AnimationView
import com.ridwanstandingby.verve.math.FloatVector2

@Composable
fun ParticleLifeActivityUi(
    createAnimationView: () -> AnimationView,
    onTouchEvent: (MotionEvent?) -> Boolean,
    vm: ParticleLifeViewModel
) {
    ParticleLifeUi(
        createAnimationView = createAnimationView,
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
        generateNewParticlesClicked = vm::generateNewParticles
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParticleLifeUi(
    createAnimationView: () -> AnimationView,
    onTouchEvent: (MotionEvent?) -> Boolean,
    onViewSizeChanged: (FloatVector2, Int) -> Unit,
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceStrengthsSelectedSpeciesIndex: MutableState<Int>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    editForceDistancesSelectedSpeciesIndex: MutableState<Int>,
    editHandOfGodPanelExpanded: MutableState<Boolean>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    species: State<List<Species>>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
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
                        .pointerInteropFilter { onTouchEvent(it) },
                        factory = {
                            onViewSizeChanged(
                                FloatVector2(maxWidth.toPx(), maxHeight.toPx()), rotation
                            )
                            createAnimationView()
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
                    generateNewParticlesClicked
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
    editHandOfGodPanelExpanded: MutableState<Boolean>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    species: State<List<Species>>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
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
                    generateNewParticlesClicked
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
        AnimatedVisibility(visible = editHandOfGodPanelExpanded.value) {
            Card(modifier = foregroundCardModifier) {
                EditHandOfGodPanelCardContent(
                    runtimeParameters,
                    runtimeParametersChanged
                )
            }
        }
        FloatingActionButton(onClick = {
            when {
                editForceStrengthsPanelExpanded.value || editForceDistancesPanelExpanded.value || editHandOfGodPanelExpanded.value -> {
                    editForceStrengthsPanelExpanded.value = false
                    editForceDistancesPanelExpanded.value = false
                    editHandOfGodPanelExpanded.value = false
                    controlPanelExpanded.value = true
                }
                else -> {
                    controlPanelExpanded.value = !controlPanelExpanded.value
                }
            }
        }) {
            Icon(imageVector = Icons.Rounded.Tune, null)
        }
    }
}

@Composable
fun ControlPanelCardContent(
    controlPanelExpanded: MutableState<Boolean>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
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
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            backgroundColor = MaterialTheme.colors.secondary,
            modifier = Modifier
                .height(fabDiameter)
                .padding(start = fabDiameter)
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

