package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.CustomerDao
import com.inventorypos.data.local.entity.CustomerEntity
import com.inventorypos.domain.model.Customer
import com.inventorypos.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override fun getAllCustomers(): Flow<List<Customer>> {
        return customerDao.getAllActive().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun searchCustomers(query: String): Flow<List<Customer>> {
        return customerDao.search(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getCustomerById(id: Long): Customer? {
        return customerDao.getById(id)?.toDomain()
    }

    override suspend fun insertCustomer(customer: Customer): Long {
        return customerDao.insert(customer.toEntity())
    }

    override suspend fun updateCustomer(customer: Customer) {
        customerDao.update(customer.toEntity())
    }

    override suspend fun deleteCustomer(id: Long) {
        customerDao.softDelete(id)
    }

    private fun CustomerEntity.toDomain(): Customer {
        return Customer(
            id = this.id,
            name = this.name,
            phone = this.phone,
            email = this.email,
            address = this.address,
            loyaltyPoints = this.loyaltyPoints,
            totalSpent = this.totalSpent,
            notes = this.notes,
            isActive = this.isActive
        )
    }

    private fun Customer.toEntity(): CustomerEntity {
        return CustomerEntity(
            id = this.id,
            name = this.name,
            phone = this.phone,
            email = this.email,
            address = this.address,
            loyaltyPoints = this.loyaltyPoints,
            totalSpent = this.totalSpent,
            notes = this.notes,
            isActive = this.isActive,
            createdAt = Date()
        )
    }
}
