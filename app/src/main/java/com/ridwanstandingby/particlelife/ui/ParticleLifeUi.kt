package com.ridwanstandingby.particlelife.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.ridwanstandingby.particlelife.R
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
    ParticleLifeUi(
        createAnimationView = createAnimationView,
        onViewSizeChanged = vm::onViewSizeChanged,
        controlPanelExpanded = vm.controlPanelExpanded,
        selectedTabIndex = vm.selectedTabIndex,
        runtimeParameters = derivedStateOf { vm.parameters.value.runtime.copy() },
        generationParameters = derivedStateOf { vm.parameters.value.generation.copy() },
        runtimeParametersChanged = vm::changeRuntimeParameters,
        generationParametersChanged = vm::changeGenerationParameters,
        generateNewParticlesClicked = vm::generateNewParticles
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
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
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
                    generationParametersChanged,
                    generateNewParticlesClicked
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
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit,
    generateNewParticlesClicked: () -> Unit
) {
    Column {
        ControlPanelTabs(selectedTabIndex)
        when (ControlPanelTab.values()[selectedTabIndex.value]) {
            ControlPanelTab.PHYSICS -> PhysicsContent(runtimeParameters, runtimeParametersChanged)
            ControlPanelTab.PARTICLES -> ParticlesContent(
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
            text = stringResource(R.string.physics_info),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        if (isPortrait()) {
            FrictionWidget(runtimeParameters, runtimeParametersChanged)
            ForceStrengthWidget(runtimeParameters, runtimeParametersChanged)
            ForceRangeWidget(runtimeParameters, runtimeParametersChanged)
            PressureWidget(runtimeParameters, runtimeParametersChanged)
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
                    PressureWidget(runtimeParameters, runtimeParametersChanged)
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
        text = stringResource(R.string.friction_label),
        description = stringResource(R.string.friction_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.friction.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FRICTION_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FRICTION_MAX.toFloat(),
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
        text = stringResource(R.string.force_strength_label),
        description = stringResource(R.string.force_strength_description),
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.forceScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FORCE_STRENGTH_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FORCE_STRENGTH_MAX.toFloat(),
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
        text = stringResource(R.string.force_range_label),
        description = stringResource(R.string.force_range_description),
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.newtonMax.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.FORCE_RANGE_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.FORCE_RANGE_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { newtonMax = it.toDouble() }
        }
    )
}

@Composable
private fun PressureWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.pressure_label),
        description = stringResource(R.string.pressure_description),
        valueToString = { it.toInt().toString() },
        value = runtimeParameters.value.fermiForceScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.PRESSURE_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.PRESSURE_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { fermiForceScale = it.toDouble() }
        }
    )
}

@Composable
private fun TimeStepWidget(
    runtimeParameters: State<ParticleLifeParameters.RuntimeParameters>,
    runtimeParametersChanged: (ParticleLifeParameters.RuntimeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.time_step_label),
        description = stringResource(R.string.time_step_description),
        valueToString = { it.decimal(2) },
        value = runtimeParameters.value.timeScale.toFloat(),
        range = ParticleLifeParameters.RuntimeParameters.TIME_STEP_MIN.toFloat()..ParticleLifeParameters.RuntimeParameters.TIME_STEP_MAX.toFloat(),
        onValueChange = {
            runtimeParametersChanged { timeScale = it.toDouble() }
        }
    )
}

@Composable
fun ParticlesContent(
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
            ForceValueRangeWidget(generationParameters, generationParametersChanged)
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    GenerateNewParticlesButton(generateNewParticlesClicked)
                    NumberOfParticlesWidget(generationParameters, generationParametersChanged)
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
                    NumberOfSpeciesWidget(generationParameters, generationParametersChanged)
                    ForceValueRangeWidget(generationParameters, generationParametersChanged)
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
private fun ForceValueRangeWidget(
    generationParameters: State<ParticleLifeParameters.GenerationParameters>,
    generationParametersChanged: (ParticleLifeParameters.GenerationParameters.() -> Unit) -> Unit
) {
    TextRangePair(
        text = stringResource(R.string.force_value_range_label),
        description = stringResource(R.string.force_value_range_description),
        valueToString = { it.decimal(1) },
        values = Pair(
            generationParameters.value.maxAttraction.toFloat(),
            generationParameters.value.maxRepulsion.toFloat()
        ),
        range = ParticleLifeParameters.GenerationParameters.FORCE_VALUE_RANGE_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.FORCE_VALUE_RANGE_MAX.toFloat(),
        onValueChange = {
            generationParametersChanged {
                maxAttraction = it.first.toDouble()
                maxRepulsion = it.second.toDouble()
            }
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
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AboutText(stringResource(R.string.about_particle_simulation))
        AboutText(stringResource(R.string.about_particle_force_explanation))
        AboutText(stringResource(R.string.about_particle_extras))
        AboutText(stringResource(R.string.about_particle_final))
    }
}

@Composable
fun AboutText(text: String) {
    Text(text, modifier = Modifier.padding(vertical = 4.dp))
}
