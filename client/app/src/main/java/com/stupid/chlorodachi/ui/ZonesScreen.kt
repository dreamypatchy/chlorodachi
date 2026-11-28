package com.stupid.chlorodachi.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stupid.chlorodachi.R
import com.stupid.chlorodachi.data.Datasource
import com.stupid.chlorodachi.model.Zone
import com.stupid.chlorodachi.ui.theme.ChlorodachiTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ZoneFormActions(
    val onNameChange: (String) -> Unit = {},
    val onPlantChange: (String) -> Unit = {},
    val onXChange: (Double) -> Unit = {},
    val onYChange: (Double) -> Unit = {},
    val onWChange: (Double) -> Unit = {},
    val onHChange: (Double) -> Unit = {},
    val onMinIlluminanceChange: (Double) -> Unit = {},
    val onMaxIlluminanceChange: (Double) -> Unit = {},
    val onValidTemperatureChange: (ClosedFloatingPointRange<Double>) -> Unit = {},
    val onValidHumidityChange: (ClosedFloatingPointRange<Double>) -> Unit = {},
    val onValidMoistureChange: (ClosedFloatingPointRange<Double>) -> Unit = {},
)

private fun ZoneFormViewModel.actions() = ZoneFormActions(
    onNameChange = ::onNameChange,
    onPlantChange = ::onPlantChange,
    onXChange = ::onXChange,
    onYChange = ::onYChange,
    onWChange = ::onWChange,
    onHChange = ::onHChange,
    onMinIlluminanceChange = ::onMinIlluminanceChange,
    onMaxIlluminanceChange = ::onMaxIlluminanceChange,
    onValidTemperatureChange = ::onValidTemperatureChange,
    onValidHumidityChange = ::onValidHumidityChange,
    onValidMoistureChange = ::onValidMoistureChange,
)

@Composable
fun ZonesRoute(
    modifier: Modifier = Modifier,
    viewModel: ZonesViewModel = viewModel(factory = ZonesViewModel.Factory),
    addViewModel: ZoneFormViewModel = viewModel(),
    editViewModel: ZoneEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val addUiState by addViewModel.uiState.collectAsStateWithLifecycle()
    val editUiState by editViewModel.uiState.collectAsStateWithLifecycle()
    val editingZoneId by editViewModel.editingZoneId.collectAsStateWithLifecycle()

    val addActions = remember(addViewModel) { addViewModel.actions() }
    val editActions = remember(editViewModel) { editViewModel.actions() }

    ZonesScreen(
        uiState = uiState,
        addUiState = addUiState,
        addActions = addActions,
        editUiState = editUiState,
        editActions = editActions,
        editingZoneId = editingZoneId,
        onNotificationsClick = {},
        onSettingsClick = {},
        onAddConfirm = {},
        onZoneClick = { zoneId ->
            viewModel.formStateFor(zoneId)?.let { editViewModel.start(zoneId, it) }
        },
        onEditDismiss = editViewModel::stop,
        onEditConfirm = editViewModel::stop,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZonesScreen(
    uiState: ZonesUiState,
    addUiState: ZoneFormUiState,
    addActions: ZoneFormActions,
    editUiState: ZoneFormUiState,
    editActions: ZoneFormActions,
    editingZoneId: String?,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddConfirm: () -> Unit,
    onZoneClick: (String) -> Unit,
    onEditDismiss: () -> Unit,
    onEditConfirm: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        val listState = rememberLazyListState()
        var showAddSheet by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.zones_screen_title),
                    onNotificationsClick = onNotificationsClick,
                    onSettingsClick = onSettingsClick,
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = !uiState.isLoading && listState.isScrollingUp(),
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    MediumFloatingActionButton(
                        onClick = { showAddSheet = true }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_add),
                            contentDescription = stringResource(R.string.zones_screen_zone_add_dialog),
                            modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize)
                        )
                    }
                }
            }
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ZonesContents(
                    zones = uiState.zones,
                    listState = listState,
                    onZoneClick = onZoneClick,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }

        if (showAddSheet) {
            ZoneFormSheet(
                state = addUiState,
                actions = addActions,
                confirmText = stringResource(R.string.zones_screen_zone_dialog_add),
                onDismiss = { showAddSheet = false },
                onConfirm = {
                    showAddSheet = false
                    onAddConfirm()
                },
            )
        }

        if (editingZoneId != null) {
            key(editingZoneId) {
                ZoneFormSheet(
                    state = editUiState,
                    actions = editActions,
                    confirmText = stringResource(R.string.zones_screen_zone_dialog_save),
                    onDismiss = onEditDismiss,
                    onConfirm = onEditConfirm,
                )
            }
        }
    }
}

@Composable
fun ZonesContents(
    zones: List<ZoneUiModel>,
    listState: LazyListState,
    onZoneClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(bottom = 112.dp)
    ) {
        items(zones, key = { it.id }) { zone ->
            ZoneCard(
                zone = zone,
                zoneInfoType = "coordinates",
                onClick = { onZoneClick(zone.id) },
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }
    }
}

private val ZONE_FORM_ROW_SPACING = 32.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoneFormSheet(
    state: ZoneFormUiState,
    actions: ZoneFormActions,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        ZoneFormContent(
            state = state,
            actions = actions,
            confirmText = confirmText,
            onConfirmClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) onConfirm()
                }
            },
        )
    }
}

@Composable
fun ZoneFormContent(
    state: ZoneFormUiState,
    actions: ZoneFormActions,
    confirmText: String,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ZoneDataTextField(
                value = state.name,
                onValueChange = actions.onNameChange,
                label = stringResource(R.string.zones_screen_zone_dialog_zone_name),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            ZoneDataTextField(
                value = state.plant,
                onValueChange = actions.onPlantChange,
                label = stringResource(R.string.zones_screen_zone_dialog_plant),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ZONE_FORM_ROW_SPACING)
            ) {
                ZoneDataDoubleField(
                    value = state.x,
                    onValueChange = actions.onXChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_x),
                    modifier = Modifier.weight(1f)
                )
                ZoneDataDoubleField(
                    value = state.y,
                    onValueChange = actions.onYChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_y),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ZONE_FORM_ROW_SPACING)
            ) {
                ZoneDataDoubleField(
                    value = state.w,
                    onValueChange = actions.onWChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_width),
                    modifier = Modifier.weight(1f)
                )
                ZoneDataDoubleField(
                    value = state.h,
                    onValueChange = actions.onHChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_height),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ZONE_FORM_ROW_SPACING)
            ) {
                ZoneDataDoubleField(
                    value = state.minIlluminance,
                    onValueChange = actions.onMinIlluminanceChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_min_illuminance),
                    modifier = Modifier.weight(1f)
                )
                ZoneDataDoubleField(
                    value = state.maxIlluminance,
                    onValueChange = actions.onMaxIlluminanceChange,
                    label = stringResource(R.string.zones_screen_zone_dialog_max_illuminance),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            ZoneDataRangeSlider(
                title = stringResource(R.string.zones_screen_zone_dialog_valid_temperature),
                value = state.validTemperature,
                bounds = VALID_TEMPERATURE_RANGE,
                stepSize = 5.0,
                onValueChangeFinished = actions.onValidTemperatureChange,
                valueLabel = { "${it.roundToInt()}°" },
                modifier = Modifier
            )
        }
        item {
            ZoneDataRangeSlider(
                title = stringResource(R.string.zones_screen_zone_dialog_valid_humidity),
                value = state.validHumidity,
                bounds = VALID_HUMIDITY_RANGE,
                stepSize = 5.0,
                onValueChangeFinished = actions.onValidHumidityChange,
                valueLabel = { "${it.roundToInt()}%" },
                modifier = Modifier
            )
        }
        item {
            ZoneDataRangeSlider(
                title = stringResource(R.string.zones_screen_zone_dialog_valid_moisture),
                value = state.validMoisture,
                bounds = VALID_MOISTURE_RANGE,
                stepSize = 5.0,
                onValueChangeFinished = actions.onValidMoistureChange,
                valueLabel = { "${it.roundToInt()}%" },
                modifier = Modifier
            )
        }
        item {
            Button(
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(confirmText)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoneDataRangeSlider(
    title: String,
    value: ClosedFloatingPointRange<Double>,
    bounds: ClosedFloatingPointRange<Double>,
    stepSize: Double,
    onValueChangeFinished: (ClosedFloatingPointRange<Double>) -> Unit,
    modifier: Modifier = Modifier,
    valueLabel: (Double) -> String = { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() },
) {
    val steps = ((bounds.endInclusive - bounds.start) / stepSize).roundToInt() - 1
    val trackRange = bounds.start.toFloat()..bounds.endInclusive.toFloat()

    fun Float.toCleanDouble(): Double =
        bounds.start + ((this - bounds.start) / stepSize.toFloat()).roundToInt() * stepSize

    val state = rememberSaveable(
        steps, trackRange,
        saver = RangeSliderState.Saver(steps, trackRange),
    ) {
        RangeSliderState(
            startValue = value.start.toFloat(),
            endValue = value.endInclusive.toFloat(),
            steps = steps,
            trackRange = trackRange,
        )
    }

    LaunchedEffect(value) {
        if (state.startValue != value.start.toFloat()) state.startValue = value.start.toFloat()
        if (state.endValue != value.endInclusive.toFloat()) state.endValue = value.endInclusive.toFloat()
    }

    val currentOnFinished by rememberUpdatedState(onValueChangeFinished)
    val span = trackRange.endInclusive - trackRange.start
    val startFraction = (state.startValue - trackRange.start) / span
    val endFraction = (state.endValue - trackRange.start) / span

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall
        )

        Box(Modifier.fillMaxWidth()) {
            ThumbLabel(startFraction, valueLabel(state.startValue.toCleanDouble()))
            ThumbLabel(endFraction, valueLabel(state.endValue.toCleanDouble()))
        }

        RangeSlider(
            state = state,
            onValueChange = { range ->
                state.startValue = range.start
                state.endValue = range.endInclusive
            },
            onValueChangeFinished = {
                currentOnFinished(
                    state.startValue.toCleanDouble()..state.endValue.toCleanDouble()
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                valueLabel(bounds.start),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                valueLabel(bounds.endInclusive),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ZoneDataTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun ZoneDataDoubleField(
    value: Double,
    onValueChange: (Double) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value.toString()) }

    LaunchedEffect(value) {
        if (text.toDoubleOrNull() != value) {
            text = value.toString()
        }
    }

    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            if (input.isEmpty() || input.matches(Regex("^-?\\d*[.,]?\\d*$"))) {
                text = input
                input.replace(',', '.').toDoubleOrNull()?.let(onValueChange)
            }
        },
        modifier = modifier,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun ThumbLabel(
    fraction: Float,
    text: String,
    thumbWidth: Dp = 4.dp
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
            val thumbPx = thumbWidth.roundToPx()
            val center = thumbPx / 2f + fraction * (constraints.maxWidth - thumbPx)
            val x = (center - placeable.width / 2f).roundToInt()
                .coerceIn(0, (constraints.maxWidth - placeable.width).coerceAtLeast(0))
            layout(constraints.maxWidth, placeable.height) {
                placeable.place(x, 0)
            }
        }
    )
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            val up = if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousOffset >= firstVisibleItemScrollOffset
            }
            previousIndex = firstVisibleItemIndex
            previousOffset = firstVisibleItemScrollOffset
            up
        }
    }.value
}

@Preview(showBackground = true)
@Composable
fun ZonesScreenPreview() {
    ChlorodachiTheme {
        ZonesScreen(
            uiState = ZonesUiState(
                isLoading = false,
                zones = Datasource().loadDemoZones().map(Zone::toUiModel),
            ),
            addUiState = ZoneFormUiState(),
            addActions = ZoneFormActions(),
            editUiState = ZoneFormUiState(),
            editActions = ZoneFormActions(),
            editingZoneId = null,
            onNotificationsClick = {},
            onSettingsClick = {},
            onAddConfirm = {},
            onZoneClick = {},
            onEditDismiss = {},
            onEditConfirm = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
fun ZoneFormContentPreview() {
    ChlorodachiTheme {
        Surface {
            ZoneFormContent(
                state = Datasource().loadDemoZones().first().toFormState(),
                actions = ZoneFormActions(),
                confirmText = stringResource(R.string.zones_screen_zone_dialog_save),
                onConfirmClick = {},
            )
        }
    }
}