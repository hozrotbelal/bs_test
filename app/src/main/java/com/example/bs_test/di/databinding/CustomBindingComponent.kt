package com.example.bs_test.di.databinding

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@BindingScoped
@DefineComponent(parent = SingletonComponent::class)
interface CustomBindingComponent