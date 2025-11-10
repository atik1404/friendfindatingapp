package com.friend.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.ShowBottomSheet
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppText
import com.friend.ui.components.AutoCompleteTextField
import com.friend.ui.components.SingleChoiceSegmentsWithIcons
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun FilterUserBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onSearchApply: () -> Unit
) {
    ShowBottomSheet(
        heightRatio = .9f,
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_individual_search),
        titleColor = MaterialTheme.textColors.error
    ) {
        FilterUi(modifier = modifier) {
            onSearchApply.invoke()
        }
    }
}

@Composable
private fun FilterUi(
    modifier: Modifier = Modifier,
    onSearchApply: () -> Unit
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var selectedValue by rememberSaveable { mutableStateOf("") }

    val items = listOf(
        "Item1",
        "Item2",
        "Item3",
        "Item4",
        "Item5",
        "Item6",
        "Item7",
        "Item8"
    )
    Column(
        modifier = modifier
            .appPadding(SpacingToken.medium)
            .verticalScroll(rememberScrollState())
    ) {
        AppOutlineTextField(
            text = userName,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = { userName = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(Modifier.height(SpacingToken.medium))

        AppText(
            text = stringResource(Res.string.label_age_range),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier,
            textColor = MaterialTheme.textColors.secondary
        )
        Spacer(Modifier.height(SpacingToken.micro))

        AgeRangeUi()

        Spacer(Modifier.height(SpacingToken.medium))

        var segIndex by remember { mutableIntStateOf(0) }
        SingleChoiceSegmentsWithIcons(
            title = stringResource(Res.string.label_am_looking_for),
            options = listOf(
                Pair(Icons.Rounded.Male, "Male"),
                Pair(Icons.Rounded.Female, "Female"),
            ),
            selectedIndex = segIndex.coerceIn(0, 1),
            onSelected = { segIndex = it }
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        SingleChoiceSegmentsWithIcons(
            title = stringResource(Res.string.label_seeking_for),
            options = listOf(
                Pair(Icons.Rounded.Male, "Male"),
                Pair(Icons.Rounded.Female, "Female"),
            ),
            selectedIndex = segIndex.coerceIn(0, 1),
            onSelected = { segIndex = it }
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AddressField()

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_body_type),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_looking_for),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }
        Spacer(Modifier.height(SpacingToken.medium))
        PersonalDetails()
        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = stringResource(Res.string.msg_online_user_only)
        )

        Spacer(modifier = Modifier.height(SpacingToken.extraSmall))

        AppCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = stringResource(Res.string.msg_photo_required)
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.action_search),
            onClick = onSearchApply,
        )
    }
}

@Composable
private fun AgeRangeUi(modifier: Modifier = Modifier) {
    var minAge by rememberSaveable { mutableStateOf("") }
    var maxAge by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBaseTextField(
            value = minAge,
            modifier = modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_min),
            onValueChange = { minAge = it },
            shape = RoundedCornerShape(RadiusToken.large),
            maxLength = 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
        )

        AppText(
            text = stringResource(Res.string.hint_to),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = modifier.appPaddingHorizontal(SpacingToken.medium),
            textColor = MaterialTheme.textColors.secondary
        )

        AppBaseTextField(
            value = maxAge,
            modifier = modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_max),
            shape = RoundedCornerShape(RadiusToken.large),
            maxLength = 3,
            onValueChange = { maxAge = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
        )
    }
}

@Composable
private fun AddressField() {
    var text by remember { mutableStateOf("") }

    val cities = listOf(
        "Dhaka",
        "Chattogram",
        "Rajshahi",
        "Khulna",
        "Sylhet",
        "Barishal",
        "Rangpur",
        "Mymensingh"
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AutoCompleteTextField(
            allOptions = cities,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.label_country),
            placeholder = stringResource(Res.string.hint_select_item),
            onValueChange = { text = it },
            value = text
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row {
            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_state),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_city),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )
        }
    }
}

@Composable
private fun PersonalDetails() {
    var selectedValue by rememberSaveable { mutableStateOf("") }

    val items = listOf(
        "Item1",
        "Item2",
        "Item3",
        "Item4",
        "Item5",
        "Item6",
        "Item7",
        "Item8"
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_height),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_weight),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }

        Spacer(Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_eyes),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_hair),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }

        Spacer(Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_smoking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_drinking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }
    }
}


@Composable
@LightPreview
private fun ScreenPreview() {
    FilterUi {

    }
}