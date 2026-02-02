package com.friend.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import com.friend.ui.components.AutoCompleteTextField
import com.friend.designsystem.R as Res

@Composable
fun AddressSection(
    selectedCountry: String,
    selectedState: String,
    selectedCity: String,
    countries: List<CountryApiEntity>,
    states: List<StateApiEntity>,
    cities: List<CityApiEntity>,
    onCountryChange: (CountryApiEntity) -> Unit,
    onStateChange: (StateApiEntity) -> Unit,
    onCityChange: (CityApiEntity) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AutoCompleteTextField(
            allOptions = countries,
            modifier = modifier.fillMaxWidth(),
            label = stringResource(Res.string.label_country),
            placeholder = stringResource(Res.string.hint_select_item),
            value = selectedCountry,
            onOptionSelected = {
                onCountryChange.invoke(it)
            }
        )

        Spacer(modifier = modifier.height(SpacingToken.medium))

        Row {
            AutoCompleteTextField(
                allOptions = states,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_state),
                placeholder = stringResource(Res.string.hint_select_item),
                value = selectedState,
                onOptionSelected = {
                    onStateChange.invoke(it)
                }
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = cities,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_city),
                placeholder = stringResource(Res.string.hint_select_item),
                onOptionSelected = {
                    onCityChange.invoke(it)
                },
                value = selectedCity
            )
        }
    }
}