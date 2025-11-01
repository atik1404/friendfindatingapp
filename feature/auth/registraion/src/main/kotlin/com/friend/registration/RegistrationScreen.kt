package com.friend.registration

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.AppDatePickerDialog
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AutoCompleteTextField
import com.friend.ui.components.SingleChoiceSegmentsWithIcons
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onBackButtonClicked: () -> Unit
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_registration),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        var dateOfBirth by rememberSaveable { mutableStateOf("") }

        var showDatePicker by remember { mutableStateOf(false) }
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
            var email by rememberSaveable { mutableStateOf("") }

            NameField()

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = email,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_email),
                placeholder = stringResource(Res.string.hint_email),
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = email,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_password),
                placeholder = stringResource(Res.string.hint_password),
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            var segIndex by remember { mutableIntStateOf(0) }
            SingleChoiceSegmentsWithIcons(
                title = "Gender*",
                options = listOf(
                    Pair(Icons.Rounded.Male, "Male"),
                    Pair(Icons.Rounded.Female, "Female"),
                ),
                selectedIndex = segIndex.coerceIn(0, 1),
                onSelected = { segIndex = it }
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            SingleChoiceSegmentsWithIcons(
                title = "Interested In*",
                options = listOf(
                    Pair(Icons.Rounded.Male, "Male"),
                    Pair(Icons.Rounded.Female, "Female"),
                ),
                selectedIndex = 1,
                onSelected = { segIndex = it }
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = dateOfBirth,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_date_of_birth),
                placeholder = stringResource(Res.string.hint_dob),
                onValueChange = { dateOfBirth = it },
                isReadOnly = true,
                onClickListener = {
                    showDatePicker = true
                }
            )

            if (showDatePicker) {
                AppDatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    onConfirm = {
                        dateOfBirth = it
                        showDatePicker = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AddressField()

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            var checked by rememberSaveable { mutableStateOf(false) }

            AppCheckbox(
                checked = checked,
                onCheckedChange = { checked = it },
                label = stringResource(Res.string.msg_agree_with_term_condition)
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = checked,
                text = stringResource(Res.string.action_sign_up),
                onClick = {

                },
            )
        }
    }
}

@Composable
fun NameField() {
    var userName by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppOutlineTextField(
            text = userName,
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = { userName = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = Modifier.width(SpacingToken.medium))

        AppOutlineTextField(
            text = fullName,
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.label_full_name),
            placeholder = stringResource(Res.string.hint_full_name),
            onValueChange = { fullName = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
        )
    }
}

@Composable
fun AddressField() {
    var zipCode by rememberSaveable { mutableStateOf("") }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_country),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_state),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row {
            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_city),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AppOutlineTextField(
                text = zipCode,
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.label_zip_code),
                placeholder = stringResource(Res.string.hint_zip_code),
                onValueChange = { zipCode = it },
            )
        }
    }
}


@Composable
@LightDarkPreview
private fun ScreenPreview() {
    RegistrationScreen {

    }
}