package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.ui.theme.Typography

@Composable
fun BleStatusView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                start = 60.dp, end = 60.dp, top = 48.dp
            ),
            text = stringResource(R.string.firstpair_tos_title),
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.weight(weight = 1f), verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(R.string.firstpair_tos_title)
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 40.dp, vertical = 8.dp
                    ),
                painter = painterResource(R.drawable.pic_instruction),
                contentDescription = stringResource(R.string.firstpair_tos_description)
            )
            ComposableFooter(onApplyPress = { /*TODO*/ })
        }

    }
}


@Composable
fun ComposableFooter(onApplyPress: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashGearButton(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 18.dp
                )
                .fillMaxWidth(0.5f),
            onClick = onApplyPress,
            text = stringResource(R.string.firstpair_tos_button)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BleStatusViewPreview() {
    Column {
        BleStatusView(Modifier)
    }
}