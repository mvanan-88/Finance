package com.mathi.finance.dataconnect

import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.ConnectorConfig
import com.google.firebase.dataconnect.getInstance
import com.mathi.finance.home.Transaction

class FinanceConnector(private val dataConnect: FirebaseDataConnect) {

    val addTransaction = AddTransactionMutation(dataConnect)
    val listTransactions = ListTransactionsQuery(dataConnect)

    companion object {
        val instance: FinanceConnector by lazy {
            val config = ConnectorConfig(
                connector = "finance-connector",
                location = "us-central1",
                serviceId = "my-finance-service"
            )
            FinanceConnector(FirebaseDataConnect.getInstance(Firebase.app, config))
        }
    }
}

class AddTransactionMutation(private val dataConnect: FirebaseDataConnect) {
    suspend fun execute(amount: Double, description: String?, category: String?, userId: String) {
        // Placeholder for real Data Connect mutation
        println("Adding transaction to Data Connect: $amount, $description, $category for $userId")
    }
}

class ListTransactionsQuery(private val dataConnect: FirebaseDataConnect) {
    suspend fun execute(userId: String): ListTransactionsResult {
        // Placeholder returning mock data - in a real SDK this would call dataConnect.query(...)
        return ListTransactionsResult(
            data = ListTransactionsData(
                transactions = listOf(
                    Transaction(amount = 100.0, description = "Mock from Data Connect", category = "Food", userId = userId)
                )
            )
        )
    }
}

data class ListTransactionsResult(val data: ListTransactionsData)
data class ListTransactionsData(val transactions: List<Transaction>)
