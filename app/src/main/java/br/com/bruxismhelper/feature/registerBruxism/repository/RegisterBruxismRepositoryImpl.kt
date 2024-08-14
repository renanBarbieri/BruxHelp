package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.BruxismRegion
import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.shared.repository.source.BruxismFirestore
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import logcat.asLog
import logcat.logcat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RegisterBruxismRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterBruxismRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mapper: RegisterBruxismRepositoryMapper
): RegisterBruxismRepository {
    override suspend fun submitForm(registerForm: RegisterBruxismForm): Result<Unit> {
        val registeredUser = runBlocking(Dispatchers.IO) {
            userLocalDataSource.getUserRegisterId().first()
        }

        val result = remoteDataSource.submitForm(
            userDocumentPath = registeredUser,
            fieldsMap = mapper.mapFromDomain(registerForm)
        )

        return result.map {}
    }
}

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

class RegisterBruxismRemoteDataSource @Inject constructor(private val bruxismFirestore: BruxismFirestore) {

    suspend fun submitForm(
        userDocumentPath: String,
        fieldsMap: Map<String, Any>,
    ): Result<String> {
        val result = CompletableDeferred<Result<String>>()

        bruxismFirestore.userCollection
            .document(userDocumentPath)
            .collection(bruxismFirestore.userBruxismFormDocument)
            .add(fieldsMap)
            .addOnSuccessListener {
                result.complete(Result.success("Form submitted with success"))
            }
            .addOnFailureListener { exception ->
                logcat { "Error while submitting form: ${exception.asLog()}" }
                result.complete(Result.failure(exception))
            }


        return result.await()
    }
}