package fr.isen.muros.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                HomePage()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeActivity", "HomeActivity destroyed")
    }
}

@Composable
fun HomePage(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolBar()
        Spacer(modifier = Modifier.height(100.dp)) // Ajouter un espacement entre la barre d'outils et les boutons
        Accueil()
        Spacer(modifier = Modifier.height(100.dp)) // Ajouter un espacement entre la barre d'outils et les boutons
        ButtonToast()
    }
}


@Composable
fun ToolBar(modifier: Modifier= Modifier) {
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
                .padding(vertical = 16.dp, horizontal = 16.dp) // Ajoute un espace vertical entre le texte et le haut de l'écran

        )
    }
}

@Composable
fun Accueil(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.align(Alignment.Top)
        ) {
            Text(
                text = "Bienvenue",
                color = Color(0xFFFFA500),
                textAlign = TextAlign.End,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(start = 80.dp)

            )
            Text(
                text = "chez",
                color = Color(0xFFFFA500),
                textAlign = TextAlign.End,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(start = 165.dp)
            )
            Text(
                text = "DroidRestaurant",
                color = Color(0xCD310010),
                textAlign = TextAlign.End,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.ojuju)))
            )
        }
        Image(
            painter = painterResource(id = R.drawable.androidchef),
            contentDescription = "Android Chef",
            modifier = Modifier.size(150.dp), // Ajuster la taille
            alignment = Alignment.TopEnd
        )
    }
}



@Composable
fun ButtonToast(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedCategory: String = ""

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Entrées",
            fontSize = 30.sp,
            color = Color(0xFFFFA500),
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                selectedCategory = "Entrées"
                val intent = Intent(context, CategoryActivity::class.java)
                intent.putExtra("selected_category", selectedCategory)
                context.startActivity(intent)
                /* Faire un toast (idem pour chaque bouton)
                Toast.makeText(
                    context,
                    "Vous avez cliqué sur les Entrées",
                    Toast.LENGTH_SHORT
                ).show()
                 */
            }
        )
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
        Text(
            text = "Plats",
            fontSize = 30.sp,
            color = Color(0xFFFFA500),
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                selectedCategory = "Plats"
                val intent = Intent(context, CategoryActivity::class.java)
                intent.putExtra("selected_category", selectedCategory)
                context.startActivity(intent)
            }
        )
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
        Text(
            text = "Desserts",
            fontSize = 30.sp,
            color = Color(0xFFFFA500),
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                selectedCategory = "Desserts"
                val intent = Intent(context, CategoryActivity::class.java)
                intent.putExtra("selected_category", selectedCategory)
                context.startActivity(intent)
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun ToolBarPreview(){
    AndroidERestaurantTheme {
        ToolBar()
    }
}

@Composable
fun AccueilPreview(){
    AndroidERestaurantTheme {
        Accueil()
    }
}
@Composable
fun ButtonToastPreview() {
    AndroidERestaurantTheme {
        ButtonToast()
    }
}
