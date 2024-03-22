package fr.isen.muros.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter

import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer les données passées via l'intent
        val selectedDish = intent.getStringExtra("selected_dish")
        val backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        val images = intent.getStringArrayExtra("images")

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    DetailPage(selectedDish ?: "Plat inconnu", images ?: emptyArray())
                }
            }
        }
    }
}


@Composable
fun DetailPage(selectedDish: String?, images: Array<String>?) {
    Column {
        ToolBarDet(modifier = Modifier.fillMaxWidth())

        Dish(selectedDish, images)
    }
}

@Composable
fun ToolBarDet(modifier: Modifier= Modifier) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Dish(selectedDish: String?, images: Array<String>?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { images?.size ?: 0 })
    if (!images.isNullOrEmpty()) {
        HorizontalPager(modifier = modifier.fillMaxWidth(), state = pagerState) { page ->
            val imageUrl = images[page]
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp) // Hauteur de l'image
                    .fillMaxWidth() // Largeur de l'image
                    .padding(8.dp), // Espacement entre les images
                contentScale = ContentScale.Crop
            )
        }
    } else {
        // Si aucune image n'est disponible, afficher une image par défaut
        val imageName = selectedDish?.replace("\\s".toRegex(), "_")?.lowercase() ?: "default_image"
        val drawableId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        if (drawableId != 0) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = null,
                modifier = modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
    Text(
        text = selectedDish ?: "Plat inconnu",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = TextStyle(fontWeight = FontWeight.Bold),
        fontSize = 20.sp
    )
}

@Preview(showBackground = true)
@Composable
fun DishPreview() {
    AndroidERestaurantTheme {
        Dish("test", arrayOf("url_image_1", "url_image_2"))
    }
}