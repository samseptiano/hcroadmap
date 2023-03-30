package com.samseptiano.hcroadmap

import java.text.SimpleDateFormat
import java.util.*

object dateConverter{
    fun convertToLocalDate(date: String):String{
        var returnDate = ""

        var month = date.split("/")[0]

        returnDate = date.split("/")[1]+" "+ getMonthName(month.toInt())+" "+date.split("/")[2]
        return returnDate
    }

    private fun getMonthName(month:Int):String{
        var retVal = ""
        when (month) {
            1 -> retVal="Januari"
            2 -> retVal="Februari"
            3 -> retVal="Maret"
            4 -> retVal="April"
            5 -> retVal="Mei"
            6 -> retVal="Juni"
            7 -> retVal="Juli"
            8 -> retVal="Agustus"
            9 -> retVal="September"
            10 -> retVal="Oktober"
            11 -> retVal="November"
            12 -> retVal="Desember"
        }
       return retVal
    }


}