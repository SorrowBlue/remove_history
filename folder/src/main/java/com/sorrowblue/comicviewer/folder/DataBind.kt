package com.sorrowblue.comicviewer.folder

import android.text.Spanned
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.google.android.material.search.SearchView
import com.sorrowblue.comicviewer.folder.databinding.FolderViewSearchBinding
import com.sorrowblue.comicviewer.framework.ui.flow.launchInWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import logcat.logcat

context(Fragment)
internal fun FolderViewSearchBinding.setViewModel(viewModel: FolderViewModel) {
    folderSearchGroupPeriod.checkedChipTwoWayBinding(
        viewModel.searchPeriodFlow,
        { viewModel.searchPeriodFlow.value = it },
        Converter::searchPeriodToButtonId,
        Converter::buttonIdToSearchPeriod
    )

    folderSearchGroupRange.checkedChipTwoWayBinding(
        viewModel.searchRangeFlow,
        { viewModel.searchRangeFlow.value = it },
        Converter::searchRangeToButtonId,
        Converter::buttonIdToSearchRange
    )

    folderSearchGroupSort.checkedChipTwoWayBinding(
        viewModel.searchSortTypeFlow,
        { viewModel.searchSortTypeFlow.value = it },
        Converter::searchSortToButtonId,
        { Converter.buttonIdToSearchSort(it, viewModel.searchSortTypeFlow.value.isAsc) }
    )

    folderSearchGroupOrder.checkedChipTwoWayBinding(
        viewModel.searchSortTypeFlow.map { it.isAsc },
        { viewModel.searchSortTypeFlow.value = viewModel.searchSortTypeFlow.value.copy2(it) },
        Converter::searchOrderToButtonId,
        Converter::buttonIdToSearchOrder
    )
}

context(Fragment)
private fun <T> ChipGroup.checkedChipTwoWayBinding(
    flow: Flow<T>,
    update: (T) -> Unit,
    toButtonId: (T) -> Int,
    toValue: (List<Int>) -> T
) {
    setOnCheckedStateChangeListener { _, checkedIds -> update(toValue(checkedIds)) }
    flow.onEach { check(toButtonId(it)) }.launchInWithLifecycle()
}

@BindingAdapter("android:text")
fun SearchView.setEditTextForBinding(text: CharSequence?) {
    val oldText = editText.text
    if ((text == oldText || text == null) && oldText.isEmpty()) {
        return
    }
    if (text is Spanned) {
        if (text == oldText) {
            return
        }
    } else if (!haveContentsChanged(text, oldText)) {
        return
    }
    editText.setText(text)
}

private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 == null != (str2 == null)) {
        return true
    } else if (str1 == null) {
        return false
    }
    val length = str1.length
    if (length != str2!!.length) {
        return true
    }
    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}

@InverseBindingAdapter(attribute = "android:text")
fun SearchView.getEditTextForBinding(): String? {
    return editText.text?.toString()
}

@BindingAdapter("android:textAttrChanged")
fun SearchView.setEditTextWatcherForBinding(textAttrChanged: InverseBindingListener) {
    editText.doOnTextChanged { a, _, _, _ ->
        logcat { "textAttrChanged=$a" }
        textAttrChanged.onChange()
    }
}
