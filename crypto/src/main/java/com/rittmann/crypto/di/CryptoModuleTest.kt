package com.rittmann.crypto.di

import com.rittmann.crypto.keep.di.RegisterCryptoModuleBuilder
import com.rittmann.crypto.listmovements.di.ListCryptoMovementsModuleBuilderTest
import com.rittmann.crypto.results.di.CryptoResultsModuleBuilder
import dagger.Module

@Module(includes = [RegisterCryptoModuleBuilder::class, ListCryptoMovementsModuleBuilderTest::class, CryptoResultsModuleBuilder::class])
abstract class CryptoModuleTest