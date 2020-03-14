package com.stay.toolslibrary.utils.extension


import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

/**
 * Created by Tony Shen on 2017/6/30.
 */

fun Bundle.put(params: Array<out Pair<String, Any>>): Bundle {

    params.forEach {
        val key = it.first
        val value = it.second
        when (value) {
            is Int -> putInt(key, value)
            is IntArray -> putIntArray(key, value)
            is Long -> putLong(key, value)
            is LongArray -> putLongArray(key, value)
            is CharSequence -> putCharSequence(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is FloatArray -> putFloatArray(key, value)
            is Double -> putDouble(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is Char -> putChar(key, value)
            is CharArray -> putCharArray(key, value)
            is Short -> putShort(key, value)
            is ShortArray -> putShortArray(key, value)
            is Boolean -> putBoolean(key, value)
            is BooleanArray -> putBooleanArray(key, value)

            is Serializable -> putSerializable(key, value)
            is Parcelable -> putParcelable(key, value)
            is Bundle -> putAll(value)
            is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(it.first, value as Array<CharSequence>)
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(it.first, value as Array<Parcelable>)
            }

        }
    }
    return this
}

fun <T> MutableList<T>.toArrayList(): ArrayList<T> {
    var list = ArrayList<T>()
    this.forEach {
        list.add(it)
    }
    return list
}
fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> =
        map { Pair(it.key, it.value!!) }.toTypedArray()