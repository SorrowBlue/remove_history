package com.sorrowblue.comicviewer.feature.settings.security

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.sorrowblue.comicviewer.feature.settings.common.Setting
import com.sorrowblue.comicviewer.feature.settings.common.SettingsDetailNavigator
import com.sorrowblue.comicviewer.feature.settings.common.SettingsDetailPane
import com.sorrowblue.comicviewer.feature.settings.common.SwitchSetting
import com.sorrowblue.comicviewer.feature.settings.security.section.BiometricsDialog
import com.sorrowblue.comicviewer.framework.ui.DialogController
import com.sorrowblue.comicviewer.framework.ui.LifecycleResumeEffect

interface SecuritySettingsScreenNavigator : SettingsDetailNavigator {

    fun navigateToChangeAuth(enabled: Boolean)
    fun navigateToPasswordChange()
}

@Destination<ExternalModuleGraph>
@Composable
internal fun SecuritySettingsScreen(
    contentPadding: PaddingValues,
    navigator: SecuritySettingsScreenNavigator,
) {
    SecuritySettingsScreen(
        contentPadding = contentPadding,
        onBackClick = navigator::navigateBack,
        onChangeAuthEnabled = navigator::navigateToChangeAuth,
        onPasswordChangeClick = navigator::navigateToPasswordChange
    )
}

@Composable
private fun SecuritySettingsScreen(
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    onChangeAuthEnabled: (Boolean) -> Unit,
    onPasswordChangeClick: () -> Unit,
    state: SecuritySettingsScreenState = rememberSecuritySettingsScreenState(),
) {
    val uiState = state.uiState

    SecuritySettingsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onChangeAuthEnabled = onChangeAuthEnabled,
        onPasswordChangeClick = onPasswordChangeClick,
        onChangeBiometricEnabled = state::onChangeBiometricEnabled,
        onChangeBackgroundLockEnabled = state::onChangeBackgroundLockEnabled,
        contentPadding = contentPadding
    )

    if (uiState.isBiometricsDialogShow) {
        BiometricsDialog(
            onConfirmClick = state::onBiometricsDialogClick,
            onDismissRequest = state::onBiometricsDialogDismissRequest
        )
    }

    LifecycleResumeEffect {
        state.onResume()
    }
}

internal class BiometricsDialogController : DialogController<Unit>(Unit)

internal data class SecuritySettingsScreenUiState(
    val isAuthEnabled: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isBackgroundLockEnabled: Boolean = false,
    val isBiometricsDialogShow: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecuritySettingsScreen(
    uiState: SecuritySettingsScreenUiState,
    onBackClick: () -> Unit,
    onChangeAuthEnabled: (Boolean) -> Unit,
    onPasswordChangeClick: () -> Unit,
    onChangeBiometricEnabled: (Boolean) -> Unit,
    onChangeBackgroundLockEnabled: (Boolean) -> Unit,
    contentPadding: PaddingValues,
) {
    SettingsDetailPane(
        title = { Text(text = stringResource(id = R.string.settings_security_title)) },
        onBackClick = onBackClick,
        contentPadding = contentPadding
    ) {
        SwitchSetting(
            title = R.string.settings_security_title_password_lock,
            checked = uiState.isAuthEnabled,
            onCheckedChange = onChangeAuthEnabled,
            summary = R.string.settings_security_summary_password_lock,
        )
        Setting(
            title = R.string.settings_security_title_change_password,
            onClick = onPasswordChangeClick,
            enabled = uiState.isAuthEnabled
        )
        SwitchSetting(
            title = R.string.settings_security_title_use_biometric_auth,
            checked = uiState.isBiometricEnabled,
            onCheckedChange = onChangeBiometricEnabled,
            summary = R.string.settings_security_summary_use_biometric_auth,
            enabled = uiState.isAuthEnabled,
        )
        SwitchSetting(
            title = R.string.settings_security_label_background_lock,
            checked = uiState.isBackgroundLockEnabled,
            onCheckedChange = onChangeBackgroundLockEnabled,
            enabled = uiState.isAuthEnabled,
        )
    }
}
