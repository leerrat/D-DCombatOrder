package com.example.testdeprojet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testdeprojet.MainActivity.Companion.PersoList
import kotlin.random.Random

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreateEnemiesScreen()  // Créez l'écran de la 2e activité avec la logique de création des ennemis
        }
    }

    // Fonction composable pour créer l'écran
    @Composable
    fun CreateEnemiesScreen() {
        // Variables d'état pour les champs de texte
        var enemyName by remember { mutableStateOf("") }
        var numEnemies by remember { mutableStateOf("") }
        var bonusOrdre by remember { mutableStateOf("") }
        var pdv by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Champ de texte pour le nom de l'ennemi
            TextField(
                value = enemyName,
                onValueChange = { enemyName = it },
                label = { Text("Nom de l'ennemi") },
                modifier = Modifier.fillMaxWidth()
            )

            // Champ de texte pour le nombre d'ennemis
            TextField(
                value = numEnemies,
                onValueChange = { numEnemies = it },
                label = { Text("Nombre d'ennemis") },
                modifier = Modifier.fillMaxWidth()
            )

            // Champ de texte pour le bonus d'ordre
            TextField(
                value = bonusOrdre,
                onValueChange = { bonusOrdre = it },
                label = { Text("Bonus d'ordre") },
                modifier = Modifier.fillMaxWidth()
            )

            // Champ de texte pour les points de vie des ennemis
            TextField(
                value = pdv,
                onValueChange = { pdv = it },
                label = { Text("Points de vie des ennemis") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton pour créer les ennemis
            Button(onClick = {
                if (enemyName.isNotEmpty() && numEnemies.isNotEmpty() && bonusOrdre.isNotEmpty() && pdv.isNotEmpty()) {
                    createEnemies(enemyName, numEnemies, bonusOrdre, pdv)
                } else {
                    Toast.makeText(this@MainActivity2, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Créer les ennemis")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton de retour à l'activité précédente
            Button(onClick = { finish() }) {
                Text("Retour à l'activité précédente")
            }
        }
    }

    // Fonction pour créer les ennemis avec un nom, un nombre d'ennemis, des points de vie et un bonus d'ordre
    private fun createEnemies(enemyName: String, numEnemies: String, bonusOrdre: String, pdv: String) {
        val numberOfEnemies = numEnemies.toIntOrNull() ?: 0  // Convertir le nombre d'ennemis en entier
        val bonus = bonusOrdre.toIntOrNull() ?: 0  // Convertir le bonus en entier, par défaut 0 si invalide
        val healthPoints = pdv.toIntOrNull() ?: 0  // Convertir les points de vie en entier, par défaut 0 si invalide

        // Vérifier que les valeurs sont valides
        if (numberOfEnemies <= 0 || healthPoints <= 0) {
            Toast.makeText(this, "Veuillez entrer un nombre d'ennemis et des points de vie valides.", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer le nombre d'ennemis spécifié
        for (i in 1..numberOfEnemies) {
            val randomOrder = Random.nextInt(1, 21)  // Génère un ordre aléatoire entre 1 et 20

            // Calculer l'ordre final en appliquant le bonus et en s'assurant que l'ordre ne soit pas inférieur à 1
            val finalOrder = (randomOrder + bonus).coerceAtLeast(1)

            // Créer un nouvel ennemi avec le nom et l'ordre final
            val newEnemy = Perso(finalOrder, "$enemyName $i", healthPoints)
            PersoList.add(newEnemy)

            // Afficher un message pour indiquer que l'ennemi a été créé
            Toast.makeText(this, "Ennemi ${newEnemy.Nom} créé avec ordre ${newEnemy.Ordre}.", Toast.LENGTH_SHORT).show()
        }
    }
}
