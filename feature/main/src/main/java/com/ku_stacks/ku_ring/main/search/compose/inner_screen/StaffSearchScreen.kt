package com.ku_stacks.ku_ring.main.search.compose.inner_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.search.compose.SearchState
import com.ku_stacks.ku_ring.main.search.compose.component.EmptyResultScreen
import com.ku_stacks.ku_ring.main.search.compose.component.StaffItem

@Composable
fun StaffSearchScreen(
    searchState: SearchState,
    staffList: List<Staff>,
    modifier: Modifier = Modifier,
) {

    if (searchState.isLoading) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.kus_green),
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    } else if (staffList.isEmpty()) {
        EmptyResultScreen()
    } else {
        LazyColumn(modifier) {
            items(staffList) {
                StaffItem(
                    staff = it,
                )
            }
        }
    }
}
