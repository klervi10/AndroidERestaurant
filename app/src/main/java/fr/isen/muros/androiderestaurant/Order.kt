package fr.isen.muros.androiderestaurant

data class Order(
    val selectedDish: String,
    val quantity: Int,
    val totalPrice: Float
)