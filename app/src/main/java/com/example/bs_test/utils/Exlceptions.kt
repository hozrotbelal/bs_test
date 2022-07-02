package com.example.bs_test.utils

import java.io.IOException

class ApiException(message: String) : Exception(message)
class NoInternetException(message: String) : IOException(message)
