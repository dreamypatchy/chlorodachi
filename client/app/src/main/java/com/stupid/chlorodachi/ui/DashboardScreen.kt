package com.stupid.chlorodachi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stupid.chlorodachi.R
import com.stupid.chlorodachi.data.Datasource
import com.stupid.chlorodachi.model.Zone
import com.stupid.chlorodachi.ui.theme.ChlorodachiTheme

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState,
        onZoneClick = {},
        onSeeAllClick = {},
        onNotificationsClick = {},
        onSettingsClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onZoneClick: (ZoneUiModel) -> Unit,
    onSeeAllClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ScreenTopBar(
                    title = stringResource(R.string.dashboard_screen_title),
                    onNotificationsClick = onNotificationsClick,
                    onSettingsClick = onSettingsClick,
                    scrollBehavior = scrollBehavior
                )
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
                DashboardContents(
                    zones = uiState.zones,
                    onZoneClick = onZoneClick,
                    onSeeAllClick = onSeeAllClick,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun DashboardContents(
    zones: List<ZoneUiModel>,
    onZoneClick: (ZoneUiModel) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            GreenhouseMapSection(
                zones = zones,
                onSeeAllClick = onSeeAllClick,
                onZoneClick = onZoneClick
            )
        }
        item {
            ZonesHeader(
                onSeeAllClick = onSeeAllClick
            )
        }
        items(zones, key = { it.id }) { zone ->
            ZoneCard(
                zone = zone,
                zoneInfoType = "measurements",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }
    }
}

@Composable
fun GreenhouseMapSection(
    zones: List<ZoneUiModel>,
    onSeeAllClick: () -> Unit,
    onZoneClick: (ZoneUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SectionHeader(
            title = stringResource(R.string.dashboard_screen_greenhouse_map_section_title),
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        ZonesGrid(
            zones = zones,
            onZoneClick = onZoneClick
        )
    }
}

@Composable
fun ZonesHeader(
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionHeader(
        title = stringResource(R.string.dashboard_screen_zones_section_title),
        onSeeAllClick = onSeeAllClick,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}

@Composable
fun ZonesGrid(
    zones: List<ZoneUiModel>,
    onZoneClick: (ZoneUiModel) -> Unit,
    modifier: Modifier = Modifier,
    outerPadding: Dp = 16.dp,
    gap: Dp = 8.dp
) {
    if (zones.isEmpty()) return

    val columns = zones.maxOf { it.gridX + it.gridWidth - 1 }
    val rows = zones.maxOf { it.gridY + it.gridHeight - 1 }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val cell = (maxWidth - outerPadding * 2 - gap * (columns - 1)) / columns
        val gridHeight = cell * rows + gap * (rows - 1) + outerPadding * 2

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight)
        ) {
            zones.forEach { zone ->
                val col = zone.gridX - 1
                val row = zone.gridY - 1
                val w = zone.gridWidth
                val h = zone.gridHeight

                ZoneTile(
                    zone = zone,
                    onClick = { onZoneClick(zone) },
                    modifier = Modifier
                        .offset(
                            x = outerPadding + (cell + gap) * col,
                            y = outerPadding + (cell + gap) * row
                        )
                        .size(
                            width = cell * w + gap * (w - 1),
                            height = cell * h + gap * (h - 1)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    ChlorodachiTheme {
        DashboardScreen(
            uiState = DashboardUiState(
                isLoading = false,
                zones = Datasource().loadDemoZones().map(Zone::toUiModel)
            ),
            onZoneClick = {},
            onSeeAllClick = {},
            onNotificationsClick = {},
            onSettingsClick = {}
        )
    }
}