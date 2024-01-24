package com.ku_stacks.ku_ring.edit_departments.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LargeTopAppBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.SearchTextField
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.R

@Composable
fun EditDepartments(
    onClose: () -> Unit,
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        EditDepartmentsTitle(
            onClose = onClose,
            modifier = Modifier.fillMaxWidth(),
        )
        EditDepartmentsContents(
            query = query,
            onQueryUpdate = onQueryUpdate,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun EditDepartmentsTitle(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LargeTopAppBar(
        title = stringResource(id = R.string.app_bar_title),
        navigationIconId = R.drawable.ic_back,
        onNavigationIconClick = onClose,
        modifier = modifier,
    )
}

@Composable
private fun EditDepartmentsContents(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        SearchTextField(
            query = query,
            onQueryUpdate = onQueryUpdate,
            placeholderText = stringResource(id = R.string.search_placeholder),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun EditDepartmentsPreview() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        EditDepartments(
            onClose = {},
            query = query,
            onQueryUpdate = { query = it },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        )
    }
}