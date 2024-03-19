package fr.isen.muros.androiderestaurant

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer la catégorie sélectionnée depuis l'Intent
        val selectedCategory = intent.getStringExtra("selected_category")

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CategoryTitle(selectedCategory ?: "Catégorie inconnue")
                }
            }
        }
    }
}

@Composable
fun CategoryTitle(selectedCategory: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val categoryItems = when(selectedCategory) {
        "Entrées" -> context.resources.getStringArray(R.array.entrees).toList()
        "Plats" -> context.resources.getStringArray(R.array.plats).toList()
        "Desserts" -> context.resources.getStringArray(R.array.desserts).toList()
        else -> emptyList()
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Afficher la catégorie sélectionnée
        Text(
            text = "$selectedCategory",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = Color(0xFFFFA500)
        )
        // Afficher la liste des éléments de la catégorie
        categoryItems.forEach { item: String ->
            Text(
                text = item,
                style = TextStyle(fontSize = 18.sp) // Taille de police 18 sp (ou toute autre taille de votre choix)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryTitlePreview() {
    AndroidERestaurantTheme {
        CategoryTitle("tessst")
    }
}