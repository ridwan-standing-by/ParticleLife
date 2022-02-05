package com.ridwanstandingby.particlelife.ui

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.Species
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.verve.animation.AnimationView
import com.ridwanstandingby.verve.math.FloatVector2
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.roundToInt

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
fun ControlPanelTab(
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

@Composable
fun PhysicsContent(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<Boolean>,
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.physics_info),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        if (isPortrait()) {
            PresetSelectionAndRandomiseButton(selectedPreset, runtimeParametersChanged)
            FrictionWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            ForceStrengthWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            ForceRangeWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            PressureWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            TimeStepWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            HandOfGodEnabledSwitchWidget(
                controlPanelExpanded,
                editHandOfGodPanelExpanded,
                runtimeParameters,
                runtimeParametersChanged
            )
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    PresetSelectionAndRandomiseButton(selectedPreset, runtimeParametersChanged)
                    FrictionWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
                    ForceStrengthWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
                    ForceRangeWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
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
                    PressureWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
                    TimeStepWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
                    HandOfGodEnabledSwitchWidget(
                        controlPanelExpanded,
                        editHandOfGodPanelExpanded,
                        runtimeParameters,
                        runtimeParametersChanged
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetSelectionAndRandomiseButton(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            Modifier
                .weight(0.80f, fill = true)
        ) {
            PresetSelectionWidget(selectedPreset, runtimeParametersChanged)
        }
        Box(
            Modifier
                .weight(0.20f, fill = true)
                .fillMaxHeight()
        ) {
            RandomiseButton(selectedPreset, runtimeParametersChanged)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.PresetSelectionWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {

    val presets = ParticleLifeParameters.RuntimeParameters.Preset.ALL
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
            .fillMaxWidth(0.9f)
            .align(Center)
    ) {
        TextField(
            readOnly = true,
            value = stringResource(selectedPreset.value.nameString()),
            onValueChange = { },
            enabled = true,
            label = { Text(stringResource(R.string.preset_label)) },
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

            presets.forEach { preset ->
                DropdownMenuItem(
                    onClick = {
                        selectedPreset.value = preset
                        runtimeParametersChanged { preset.applyPreset(this) }
                        expanded = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text(stringResource(preset.nameString()))
                }
            }
        }
    }
}

@Composable
private fun BoxScope.RandomiseButton(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Button(
        onClick = {
            runtimeParametersChanged { randomise() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        },
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.95f)
            .fillMaxHeight()
            .align(Center)
    ) {
        Icon(imageVector = Icons.Rounded.Casino, stringResource(R.string.randomise_label))
    }
}

@Composable
private fun FrictionWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.friction_label),
        description = stringResource(R.string.friction_description),
        valueToString = { """${(it * 100f).decimal(1)}%""" },
        value = runtimeParameters.value.friction.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FRICTION_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FRICTION_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { friction = it.toDouble() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
private fun ForceStrengthWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.force_strength_label),
        description = stringResource(R.string.force_strength_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.forceStrengthScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FORCE_STRENGTH_SCALE_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FORCE_STRENGTH_SCALE_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { forceStrengthScale = it.toDouble() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
private fun ForceRangeWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.force_range_label),
        description = stringResource(R.string.force_range_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.forceDistanceScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FORCE_DISTANCE_SCALE_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FORCE_DISTANCE_SCALE_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { forceDistanceScale = it.toDouble() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
private fun PressureWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.pressure_label),
        description = stringResource(R.string.pressure_description),
        valueToString = { it.roundToInt().toString() },
        value = runtimeParameters.value.pressureStrength.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.PRESSURE_STRENGTH_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.PRESSURE_STRENGTH_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { pressureStrength = it.toDouble() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
private fun TimeStepWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.time_step_label),
        description = stringResource(R.string.time_step_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.timeScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.TIME_STEP_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.TIME_STEP_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { timeScale = it.toDouble() }
            selectedPreset.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
private fun HandOfGodEnabledSwitchWidget(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<Boolean>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.enable_hand_of_god_label),
                modifier = Modifier
                    .weight(0.425f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = runtimeParameters.value.handOfGodEnabled,
                onCheckedChange = { runtimeParametersChanged { handOfGodEnabled = it } },
                modifier = Modifier
                    .weight(0.275f)
                    .align(Alignment.CenterVertically)
            )
            Button(
                onClick = {
                    controlPanelExpanded.value = false
                    editHandOfGodPanelExpanded.value = true
                },
                enabled = runtimeParameters.value.handOfGodEnabled,
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Tune,
                    contentDescription = stringResource(R.string.hand_of_god_settings_button_content_description)
                )
            }
        }
        Text(
            text = stringResource(R.string.enable_hand_of_god_description),
            fontSize = MaterialTheme.typography.caption.fontSize
        )
    }
}

@Composable
private fun EditHandOfGodPanelCardContent(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Column {
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .height(fabDiameter)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hand_of_god_settings_title),
                style = MaterialTheme.typography.h5,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.hand_of_god_settings_description),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
            if (isPortrait()) {
                SwipeToHerdEnabledSwitchWidget(runtimeParameters, runtimeParametersChanged)
                AnimatedVisibility(visible = runtimeParameters.value.herdEnabled) {
                    Column {
                        SwipeToHerdStrengthSliderWidget(runtimeParameters, runtimeParametersChanged)
                        SwipeToHerdRangeSliderWidget(runtimeParameters, runtimeParametersChanged)
                    }
                }
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .width(1.dp)
                        .padding(top = 12.dp)
                )
                PressToBeckonEnabledSwitchWidget(runtimeParameters, runtimeParametersChanged)
                AnimatedVisibility(visible = runtimeParameters.value.beckonEnabled) {
                    Column {
                        PressToBeckonStrengthSliderWidget(
                            runtimeParameters,
                            runtimeParametersChanged
                        )
                        PressToBeckonRangeSliderWidget(runtimeParameters, runtimeParametersChanged)
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .weight(0.5f)
                            .padding(end = 12.dp)
                    ) {
                        SwipeToHerdEnabledSwitchWidget(runtimeParameters, runtimeParametersChanged)
                        AnimatedVisibility(visible = runtimeParameters.value.herdEnabled) {
                            Column {
                                SwipeToHerdStrengthSliderWidget(
                                    runtimeParameters,
                                    runtimeParametersChanged
                                )
                                SwipeToHerdRangeSliderWidget(
                                    runtimeParameters,
                                    runtimeParametersChanged
                                )
                            }
                        }
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
                        PressToBeckonEnabledSwitchWidget(
                            runtimeParameters,
                            runtimeParametersChanged
                        )
                        AnimatedVisibility(visible = runtimeParameters.value.beckonEnabled) {
                            Column {
                                PressToBeckonStrengthSliderWidget(
                                    runtimeParameters,
                                    runtimeParametersChanged
                                )
                                PressToBeckonRangeSliderWidget(
                                    runtimeParameters,
                                    runtimeParametersChanged
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SwipeToHerdEnabledSwitchWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSwitchPair(
        text = stringResource(R.string.swipe_to_herd_label),
        description = stringResource(R.string.swipe_to_herd_description),
        checked = runtimeParameters.value.herdEnabled,
        onToggle = { runtimeParametersChanged { herdEnabled = it } }
    )
}

@Composable
private fun SwipeToHerdStrengthSliderWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.swipe_to_herd_strength_label),
        description = stringResource(R.string.swipe_to_herd_strength_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.herdStrength.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.HERD_STRENGTH_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.HERD_STRENGTH_MAX.toFloat(),
        onValueChange = { runtimeParametersChanged { herdStrength = it.toDouble() } }
    )
}

@Composable
private fun SwipeToHerdRangeSliderWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.swipe_to_herd_range_label),
        description = stringResource(R.string.swipe_to_herd_range_description),
        valueToString = { it.roundToInt().toString() },
        value = runtimeParameters.value.herdRadius.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.HERD_RADIUS_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.HERD_RADIUS_MAX.toFloat(),
        onValueChange = { runtimeParametersChanged { herdRadius = it.toDouble() } }
    )
}

@Composable
private fun PressToBeckonEnabledSwitchWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSwitchPair(
        text = stringResource(R.string.press_to_beckon_label),
        description = stringResource(R.string.press_to_beckon_description),
        checked = runtimeParameters.value.beckonEnabled,
        onToggle = { runtimeParametersChanged { beckonEnabled = it } }
    )
}

@Composable
private fun PressToBeckonStrengthSliderWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.press_to_beckon_strength_label),
        description = stringResource(R.string.press_to_beckon_strength_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.beckonStrength.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.BECKON_STRENGTH_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.BECKON_STRENGTH_MAX.toFloat(),
        onValueChange = { runtimeParametersChanged { beckonStrength = it.toDouble() } }
    )
}

@Composable
private fun PressToBeckonRangeSliderWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.press_to_beckon_range_label),
        description = stringResource(R.string.press_to_beckon_range_description),
        valueToString = { it.roundToInt().toString() },
        value = runtimeParameters.value.beckonRadius.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.BECKON_RADIUS_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.BECKON_RADIUS_MAX.toFloat(),
        onValueChange = { runtimeParametersChanged { beckonRadius = it.toDouble() } }
    )
}

@Composable
fun ParticlesContent(
    controlPanelExpanded: MutableState<Boolean>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>,
    editForceDistancesPanelExpanded: MutableState<Boolean>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.particles_info),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )

        if (isPortrait()) {
            GenerateNewParticlesButton(generateNewParticlesClicked)
            NumberOfParticlesWidget(generationParameters, generationParametersChanged)
            NumberOfSpeciesWidget(generationParameters, generationParametersChanged)
            ForceStrengthsRangeWidget(generationParameters, generationParametersChanged)
            EditForceStrengthsButton(controlPanelExpanded, editForceStrengthsPanelExpanded)
            ForceDistancesRangeWidget(generationParameters, generationParametersChanged)
            EditForceDistancesButton(controlPanelExpanded, editForceDistancesPanelExpanded)
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    GenerateNewParticlesButton(generateNewParticlesClicked)
                    NumberOfParticlesWidget(generationParameters, generationParametersChanged)
                    NumberOfSpeciesWidget(generationParameters, generationParametersChanged)
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
                    ForceStrengthsRangeWidget(generationParameters, generationParametersChanged)
                    EditForceStrengthsButton(controlPanelExpanded, editForceStrengthsPanelExpanded)
                    ForceDistancesRangeWidget(generationParameters, generationParametersChanged)
                    EditForceDistancesButton(controlPanelExpanded, editForceDistancesPanelExpanded)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.GenerateNewParticlesButton(generateNewParticlesClicked: () -> Unit) {
    Button(
        onClick = generateNewParticlesClicked,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.generate_new_particles_label))
    }
}

@Composable
private fun NumberOfParticlesWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.number_of_particles_label),
        description = stringResource(R.string.number_of_particles_description),
        valueToString = { it.roundToInt().toString() },
        value = generationParameters.value.nParticles.toFloat(),
        range = ParticleLifeParameters.GenerationParameters.N_PARTICLES_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.N_PARTICLES_MAX.toFloat(),
        onValueChange = {
            generationParametersChanged { nParticles = it.roundToInt() }
        }
    )
}

@Composable
private fun NumberOfSpeciesWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.number_of_species_label),
        description = stringResource(R.string.number_of_species_description),
        valueToString = { it.roundToInt().toString() },
        value = generationParameters.value.nSpecies.toFloat(),
        range = ParticleLifeParameters.GenerationParameters.N_SPECIES_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX.toFloat(),
        onValueChange = {
            generationParametersChanged { nSpecies = it.roundToInt() }
        }
    )
}

@Composable
private fun ForceStrengthsRangeWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextRangePair(
        text = stringResource(R.string.force_strengths_range_label),
        description = stringResource(R.string.force_strengths_range_description),
        valueToString = { it.decimal(1) },
        values = Pair(
            generationParameters.value.maxRepulsion.toFloat(),
            generationParameters.value.maxAttraction.toFloat()
        ),
        range = ParticleLifeParameters.GenerationParameters.FORCE_STRENGTH_RANGE_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.FORCE_STRENGTH_RANGE_MAX.toFloat(),
        onValueChange = {
            generationParametersChanged {
                maxRepulsion = it.first.toDouble()
                maxAttraction = it.second.toDouble()
            }
        }
    )
}

@Composable
private fun ColumnScope.EditForceStrengthsButton(
    controlPanelExpanded: MutableState<Boolean>,
    editForceStrengthsPanelExpanded: MutableState<Boolean>
) {
    Button(
        onClick = {
            controlPanelExpanded.value = false
            editForceStrengthsPanelExpanded.value = true
        },
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.edit_force_strengths_label))
    }
}

@Composable
private fun EditForceStrengthsPanelCardContent(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    species: State<List<Species>>,
    editForceStrengthsSelectedSpeciesIndex: MutableState<Int>
) {
    Column {
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .height(fabDiameter)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.edit_force_strengths_label),
                style = MaterialTheme.typography.h5,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.edit_force_strengths_description),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
            if (isPortrait()) {
                species.value.forEachIndexed { index, _ ->
                    EditSpeciesForceStrengthSlider(
                        thisSpeciesIndex = index,
                        selectedSpeciesIndex = editForceStrengthsSelectedSpeciesIndex,
                        allSpecies = species.value,
                        runtimeParameters = runtimeParameters,
                        runtimeParametersChanged = runtimeParametersChanged
                    )
                }
            } else {
                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .weight(0.5f)
                            .padding(end = 12.dp)
                    ) {
                        species.value.chunked(ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2)
                            .getOrNull(0)
                            ?.forEachIndexed { index, _ ->
                                EditSpeciesForceStrengthSlider(
                                    thisSpeciesIndex = index,
                                    selectedSpeciesIndex = editForceStrengthsSelectedSpeciesIndex,
                                    allSpecies = species.value,
                                    runtimeParameters = runtimeParameters,
                                    runtimeParametersChanged = runtimeParametersChanged
                                )
                            }
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
                        species.value.chunked(ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2)
                            .getOrNull(1)
                            ?.forEachIndexed { index, _ ->
                                EditSpeciesForceStrengthSlider(
                                    thisSpeciesIndex = index + ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2,
                                    selectedSpeciesIndex = editForceStrengthsSelectedSpeciesIndex,
                                    allSpecies = species.value,
                                    runtimeParameters = runtimeParameters,
                                    runtimeParametersChanged = runtimeParametersChanged
                                )
                            }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditSpeciesForceStrengthSlider(
    thisSpeciesIndex: Int,
    selectedSpeciesIndex: MutableState<Int>,
    allSpecies: List<Species>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 8.dp)
    ) {
        val value =
            runtimeParameters.value.forceStrengths[selectedSpeciesIndex.value][thisSpeciesIndex].toFloat()
        val isSelected = thisSpeciesIndex == selectedSpeciesIndex.value
        Box(
            modifier = Modifier
                .weight(0.2125f)
                .padding(vertical = 2.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex },
                modifier = Modifier
                    .run {
                        if (isSelected) background(
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(50)
                        ) else Modifier
                    }
                    .align(Center)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = stringResource(R.string.select_species_icon_content_description)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.2125f)
                .padding(vertical = 2.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex },
                modifier = Modifier
                    .align(Center)
                    .fillMaxHeight()
                    .background(
                        color = Color(allSpecies[thisSpeciesIndex].color),
                        shape = RoundedCornerShape(50)
                    )
            ) {
            }
        }
        Text(
            text = value.decimal(2), textAlign = TextAlign.End, modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically)
        )
        Slider(
            value = value,
            onValueChange = {
                runtimeParametersChanged {
                    forceStrengths[selectedSpeciesIndex.value][thisSpeciesIndex] = it.toDouble()
                }
            },
            valueRange = ParticleLifeParameters.GenerationParameters.FORCE_STRENGTH_RANGE_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.FORCE_STRENGTH_RANGE_MAX.toFloat(),
            steps = 0,
            modifier = Modifier
                .weight(0.425f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun ForceDistancesRangeWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextRangePair(
        text = stringResource(R.string.force_distances_range_label),
        description = stringResource(R.string.force_distances_range_description),
        valueToString = { it.roundToInt().toString() },
        values = Pair(
            generationParameters.value.forceDistanceLowerBoundMin.toFloat(),
            generationParameters.value.forceDistanceUpperBoundMax.toFloat()
        ),
        range = ParticleLifeParameters.GenerationParameters.FORCE_DISTANCE_RANGE_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.FORCE_DISTANCE_RANGE_MAX.toFloat(),
        onValueChange = {
            generationParametersChanged {
                forceDistanceLowerBoundMin = it.first.toDouble()
                forceDistanceUpperBoundMax = it.second.toDouble()
            }
        }
    )
}

@Composable
private fun ColumnScope.EditForceDistancesButton(
    controlPanelExpanded: MutableState<Boolean>,
    editForceDistancesPanelExpanded: MutableState<Boolean>
) {
    Button(
        onClick = {
            controlPanelExpanded.value = false
            editForceDistancesPanelExpanded.value = true
        },
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth(0.95f)
            .align(CenterHorizontally)
    ) {
        Text(stringResource(R.string.edit_force_distances_label))
    }
}

@Composable
private fun EditForceDistancesPanelCardContent(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    species: State<List<Species>>,
    editForceDistancesSelectedSpeciesIndex: MutableState<Int>
) {
    Column {
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .height(fabDiameter)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.edit_force_distances_label),
                style = MaterialTheme.typography.h5,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.edit_force_distances_description),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
            if (isPortrait()) {
                species.value.forEachIndexed { index, _ ->
                    EditSpeciesForceDistanceSlider(
                        thisSpeciesIndex = index,
                        selectedSpeciesIndex = editForceDistancesSelectedSpeciesIndex,
                        allSpecies = species.value,
                        runtimeParameters = runtimeParameters,
                        runtimeParametersChanged = runtimeParametersChanged
                    )
                }
            } else {
                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .weight(0.5f)
                            .padding(end = 12.dp)
                    ) {
                        species.value.chunked(ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2)
                            .getOrNull(0)
                            ?.forEachIndexed { index, _ ->
                                EditSpeciesForceDistanceSlider(
                                    thisSpeciesIndex = index,
                                    selectedSpeciesIndex = editForceDistancesSelectedSpeciesIndex,
                                    allSpecies = species.value,
                                    runtimeParameters = runtimeParameters,
                                    runtimeParametersChanged = runtimeParametersChanged
                                )
                            }
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
                        species.value.chunked(ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2)
                            .getOrNull(1)
                            ?.forEachIndexed { index, _ ->
                                EditSpeciesForceDistanceSlider(
                                    thisSpeciesIndex = index + ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX / 2,
                                    selectedSpeciesIndex = editForceDistancesSelectedSpeciesIndex,
                                    allSpecies = species.value,
                                    runtimeParameters = runtimeParameters,
                                    runtimeParametersChanged = runtimeParametersChanged
                                )
                            }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditSpeciesForceDistanceSlider(
    thisSpeciesIndex: Int,
    selectedSpeciesIndex: MutableState<Int>,
    allSpecies: List<Species>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 8.dp)
    ) {
        val lowerValue =
            runtimeParameters.value.forceDistanceLowerBounds[selectedSpeciesIndex.value][thisSpeciesIndex].toFloat()
        val upperValue =
            runtimeParameters.value.forceDistanceUpperBounds[selectedSpeciesIndex.value][thisSpeciesIndex].toFloat()
        val isSelected = thisSpeciesIndex == selectedSpeciesIndex.value
        Box(
            modifier = Modifier
                .weight(0.2125f)
                .padding(vertical = 2.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex },
                modifier = Modifier
                    .run {
                        if (isSelected) background(
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(50)
                        ) else Modifier
                    }
                    .align(Center)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = stringResource(R.string.select_species_icon_content_description)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.2125f)
                .padding(vertical = 2.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex },
                modifier = Modifier
                    .align(Center)
                    .fillMaxHeight()
                    .background(
                        color = Color(allSpecies[thisSpeciesIndex].color),
                        shape = RoundedCornerShape(50)
                    )
            ) {
            }
        }

        Text(
            text = lowerValue.roundToInt().toString(),
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically)
        )
        Box(
            modifier = Modifier
                .weight(0.375f)
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            RangeSlider(
                values = lowerValue..upperValue,
                onValueChange = {
                    runtimeParametersChanged {
                        forceDistanceLowerBounds[selectedSpeciesIndex.value][thisSpeciesIndex] =
                            it.start.toDouble()
                        forceDistanceUpperBounds[selectedSpeciesIndex.value][thisSpeciesIndex] =
                            it.endInclusive.toDouble()
                    }
                },
                valueRange = ParticleLifeParameters.GenerationParameters.FORCE_DISTANCE_RANGE_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.FORCE_DISTANCE_RANGE_MAX.toFloat(),
                steps = 0
            )
        }
        Text(
            text = upperValue.roundToInt().toString(),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(0.1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun LogarithmicTextSliderPair(
    text: String,
    description: String,
    valueToString: (Float) -> String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {

    TextSliderPair(
        text = text,
        description = description,
        valueToString = { valueToString(2f.pow(it)) },
        value = log2(value),
        range = log2(range.start)..log2(range.endInclusive),
        steps = steps,
        onValueChange = { onValueChange(2f.pow(it)) }
    )
}

@Composable
fun TextSliderPair(
    text: String,
    description: String,
    valueToString: (Float) -> String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(0.425f)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = valueToString(value), textAlign = TextAlign.End, modifier = Modifier
                    .weight(0.15f)
                    .align(Alignment.CenterVertically)
            )
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = range,
                steps = steps,
                modifier = Modifier
                    .weight(0.425f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(text = description, fontSize = MaterialTheme.typography.caption.fontSize)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextRangePair(
    text: String,
    description: String,
    valueToString: (Float) -> String,
    values: Pair<Float, Float>,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Pair<Float, Float>) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(0.4f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = valueToString(values.first), textAlign = TextAlign.End, modifier = Modifier
                    .weight(0.1f)
                    .align(Alignment.CenterVertically)
            )
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                RangeSlider(
                    values = values.first..values.second,
                    onValueChange = { onValueChange(Pair(it.start, it.endInclusive)) },
                    valueRange = range,
                    steps = steps
                )
            }
            Text(
                text = valueToString(values.second),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(0.1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(text = description, fontSize = MaterialTheme.typography.caption.fontSize)
    }
}

@Composable
fun TextSwitchPair(
    text: String,
    description: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(0.725f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = checked,
                onCheckedChange = { onToggle(it) },
                modifier = Modifier
                    .weight(0.275f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(text = description, fontSize = MaterialTheme.typography.caption.fontSize)
    }
}

@Composable
fun AboutContent() {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AboutText(stringResource(R.string.about_particle_simulation))
        AboutText(stringResource(R.string.about_particle_force_explanation))
        AboutText(stringResource(R.string.about_particle_extras))
        AboutText(stringResource(R.string.about_particle_final))
        AboutText(stringResource(R.string.about_particle_hand_of_god))
    }
}

@Composable
fun AboutText(text: String) {
    Text(text, modifier = Modifier.padding(vertical = 4.dp))
}
