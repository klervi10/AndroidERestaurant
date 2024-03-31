package fr.isen.muros.androiderestaurant

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.google.gson.Gson
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import fr.isen.muros.androiderestaurant.model.Items
import fr.isen.muros.androiderestaurant.model.DataResult
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme


class CategoryActivity : ComponentActivity() {
    private var backgroundColor: String = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedCategory = intent.getStringExtra("selected_category")
        backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        fetchData(selectedCategory)
    }

    private fun fetchData(selectedCategory: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val params = JSONObject().apply { put("id_shop", "1") }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                val result: DataResult = Gson().fromJson(response.toString(), DataResult::class.java)
                val dishesFromCategory = result.data.flatMap { it.items }.filter { it.categNameFr == selectedCategory }
                setContent {
                    AndroidERestaurantTheme {
                        Surface(
                            color = Color(android.graphics.Color.parseColor(backgroundColor))
                        ) {
                            Column {
                                ToolBarCat(modifier = Modifier.fillMaxWidth()) // Appel de la fonction ToolBarCat
                                CategoryTitle(selectedCategory ?: "Catégorie inconnue", dishesFromCategory)
                            }
                        }
                    }
                }
            },
            { error ->
                Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}

@Composable
fun ToolBarCat(modifier: Modifier= Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DroidRestaurant",
            color = Color.White,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .background(Color(0xFFFFA500))
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)

        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CategoryTitle(selectedCategory: String, dishes: List<Items>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = selectedCategory,
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(dishes.size) { index ->
            val item = dishes[index]
            val selectedPrice = item.prices.firstOrNull()?.price
            val imageName = item.nameFr?.replace("\\s".toRegex(), "")?.lowercase() ?: "default_image"
            val imageUrl = item.images.lastOrNull()
            val painter = if (!imageUrl.isNullOrEmpty()) {
                rememberImagePainter(imageUrl)
            } else {
                painterResource(id = context.resources.getIdentifier(imageName, "drawable", context.packageName))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("selected_dish", item.nameFr)
                    intent.putExtra("background_color", "#FFFFFF")
                    intent.putExtra("images", item.images.toTypedArray())
                    intent.putExtra("selected_price", selectedPrice)
                    intent.putStringArrayListExtra("ingredients", ArrayList(item.ingredients.map { it.nameFr }))
                    context.startActivity(intent)
                }
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "${item.nameFr ?: ""} : ${item.prices.firstOrNull()?.price} €",
                    style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier.padding(end = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

