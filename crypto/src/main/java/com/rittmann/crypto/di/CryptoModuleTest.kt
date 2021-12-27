package com.rittmann.crypto.di

import com.rittmann.crypto.keep.di.RegisterCryptoModuleBuilder
import com.rittmann.crypto.list.di.ListCryptoMovementsModuleBuilderTest
import dagger.Module

@Module(includes = [RegisterCryptoModuleBuilder::class, ListCryptoMovementsModuleBuilderTest::class])
abstract class CryptoModuleTest