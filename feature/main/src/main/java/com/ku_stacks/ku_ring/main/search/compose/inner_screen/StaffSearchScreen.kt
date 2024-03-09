package com.ku_stacks.ku_ring.main.search.compose.inner_screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.search.compose.SearchState
import com.ku_stacks.ku_ring.main.search.compose.component.EmptyResultScreen
import com.ku_stacks.ku_ring.main.search.compose.component.StaffItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        StaffResultScreen(
            staffList = staffList,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StaffResultScreen(
    staffList: List<Staff>,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 250),
    )
    val staffDetailInfo = remember { mutableStateOf<Staff?>(null) }

    ModalBottomSheetLayout(
        sheetContent = {
            staffDetailInfo.value?.let {
                StaffDetailBottomSheet(staff = it)
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        LazyColumn(modifier) {
            items(staffList) {
                StaffItem(
                    staff = it,
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                staffDetailInfo.value = it
                                sheetState.show()
                            }
                        }
                )
            }
        }
    }
}
