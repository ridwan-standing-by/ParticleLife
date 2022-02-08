package com.ridwanstandingby.particlelife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import kotlin.math.roundToInt

@Composable
fun EditHandOfGodPanelCardContent(
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
            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(top = 4.dp)
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

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun HandOfGodPanelUiPreview() {
    val runtimeParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ).runtime
        )
    }
    ParticleLifeTheme {
        Scaffold {
            EditHandOfGodPanelCardContent(
                runtimeParameters = runtimeParameters,
                runtimeParametersChanged = {
                    runtimeParameters.value = runtimeParameters.value.copy().apply(it)
                }
            )
        }
    }
}