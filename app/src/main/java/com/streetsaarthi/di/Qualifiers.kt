package com.streetsaarthi.di
import javax.inject.Qualifier

class Qualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Normal

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Translate
}