package com.example.sketchtrain.other
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val fireauth: FirebaseAuth = FirebaseAuth.getInstance()
}