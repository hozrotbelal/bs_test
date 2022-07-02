package com.example.bs_test.di.databinding

import dagger.hilt.DefineComponent

@DefineComponent.Builder
interface CustomBindingComponentBuilder {
    fun build(): CustomBindingComponent
}