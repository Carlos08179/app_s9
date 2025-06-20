package com.example.app_s9

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Switch
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextUsername: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewVisitCount:TextView
    private lateinit var buttonResetCount:Button
    private lateinit var buttonGoToProfile:Button
    private lateinit var mainLayout: LinearLayout
    private lateinit var switchDarkMode: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Inicializar vistas
        initViews()
        textViewVisitCount = findViewById(R.id.textViewVisitCount)
        buttonResetCount = findViewById(R.id.buttonResetCount)
        buttonGoToProfile=findViewById(R.id.buttonGoToProfile)
        mainLayout = findViewById(R.id.main)
        switchDarkMode = findViewById(R.id.switchDarkMode)


        // Restaurar el modo oscuro si estaba activado.
        val isDarkMode = sharedPreferencesHelper.getBoolean("dark_mode", false)
        switchDarkMode.isChecked = isDarkMode
        applyTheme(isDarkMode)

        // Configurar listeners,
        setupListeners()

        buttonGoToProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesHelper.saveBoolean("dark_mode", isChecked)
            applyTheme(isChecked)
        }

        // Verificar si es la primera vez que se abre la app
        checkFirstTime()
        updateVisitCounter()
    }

    private fun initViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonClear = findViewById(R.id.buttonClear)
        textViewResult = findViewById(R.id.textViewResult)

    }

    private fun setupListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }

        buttonLoad.setOnClickListener {
            loadData()
        }

        buttonClear.setOnClickListener {
            clearAllData()
        }
        buttonResetCount.setOnClickListener{
            sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0)
            textViewVisitCount.text = "Visitas: 0"
            Toast.makeText(this, "Contador reiniciado", Toast.LENGTH_SHORT).show()

        }
    }

    private fun saveData() {
        val username = editTextUsername.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar datos
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USERNAME, username)
        sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, false)
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_USER_ID, (1000..9999).random())

        Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
        editTextUsername.setText("")
    }

    private fun loadData() {
        val username = sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME, "Sin nombre")
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        val userId = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_USER_ID, 0)

        val result = "Usuario: $username\nID: $userId\nPrimera vez: ${if (isFirstTime) "Sí" else "No"}"
        textViewResult.text = result
    }

    private fun clearAllData() {
        sharedPreferencesHelper.clearAll()
        textViewResult.text = ""
        editTextUsername.setText("")
        Toast.makeText(this, "Todas las preferencias han sido eliminadas", Toast.LENGTH_SHORT).show()
    }

    private fun checkFirstTime() {
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)

        if (isFirstTime) {
            Toast.makeText(this, "¡Bienvenido por primera vez!", Toast.LENGTH_LONG).show()
        }

    }

    private fun updateVisitCounter() {
        val count = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0) + 1
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, count)
        textViewVisitCount.text = "Visitas: $count"
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            mainLayout.setBackgroundColor(Color.DKGRAY)
            textViewResult.setTextColor(Color.WHITE)
            editTextUsername.setTextColor(Color.WHITE)
            editTextUsername.setHintTextColor(Color.LTGRAY)
            editTextUsername.setBackgroundColor(Color.GRAY)
            textViewVisitCount.setTextColor(Color.WHITE)
        } else {
            mainLayout.setBackgroundColor(Color.WHITE)
            textViewResult.setTextColor(Color.BLACK)
            editTextUsername.setTextColor(Color.BLACK)
            editTextUsername.setHintTextColor(Color.DKGRAY)
            editTextUsername.setBackgroundColor(Color.LTGRAY)
            textViewVisitCount.setTextColor(Color.BLACK)
        }
    }

}