package com.k2.deskclock.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.k2.deskclock.R
import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.ui.UiStateHolder
import com.k2.deskclock.ui.theme.Typography

@Composable
fun Settings(
    uiStates: UiStateHolder,
    navController: NavController,
    preferenceStore: PreferenceStore
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.size(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "back",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .size(28.dp)
                    .alpha(0.8F)
                    .clickable { navController.navigateUp() }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = LocalContext.current.getString(R.string.app_name),
                style = Typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .clickable { uiStates.darkTheme.value = uiStates.darkTheme.value.not() },
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ListText(text = stringResource(R.string.dark_theme))
            Switch(
                checked = uiStates.darkTheme.value,
                onCheckedChange = { enabled ->
                    uiStates.darkTheme.value = enabled
                    preferenceStore.darkTheme = enabled
                },
                modifier = Modifier.fillMaxWidth(0.2F)
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .padding(horizontal = 8.dp)
                .background(Color.Gray)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ListText(
                text = stringResource(R.string.clock_size),
                modifier = Modifier.padding(start = 16.dp)
            )
            ListText(
                text = uiStates.clockSize.value.toInt().toString(),
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Slider(
            value = uiStates.clockSize.value,
            onValueChange = {
                uiStates.clockSize.value = it
                preferenceStore.clockSize = it
            },
            valueRange = 36F..120F,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .padding(horizontal = 8.dp)
                .background(Color.Gray)
        )

        ListText(
            text = stringResource(R.string.wallpaper_query),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val cats = remember {
            mutableStateOf(preferenceStore.wallpaperCategory)
        }
        TextField(
            value = cats.value,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            onValueChange = {
                cats.value = it
                preferenceStore.wallpaperCategory = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .padding(horizontal = 8.dp)
                .background(Color.Gray)
        )
    }
}

@Composable
fun ListText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        style = Typography.body1,
        modifier = modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(0.8F)
    )
}
