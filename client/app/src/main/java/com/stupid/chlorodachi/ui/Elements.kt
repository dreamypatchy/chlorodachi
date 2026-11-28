package com.stupid.chlorodachi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.stupid.chlorodachi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    title: String,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(
                onClick = onNotificationsClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_notifications),
                    contentDescription = stringResource(R.string.app_bar_actions_notifications)
                )
            }
            IconButton(
                onClick = onSettingsClick
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
fun ZoneCard(
    zone: ZoneUiModel,
    zoneInfoType: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val noData = stringResource(R.string.no_data_short)
    val soilText = when (zone.soilStatus) {
        SoilStatus.DRY -> stringResource(R.string.soil_status_dry)
        SoilStatus.MOIST -> stringResource(R.string.soil_status_moist)
        SoilStatus.WET -> stringResource(R.string.soil_status_wet)
        SoilStatus.UNKNOWN -> noData
    }

    val cardModifier = modifier
        .fillMaxWidth()
        .height(100.dp)
    val content: @Composable ColumnScope.() -> Unit = {
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
                when (zoneInfoType) {
                    "measurements" ->
                        Text(
                            stringResource(
                                R.string.dashboard_screen_zones_section_zone_info,
                                zone.temperature?.let { "%.1f°C".format(it) } ?: noData,
                                zone.humidity?.let { "%.0f%%".format(it) } ?: noData,
                                zone.illuminance?.let { "%.0f lx".format(it) } ?: noData,
                                soilText
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    "coordinates" ->
                        Text(
                            stringResource(
                                R.string.zones_screen_zone_info,
                                zone.gridX,
                                zone.gridY,
                                zone.gridWidth,
                                zone.gridHeight
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    else ->
                        Text(
                            stringResource(R.string.no_data_long),
                            style = MaterialTheme.typography.bodyMedium
                        )
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = zone.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        }
    }

    if (onClick != null) {
        OutlinedCard(onClick = onClick, modifier = cardModifier, content = content)
    } else {
        OutlinedCard(modifier = cardModifier, content = content)
    }
}

@Composable
fun ZoneTile(
    zone: ZoneUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
        if (zone.needsAttention) {
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