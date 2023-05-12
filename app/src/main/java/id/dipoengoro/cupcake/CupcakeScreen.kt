package id.dipoengoro.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.dipoengoro.cupcake.data.Datasource.flavors
import id.dipoengoro.cupcake.data.Datasource.quantityOptions
import id.dipoengoro.cupcake.ui.OrderSummaryScreen
import id.dipoengoro.cupcake.ui.OrderViewModel
import id.dipoengoro.cupcake.ui.SelectOptionScreen
import id.dipoengoro.cupcake.ui.StartOrderScreen

enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    modifier: Modifier = Modifier,
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeApp(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = CupcakeScreen.valueOf(
                    backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
                ),
                canNavigateBack = navController.previousBackStackEntry != null
            ) {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(quantityOptions = quantityOptions) {
                    viewModel.setQuantity(it)
                    navController.navigate(CupcakeScreen.Flavor.name)
                }
            }
            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(
                            viewModel,
                            navController
                        )
                    }
                ) {
                    navController.navigate(CupcakeScreen.Pickup.name)
                }
            }
            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(
                            viewModel,
                            navController
                        )
                    }
                ) {
                    navController.navigate(CupcakeScreen.Summary.name)
                }
            }
            composable(route = CupcakeScreen.Summary.name) {
                LocalContext.current.let { context ->
                    OrderSummaryScreen(
                        orderUiState = uiState,
                        onCancelButtonClicked = {
                            cancelOrderAndNavigateToStart(
                                viewModel,
                                navController
                            )
                        }) { subject: String, summary: String ->
                        shareOrder(context, subject, summary)
                    }
                }
            }
        }
    }
}

private fun shareOrder(context: Context, subject: String, summary: String) =
    Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
        context.startActivity(
            Intent.createChooser(
                this,
                context.getString(R.string.new_cupcake_order)
            )
        )
    }

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, false)
}