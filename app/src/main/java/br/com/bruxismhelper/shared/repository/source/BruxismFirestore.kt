package br.com.bruxismhelper.shared.repository.source

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BruxismFirestore @Inject constructor(firestore: FirebaseFirestore) {
    val userCollection = firestore.collection("user")
}