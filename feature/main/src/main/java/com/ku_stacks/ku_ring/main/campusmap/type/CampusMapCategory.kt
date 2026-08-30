package com.ku_stacks.ku_ring.main.campusmap.type

import androidx.annotation.DrawableRes
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.main.R

internal enum class CampusMapCategory(
    val apiName: String,
    val label: String,
    private val matchingNames: Set<String>,
    @get:DrawableRes val iconRes: Int,
) {
    CAFE(
        "cafe",
        "교내 카페",
        setOf("카페", "교내 카페"),
        R.drawable.ic_campus_map_icon_cafe,
    ),
    RESTAURANT(
        "restaurant",
        "식당",
        setOf("식당"),
        R.drawable.ic_campus_map_icon_restaurant,
    ),
    PRINT(
        "printer",
        "프린터",
        setOf("프린터", "프린터기", "프린트", "복사", "복사실"),
        R.drawable.ic_campus_map_icon_print,
    ),
    SMOKE(
        "smoking_booth",
        "흡연부스",
        setOf("흡연부스", "흡연"),
        R.drawable.ic_campus_map_icon_smoke,
    ),
    STORE(
        "convenience_store",
        "편의점",
        setOf("편의점", "매점"),
        R.drawable.ic_campus_map_icon_store,
    ),
    REST(
        "lounge",
        "휴게실",
        setOf("휴게실", "쉼터"),
        R.drawable.ic_campus_map_icon_rest,
    ),
    K_CUBE(
        "kcube",
        "K-CUBE",
        setOf("k-cube", "K-CUBE", "케이큐브"),
        R.drawable.ic_campus_map_icon_kcube,
    ),
    BANK_ATM(
        "bank_atm",
        "은행·ATM",
        setOf("은행", "ATM", "은행/ATM", "은행·ATM"),
        R.drawable.ic_campus_map_icon_atm,
    ),
    POST_OFFICE(
        "post_office",
        "우편",
        setOf("우편", "우편취급국", "우체국"),
        R.drawable.ic_campus_map_icon_postoffice,
    ),
    RESTROOM(
        "restroom",
        "화장실",
        setOf("화장실"),
        R.drawable.ic_campus_map_icon_restroom,
    ),
    LIBRARY(
        "library",
        "도서관",
        setOf("도서관", "열람실"),
        R.drawable.ic_campus_map_icon_library,
    ),
    PARKING(
        "parking",
        "주차장",
        setOf("주차장"),
        R.drawable.ic_campus_map_icon_parking,
    ),
    CULTURAL_FACILITY(
        "cultural_facility",
        "문화시설",
        setOf("문화시설"),
        R.drawable.ic_campus_map_icon_culture,
    ),
    WELFARE_STORE(
        "welfare_store",
        "복지매장",
        setOf("복지매장"),
        R.drawable.ic_campus_map_icon_welfare,
    ),
    GENERAL(
        "general",
        "일반",
        setOf("일반"),
        R.drawable.ic_campus_map_icon_general,
    );

    fun matches(category: String): Boolean =
        apiName.equals(category, ignoreCase = true) ||
            matchingNames.any { it.equals(category, ignoreCase = true) }

    companion object {
        fun fromApiName(apiName: String): CampusMapCategory? =
            entries.firstOrNull { it.apiName.equals(apiName, ignoreCase = true) }

        @DrawableRes
        fun iconRes(
            category: String,
            facilityName: String? = null,
        ): Int {
            if (CULTURAL_FACILITY.matches(category) && facilityName.isKuCinema()) {
                return R.drawable.ic_campus_map_icon_cinema
            }

            return entries
                .firstOrNull { it.matches(category) }
                ?.iconRes
                ?: R.drawable.ic_campus_map_icon_building
        }

        private fun String?.isKuCinema(): Boolean =
            this?.contains("KU시네마", ignoreCase = true) == true ||
                this?.contains("KU Cinema", ignoreCase = true) == true
    }
}

internal data class CampusMapCategoryItem(
    val category: CampusMapCategory,
    val label: String,
)

internal fun List<PlaceCategory>.toCampusMapCategoryItems(): List<CampusMapCategoryItem> =
    sortedBy(PlaceCategory::displayOrder)
        .mapNotNull { placeCategory ->
            CampusMapCategory.fromApiName(placeCategory.name)?.let { category ->
                CampusMapCategoryItem(
                    category = category,
                    label = placeCategory.korName.ifBlank { category.label },
                )
            }
        }
