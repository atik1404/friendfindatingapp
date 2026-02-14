package com.friend.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.backgroundColors
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun ProfileInformationSection(
    data: ProfileApiEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.backgroundColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .appPadding(SpacingToken.medium)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_date_of_birth),
                value = DateTimeParser.parseToPattern(
                    data.birthdate,
                    DateTimePatterns.MDY_TEXT_COMMA
                ),
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_gender),
                value = data.gender,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_language),
                value = "English",
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_interested_in),
                value = data.interestedIn,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_height),
                value = data.height,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_weight),
                value = data.weight,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_eyes),
                value = data.eyes,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_hair),
                value = data.hair,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_smoking),
                value = data.smoking,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_drinking),
                value = data.drinking,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_body_type),
                value = data.bodyType,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_looking_for),
                value = data.lookingFor,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End,
                maxLines = 5
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_country),
                value = data.country,
                modifier = Modifier.weight(1f),
                maxLines = 3
            )

            LabeledValue(
                title = stringResource(Res.string.label_city),
                value = data.city,
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterHorizontally,
                maxLines = 3
            )

            LabeledValue(
                title = stringResource(Res.string.label_state),
                value = data.state,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End,
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_title),
            value = data.title,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_about_yourself),
            value = data.aboutYou,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_interest),
            value = data.interests.replace(":", ", "),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_whats_up),
            value = data.whatsUp,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
    }
}

@LightPreview
@Composable
private fun ScreenPreview() {
    ProfileInformationSection(
        data = ProfileApiEntity(
            userName = "Tom Cruise",
            fullName = "Tom Cruise",
            gender = "Male",
            birthdate = "September 29, 1995",
            email = "tom@gmail.com",
            interestedIn = "Female",
            country = "Bangladesh",
            state = "Dhaka",
            city = "Dhaka",
            zipCode = "",
            profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-",
            bodyType = "",
            drinking = "",
            eyes = "",
            hair = "",
            height = "",
            interests = "",
            lookingFor = "",
            smoking = "",
            aboutYou = "",
            title = "",
            weight = "",
            whatsUp = "",
            isProfileComplete = false
        )
    )
}