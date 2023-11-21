package com.k2.deskclock.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k2.deskclock.R
import com.k2.deskclock.location.models.ErrorType
import com.k2.deskclock.ui.UiStateHolder
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun BoxScope.LocationErrorSnackBar(
    modifier: Modifier = Modifier,
    uiStateHolder: UiStateHolder,
) {
    val message: String? =
        when (uiStateHolder.locationError.value) {
            ErrorType.LocationDisabled -> stringResource(R.string.location_error_disabled)
            ErrorType.NoPermission -> stringResource(R.string.location_error_permission)
            else -> null
        }
    LaunchedEffect(key1 = System.currentTimeMillis(), block = {
        delay(TimeUnit.SECONDS.toMillis(5))
        uiStateHolder.locationError.value = null
    })
    message?.let {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.BottomCenter)
                    .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(12.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_location_off),
                contentDescription = "",
            )
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
            )
        }
    }
}
