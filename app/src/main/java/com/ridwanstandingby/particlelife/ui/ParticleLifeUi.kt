package com.ridwanstandingby.particlelife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlin.math.roundToInt

@Composable
fun ParticleLifeActivityUi(
    createAnimationView: () -> AnimationView,
    vm: ParticleLifeViewModel
) {
    // TODO hoist to view model for orientation changes
    val controlPanelExpanded = remember { mutableStateOf(false) }
    val selectedTabIndex = remember { mutableStateOf(0) }
    val runtimeParameters = derivedStateOf { vm.parameters.value.runtime.copy() }
    val generationParameters = derivedStateOf { vm.parameters.value.generation.copy() }
    ParticleLifeUi(
        createAnimationView = createAnimationView,
        onViewSizeChanged = vm::onViewSizeChanged,
        controlPanelExpanded = controlPanelExpanded,
        selectedTabIndex = selectedTabIndex,
        runtimeParameters = runtimeParameters,
        generationParameters = generationParameters,
        runtimeParametersChanged = vm::changeRuntimeParameters,
        generationParametersChanged = vm::changeGenerationParameters
    )
}

@Composable
fun ParticleLifeUi(
    createAnimationView: () -> AnimationView,
    onViewSizeChanged: (FloatVector2, Int) -> Unit,
    controlPanelExpanded: MutableState<Boolean>,
    selectedTabIndex: MutableState<Int>,
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
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
                    controlPanelExpanded,
                    selectedTabIndex,
                    runtimeParameters,
                    generationParameters,
                    runtimeParametersChanged,
                    generationParametersChanged
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
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
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
                ControlPanelCardContent(
                    selectedTabIndex,
                    runtimeParameters,
                    generationParameters,
                    runtimeParametersChanged,
                    generationParametersChanged
                )
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
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    Column {
        ControlPanelTabs(selectedTabIndex)
        when (ControlPanelTab.values()[selectedTabIndex.value]) {
            ControlPanelTab.PHYSICS -> PhysicsContent(runtimeParameters, runtimeParametersChanged)
            ControlPanelTab.SPECIES -> SpeciesContent(
                generationParameters,
                generationParametersChanged
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
                text = tab.toTabNameString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        })
}

@Composable
fun PhysicsContent(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Change global simulation parameters",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        if (isPortrait()) {
            FrictionWidget(runtimeParameters, runtimeParametersChanged)
            ForceStrengthWidget(runtimeParameters, runtimeParametersChanged)
            ForceRangeWidget(runtimeParameters, runtimeParametersChanged)
            TimeStepWidget(runtimeParameters, runtimeParametersChanged)
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    FrictionWidget(runtimeParameters, runtimeParametersChanged)
                    ForceStrengthWidget(runtimeParameters, runtimeParametersChanged)
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
                    ForceRangeWidget(runtimeParameters, runtimeParametersChanged)
                    TimeStepWidget(runtimeParameters, runtimeParametersChanged)
                }
            }
        }
    }
}

@Composable
private fun FrictionWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = "Friction",
        description = "The amount by which particles are slowed over time.",
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.friction.toFloat(),
        range = 0f..5f,
        onValueChange = {
            runtimeParametersChanged { friction = it.toDouble() }
        }
    )
}

@Composable
private fun ForceStrengthWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = "Force strength",
        description = "Global multiplier for all repulsive and attractive forces.",
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.forceScale.toFloat(),
        range = 1f..500f,
        onValueChange = {
            runtimeParametersChanged { forceScale = it.toDouble() }
        }
    )
}

@Composable
private fun ForceRangeWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = "Force range",
        description = "Global multiplier for the range at which the repulsive and attractive forces act.",
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.newtonMax.toFloat(),
        range = 20f..200f,
        onValueChange = {
            runtimeParametersChanged { newtonMax = it.toDouble() }
        }
    )
}

@Composable
private fun TimeStepWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = "Time step",
        description = "Simulation time step multiplier. Lower values lead to higher fidelity but slower simulation, higher values lead to faster simulation with more instability.",
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.timeScale.toFloat(),
        range = 0.1f..5f,
        onValueChange = {
            runtimeParametersChanged { timeScale = it.toDouble() }
        }
    )
}

@Composable
fun SpeciesContent(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Modify particle species, and how they interact. These settings only take effect upon generating new particles.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )

        if (isPortrait()) {
            NumberOfParticlesWidget(generationParameters, generationParametersChanged)
            NumberOfSpeciesWidget(generationParameters, generationParametersChanged)
            ForceValueRangeWidget(generationParameters, generationParametersChanged)
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    NumberOfParticlesWidget(generationParameters, generationParametersChanged)
                    NumberOfSpeciesWidget(generationParameters, generationParametersChanged)
                    ForceValueRangeWidget(generationParameters, generationParametersChanged)
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

                }
            }
        }
    }
}

@Composable
private fun NumberOfParticlesWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = "Number of particles",
        description = "Too many particles will impact simulation performance",
        valueToString = { it.roundToInt().toString() },
        value = generationParameters.value.nParticles.toFloat(),
        range = 50f..1200f,
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
        text = "Number of species",
        description = "More species increases the complexity",
        valueToString = { it.roundToInt().toString() },
        value = generationParameters.value.nSpecies.toFloat(),
        range = 1f..10f,
        onValueChange = {
            generationParametersChanged { nSpecies = it.roundToInt() }
        }
    )
}

@Composable
private fun ForceValueRangeWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextRangePair(
        text = "Force value range",
        description = "Maximum and minimum values for repulsion/attraction forces between species. Positive values indicate repulsion and negative values indicate attraction.",
        valueToString = { it.decimal(1) },
        values = Pair(
            generationParameters.value.maxAttraction.toFloat(),
            generationParameters.value.maxRepulsion.toFloat()
        ),
        range = -2f..2f,
        onValueChange = {
            generationParametersChanged {
                maxAttraction = it.first.toDouble()
                maxRepulsion = it.second.toDouble()
            }
        }
    )
}

@Composable
fun GenerateNewParticlesButton() {
    Button(onClick = {
        // TODO Viewmodel generate random particles / new animation
    }) {

    }
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
fun AboutContent() {
//    TODO("Not yet implemented")
}
