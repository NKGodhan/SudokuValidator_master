package com.codetest.ng.sudokuvalidator.utils

import java.io.IOException
import java.io.InputStream

/**
 * Utility class
 */
object Utility {
    const val JSON_SUDOKU_BOARD_KEY = "board"

    /**
     * Fetch json from assets folder
     */
    fun getAssetJsonData(assetsStr: InputStream): String? {
        val strJson: String?
        try {
            val size = assetsStr.available()
            val buffer = ByteArray(size)
            assetsStr.read(buffer)
            assetsStr.close()
            strJson = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return strJson
    }
}