package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R
import com.ridwanstandingby.particlelife.domain.ParticleLifeParameters
import com.ridwanstandingby.particlelife.domain.Species
import com.ridwanstandingby.particlelife.ui.theme.ParticleLifeTheme
import com.ridwanstandingby.particlelife.ui.theme.icons.Icons
import com.ridwanstandingby.particlelife.ui.theme.icons.rounded.ChevronRight
import kotlin.math.roundToInt

@Composable
fun EditForceStrengthsPanelCardContent(
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
            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(vertical = 4.dp)
            )
            SelectSpeciesWidget(
                selectedSpeciesIndex = editForceStrengthsSelectedSpeciesIndex,
                allSpecies = species.value
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(vertical = 4.dp)
            )
            if (isPortrait()) {
                species.value.forEachIndexed { index, _ ->
                    EditForceStrengthSlider(
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
                                EditForceStrengthSlider(
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
                                EditForceStrengthSlider(
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

@Composable
fun EditForceDistancesPanelCardContent(
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
            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(vertical = 4.dp)
            )
            SelectSpeciesWidget(
                selectedSpeciesIndex = editForceDistancesSelectedSpeciesIndex,
                allSpecies = species.value
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(vertical = 4.dp)
            )
            if (isPortrait()) {
                species.value.forEachIndexed { index, _ ->
                    EditForceDistanceSlider(
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
                                EditForceDistanceSlider(
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
                                EditForceDistanceSlider(
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
private fun EditForceStrengthSlider(
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
                            shape = CircleShape
                        ) else Modifier
                    }
                    .align(Alignment.Center)
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
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex }
            ) {
                SpeciesIcon(color = Color(allSpecies[thisSpeciesIndex].color)) { align(Alignment.Center) }
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditForceDistanceSlider(
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
                            shape = CircleShape
                        ) else Modifier
                    }
                    .align(Alignment.Center)
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
                onClick = { selectedSpeciesIndex.value = thisSpeciesIndex }
            ) {
                SpeciesIcon(color = Color(allSpecies[thisSpeciesIndex].color)) { align(Alignment.Center) }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.SelectSpeciesWidget(
    selectedSpeciesIndex: MutableState<Int>,
    allSpecies: List<Species>
) {
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

        Text(
            stringResource(R.string.edit_values_for_label),
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
                focusManager.clearFocus()
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterVertically)
        ) {
            Row {
                SpeciesIcon(color = Color(allSpecies[selectedSpeciesIndex.value].color)) {
                    align(Alignment.CenterVertically)
                }
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) { expanded != expanded }
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                }
            ) {
                allSpecies.indices.forEach { speciesIdx ->
                    DropdownMenuItem(
                        onClick = {
                            selectedSpeciesIndex.value = speciesIdx
                            expanded = false
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SpeciesIcon(color = Color(allSpecies[speciesIdx].color)) {
                                align(Alignment.CenterHorizontally)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeciesIcon(color: Color, modifier: Modifier.() -> Modifier) {
    Box(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .height(48.dp)
            .width(48.dp)
            .background(color, RoundedCornerShape(percent = 25))
            .modifier()
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun EditForceStrengthsPanelUiPreview() {
    val runtimeParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ).runtime
        )
    }
    ParticleLifeTheme {
        Scaffold {
            EditForceStrengthsPanelCardContent(
                runtimeParameters = runtimeParameters,
                runtimeParametersChanged = {
                    runtimeParameters.value = runtimeParameters.value.copy().apply(it)
                },
                species = remember {
                    mutableStateOf(
                        ParticleLifeParameters.GenerationParameters().generateRandomSpecies()
                    )
                },
                editForceStrengthsSelectedSpeciesIndex = remember { mutableStateOf(0) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun EditForceDistancesPanelUiPreview() {
    val runtimeParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ).runtime
        )
    }
    ParticleLifeTheme {
        Scaffold {
            EditForceDistancesPanelCardContent(
                runtimeParameters = runtimeParameters,
                runtimeParametersChanged = {
                    runtimeParameters.value = runtimeParameters.value.copy().apply(it)
                },
                species = remember {
                    mutableStateOf(
                        ParticleLifeParameters.GenerationParameters().generateRandomSpecies()
                    )
                },
                editForceDistancesSelectedSpeciesIndex = remember { mutableStateOf(0) }
            )
        }
    }
}