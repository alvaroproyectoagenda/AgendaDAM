package es.proyecto.agendadam.acceso

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccesoViewModel(): ViewModel() {

    private val _error: MutableStateFlow<String> =
        MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _login: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val login: StateFlow<Boolean?>
        get() = _login

    private val _register: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val register: StateFlow<Boolean?>
        get() = _register

    private val _addUser: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val addUser: StateFlow<Boolean?>
        get() = _addUser

    fun login(email: String, password: String){
        Firebase.auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                _login.value = it.isSuccessful
            }
    }

    fun register(email: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                _register.value = it.isSuccessful
            }
    }

    fun addUser(user: User){

        user.id?.let {
            Firebase.firestore.collection("usuarios")
                .document(user.id)
                .set(user)
                .addOnSuccessListener {
                    _addUser.value = true
                }
                .addOnFailureListener {
                    _addUser.value = false
                    _error.value = it.message.toString()
                }
        }

    }
}