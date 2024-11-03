package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.BruxismRegion
import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.model.ResponseBruxismForm
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class RegisterBruxismRepositoryMapper @Inject constructor() {
    fun mapFromDomain(registerForm: RegisterBruxismForm): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "createdAt" to Timestamp.now(),
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

    fun mapToDomain(map: Map<String, Any>): List<ResponseBruxismForm> {
        val registerFormList = mutableListOf<ResponseBruxismForm>()

        val registerForm = ResponseBruxismForm(
            createdAt = createCalendarFromString(map["createdAt"].toString())!!,
            isEating = map["eating"] as Boolean,
            selectedActivity = map["activity"] as? String,
            stressLevel = map["stress_level"] as? Int,
            anxietyLevel = map["anxiety_level"] as? Int,
            isInPain = map["pain"] as? Boolean,
            painLevel = map["pain_level"] as? Int,
            selectableImages = (map["pain_regions"] as? List<*>)?.map {
                BruxismRegion(it as String, true)
            }
        )

        registerFormList.add(registerForm)

        return registerFormList
    }

    private fun createCalendarFromString(dateString: String): Calendar? {
        // Define the date pattern matching the input string
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm:ss a z", Locale.ENGLISH)
        // Parse the string and set the time zone if needed
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            dateFormat.parse(dateString)?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                calendar
            } ?: throw Exception("Invalid date format while parsing $dateString")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}