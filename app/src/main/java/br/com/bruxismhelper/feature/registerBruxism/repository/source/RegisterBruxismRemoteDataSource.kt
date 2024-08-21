package br.com.bruxismhelper.feature.registerBruxism.repository.source

import br.com.bruxismhelper.shared.repository.source.BruxismFirestore
import kotlinx.coroutines.CompletableDeferred
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

class RegisterBruxismRemoteDataSource @Inject constructor(private val bruxismFirestore: BruxismFirestore) {

    suspend fun submitForm(
        userDocumentPath: String,
        fieldsMap: Map<String, Any>,
    ): Result<String> {
        val result = CompletableDeferred<Result<String>>()

        logcat { "userDocPath: $userDocumentPath" }

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