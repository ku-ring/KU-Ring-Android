package com.ku_stacks.ku_ring.club.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ku_stacks.ku_ring.club.R
import com.ku_stacks.ku_ring.club.onboarding.components.ClubCategoryItem
import com.ku_stacks.ku_ring.club.onboarding.components.ClubCategoryItemState
import com.ku_stacks.ku_ring.club.onboarding.components.item
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
fun ClubOnboardingScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClubOnboardingViewModel = hiltViewModel(),
) {
    ClubOnboardingScreen(
        saveSelectedCategory = {
            if (viewModel.saveInitialCategory()) {
                onClose()
            }
        },
        onDismiss = onClose,
        categoryItems = viewModel.onboardingItems,
        selectedItemIndex = viewModel.selectedItemIndex,
        selectItem = viewModel::setSelectedItem,
        isSelectedItemIndexValid = viewModel::isSelectedItemIndexValid,
        modifier = modifier,
    )
}

@Composable
private fun ClubOnboardingScreen(
    saveSelectedCategory: () -> Unit,
    onDismiss: () -> Unit,
    categoryItems: List<ClubCategoryItemState>,
    selectedItemIndex: Int,
    selectItem: (Int) -> Unit,
    isSelectedItemIndexValid: (Int) -> Boolean,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterTitleTopBar(title = stringResource(R.string.club_onboarding_topbar_title))
        },
        containerColor = KuringTheme.colors.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp, bottom = 20.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ClubOnboardingTitle()
                Spacer(modifier = Modifier.height(32.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    categoryItems.forEachIndexed { index, item ->
                        ClubCategoryItem(
                            item = item,
                            selected = (selectedItemIndex == index),
                            onClick = { selectItem(index) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ClubOnboardingActions(
                onDismiss = onDismiss,
                isSelectedItemIndexValid = isSelectedItemIndexValid,
                selectedItemIndex = selectedItemIndex,
                saveSelectedCategory = saveSelectedCategory,
            )
        }
    }
}

@Composable
private fun ClubOnboardingTitle() {
    Text(
        text = stringResource(R.string.club_onboarding_title),
        style = KuringTheme.typography.title1,
        color = KuringTheme.colors.textTitle,
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = stringResource(R.string.club_onboarding_description),
        style = KuringTheme.typography.body1,
        color = KuringTheme.colors.textCaption1,
    )
}

@Composable
private fun ClubOnboardingActions(
    onDismiss: () -> Unit,
    isSelectedItemIndexValid: (Int) -> Boolean,
    selectedItemIndex: Int,
    saveSelectedCategory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KuringCallToAction(
            text = stringResource(R.string.club_onboarding_cta),
            onClick = {
                if (isSelectedItemIndexValid(selectedItemIndex)) {
                    saveSelectedCategory()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(R.string.club_onboarding_dismiss),
            style = KuringTheme.typography.body2SB,
            color = KuringTheme.colors.textCaption1,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(onClick = onDismiss),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubOnboardingScreenPreview() {
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val items = (0..3).map { item }
    KuringTheme {
        ClubOnboardingScreen(
            saveSelectedCategory = {},
            onDismiss = {},
            categoryItems = items,
            selectedItemIndex = selectedItemIndex,
            selectItem = { selectedItemIndex = it },
            isSelectedItemIndexValid = { it in items.indices },
        )
    }
}