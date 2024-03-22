package fr.isen.muros.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import coil.compose.rememberImagePainter
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer les données passées via l'intent
        val selectedDish = intent.getStringExtra("selected_dish")
        val backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        val images = intent.getStringArrayExtra("images")
        val selectedPrice = intent.getStringExtra("selected_price")

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    DetailPage(selectedDish ?: "Plat inconnu", images ?: emptyArray(), selectedPrice)
                }
            }
        }
    }
}


@Composable
fun DetailPage(selectedDish: String?, images: Array<String>?, price: String?, modifier: Modifier = Modifier) {
    val quantityState = remember { mutableStateOf(1) } // État pour stocker la quantité choisie
    val totalPrice = price?.toFloatOrNull()?.times(quantityState.value) ?: 0.0f // Calcul du prix total

    Column(modifier = modifier.fillMaxWidth()) {
        ToolBarDet(modifier = Modifier.fillMaxWidth())
        Dish(selectedDish, images) // Appel de la fonction Dish avec les paramètres appropriés

        // Afficher le bouton moins, la quantité et le bouton plus
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Button(
                onClick = { if (quantityState.value > 0) quantityState.value-- },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFA500)), // Couleur de fond du bouton
            ) {
                Text(text = "-", color = Color.White)
            }
            Text(
                text = quantityState.value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(fontSize = 18.sp)
            )
            Button(
                onClick = { quantityState.value++ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFA500)), // Couleur de fond du bouton
            ) {
                Text(text = "+", color = Color.White)
            }
        }

        // Afficher le bouton Total en bas de la page
        Button(
            onClick = { /* Action à effectuer lors du clic sur le bouton Total */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFFFA500)), // Couleur de fond du bouton
        ) {
            Text(text = "Total $totalPrice €", color = Color.White)
        }
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