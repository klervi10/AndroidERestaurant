package fr.isen.muros.androiderestaurant

import android.os.Bundle
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val selectedDish = remember {
                    mutableStateOf(
                        sharedPreferences.getString("selected_dish", "") ?: ""
                    )
                }
                val quantity = remember { mutableStateOf(sharedPreferences.getInt("quantity", 0)) }
                val totalPrice =
                    remember { mutableStateOf(sharedPreferences.getFloat("total_price", 0.0f)) }

                var showDialog by remember { mutableStateOf(false) }

                Column {
                    ToolBarCart()
                    CartContent(
                        selectedDish = selectedDish,
                        quantity = quantity,
                        totalPrice = totalPrice,
                        onDeleteClicked = {
                            with(sharedPreferences.edit()) {
                                remove("selected_dish")
                                remove("quantity")
                                remove("total_price")
                                apply()
                            }
                            selectedDish.value = ""
                            quantity.value = 0
                            totalPrice.value = 0.0f
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RoundButtonCart(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "Passer la commande", color = Color.White)
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmation") },
                        text = { Text("Merci pour votre commande !") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val intent = Intent(this@CartActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    showDialog = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false }
                            ) {
                                Text("Annuler")
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ToolBarCart(modifier: Modifier= Modifier) {
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

@Composable
fun CartContent(selectedDish: MutableState<String>, quantity: MutableState<Int>, totalPrice: MutableState<Float>, onDeleteClicked: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Plat choisi: ${selectedDish.value}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Quantité: ${quantity.value}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Prix total: ${totalPrice.value} €")
        Spacer(modifier = Modifier.height(16.dp))
        RoundButtonCart(
            onClick = onDeleteClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
        ) {
            Text(text = "Supprimer", color = Color.White)
        }
    }
}

@Composable
fun RoundButtonCart(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = Color(0xFFFFA500),
        contentColor = Color.White
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}