package com.example.testdeprojet
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.testdeprojet.ui.theme.TestDeProjetTheme
import androidx.compose.ui.platform.ComposeView
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.compose.runtime.*
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.MaterialTheme

data class Perso(val Ordre:Int, val Nom:String, val Pdv: Int)

class MainActivity : ComponentActivity() {

    companion object {
        // Liste globale accessible à partir de toute autre classe
        val PersoList = mutableStateListOf<Perso>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttonadd = findViewById<Button>(R.id.buttonAdd)
        val buttonremove = findViewById<Button>(R.id.buttonRemove)
        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        val buttonNextPage = findViewById<Button>(R.id.buttonNextPage)
        buttonNextPage.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        buttonadd.setOnClickListener {
            showPopupAdd()
        }
        buttonremove.setOnClickListener {
            showPopupRemove()
        }
        composeView.setContent  {
            TestDeProjetTheme {
                Scaffold(
                    containerColor = Color.Transparent, // Assurez-vous d'utiliser cette propriété
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // Fond explicite transparent
                ) {
                    PersoList(
                        people = PersoList,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
    private fun showPopupRemove() {
        if (PersoList.isEmpty()) {
            Toast.makeText(this, "Aucun personnage à supprimer", Toast.LENGTH_SHORT).show()
            return
        }
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.supp_perso, null)

        // Références aux éléments du pop-up
        val listView = popupView.findViewById<ListView>(R.id.removeListView)
        val confirmButton = popupView.findViewById<Button>(R.id.confirmRemoveButton)

        // Créer un adaptateur pour afficher les personnages
        val persoNames = PersoList.map { it.Nom }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, persoNames)
        listView.adapter = adapter

        // Créer la boîte de dialogue
        val dialog = AlertDialog.Builder(this)
            .setView(popupView)
            .create()

        confirmButton.setOnClickListener {
            // Obtenir les positions sélectionnées
            val selectedPositions = listView.checkedItemPositions
            val toRemove = mutableListOf<Perso>()

            for (i in 0 until listView.count) {
                if (selectedPositions[i]) {
                    val persoToRemove = PersoList.find { it.Nom == persoNames[i] }
                    if (persoToRemove != null) {
                        toRemove.add(persoToRemove)
                    }
                }
            }

            // Supprimer les personnages sélectionnés
            if (toRemove.isNotEmpty()) {
                PersoList.removeAll(toRemove)
                Toast.makeText(this, "Personnages supprimés : ${toRemove.joinToString { it.Nom }}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Aucun personnage sélectionné", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss() // Fermer le pop-up
        }

        // Afficher la boîte de dialogue
        dialog.show()
    }
    private fun showPopupAdd() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.ajout_perso, null)

        val editTextNom = popupView.findViewById<EditText>(R.id.editTextNom)
        val editTextOrdre = popupView.findViewById<EditText>(R.id.editTextOrdre)
        val editTextPdv = popupView.findViewById<EditText>(R.id.editTextPdv)
        val submitButton = popupView.findViewById<Button>(R.id.submitButton)

        val dialog = AlertDialog.Builder(this)
            .setView(popupView)
            .create()

        submitButton.setOnClickListener {
            val nom = editTextNom.text.toString()
            val ordre = editTextOrdre.text.toString().toIntOrNull()
            val pdv = editTextPdv.text.toString().toIntOrNull()
            if (nom.isNotEmpty() && ordre != null && pdv != null) {
                val newPerso = Perso(ordre, nom, pdv)
                PersoList.add(newPerso)
                dialog.dismiss() //
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement.", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.show()
    }

    @Composable
    fun PersoList(people: MutableList<Perso>, modifier: Modifier = Modifier) {
        val sortedPeople = people.sortedByDescending { it.Ordre }
        LazyColumn(modifier = modifier) {
            items(sortedPeople.size) { index ->
                val perso = sortedPeople[index]

                // États pour chaque icône d'étoile, indépendants les uns des autres
                val (isStarFilled1, setStarFilled1) = remember { mutableStateOf(true) }
                val (isStarFilled2, setStarFilled2) = remember { mutableStateOf(true) }
                val (isStarFilled3, setStarFilled3) = remember { mutableStateOf(true) }

                ListItem(
                    modifier = Modifier.background(Color.Transparent),
                    headlineContent = {
                        Column {
                            Text(text = "${perso.Nom}, Ordre: ${perso.Ordre}, PDV: ${perso.Pdv}")
                            Row {
                                // Première étoile avec son propre état
                                Icon(
                                    imageVector = if (isStarFilled1) Icons.Filled.Star else Icons.TwoTone.Star,
                                    contentDescription = if (isStarFilled1) "Star Filled" else "Star TwoTone",
                                    tint = if (isStarFilled1) Color(0xFF006400) else Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { setStarFilled1(!isStarFilled1) } // Changer uniquement cette étoile
                                )
                                // Deuxième étoile avec son propre état
                                Icon(
                                    imageVector = if (isStarFilled2) Icons.Filled.Star else Icons.TwoTone.Star,
                                    contentDescription = if (isStarFilled2) "Star Filled2" else "Star TwoTone2",
                                    tint = if (isStarFilled2) Color(0xFFFF9800) else Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { setStarFilled2(!isStarFilled2) } // Changer uniquement cette étoile
                                )
                                // Troisième étoile avec son propre état
                                Icon(
                                    imageVector = if (isStarFilled3) Icons.Filled.Star else Icons.TwoTone.Star,
                                    contentDescription = if (isStarFilled3) "Star Filled3" else "Star TwoTone3",
                                    tint = if (isStarFilled3) Color(0xFF800080) else Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { setStarFilled3(!isStarFilled3) } // Changer uniquement cette étoile
                                )
                            }
                        }
                    },
                    trailingContent = {
                        Row {
                            // Bouton pour décrémenter les points de vie
                            Button(onClick = {
                                if (perso.Pdv > 0) {
                                    people[index] = perso.copy(Pdv = perso.Pdv - 1)
                                }
                            }) {
                                Text("-")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Bouton pour incrémenter les points de vie
                            Button(onClick = {
                                people[index] = perso.copy(Pdv = perso.Pdv + 1)
                            }) {
                                Text("+")
                            }
                        }
                    }
                )
            }
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun PeopleListPreview() {
        val previewPeople = remember { mutableStateListOf(Perso(20, "Louis", 50)) }
        TestDeProjetTheme {
            PersoList(people = previewPeople)
        }
    }
}
