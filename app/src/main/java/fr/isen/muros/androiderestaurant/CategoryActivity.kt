package fr.isen.muros.androiderestaurant

import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
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

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.google.gson.Gson
import android.widget.Toast

import fr.isen.muros.androiderestaurant.model.Data
import fr.isen.muros.androiderestaurant.model.Items
import fr.isen.muros.androiderestaurant.model.DataResult
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme


class CategoryActivity : ComponentActivity() {
    private var backgroundColor: String = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer la catégorie sélectionnée depuis l'Intent
        val selectedCategory = intent.getStringExtra("selected_category")
        backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"

        // Appel à la fonction fetchData avant l'initialisation de l'interface utilisateur
        fetchData(selectedCategory)

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    // Passer une liste vide en attendant la récupération des données
                    CategoryTitle(selectedCategory ?: "Catégorie inconnue", emptyList())
                }
            }
        }
    }

    private fun fetchData(selectedCategory: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"

        // Paramètres JSON
        val params = JSONObject()
        params.put("id_shop", "1")

        // JSON Request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val result: DataResult = Gson().fromJson(response.toString(), DataResult::class.java)
                Log.d("JSON_DATA", "Data result: $result")

                // Filtrer les plats par catégorie
                val dishesFromCategory = result.data.flatMap { it.items }.filter { it.categNameFr == selectedCategory }
                Log.d("DISHES_FILTER", "Dishes from category: $dishesFromCategory")

                // Mettre à jour l'interface utilisateur avec les données récupérées
                updateUI(selectedCategory ?: "Catégorie inconnue", dishesFromCategory)
            },
            { error ->
                Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun updateUI(selectedCategory: String, dishes: List<Items>) {
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    CategoryTitle(selectedCategory, dishes)
                }
            }
        }
    }
}

@Composable
fun CategoryTitle(selectedCategory: String, dishes: List<Items>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Afficher la catégorie sélectionnée
        Text(
            text = selectedCategory,
            style = TextStyle(fontSize = 24.sp)
        )
        // Afficher la liste des éléments de la catégorie
        dishes.forEach { item: Items ->
            Text(
                text = item.nameFr ?: "",
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.clickable {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("selected_dish", item.nameFr)
                    intent.putExtra("background_color", "#FFFFFF")
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryTitlePreview() {
    AndroidERestaurantTheme {
        CategoryTitle("test", emptyList())
    }
}