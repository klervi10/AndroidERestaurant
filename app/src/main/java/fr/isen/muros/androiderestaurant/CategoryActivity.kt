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
import android.widget.Toast
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme


class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer la catégorie sélectionnée depuis l'Intent
        val selectedCategory = intent.getStringExtra("selected_category")
        val backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    CategoryTitle(selectedCategory ?: "Catégorie inconnue")
                }
            }
        }

        // Appeler la fonction pour récupérer les données
        fetchData()
    }

    private fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"

        // Paramètres JSON
        val params = JSONObject()
        params.put("id_shop", "1")

        // JSON Request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                Log.d("VolleyResponse", "Response: $response")
                Toast.makeText(applicationContext, "Response: $response", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
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
        verticalArrangement = Arrangement.Top
    ) {
        // Afficher la catégorie sélectionnée
        Text(
            text = selectedCategory,
            style = TextStyle(fontSize = 24.sp)
        )
        // Afficher la liste des éléments de la catégorie
        categoryItems.forEach { item: String ->
            Text(
                text = item,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.clickable {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("selected_dish", item)
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
        CategoryTitle("tessst")
    }
}