package com.example.bs_test.util

import com.example.bs_test.utils.getFormattedPhone
import com.example.bs_test.utils.isNotNull
import com.example.bs_test.utils.isValidMobile
import timber.log.Timber
import java.text.DecimalFormat
import java.util.regex.Pattern


val String.Companion.BACK_SLASH: String
    get() = "/"

val String.Companion.EMPTY: String
    get() = ""
val String.Companion.ZERO: String
    get() = "0"
val String.Companion.OTP_RECEIVER: String
    get() = "otp_receiver"
val String.Companion.OTP: String
    get() = "otp"
val String.Companion.PHONE_NUMBER: String
    get() = "phone_number"
val String.Companion.NAME: String
    get() = "name"
val String.Companion.WEB_FILE_NO: String
    get() = "web_file_no"
val String.Companion.PASSPORT_NO: String
    get() = "passport_no"
val String.Companion.BANK_NAME: String
    get() = "bankName"
val String.Companion.ACCOUNT_NUMBER: String
    get() = "accountNumber"
val String.Companion.ACTION_TYPE: String
    get() = "actionType"
val String.Companion.ID: String
    get() = "id"
val String.Companion.IMAGE: String
    get() = "image"

val String.Companion.PHONE: String
    get() = "phone"
val String.Companion.CONTACT: String
    get() = "contact"
val String.Companion.UNKNOWN: String
    get() = "Unknown"

val String.Companion.AMOUNT: String
    get() = "amount"
val String.Companion.SUCCESS: String
    get() = "success"
val String.Companion.REFUNDED: String
    get() = "refunded"
val String.Companion.ERROR: String
    get() = "error"
val String.Companion.NOT_FOUND: String
    get() = "not_found"
val String.Companion.OTPCHECKER1: String
    get() = "upay"
val String.Companion.OTPCHECKER2: String
    get() = "upay."
val String.Companion.OTPCHECKER3: String
    get() = "16268"
val String.Companion.CUSTOMER_CODE: String
    get() = "0001"
val String.Companion.AGENT_CODE: String
    get() = "0002"
val String.Companion.AGENT: String
    get() = "agent"
val String.Companion.ATM: String
    get() = "atm"
val String.Companion.MERCENT_CODE: String
    get() = "0004"

val String.Companion.URL: String
    get() = "url"

val String.Companion.FROM: String
    get() = "from"


val String.Companion.NEW_LINE: String
    get() = "\n"

val String.Companion.CLONE: String
    get() = ":"

val String.Companion.SPACE: String
    get() = " "

val String.JWT: String
    get() = "JWT $this"

val String.Companion.PaymentMessage: String
    get() = "payment_message"

const val BD_PHONE_REGEX: String = "^?01[3-9]\\d{8}\$"

fun String?.isValidBdPhoneNumber(): Boolean =
    !this.isNullOrEmpty() && Pattern.compile(BD_PHONE_REGEX).matcher(this).matches()

fun getHashValue(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..16)
        .map { allowedChars.random() }
        .joinToString("")
}

fun isNotEmptyOrNull(data: String?): Boolean = !data.isNullOrEmpty()

fun String?.isValidEmailAddress(): Boolean =
    !this.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.authToken(): String = "Bearer $this"

fun Int.toPercentageValue(value: Int): Int {
    if (this == 0) return 0
    if (value == 0) return 0
    return this.times(100).div(value)
}

fun isEmpty(str: String?): Boolean {
    return str == null || str!!.isNullOrEmpty() || str.trim() == ""
}

fun nullSafetyValue(str: String?): String {
    return if (str == null || str!!.isNullOrEmpty() || str.trim() == "")
        ""
    else
        str

}
//
//fun Number.toLocalString(context: Context): String {
//    return getLocalNumberText(context, this.toString())
//}
//
//fun String.toLocalString(context: Context): String {
//    return getLocalNumberText(context, this.toString())
//}


fun isEmptyNumber(str: String?): Boolean {
    try {
        return str == null || str.trim().equals("") || str.trim().equals("0") || str.trim()
            .equals(".") || str.toDouble() == 0.0
    } catch (e: Exception) {
        return true
    }

}

fun isMultipleofFiveHundred(number: String): Boolean {
    var amount = if (!isEmptyNumber(number)) number.toInt() else 0
    if (amount >= 500 && amount % 500 == 0 && amount <= 25000) {
        return true
    } else return false
}


fun isValidAmount(number: String, min: Double, max: Double): Boolean {
    val amount: Double = if (!isEmptyNumber(number)) number.toDouble() ?: 0.0
    else
        0.0
    return amount in min..max

}

fun getNumber(str: String?): String {
    return try {
        if (str == null || str.trim() == "" || str.trim() == "0" || str.trim() == ".")
            "0"
        else {
            if (isValidNumber(str)) str else "0"
        }
    } catch (e: Exception) {
        "0"
    }
}

fun isValidNumber(str: String): Boolean {
    try {
        str.toDouble()
        return true
    } catch (e: Exception) {
        return false
    }
}

//fun getLocalNumberText(context: Context, value: String): String {
//    return if (context.isBanglaLanguage) {
//        val data = value.replace("1", "১")
//            .replace("2", "২")
//            .replace("3", "৩")
//            .replace("4", "৪")
//            .replace("5", "৫")
//            .replace("6", "৬")
//            .replace("7", "৭")
//            .replace("8", "৮")
//            .replace("9", "৯")
//            .replace("0", "০")
//        data
//    } else value
//}

fun getBdFormatCurrency(taka: String?): String {
    if (isEmpty(taka)) {
        return " ৳ 0"
    } else {
        try {
            return getBdFormatCurrency(taka.toString().toDouble())
        } catch (e: Exception) {
            return " ৳ 0"
        }
    }
//
//    val takaStringBuilder = StringBuilder()
//    return try {
//        if (isEmpty(taka?.trim())) takaStringBuilder.append("0")
//        else takaStringBuilder.append(taka!!.trim())
//        getBdFormatCurrency(takaStringBuilder.toString().toDouble())
//    } catch (e: Exception) {
//        Timber.e("convertion: " + e.message)
//        taka.toString() + "/-"
//    }
}

fun getBdFormatCurrency(taka: Double?): String {
    val takaAmount = if (taka.isNotNull()) taka else 0.0
    val formatter = DecimalFormat("##,##,##,##0.00")
    val formattedTaka = formatter.format(takaAmount)
    return "৳ $formattedTaka"
}

fun getBdFormatCurrencyWithoutMoneySign(taka: Double?): String {
    val takaAmount = if (taka.isNotNull()) taka else 0.0
    val formatter = DecimalFormat("##,##,##,##0.00")
    val formattedTaka = formatter.format(takaAmount)
    return formattedTaka
}

fun getBdFormatCurrencyWithoutMoneySign(taka: String?): String {
    var value = if (taka == null || taka.isNullOrEmpty()) 0.0 else taka?.toDouble()
    val takaAmount = if (value.isNotNull()) value else 0.0
    val formatter = DecimalFormat("##,##,##,##0.00")
    val formattedTaka = formatter.format(takaAmount)
    return formattedTaka
}

fun getBdFormatCurrencyWithoutMoneySignForCreditCard(taka: Double?): String {
    val takaAmount = if (taka.isNotNull()) taka else 0.0
    val formatter = DecimalFormat("########0.00")
    val formattedTaka = formatter.format(takaAmount)
    return formattedTaka
}

fun getAmmountAfterTwoDigitIfFloating(str: String): String {
    if (str.isNullOrEmpty())
        return "0"
    else if (str.contains(".")) {


        return String.format("%.2f", str.toDouble())
    } else return str

}

fun getStringValue(value: Int): String {
    return value.toString()
}

fun getThousandSeparetor(taka: Double?): String {
    val takaAmount = if (taka.isNotNull()) taka else 0.0
    val formatter = DecimalFormat("##,##,##,##0")
    val formattedTaka = formatter.format(takaAmount)
    return formattedTaka
}

fun getPhoneMunber(str: String): String {
    Timber.e(str)
    var str = getFormattedPhone(str)
    if (isValidMobile(str) && ((str.startsWith("+8801") && str.length == 14))) {
        str = str.substring(3, str.length)
    } else if (str.startsWith("8801") && str.length == 13) {
        str = str.substring(2, str.length)

    }


    Timber.d("str" + str)
    return str!!

}

fun getMaskedCardNumber(cardNumber: String?): String {
    when {
        cardNumber.isNullOrBlank() -> return String.EMPTY
        cardNumber.length > 10 -> {
            val first6 = cardNumber.take(6)
            val last4 = cardNumber.takeLast(4)
            val starLength = cardNumber.length - 10
            val starString = StringBuilder()
            for (i in 1..starLength)
                starString.append("*")

            return "$first6$starString$last4"
        }
        else -> {
            return String.EMPTY
        }
    }
}
