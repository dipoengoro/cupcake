package id.dipoengoro.cupcake.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import id.dipoengoro.cupcake.R
import id.dipoengoro.cupcake.data.OrderUiState
import id.dipoengoro.cupcake.ui.components.FormattedPriceLabel
import id.dipoengoro.cupcake.ui.theme.Dimen

@Composable
fun OrderSummaryScreen(
    modifier: Modifier = Modifier,
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit
) {
    val resources = LocalContext.current.resources

    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )

    val orderSummary = stringResource(
        id = R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity
    )

    val newOrder = stringResource(id = R.string.new_cupcake_order)

    val items = listOf(
        Pair(stringResource(id = R.string.quantity), numberOfCupcakes),
        Pair(stringResource(id = R.string.flavor), orderUiState.flavor),
        Pair(stringResource(id = R.string.pickup_date), orderUiState.date)
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.padding(Dimen.PADDING_MEDIUM),
            verticalArrangement = Arrangement.spacedBy(Dimen.PADDING_SMALL)
        ) {
            items.forEach { item ->
                Text(text = item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                Divider(thickness = Dimen.THICKNESS_DIVIDER)
            }
            Spacer(modifier = Modifier.height(Dimen.PADDING_SMALL))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f, false)
                .padding(Dimen.PADDING_MEDIUM)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimen.PADDING_SMALL)) {
                Button(onClick = { onSendButtonClicked(newOrder, orderSummary) }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.send))
                }
                OutlinedButton(onClick = onCancelButtonClicked, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    OrderSummaryScreen(
        orderUiState = OrderUiState(0, "Test", "Test", "$300.00"),
        modifier = Modifier.fillMaxHeight(),
        onCancelButtonClicked = {}
    ) {_, _ ->}
}