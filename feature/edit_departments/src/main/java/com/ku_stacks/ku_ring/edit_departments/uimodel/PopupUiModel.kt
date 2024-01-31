package com.ku_stacks.ku_ring.edit_departments.uimodel

import androidx.annotation.StringRes
import com.ku_stacks.ku_ring.edit_departments.R

/**
 * 학과 선택 화면에서 보여줄 팝업 데이터를 wrap하는 클래스이다.
 *
 * @param departmentKoreanName 팝업에서 보여줄 학과의 한국어 이름이다.
 * @param stringResId 팝업에서 보여줄 문자열의 resource id이다.
 */
sealed class PopupUiModel(
    val departmentName: String,
    val departmentKoreanName: String,
    @StringRes val stringResId: Int,
) {
    class AddPopupUiModel(departmentName: String, departmentKoreanName: String) :
        PopupUiModel(departmentName, departmentKoreanName, R.string.add_department_popup_title)

    class DeletePopupUiModel(departmentName: String, departmentKoreanName: String) :
        PopupUiModel(departmentName, departmentKoreanName, R.string.delete_department_popup_title)

    class DeleteAllPopupUiModel : PopupUiModel("", "", R.string.delete_all_department_popup_title)
}