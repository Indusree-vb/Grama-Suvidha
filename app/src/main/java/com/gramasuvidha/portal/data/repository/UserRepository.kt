package com.gramasuvidha.portal.data.repository

import com.gramasuvidha.portal.data.local.dao.UserDao
import com.gramasuvidha.portal.data.local.entities.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
}
