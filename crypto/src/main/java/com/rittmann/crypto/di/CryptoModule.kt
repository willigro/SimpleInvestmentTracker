package com.rittmann.crypto.di

import com.rittmann.crypto.keep.di.RegisterCryptoModuleBuilder
import com.rittmann.crypto.list.di.ListCryptoMovementsModuleBuilder
import dagger.Module

@Module(includes = [RegisterCryptoModuleBuilder::class, ListCryptoMovementsModuleBuilder::class])
abstract class CryptoModule