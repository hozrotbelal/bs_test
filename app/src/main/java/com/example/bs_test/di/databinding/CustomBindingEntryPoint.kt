package com.example.bs_test.di.databinding

import androidx.databinding.DataBindingComponent

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import com.example.bs_test.utils.BindingUtil

@EntryPoint
@BindingScoped
@InstallIn(CustomBindingComponent::class)
interface CustomBindingEntryPoint: DataBindingComponent {

    @BindingScoped
    override fun getBindingUtil(): BindingUtil
}