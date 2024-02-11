package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.search.SearchViewModel
import com.ku_stacks.ku_ring.main.search.compose.components.SearchTextField

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    searchState: SearchState = rememberSearchState(),
) {

    SearchScreen(
        modifier = modifier,
        onNavigationClick = { viewModel.onCloseNavigationClick() },
        searchState = searchState,
    )
}

@Composable
private fun SearchScreen(
    searchState: SearchState,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(MaterialTheme.colors.surface)
    ) {
        CenterTitleTopBar(
            title = stringResource(id = R.string.search),
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor = MaterialTheme.colors.surface)
                )
            },
            onNavigationClick = { onNavigationClick() },
            action = ""
        )
        
        SearchTextField(
            value = searchState.query,
            onValueChange = { searchState.query = it },
            onClickClearButton = { searchState.query = TextFieldValue("") },
            modifier = Modifier.padding(top = 20.dp),
        )

    }
}

@Composable
fun rememberSearchState(
    query: String = "",
) : SearchState {
    return remember {
        SearchState(
            query = TextFieldValue(query),
            currentTab = "공지탭"
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SearchScreenPreview() {
    KuringTheme {
        SearchScreen(
            searchState = rememberSearchState("산학협력"),
            onNavigationClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
