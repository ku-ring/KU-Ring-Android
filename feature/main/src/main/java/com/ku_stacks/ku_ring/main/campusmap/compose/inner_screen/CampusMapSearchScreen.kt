package com.ku_stacks.ku_ring.main.campusmap.compose.inner_screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.campusmap.CampusMapViewModel
import com.ku_stacks.ku_ring.main.campusmap.compose.preview.CampusMapPlacesPreviewParameterProvider
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.model.buildCampusMapSearchResults
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.navigation.KuringRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
internal data object CampusMapSearchDestination : KuringRoute

@Composable
internal fun CampusMapSearchRoute(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CampusMapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CampusMapSearchScreen(
        searchQuery = uiState.searchInput,
        recentSearches = uiState.recentSearches,
        searchResults = uiState.liveSearchResults,
        showEmptySearchResult = uiState.shouldShowEmptySearchResult,
        onQueryChange = viewModel::updateSearchInput,
        onQueryClear = viewModel::clearSearchInput,
        onNavigateUp = onNavigateUp,
        onSearchSubmit = {
            val shouldShowResultList = uiState.searchInput.isNotBlank() &&
                uiState.liveSearchResults.isNotEmpty()
            viewModel.submitSearch()
            if (shouldShowResultList) {
                onNavigateUp()
            }
        },
        onResultClick = { result ->
            viewModel.selectSearchPlace(result.place)
            onNavigateUp()
        },
        onRecentClick = { recentSearch ->
            viewModel.selectRecentSearch(recentSearch)
            onNavigateUp()
        },
        onRecentDelete = viewModel::deleteRecentSearch,
        onRecentClear = viewModel::clearRecentSearches,
        modifier = modifier,
    )
}

@Composable
internal fun CampusMapSearchScreen(
    searchQuery: String,
    recentSearches: ImmutableList<CampusMapRecentSearch>,
    searchResults: ImmutableList<CampusMapSearchResult>,
    showEmptySearchResult: Boolean,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
    onNavigateUp: () -> Unit,
    onSearchSubmit: () -> Unit,
    onResultClick: (CampusMapSearchResult) -> Unit,
    onRecentClick: (CampusMapRecentSearch) -> Unit,
    onRecentDelete: (CampusMapRecentSearch) -> Unit,
    onRecentClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        CampusMapSearchTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            query = searchQuery,
            onQueryChange = onQueryChange,
            onQueryClear = onQueryClear,
            onNavigateUp = onNavigateUp,
            onSearchSubmit = onSearchSubmit,
        )

        CampusMapSearchContent(
            modifier = Modifier.fillMaxSize(),
            query = searchQuery,
            recentSearches = recentSearches,
            searchResults = searchResults,
            showEmptySearchResult = showEmptySearchResult,
            onResultClick = onResultClick,
            onRecentClick = onRecentClick,
            onRecentDelete = onRecentDelete,
            onRecentClear = onRecentClear,
        )
    }
}

@Composable
private fun CampusMapSearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
    onNavigateUp: () -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 4.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onNavigateUp,
                ),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_v2),
                contentDescription = stringResource(id = R.string.campus_map_search_navigate_up),
                tint = KuringTheme.colors.gray600,
                modifier = Modifier.size(24.dp),
            )
        }

        CampusMapSearchTextField(
            query = query,
            onQueryChange = onQueryChange,
            onQueryClear = onQueryClear,
            onSearchSubmit = onSearchSubmit,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
        )
    }
}

@Composable
private fun CampusMapSearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = query,
                selection = TextRange(query.length),
            ),
        )
    }

    LaunchedEffect(query) {
        if (textFieldValue.text != query) {
            textFieldValue = TextFieldValue(
                text = query,
                selection = TextRange(query.length),
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { updatedValue ->
            textFieldValue = updatedValue
            if (updatedValue.text != query) {
                onQueryChange(updatedValue.text)
            }
        },
        singleLine = true,
        textStyle = KuringTheme.typography.body2.copy(
            color = KuringTheme.colors.textBody,
            fontWeight = FontWeight.Medium,
        ),
        cursorBrush = SolidColor(KuringTheme.colors.mainPrimary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearchSubmit()
            },
        ),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(KuringTheme.colors.gray100)
            .focusRequester(focusRequester)
            .padding(start = 16.dp, end = 12.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.weight(1f),
                ) {
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.campus_map_search_placeholder),
                            style = KuringTheme.typography.body2,
                            color = KuringTheme.colors.textCaption1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    innerTextField()
                }

                SearchFieldTrailingIcon(
                    hasQuery = textFieldValue.text.isNotEmpty(),
                    onQueryClear = onQueryClear,
                )
            }
        },
    )
}

@Composable
private fun SearchFieldTrailingIcon(
    hasQuery: Boolean,
    onQueryClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (hasQuery) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(32.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onQueryClear,
                ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_campus_map_search_clear),
                contentDescription = stringResource(id = R.string.campus_map_clear_search_query),
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
        }
    } else {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
            modifier = modifier.size(24.dp),
        )
    }
}

@Composable
private fun CampusMapSearchContent(
    query: String,
    recentSearches: ImmutableList<CampusMapRecentSearch>,
    searchResults: ImmutableList<CampusMapSearchResult>,
    showEmptySearchResult: Boolean,
    onResultClick: (CampusMapSearchResult) -> Unit,
    onRecentClick: (CampusMapRecentSearch) -> Unit,
    onRecentDelete: (CampusMapRecentSearch) -> Unit,
    onRecentClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val trimmedQuery = query.trim()

    if (trimmedQuery.isEmpty() && recentSearches.isEmpty()) {
        CampusMapSearchEmptyState(
            message = stringResource(id = R.string.campus_map_empty_recent_search),
            modifier = modifier,
        )
        return
    }

    if (showEmptySearchResult) {
        CampusMapSearchEmptyState(
            message = stringResource(id = R.string.campus_map_empty_search_result),
            topSpacing = 180.dp,
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
        modifier = modifier,
    ) {
        if (trimmedQuery.isEmpty()) {
            item(key = "recent-search-header") {
                RecentSearchHeader(
                    onRecentClear = onRecentClear,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            items(
                items = recentSearches,
                key = CampusMapRecentSearch::id,
            ) { recentSearch ->
                CampusMapSearchPlaceItem(
                    label = recentSearch.label,
                    iconRes = R.drawable.ic_campus_map_recent_search,
                    query = "",
                    showDelete = true,
                    onClick = { onRecentClick(recentSearch) },
                    onDelete = { onRecentDelete(recentSearch) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            items(
                items = searchResults,
                key = CampusMapSearchResult::id,
            ) { result ->
                CampusMapSearchPlaceItem(
                    label = result.title,
                    iconRes = if (result.isRecentMatch) {
                        R.drawable.ic_campus_map_recent_search
                    } else {
                        CampusMapCategory.iconRes(result.category)
                    },
                    query = trimmedQuery,
                    showDelete = false,
                    onClick = { onResultClick(result) },
                    onDelete = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun RecentSearchHeader(
    onRecentClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(44.dp)
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = stringResource(id = R.string.campus_map_recent_search_title),
            style = KuringTheme.typography.body2.copy(
                color = KuringTheme.colors.textBody,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.weight(1f),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(44.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onRecentClear,
                )
                .padding(horizontal = 4.dp),
        ) {
            Text(
                text = stringResource(id = R.string.delete_all_keyword_history),
                style = KuringTheme.typography.caption1,
                color = KuringTheme.colors.textCaption1,
            )
        }
    }
}

@Composable
private fun CampusMapSearchEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    topSpacing: Dp = 90.dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(topSpacing))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_campus_map_empty_search),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(150.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = KuringTheme.typography.body2,
            color = KuringTheme.colors.textCaption1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CampusMapSearchPlaceItem(
    label: String,
    @DrawableRes iconRes: Int,
    query: String,
    showDelete: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(44.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .padding(start = 28.dp, end = 24.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = KuringTheme.colors.gray100,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = KuringTheme.colors.gray400,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = highlightedPlaceName(
                name = label,
                query = query,
                highlightStyle = SpanStyle(color = KuringTheme.colors.mainPrimary),
            ),
            style = KuringTheme.typography.body2.copy(
                color = KuringTheme.colors.textBody,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        if (showDelete) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        role = Role.Button,
                        onClick = onDelete,
                    ),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_x_v2),
                    contentDescription = stringResource(id = R.string.campus_map_delete_recent_search),
                    tint = KuringTheme.colors.gray300,
                    modifier = Modifier.size(20.dp),
                )
            }
        } else {
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

private fun highlightedPlaceName(
    name: String,
    query: String,
    highlightStyle: SpanStyle,
) = buildAnnotatedString {
    append(name)

    val startIndex = name.indexOf(query, ignoreCase = true)
    if (query.isEmpty() || startIndex == -1) {
        return@buildAnnotatedString
    }

    val endIndex = startIndex + query.length
    addStyle(highlightStyle, startIndex, endIndex)
}

@LightAndDarkPreview
@Composable
private fun CampusMapSearchScreenPreview(
    @PreviewParameter(CampusMapPlacesPreviewParameterProvider::class)
    places: ImmutableList<Place>,
) {
    KuringTheme {
        CampusMapSearchScreen(
            searchQuery = "도서관",
            recentSearches = places
                .map(CampusMapRecentSearch::PlaceResult)
                .toImmutableList(),
            searchResults = buildCampusMapSearchResults(
                places = places,
                selectedCategory = null,
            ).toImmutableList(),
            showEmptySearchResult = false,
            onQueryChange = {},
            onQueryClear = {},
            onNavigateUp = {},
            onSearchSubmit = {},
            onResultClick = {},
            onRecentClick = {},
            onRecentDelete = {},
            onRecentClear = {},
        )
    }
}
