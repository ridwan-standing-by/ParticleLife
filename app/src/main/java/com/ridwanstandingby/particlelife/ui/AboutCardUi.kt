package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ridwanstandingby.particlelife.R

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
private fun AboutText(text: String) {
    Text(text, modifier = Modifier.padding(vertical = 4.dp))
}