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
import com.ridwanstandingby.particlelife.ui.theme.icons.Icons
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.Casino
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.Tune
import kotlin.math.roundToInt

@Composable
fun PhysicsContent(
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
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
        Divider(
            Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(top = 4.dp)
        )
        if (isPortrait()) {
            PresetSelectionAndRandomiseButton(selectedPreset, runtimeParametersChanged)
            FrictionWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            ForceStrengthWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            ForceRangeWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            PressureWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            TimeStepWidget(selectedPreset, runtimeParameters, runtimeParametersChanged)
            HandOfGodEnabledSwitchWidget(
                HandOfGodPanelMode.PHYSICS,
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
                        HandOfGodPanelMode.PHYSICS,
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
            .padding(top = 4.dp)
    ) {
        Box(
            Modifier
                .weight(0.80f, fill = true)
        ) {
            PresetSelectionWidget(
                selectedPreset,
                runtimeParametersChanged,
                Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
            )
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
fun PresetSelectionWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    modifier: Modifier
) {
    val presets = ParticleLifeParameters.RuntimeParameters.Preset.all()
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            focusManager.clearFocus()
        },
        modifier = modifier
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
            .align(Alignment.Center)
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
fun TimeStepWidget(
    selectedPreset: MutableState<ParticleLifeParameters.RuntimeParameters.Preset>?,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    LogarithmicTextSliderPair(
        text = stringResource(R.string.time_step_label),
        description = stringResource(R.string.time_step_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.timeScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.TIME_SCALE_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.TIME_SCALE_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { timeScale = it.toDouble() }
            selectedPreset?.value = ParticleLifeParameters.RuntimeParameters.Preset.Custom
        }
    )
}

@Composable
fun HandOfGodEnabledSwitchWidget(
    mode: HandOfGodPanelMode,
    controlPanelExpanded: MutableState<Boolean>,
    editHandOfGodPanelExpanded: MutableState<HandOfGodPanelMode>,
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
                    editHandOfGodPanelExpanded.value = mode
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
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun PhysicsCardUiPreview() {
    val runtimeParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ).runtime, neverEqualPolicy()
        )
    }
    ParticleLifePreview {
        PhysicsContent(
            controlPanelExpanded = remember { mutableStateOf(true) },
            editHandOfGodPanelExpanded = remember { mutableStateOf(HandOfGodPanelMode.OFF) },
            selectedPreset = remember { mutableStateOf(ParticleLifeParameters.RuntimeParameters.Preset.default()) },
            runtimeParameters = runtimeParameters,
            runtimeParametersChanged = {
                runtimeParameters.value = runtimeParameters.value.copy().apply(it)
            }
        )
    }
}