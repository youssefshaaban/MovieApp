package com.example.data.di.qulifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieDataMapper


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoviesLocalDataMapper


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieLocalDataMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieRemoteDataMapper
