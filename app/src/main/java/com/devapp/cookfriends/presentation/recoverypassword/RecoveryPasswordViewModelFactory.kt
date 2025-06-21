import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.cookfriends.domain.model.RecoveryStep // Asegúrate de importar RecoveryStep
import com.devapp.cookfriends.presentation.recoverypassword.PreviewRecoveryPasswordViewModel // Importa tu Preview VM
import com.devapp.cookfriends.presentation.recoverypassword.RecoveryPasswordViewModel // Importa tu VM base

class PreviewRecoveryViewModelFactory(private val initialStep: RecoveryStep) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviewRecoveryPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviewRecoveryPasswordViewModel(initialStep) as T
        }
        // Podrías extenderlo para manejar el RecoveryPasswordViewModel normal si es necesario,
        // pero para previews, enfocarse en el PreviewRecoveryPasswordViewModel es suficiente.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}