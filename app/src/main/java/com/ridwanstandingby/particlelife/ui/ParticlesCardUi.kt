package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import kotlin.math.roundToInt

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
            .align(Alignment.CenterHorizontally)
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
            .align(Alignment.CenterHorizontally)
    ) {
        Text(stringResource(R.string.edit_force_strengths_label))
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
            .align(Alignment.CenterHorizontally)
    ) {
        Text(stringResource(R.string.edit_force_distances_label))
    }
}