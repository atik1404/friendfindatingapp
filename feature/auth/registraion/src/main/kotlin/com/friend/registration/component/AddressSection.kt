package com.friend.registration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AutoCompleteTextField

@Composable
fun AddressSection() {
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
