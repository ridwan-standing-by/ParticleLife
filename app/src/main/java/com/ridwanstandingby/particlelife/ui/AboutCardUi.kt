package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R

@Composable
fun AboutContent() {
    if (isPortrait()) {
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
            DiagramWithCaption()
        }
    } else {
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Column(
                Modifier
                    .weight(0.5f)
                    .padding(end = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AboutText(stringResource(R.string.about_particle_simulation))
                AboutText(stringResource(R.string.about_particle_force_explanation))
                AboutText(stringResource(R.string.about_particle_extras))
                AboutText(stringResource(R.string.about_particle_final))
                AboutText(stringResource(R.string.about_particle_hand_of_god))
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
                    .verticalScroll(rememberScrollState())
            ) {
                DiagramWithCaption()
            }
        }
    }
}

@Composable
private fun DiagramWithCaption() {
    Image(
        painter = painterResource(id = R.drawable.force_against_distance_diagram),
        contentDescription = stringResource(R.string.about_particle_diagram_content_description),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix(negativeColourMatrix))
    )
    Text(
        stringResource(R.string.about_particle_diagram_content_description),
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(all = 4.dp)
    )
}

@Composable
private fun AboutText(text: String) {
    Text(text, modifier = Modifier.padding(vertical = 4.dp))
}

private val negativeColourMatrix = floatArrayOf(
    -1.0f, .0f, .0f, .0f, 255.0f,
    .0f, -1.0f, .0f, .0f, 255.0f,
    .0f, .0f, -1.0f, .0f, 255.0f,
    .0f, .0f, .0f, 1.0f, .0f
)

@Preview(showBackground = true, widthDp = 300, heightDp = 600, device = Devices.PIXEL)
@Preview(showBackground = true, widthDp = 600, heightDp = 300, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun AboutCardUiPreview() {
    ParticleLifePreview {
        AboutContent()
    }
}