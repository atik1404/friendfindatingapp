package com.friend.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.Gender
import com.friend.ui.components.SingleChoiceSegmentsWithIcons
import com.friend.designsystem.R as Res

val genders = listOf(
    Pair(Icons.Rounded.Male, Gender.MALE.name),
    Pair(Icons.Rounded.Female, Gender.FEMALE.name),
)

@Composable
fun GenderSelection(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.label_gender),
    selectedValue: String,
    onSelected: (Gender) -> Unit,
) {
    val selectedIndex = genders.indexOfFirst { it.second == selectedValue }

    SingleChoiceSegmentsWithIcons(
        modifier = modifier,
        title = title,
        options = genders,
        selectedIndex = selectedIndex,
        onSelected = {
            onSelected.invoke(Gender.toEnum(genders[it].second))
        }
    )
}