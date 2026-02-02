package com.example.myapplication_mainmenu1

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val itemCount: Int = 0,
    val paymentMethod: String = "",
    val orderDate: Timestamp = Timestamp.now(),
    val status: String = "Pending"
) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "userEmail" to userEmail,
            "items" to items.map { it.toMap() },
            "totalPrice" to totalPrice,
            "itemCount" to itemCount,
            "paymentMethod" to paymentMethod,
            "orderDate" to orderDate,
            "status" to status
        )
    }

    companion object {
        fun fromDocument(document: DocumentSnapshot): Order? {
            return try {
                val rawItems = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                val items = rawItems.mapNotNull { OrderItem.fromMap(it) }

                Order(
                    orderId = document.id,
                    userId = document.getString("userId") ?: "",
                    userEmail = document.getString("userEmail") ?: "",
                    items = items,
                    totalPrice = document.getDouble("totalPrice") ?: 0.0,
                    itemCount = (document.getLong("itemCount") ?: 0).toInt(),
                    paymentMethod = document.getString("paymentMethod") ?: "",
                    orderDate = document.getTimestamp("orderDate") ?: Timestamp.now(),
                    status = document.getString("status") ?: "Pending"
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}

data class OrderItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "quantity" to quantity
        )
    }

    companion object {
        fun fromMap(map: Map<*, *>): OrderItem? {
            return try {
                OrderItem(
                    id = map["id"] as? String ?: "",
                    name = map["name"] as? String ?: "",
                    price = (map["price"] as? Number)?.toDouble() ?: 0.0,
                    quantity = (map["quantity"] as? Number)?.toInt() ?: 0
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}

