package com.example.app_s9

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSaveProfile: Button
    private lateinit var buttonLoadProfile: Button
    private lateinit var textViewProfileResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        buttonLoadProfile = findViewById(R.id.buttonLoadProfile)
        textViewProfileResult = findViewById(R.id.textViewProfileResult)

        buttonSaveProfile.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull() ?: 0
            val email = editTextEmail.text.toString()

            sharedPreferencesHelper.saveString("profile_name", name)
            sharedPreferencesHelper.saveInt("profile_age", age)
            sharedPreferencesHelper.saveString("profile_email", email)

            Toast.makeText(this, "Perfil guardado", Toast.LENGTH_SHORT).show()
        }

        buttonLoadProfile.setOnClickListener {
            val name = sharedPreferencesHelper.getString("profile_name", "Sin nombre")
            val age = sharedPreferencesHelper.getInt("profile_age", 0)
            val email = sharedPreferencesHelper.getString("profile_email", "Sin email")

            textViewProfileResult.text = "Nombre: $name\nEdad: $age\nEmail: $email"
        }
    }
}