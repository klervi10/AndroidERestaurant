package fr.isen.muros.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                NameAndLogo("Android")
            }
        }
    }
}

@Composable
fun NameAndLogo(name: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Android",
        )
        Column {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NameAndLogoPreview() {
    AndroidERestaurantTheme {
        NameAndLogo("Android")
    }
}
