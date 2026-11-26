package com.stupid.chlorodachi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stupid.chlorodachi.R
import com.stupid.chlorodachi.data.Datasource
import com.stupid.chlorodachi.model.Zone
import com.stupid.chlorodachi.ui.theme.ChlorodachiTheme

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            )
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            stringResource(R.string.dashboard_screen_title),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_notifications),
                                contentDescription = stringResource(R.string.app_bar_actions_notifications)
                            )
                        };
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = stringResource(R.string.app_bar_actions_settings)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            DashboardContents(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun DashboardContents(
    modifier: Modifier = Modifier
) {
    val zones = remember { Datasource().loadDemoZones() }

    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        item {
            GreenhouseMapSection(
                {},
                zones = zones
            )
        }
        item {
            ZonesHeader(
                onSeeAllClick = {}
            )
        }
        items(zones) { zone ->
            ZoneCard(
                zone = zone,
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
    onSeeAllClick: () -> Unit,
    zones: List<Zone>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SectionHeader(
            title = stringResource(R.string.dashboard_screen_greenhouse_map_section_title),
            onSeeAllClick = onSeeAllClick,
            modifier = modifier.padding(horizontal = 8.dp)
        )
        ZonesGrid(
            zones = zones,
            onZoneClick = {}
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
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        if (onSeeAllClick != null) {
            IconButton(onClick = onSeeAllClick) {
                Icon(
                    painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = title
                )
            }
        }
    }
}

@Composable
fun ZoneCard(zone: Zone, modifier: Modifier = Modifier) {
    val noData = stringResource(R.string.no_data)
    val zoneInfo = zone.zoneInformation

    val soilStatus = if (zoneInfo?.moisture != null && zoneInfo.ranges != null) {
        when (zoneInfo.moisture) {
            in zoneInfo.ranges.dryMoisture -> stringResource(R.string.soil_status_dry)
            in zoneInfo.ranges.wetMoisture -> stringResource(R.string.soil_status_wet)
            in zoneInfo.ranges.moistMoisture -> stringResource(R.string.soil_status_moist)
            else -> stringResource(R.string.no_data)
        }
    } else noData

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    zone.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    stringResource(
                        R.string.dashboard_screen_zones_section_zone_info,
                        zoneInfo?.temperature?.let { "%.1f°C".format(it) } ?: noData,
                        zoneInfo?.humidity?.let { "%.0f%%".format(it) } ?: noData,
                        zoneInfo?.illuminance?.let { "%.0f lx".format(it) } ?: noData,
                        soilStatus
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = zone.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun ZonesGrid(
    zones: List<Zone>,
    onZoneClick: (Zone) -> Unit,
    modifier: Modifier = Modifier,
    outerPadding: Dp = 16.dp,
    gap: Dp = 8.dp
) {
    if (zones.isEmpty()) return

    val columns = zones.maxOf { (it.x + it.w - 1).toInt() }
    val rows = zones.maxOf { (it.y + it.h - 1).toInt() }

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
                val col = zone.x.toInt() - 1
                val row = zone.y.toInt() - 1
                val w = zone.w.toInt()
                val h = zone.h.toInt()

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

@Composable
fun ZoneTile(
    zone: Zone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    needsAttention: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
    ) {
        Image(
            painterResource(R.drawable.ic_launcher_background),
            contentDescription = zone.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (needsAttention) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, MaterialTheme.colorScheme.error)
                        )
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    ChlorodachiTheme {
        DashboardScreen()
    }
}