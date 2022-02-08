/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.ridwanstandingby.particlelife.ui.theme.icons.rounded

import androidx.compose.ui.graphics.vector.ImageVector
import com.ridwanstandingby.particlelife.ui.theme.icons.Icons
import com.ridwanstandingby.particlelife.ui.theme.icons.materialIcon
import com.ridwanstandingby.particlelife.ui.theme.icons.materialPath

val Icons.Rounded.Tune: ImageVector
    get() {
        if (_tune != null) {
            return _tune!!
        }
        _tune = materialIcon(name = "Rounded.Tune") {
            materialPath {
                moveTo(3.0f, 18.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(5.0f)
                verticalLineToRelative(-2.0f)
                lineTo(4.0f, 17.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                close()
                moveTo(3.0f, 6.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(9.0f)
                lineTo(13.0f, 5.0f)
                lineTo(4.0f, 5.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                close()
                moveTo(13.0f, 20.0f)
                verticalLineToRelative(-1.0f)
                horizontalLineToRelative(7.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                reflectiveCurveToRelative(-0.45f, -1.0f, -1.0f, -1.0f)
                horizontalLineToRelative(-7.0f)
                verticalLineToRelative(-1.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                reflectiveCurveToRelative(-1.0f, 0.45f, -1.0f, 1.0f)
                verticalLineToRelative(4.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                reflectiveCurveToRelative(1.0f, -0.45f, 1.0f, -1.0f)
                close()
                moveTo(7.0f, 10.0f)
                verticalLineToRelative(1.0f)
                lineTo(4.0f, 11.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                reflectiveCurveToRelative(0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(3.0f)
                verticalLineToRelative(1.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                reflectiveCurveToRelative(1.0f, -0.45f, 1.0f, -1.0f)
                verticalLineToRelative(-4.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                reflectiveCurveToRelative(-1.0f, 0.45f, -1.0f, 1.0f)
                close()
                moveTo(21.0f, 12.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                horizontalLineToRelative(-9.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(9.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                close()
                moveTo(16.0f, 9.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                lineTo(17.0f, 7.0f)
                horizontalLineToRelative(3.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                reflectiveCurveToRelative(-0.45f, -1.0f, -1.0f, -1.0f)
                horizontalLineToRelative(-3.0f)
                lineTo(17.0f, 4.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                reflectiveCurveToRelative(-1.0f, 0.45f, -1.0f, 1.0f)
                verticalLineToRelative(4.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                close()
            }
        }
        return _tune!!
    }

@Suppress("ObjectPropertyName")
private var _tune: ImageVector? = null