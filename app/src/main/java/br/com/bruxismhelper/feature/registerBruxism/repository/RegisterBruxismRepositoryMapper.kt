package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.BruxismRegion
import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class RegisterBruxismRepositoryMapper @Inject constructor() {
    fun mapFromDomain(registerForm: RegisterBruxismForm): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "date" to Calendar.getInstance().asRFC3339(),
            "eating" to registerForm.isEating
        )

        registerForm.selectedActivity?.let { activity -> map["activity"] = activity }
        registerForm.stressLevel?.let { stress -> map["stress_level"] = stress }
        registerForm.anxietyLevel?.let { anxiety -> map["anxiety_level"] = anxiety }
        registerForm.isInPain?.let { pain -> map["pain"] = pain }
        registerForm.painLevel?.let { level -> map["pain_level"] = level }
        registerForm.selectableImages?.mapSelectedToString()?.let { images ->
            if(images.isNotEmpty()) map["pain_regions"] = images
        }

        return map
    }

    private fun List<BruxismRegion>.mapSelectedToString(): List<String> = this.filter { it.selected }.map { it.name }

    private fun Calendar.asRFC3339(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

        return dateFormat.format(this.time)
    }
}