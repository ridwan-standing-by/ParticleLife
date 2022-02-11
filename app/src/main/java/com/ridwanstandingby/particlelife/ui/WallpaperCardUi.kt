package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun WallpaperContent(
    setWallpaperClicked: () -> Unit,
    wallpaperParameters: State<ParticleLifeParameters>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit,
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.wallpaper_info),
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
            SetWallpaperButton(setWallpaperClicked)
            WallpaperNumberOfParticlesWidget(
                wallpaperParameters,
                wallpaperParametersChanged
            )
            WallpaperNumberOfSpeciesWidget(
                wallpaperParameters,
                wallpaperParametersChanged
            )
        } else {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.5f)
                        .padding(end = 12.dp)
                ) {
                    SetWallpaperButton(setWallpaperClicked)
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
                    WallpaperNumberOfParticlesWidget(
                        wallpaperParameters,
                        wallpaperParametersChanged
                    )
                    WallpaperNumberOfSpeciesWidget(
                        wallpaperParameters,
                        wallpaperParametersChanged
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SetWallpaperButton(setWallpaperClicked: () -> Unit) {
    Button(
        onClick = setWallpaperClicked,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.95f)
            .align(Alignment.CenterHorizontally)
    ) {
        Text(stringResource(R.string.set_wallpaper_button_label))
    }
}

@Composable
private fun WallpaperNumberOfParticlesWidget(
    wallpaperParameters: State<ParticleLifeParameters>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.number_of_particles_label),
        description = stringResource(R.string.number_of_particles_description),
        valueToString = { it.roundToInt().toString() },
        value = wallpaperParameters.value.generation.nParticles.toFloat(),
        range = ParticleLifeParameters.GenerationParameters.N_PARTICLES_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.N_PARTICLES_MAX.toFloat(),
        onValueChange = {
            wallpaperParametersChanged { generation.nParticles = it.roundToInt() }
        }
    )
}

@Composable
private fun WallpaperNumberOfSpeciesWidget(
    wallpaperParameters: State<ParticleLifeParameters>,
    wallpaperParametersChanged: (ParticleLifeParameters.() -> Unit) -> Unit
) {
    TextSliderPair(
        text = stringResource(R.string.number_of_species_label),
        description = stringResource(R.string.number_of_species_description),
        valueToString = { it.roundToInt().toString() },
        value = wallpaperParameters.value.generation.nSpecies.toFloat(),
        range = ParticleLifeParameters.GenerationParameters.N_SPECIES_MIN.toFloat()..ParticleLifeParameters.GenerationParameters.N_SPECIES_MAX.toFloat(),
        onValueChange = {
            wallpaperParametersChanged { generation.nSpecies = it.roundToInt() }
        }
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun WallpaperCardUiPreview() {
    val wallpaperParameters = remember {
        mutableStateOf(
            ParticleLifeParameters.buildDefault(
                100.0, 100.0, ParticleLifeParameters.GenerationParameters()
            ), neverEqualPolicy()
        )
    }
    ParticleLifeTheme {
        Scaffold {
            WallpaperContent(
                setWallpaperClicked = {},
                wallpaperParameters = wallpaperParameters,
                wallpaperParametersChanged = { wallpaperParameters.value = wallpaperParameters.value.copy().apply(it) }
            )
        }
    }
}