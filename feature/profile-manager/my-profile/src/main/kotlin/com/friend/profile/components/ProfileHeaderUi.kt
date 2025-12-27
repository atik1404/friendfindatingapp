package com.friend.profile.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.friend.common.utils.ImageUtils.convertToFile
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.ImageSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.AppTextButton
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightPreview
import timber.log.Timber
import java.io.File
import com.friend.designsystem.R as Res

@Composable
fun ProfileHeaderUi(
    modifier: Modifier = Modifier,
    fullName: String,
    email: String,
    isImageLoading: Boolean,
    profilePicture: String,
    onEditClick: () -> Unit,
    onImageSelected: (File) -> Unit,
) {
    val picSize = ImageSizeToken.profilePictureLarge
    var isPickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val file = result.uriContent?.convertToFile(context)
            file?.let {
                onImageSelected.invoke(file)
            }?.run { Timber.e("Image picker error") }
        }else {
            Timber.e("Image picker error")
        }
    }

    ImagePickerBottomSheet(
        isVisible = isPickerVisible,
        onDismissRequest = { isPickerVisible = false },
        onImageSelected = {
            val options = CropImageContractOptions(
                cropImageOptions = CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON,
                    fixAspectRatio = true,
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                ),
                uri = it
            )
            cropLauncher.launch(options)
        },
        onError = {
            Timber.e("Image picker error")
        }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.backgroundColors.white,  // background
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer    // text/icon color
        )
    ) {
        Row(
            modifier = modifier
                .appPaddingOnly(
                    start = SpacingToken.extraSmall,
                    top = SpacingToken.medium,
                    bottom = SpacingToken.medium
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = modifier.size(picSize)
            ) {
                NetworkImageLoader(
                    url = profilePicture,
                    name = fullName,
                    isLoading = isImageLoading,
                    shape = CircleShape,
                    modifier = Modifier.matchParentSize()
                )

                // Overlay edit button at bottom-right (RTL-aware via BottomEnd)
                AppIconButton(
                    vectorIcon = Icons.Default.Edit,
                    tint = MaterialTheme.surfaceColors.grayLight,
                    onClick = {
                        isPickerVisible = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(IconSizeToken.medium) // tweak as you like
                        .clip(CircleShape)
                        .background(MaterialTheme.surfaceColors.primary)
                        .border(
                            width = StrokeTokens.hairline,
                            color = MaterialTheme.strokeColors.primary,
                            shape = CircleShape
                        )
                        .appPadding(SpacingToken.micro) // inner padding for icon touch target
                )
            }

            Spacer(modifier = modifier.width(SpacingToken.medium))

            Column(
                modifier = modifier.weight(1f)
            ) {
                AppText(
                    text = fullName,
                    textStyle = AppTypography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textColor = MaterialTheme.textColors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(SpacingToken.micro)
                )

                AppText(
                    text = email,
                    textStyle = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textColor = MaterialTheme.textColors.primary,
                    maxLines = 2
                )
            }

            AppTextButton(
                text = stringResource(Res.string.action_edit_profile),
                onClick = onEditClick,
                textColor = MaterialTheme.textColors.brand,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ProfileHeaderUi(
        fullName = "Tom Cruise",
        email = "tom@gmail.com",
        isImageLoading = true,
        profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
        onEditClick = {},
        onImageSelected = {}
    )
}
