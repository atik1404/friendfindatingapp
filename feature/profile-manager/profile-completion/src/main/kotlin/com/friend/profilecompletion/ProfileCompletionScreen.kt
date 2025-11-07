package com.friend.profilecompletion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppChipMultiWithTitle
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AutoCompleteTextField
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompletionScreen(
    onBackButtonClicked: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var aboutYou by rememberSaveable { mutableStateOf("") }
    var lookingFor by rememberSaveable { mutableStateOf("") }

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_personal_information),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()              // from Scaffold
                .padding(padding)
                .consumeWindowInsets(padding)    // prevent double-inset consumption downstream
                .navigationBarsPadding()         // keep content above system nav bar
                .imePadding()                    // lift content when keyboard shows
                .verticalScroll(rememberScrollState()) // simple, contents are small; LazyColumn not necessary
                .appPadding(SpacingToken.small),
        ) {
            PersonalDetails()

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_title),
                placeholder = stringResource(Res.string.hint_title),
                onValueChange = { title = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = aboutYou,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                title = stringResource(Res.string.label_about_yourself),
                placeholder = stringResource(Res.string.hint_about_you),
                onValueChange = { aboutYou = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = lookingFor,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                title = stringResource(Res.string.label_whats_up),
                placeholder = stringResource(Res.string.hint_whats_up),
                onValueChange = { lookingFor = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = false,
                ),
            )

            val interests = listOf("Computer", "Music", "Nature", "Adventures", "Sports", "Movies", "Chat")
            var multi by remember { mutableStateOf(emptySet<Int>()) }
            Spacer(modifier = Modifier.height(SpacingToken.medium))
            AppChipMultiWithTitle(
                title = stringResource(Res.string.label_interest),
                items = interests,
                selected = multi,
                onSelectionChange = { multi = it }
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_save),
                onClick = {

                },
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
@LightDarkPreview
private fun ScreenPreview() {
    ProfileCompletionScreen{

    }
}