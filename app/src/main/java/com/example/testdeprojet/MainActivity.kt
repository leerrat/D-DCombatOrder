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
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row

data class Perso(val Ordre:Int, val Nom:String, val Pdv: Int)
val PersoList = mutableStateListOf<Perso>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttonadd = findViewById<Button>(R.id.buttonAdd)
        val buttonremove = findViewById<Button>(R.id.buttonRemove)
        val composeView = findViewById<ComposeView>(R.id.composeView)


        buttonadd.setOnClickListener {
            showPopupAdd()
        }
        buttonremove.setOnClickListener {
            showPopupRemove()
        }
        composeView.setContent {
            TestDeProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PersoList(
                        people = PersoList,
                        modifier = Modifier.padding(innerPadding)
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
                val perso = sortedPeople[index] // Obtenez l'objet actuel
                ListItem(
                    headlineContent = {
                        Text(text = "${perso.Nom}, Ordre: ${perso.Ordre}, PDV: ${perso.Pdv}")
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
