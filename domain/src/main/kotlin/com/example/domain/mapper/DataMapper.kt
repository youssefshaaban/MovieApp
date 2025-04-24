package com.example.domain.mapper

interface DataMapper<T,R> {
    fun execute(data:T):R
}