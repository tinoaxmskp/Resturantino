package com.example.myapplication_mainmenu1

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class Order(
    val orderId: String = "",
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val itemCount: Int,
    val paymentMethod: String,
    val orderDate: Timestamp = Timestamp.now(),
    val status: String = "Pending"
) {
    constructor() : this("", "", emptyList(), 0.0, 0, "", Timestamp.now(), "Pending")

    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
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
                val items = (document.get("items") as? List<Map<String, Any>>)
                    ?.mapNotNull { OrderItem.fromMap(it) } ?: emptyList()

                Order(
                    orderId = document.id,
                    userId = document.getString("userId") ?: "",
                    items = items,
                    totalPrice = document.getDouble("totalPrice") ?: 0.0,
                    itemCount = (document.getLong("itemCount") ?: 0).toInt(),
                    paymentMethod = document.getString("paymentMethod") ?: "",
                    orderDate = document.getTimestamp("orderDate") ?: Timestamp.now(),
                    status = document.getString("status") ?: "Pending"
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}

data class OrderItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageResId: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "quantity" to quantity,
            "imageResId" to imageResId
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): OrderItem? {
            return try {
                OrderItem(
                    id = map["id"] as? String ?: "",
                    name = map["name"] as? String ?: "",
                    price = (map["price"] as? Number)?.toDouble() ?: 0.0,
                    quantity = (map["quantity"] as? Number)?.toInt() ?: 0,
                    imageResId = (map["imageResId"] as? Number)?.toInt() ?: 0
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}

