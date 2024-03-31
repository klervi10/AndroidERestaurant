package fr.isen.muros.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import coil.compose.rememberImagePainter
import fr.isen.muros.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DetailActivity : ComponentActivity() {
    internal var itemsInCart = 0
    internal var showCartBadge by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer les données passées via l'intent
        val selectedDish = intent.getStringExtra("selected_dish")
        val backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        val images = intent.getStringArrayExtra("images")
        val selectedPrice = intent.getStringExtra("selected_price")
        val ingredients = intent.getStringArrayListExtra("ingredients")

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {

                    DetailPage(
                        context = this,
                        selectedDish ?: "Plat inconnu",
                        images ?: emptyArray(),
                        selectedPrice,
                        ingredients ?: emptyList(),
                    )
                }
            }
        }
    }

    fun updateCartItemCount(count: Int){
        itemsInCart += count
    }
}

@Composable
fun DetailPage(
    context: Context,
    selectedDish: String?,
    images: Array<String>?,
    price: String?,
    ingredients: List<String>?,
    modifier: Modifier = Modifier
) {
    val quantityState = remember { mutableStateOf(1) } //quantité initiale
    val totalPrice = price?.toFloatOrNull()?.times(quantityState.value) ?: 0.0f // Calcul du prix total
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Commande ajoutée") },
            text = { Text("Votre commande a été ajoutée au panier") },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ToolBarDet(
            modifier = Modifier.fillMaxWidth(),
            quantity = quantityState.value,
            showCartBadge = (context as DetailActivity).showCartBadge
        )

        Dish(selectedDish, images)

        Text(
            text = ingredients?.joinToString(", ") ?: "",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 16.sp
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            RoundButton(
                onClick = { if (quantityState.value > 0) quantityState.value-- },
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "-",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = quantityState.value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(fontSize = 18.sp)
            )
            RoundButton(
                onClick = { quantityState.value++ },
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        RoundButton(
            onClick = {
                val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("selected_dish", selectedDish)
                editor.putInt("quantity", quantityState.value)
                editor.putFloat("total_price", totalPrice)
                editor.apply()

                showDialog = true
                Log.d("OrderDetails", "Selected Dish: ${selectedDish ?: ""}, Quantity: ${quantityState.value}, Total Price: $totalPrice")
                context.updateCartItemCount(quantityState.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp)
        ) {
            Text(text = "Total $totalPrice €", color = Color.White)
        }
    }
}

@Composable
fun RoundButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
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
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        val imageName = selectedDish?.replace("\\s".toRegex(), "")?.lowercase() ?: "default_image"
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

@Composable
fun ToolBarDet(
    modifier: Modifier = Modifier,
    quantity: Int,
    showCartBadge: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFFFFA500))
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "DroidRestaurant",
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        val intent = Intent(context, CartActivity::class.java)
                        context.startActivity(intent)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cartimage),
                    contentDescription = stringResource(id = R.string.cart),
                    modifier = Modifier.fillMaxSize()
                )
                if (showCartBadge && quantity > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .background(Color.Red, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = quantity.toString(),
                            color = Color.White,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}