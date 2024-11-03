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

    suspend fun getResponsesFromUser(userDocumentPath: String): Result<HashMap<String, Any>> {
        val deferredResult = CompletableDeferred<Result<HashMap<String, Any>>>()

        bruxismFirestore.userCollection
            .document(userDocumentPath)
            .collection(bruxismFirestore.userBruxismFormDocument)
            .get()
            .addOnSuccessListener { result ->
                val hashMap = result.associateTo(HashMap<String, Any>()) { it.id to it.data }
                deferredResult.complete(Result.success(hashMap))
            }
            .addOnFailureListener { exception ->
                logcat { "Error while getting responses: ${exception.asLog()}" }
                deferredResult.complete(Result.failure(exception))
            }

        return deferredResult.await()
    }
}