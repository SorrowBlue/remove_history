package com.sorrowblue.comicviewer.settings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sorrowblue.comicviewer.domain.model.settings.Settings
import com.sorrowblue.comicviewer.domain.model.settings.UpdateSettingsRequest
import com.sorrowblue.comicviewer.domain.usecase.LoadSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import logcat.logcat
import android.provider.Settings as AndroidSettings

@AndroidEntryPoint
internal class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        findPreference<SwitchPreferenceCompat>("settings_key_use_auth")?.let { useAuth ->
            useAuth.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue as Boolean) {
                    a()
                } else {
                    viewModel.updateUseAuth(false)
                }
                false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<SwitchPreferenceCompat>("settings_key_use_auth")?.let { useAuth ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.settings.collectLatest {
                    useAuth.isChecked = it.useAuth
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun a() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                logcat { "App can authenticate using biometrics." }
                viewModel.updateUseAuth(true)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                logcat { "No biometric features available on this device." }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                logcat { "Biometric features are currently unavailable." }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                findNavController().navigate(SettingsFragmentDirections.settingsActionSettingsFragmentToSettingsAuthRequestDialog())
            }
        }
    }
}

@AndroidEntryPoint
internal class AuthRequestDialog : DialogFragment() {

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(requireContext(), "設定完了", Toast.LENGTH_SHORT).show()
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("端末の設定で品証を有効化してください")
            .setMessage("端末の設定で品証を有効化してください")
            .setPositiveButton("設定") { _, _ ->
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(AndroidSettings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(AndroidSettings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                }
                resultLauncher.launch(enrollIntent)
            }
            .create()
    }
}

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
) : ViewModel() {
    fun updateUseAuth(newValue: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.execute(UpdateSettingsRequest(newValue))
        }
    }

    val settings =
        loadSettingsUseCase.execute().stateIn(viewModelScope, SharingStarted.Eagerly, Settings())
}
