package com.ridwanstandingby.particlelife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.log2
import kotlin.math.pow

@Composable
fun LogarithmicTextSliderPair(
    text: String,
    description: String,
    valueToString: (Float) -> String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {

    TextSliderPair(
        text = text,
        description = description,
        valueToString = { valueToString(2f.pow(it)) },
        value = log2(value),
        range = log2(range.start)..log2(range.endInclusive),
        steps = steps,
        onValueChange = { onValueChange(2f.pow(it)) }
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
fun TextSwitchPair(
    text: String,
    description: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
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
                    .weight(0.725f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = checked,
                onCheckedChange = { onToggle(it) },
                modifier = Modifier
                    .weight(0.275f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(text = description, fontSize = MaterialTheme.typography.caption.fontSize)
    }
}