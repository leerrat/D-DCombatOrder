package com.example.testdeprojet

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlin.random.Random

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        val enemyNameEditText = findViewById<EditText>(R.id.editTextEnemyName)
        val numEnemiesEditText = findViewById<EditText>(R.id.editTextNumEnemies)
        val bonusOrdreEditText = findViewById<EditText>(R.id.editTextBonusOrdre)
        val pdvEditText = findViewById<EditText>(R.id.editTextPdv)
        val createEnemiesButton = findViewById<Button>(R.id.buttonCreateEnemies)
        val backButton = findViewById<Button>(R.id.buttonBack)

        createEnemiesButton.setOnClickListener {
            val enemyName = enemyNameEditText.text.toString()
            val numEnemies = numEnemiesEditText.text.toString()
            val bonusOrdre = bonusOrdreEditText.text.toString()
            val pdv = pdvEditText.text.toString()

            if (enemyName.isNotEmpty() && numEnemies.isNotEmpty() && bonusOrdre.isNotEmpty() && pdv.isNotEmpty()) {
                createEnemies(enemyName, numEnemies, bonusOrdre, pdv)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
            }
        }
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun createEnemies(enemyName: String, numEnemies: String, bonusOrdre: String, pdv: String) {
        val numberOfEnemies = numEnemies.toIntOrNull() ?: 0
        val bonus = bonusOrdre.toIntOrNull() ?: 0
        val healthPoints = pdv.toIntOrNull() ?: 0

        if (numberOfEnemies <= 0 || healthPoints <= 0) {
            Toast.makeText(this, "Veuillez entrer un nombre d'ennemis et des points de vie valides.", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 1..numberOfEnemies) {
            val randomOrder = Random.nextInt(1, 21)
            val finalOrder = (randomOrder + bonus).coerceAtLeast(1)

            val newEnemy = Perso(finalOrder, "$enemyName $i", healthPoints)
            MainActivity.PersoList.add(newEnemy)

            Toast.makeText(this, "Ennemi ${newEnemy.Nom} créé avec ordre ${newEnemy.Ordre}.", Toast.LENGTH_SHORT).show()
        }
    }
}
