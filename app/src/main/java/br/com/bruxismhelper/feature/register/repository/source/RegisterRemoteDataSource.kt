package br.com.bruxismhelper.feature.register.repository.source

import br.com.bruxismhelper.shared.repository.source.BruxismFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

class RegisterRemoteDataSource @Inject constructor(private val bruxismFirestore: BruxismFirestore) {

    /**
     * TODO
     *
     * https://firebase.google.com/docs/firestore/quickstart#java_1
     *
     * @param fieldsMap
     */
    suspend fun submitForm(
        fieldsMap: Map<String, Any>,
    ): Result<String> {
        val result = CompletableDeferred<Result<String>>()

        bruxismFirestore.userCollection
            .add(fieldsMap)
            .addOnSuccessListener { documentReference ->
                logcat { "User created with success: ${documentReference.path}" }
                result.complete(Result.success(documentReference.path))
            }
            .addOnFailureListener { exception ->
                logcat { "Error while creating user: ${exception.asLog()}" }
                result.complete(Result.failure(exception))
            }

        return result.await()
    }
}