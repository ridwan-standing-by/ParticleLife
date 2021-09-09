package com.ridwanstandingby.particlelife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.verve.animation.AnimationView
import com.ridwanstandingby.verve.math.FloatVector2

@Composable
fun ParticleLifeActivityUi(
    createAnimationView: () -> AnimationView,
    vm: ParticleLifeViewModel
) {
    // TODO hoist to view model for orientation changes
    val controlPanelExpanded = remember { mutableStateOf(false) }
    val selectedTabIndex = remember { mutableStateOf(0) }
    val runtimeParameters =
        remember { mutableStateOf(vm.parameters.runtime, policy = neverEqualPolicy()) }
    val generationParameters =
        remember { mutableStateOf(vm.parameters.generation, policy = neverEqualPolicy()) }
    ParticleLifeUi(
        createAnimationView = createAnimationView,
        onViewSizeChanged = vm::onViewSizeChanged,
        controlPanelExpanded = controlPanelExpanded,
        selectedTabIndex = selectedTabIndex,
        runtimeParameters = runtimeParameters,
        generationParameters = generationParameters
    )
}

@Composable
fun ParticleLifeUi(
    createAnimationView: () -> AnimationView,
    onViewSizeChanged: (FloatVector2, Int) -> Unit,
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: MutableState<ParticleLifeParameters.GenerationParameters>
) {
    ParticleLifeTheme {
        Scaffold {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                with(LocalDensity.current) {
                    val rotation = LocalView.current.display.rotation
                    AndroidView(modifier = Modifier.size(
                        width = maxWidth,
                        height = maxHeight,
                    ), factory = {
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
                    controlPanelExpanded = controlPanelExpanded,
                    selectedTabIndex = selectedTabIndex,
                    runtimeParameters = runtimeParameters,
                    generationParameters = generationParameters
                )
            }
        }
    }
}

val fabDiameter = 56.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ControlPanelUi(
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: MutableState<ParticleLifeParameters.GenerationParameters>
) {
    val controlPanelCardModifier = if (isPortrait()) {
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
            Card(modifier = controlPanelCardModifier) {
                ControlPanelCardContent(selectedTabIndex, runtimeParameters, generationParameters)
            }
        }
        FloatingActionButton(onClick = {
            controlPanelExpanded.value = !controlPanelExpanded.value
        }) {
            Icon(imageVector = Icons.Rounded.Tune, null)
        }
    }
}

@Composable
fun ControlPanelCardContent(
    selectedTabIndex: MutableState<Int>,
    runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: MutableState<ParticleLifeParameters.GenerationParameters>
) {
    Column {
        ControlPanelTabs(selectedTabIndex)
        when (ControlPanelTab.values()[selectedTabIndex.value]) {
            ControlPanelTab.PHYSICS -> PhysicsContent(runtimeParameters)
            ControlPanelTab.SPECIES -> SpeciesContent(generationParameters)
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
                text = tab.toTabNameString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        })
}

@Composable
fun PhysicsContent(runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Change global simulation parameters",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        if (isPortrait()) {
            FrictionWidget(runtimeParameters)
            ForceStrengthWidget(runtimeParameters)
            ForceRangeWidget(runtimeParameters)
            TimeStepWidget(runtimeParameters)
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    FrictionWidget(runtimeParameters)
                    ForceStrengthWidget(runtimeParameters)
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
                    ForceRangeWidget(runtimeParameters)
                    TimeStepWidget(runtimeParameters)
                }
            }
        }
    }
}

@Composable
private fun FrictionWidget(runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>) {
    TextSliderPair(
        text = "Friction",
        description = "The amount by which particles are slowed over time.",
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.friction.toFloat(),
        range = 0f..5f,
        onValueChange = {
            runtimeParameters.value =
                runtimeParameters.value.apply { friction = it.toDouble() }
        }
    )
}

@Composable
private fun ForceStrengthWidget(runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>) {
    TextSliderPair(
        text = "Force strength",
        description = "Global multiplier for all repulsive and attractive forces.",
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.forceScale.toFloat(),
        range = 1f..500f,
        onValueChange = {
            runtimeParameters.value =
                runtimeParameters.value.apply { forceScale = it.toDouble() }
        }
    )
}

@Composable
private fun ForceRangeWidget(runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>) {
    TextSliderPair(
        text = "Force range",
        description = "Global multiplier for the range at which the repulsive and attractive forces act.",
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.newtonMax.toFloat(),
        range = 20f..200f,
        onValueChange = {
            runtimeParameters.value =
                runtimeParameters.value.apply { newtonMax = it.toDouble() }
        }
    )
}

@Composable
private fun TimeStepWidget(runtimeParameters: MutableState<ParticleLifeParameters.RuntimeParameters>) {
    TextSliderPair(
        text = "Time step",
        description = "Simulation time step multiplier. Lower values lead to higher fidelity but slower simulation, higher values lead to faster simulation with more instability.",
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.timeScale.toFloat(),
        range = 0.1f..5f,
        onValueChange = {
            runtimeParameters.value =
                runtimeParameters.value.apply { timeScale = it.toDouble() }
        }
    )
}

@Composable
fun TextSliderPair(
    text: String,
    description: String,
    valueToString: (Float) -> String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
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
                modifier = Modifier
                    .weight(0.425f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(text = description, fontSize = MaterialTheme.typography.caption.fontSize)
    }
}

@Composable
fun SpeciesContent(generationParameters: MutableState<ParticleLifeParameters.GenerationParameters>) {
    TODO("Not yet implemented")
}

@Composable
fun AboutContent() {
    TODO("Not yet implemented")
}
