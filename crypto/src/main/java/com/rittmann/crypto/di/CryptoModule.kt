package com.rittmann.crypto.di

import com.rittmann.crypto.keep.di.RegisterCryptoModuleBuilder
import com.rittmann.crypto.list.di.ListCryptoMovementsModuleBuilder
import com.rittmann.crypto.results.di.CryptoResultsModuleBuilder
import dagger.Module

@Module(includes = [RegisterCryptoModuleBuilder::class, ListCryptoMovementsModuleBuilder::class,  CryptoResultsModuleBuilder::class])
abstract class CryptoModule