package com.ku_stacks.ku_ring.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

@Composable
internal fun MainScreenNavigationBar(
    currentRoute: MainScreenRoute,
    onNavigationItemClick: (MainScreenRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.background(KuringTheme.colors.background)) {
        MainScreenRoute.entries.forEach { route ->
            val navigationItem = MainScreenNavigationBarItem.get(route)
            val label = stringResource(id = navigationItem.labelId)

            MainScreenNavigationItem(
                item = navigationItem,
                isSelected = route == currentRoute,
                modifier = Modifier.weight(1f)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(
                            id = R.string.main_bottom_navigation_item,
                            label
                        ),
                        onClick = { onNavigationItemClick(route) },
                    ).padding(vertical = 11.dp),
            )
        }
    }
}

@Composable
private fun MainScreenNavigationItem(
    item: MainScreenNavigationBarItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val label = stringResource(id = item.labelId)
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) KuringTheme.colors.gray600 else KuringTheme.colors.textCaption1,
        label = "MainScreenNavigationItem labelColor",
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Crossfade(
            targetState = isSelected,
            label = "$label icon",
        ) { selected ->
            Icon(
                imageVector = ImageVector.vectorResource(id = if (selected) item.selectedIconId else item.unselectedIconId),
                contentDescription = null,
                tint = contentColor,
            )
        }
        Text(
            text = label,
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 16.3.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = contentColor,
            )
        )
    }
}

data class MainScreenNavigationBarItem(
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unselectedIconId: Int,
    @StringRes val labelId: Int,
) {
    companion object {
        fun get(destination: MainScreenRoute): MainScreenNavigationBarItem {
            return when (destination) {
                MainScreenRoute.Notice -> MainScreenNavigationBarItem(
                    selectedIconId = R.drawable.ic_list_fill_v2,
                    unselectedIconId = R.drawable.ic_list_v2,
                    labelId = R.string.navigator_notice_screen,
                )

                MainScreenRoute.Archive -> MainScreenNavigationBarItem(
                    selectedIconId = R.drawable.ic_archive_fill_v2,
                    unselectedIconId = R.drawable.ic_archive_v2,
                    labelId = R.string.navigator_archive_screen,
                )

                MainScreenRoute.CampusMap -> MainScreenNavigationBarItem(
                    selectedIconId = R.drawable.ic_map_pin_fill_v2,
                    unselectedIconId = R.drawable.ic_map_pin_v2,
                    labelId = R.string.navigator_campus_map_screen,
                )

                MainScreenRoute.Settings -> MainScreenNavigationBarItem(
                    selectedIconId = R.drawable.ic_more_horizontal_fill_v2,
                    unselectedIconId = R.drawable.ic_more_horizontal_v2,
                    labelId = R.string.navigator_setting_screen,
                )
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun MainScreenNavigationBarPreview() {
    var selectedDestination: MainScreenRoute by remember {
        mutableStateOf(
            MainScreenRoute.Notice
        )
    }
    KuringTheme {
        MainScreenNavigationBar(
            currentRoute = selectedDestination,
            onNavigationItemClick = { selectedDestination = it },
            modifier = Modifier.background(KuringTheme.colors.background).fillMaxWidth(),
        )
    }
}