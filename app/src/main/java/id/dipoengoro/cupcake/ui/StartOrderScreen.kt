package id.dipoengoro.cupcake.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dipoengoro.cupcake.R
import id.dipoengoro.cupcake.data.Datasource
import id.dipoengoro.cupcake.ui.theme.Dimen

@Composable
fun StartOrderScreen(
    modifier: Modifier = Modifier,
    quantityOptions: List<Pair<Int, Int>>,
    onNextButtonClick: (Int) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Dimen.PADDING_SMALL
            )
        ) {
            Spacer(modifier = Modifier.height(Dimen.PADDING_MEDIUM))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(Dimen.PADDING_MEDIUM))
            Text(
                text = stringResource(id = R.string.order_cupcakes),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(Dimen.PADDING_SMALL))
        }
        Row(modifier = Modifier.weight(1f, false)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimen.PADDING_MEDIUM)
            ) {
                quantityOptions.forEach { item ->
                    SelectQuantityButton(
                        labelResourceId = item.first,
                        onClick = { onNextButtonClick(item.second) }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectQuantityButton(
    modifier: Modifier = Modifier,
    @StringRes labelResourceId: Int,
    onClick: () -> Unit
) {
    Button(onClick = onClick, modifier = modifier.widthIn(min = 250.dp)) {
        Text(text = stringResource(id = labelResourceId))
    }
}

@Preview
@Composable
fun StartOrderPreview() {
    StartOrderScreen(
        quantityOptions = Datasource.quantityOptions,
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.PADDING_MEDIUM),
    ) {}
}