package com.rittmann.crypto.di

import com.rittmann.crypto.keep.di.CryptoModuleBuilder
import dagger.Module

@Module(includes = [CryptoModuleBuilder::class])
class CryptoModule