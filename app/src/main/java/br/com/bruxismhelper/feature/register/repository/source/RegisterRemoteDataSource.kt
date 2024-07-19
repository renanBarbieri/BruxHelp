package br.com.bruxismhelper.feature.register.repository.source

import com.google.firebase.firestore.FirebaseFirestore
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

class RegisterRemoteDataSource @Inject constructor() {
    private val firestoreDB = FirebaseFirestore.getInstance();

    /**
     * TODO
     *
     * https://firebase.google.com/docs/firestore/quickstart#java_1
     *
     * @param fieldsMap
     */
    fun submitForm(
        fieldsMap: Map<String, Any>,
        onFormSubmitted: suspend (userRegisterId: String) -> Unit = {},
        onFormSubmitFailure: suspend (exception: Exception) -> Unit = {},
    ) {
        firestoreDB
            .collection("user")
            .add(fieldsMap)
            .addOnSuccessListener { documentReference ->
                logcat { "User created with success: ${documentReference.id}" }
                suspend { onFormSubmitted(documentReference.id) }
            }
            .addOnFailureListener { exception ->
                logcat { "Error while creating user: ${exception.asLog()}" }
                suspend { onFormSubmitFailure(exception) }
            }
    }
}