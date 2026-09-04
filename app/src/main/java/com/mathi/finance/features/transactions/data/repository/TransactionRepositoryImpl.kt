package com.mathi.finance.features.transactions.data.repository

import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.transactions.domain.model.PaymentsModel
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import io.github.jan.supabase.postgrest.from

class TransactionRepositoryImpl(
    preferenceManager: PreferenceManager
) : TransactionRepository {
    private val currentUserId = preferenceManager.getUserId()

    override suspend fun fetchTransactions(): Result<List<TransactionSummary>> {
        if (currentUserId == -1) return Result.success(emptyList())
        return try {
            val result = SupabaseClient.client.from("transaction_summary_view")
                .select() {
                    filter {
                        eq("created_by", currentUserId)
                    }
                }
                .decodeList<TransactionSummary>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchContacts(): Result<List<Contact>> {
        return try {
            val result = SupabaseClient.client.from("contacts")
                .select {
                    filter {
                        eq("created_by", currentUserId)
                    }
                }
                .decodeList<Contact>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchTransactionTypes(): Result<List<TransactionType>> {
        return try {
            val result = SupabaseClient.client.from("transaction_type")
                .select {
                    filter {
                        eq("status", 1)
                        eq("created_by", currentUserId)

                    }
                }
                .decodeList<TransactionType>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchInterestRates(): Result<List<InterestRates>> {
        return try {
            val result = SupabaseClient.client.from("interest_rates")
                .select {
                    filter {
                        eq("status", 1)
                        eq("created_by", currentUserId)
                    }
                }
                .decodeList<InterestRates>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchInstalments(): Result<List<instalment_data>> {
        return try {
            val result = SupabaseClient.client.from("installment_tenure")
                .select {
                    filter {
                        eq("status", 1)
                        eq("created_by", currentUserId)
                    }
                }
                .decodeList<instalment_data>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(transaction: PerPersonTransaction): Result<Unit> {
        return try {
            val transactionWithUser = transaction.copy(created_by = currentUserId)
            SupabaseClient.client.from("per_person_transaction")
                .insert(transactionWithUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun makePayment(loanId: Int, amountPaid: Float, notes: String): Result<Unit> {
        val payment = PaymentsModel(
            loan_id = loanId,
            amount_paid = amountPaid,
            notes = notes,
            created_by = currentUserId,
        )
        return try {
            SupabaseClient.client.from("payments_table")
                .insert(payment)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchPaymentHistory(loanId: Int): Result<List<PaymentsModel>> {
        return try {
            val result = SupabaseClient.client.from("payments_table")
                .select {
                    filter {
                        eq("created_by", currentUserId)
                        eq("loan_id", loanId)
                    }
                }
                .decodeList<PaymentsModel>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
