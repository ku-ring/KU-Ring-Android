package com.ku_stacks.ku_ring.club.onboarding


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.club.R
import com.ku_stacks.ku_ring.club.onboarding.components.ClubCategoryItemState
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

@HiltViewModel
class ClubOnboardingViewModel @Inject constructor(
    private val preferences: PreferenceUtil,
) : ViewModel() {

    var selectedItemIndex by mutableIntStateOf(-1)
        private set

    var onboardingItems by mutableStateOf(initOnboardingItems())
        private set

    fun saveInitialCategory(): Boolean {
        return if (selectedItemIndex in onboardingItems.indices) {
            preferences.clubInitialCategory = onboardingItems[selectedItemIndex].categoryId
            true
        } else {
            false
        }
    }

    fun setSelectedItem(index: Int) {
        if (index != selectedItemIndex && index in onboardingItems.indices) {
            selectedItemIndex = index
        }
    }

    fun isSelectedItemIndexValid(index: Int): Boolean {
        return index in onboardingItems.indices
    }

    private fun initOnboardingItems(): ImmutableList<ClubCategoryItemState> {
        return persistentListOf(
            ClubCategoryItemState(
                categoryName = R.string.category_academic,
                description = R.string.description_academic,
                categoryId = "academic",
                iconId = R.drawable.ic_academic_v2,
            ),
            ClubCategoryItemState(
                categoryName = R.string.category_culture_art,
                description = R.string.description_culture_art,
                categoryId = "culture_art",
                // TODO: 아이콘 새로 만들어지면 바꾸기
                iconId = R.drawable.ic_academic_v2,
            ),
            ClubCategoryItemState(
                categoryName = R.string.category_social_value,
                description = R.string.description_social_value,
                categoryId = "social_value",
                iconId = R.drawable.ic_academic_v2,
            ),
            ClubCategoryItemState(
                categoryName = R.string.category_activity,
                description = R.string.description_activity,
                categoryId = "activity",
                iconId = R.drawable.ic_academic_v2,
            ),
        )
    }
}