package com.codetest.ng.sudokuvalidator.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.codetest.ng.sudokuvalidator.R
import com.codetest.ng.sudokuvalidator.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/***
 * ViewModel class to observe the events and updates on the client side
 */
class SudokuValidatorViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val validationMessage: MutableLiveData<String> = MutableLiveData()
    val clickValidate: MutableLiveData<Boolean> = MutableLiveData()
    val boardInputData: MutableLiveData<IntArray> = MutableLiveData()

    /**
     * Calls the validate operation and sends the message accordingly
     */
    fun provideValidationMessage(arrayToValidate: Array<IntArray>) {

        if (arrayToValidate.isNullOrEmpty()) {
//            clickValidate.postValue(true)
//            validationMessage.postValue(context.getString(R.string.valid_message))
            return
        }

        if (isValidSudoku(arrayToValidate)) {
            clickValidate.postValue(true)
            validationMessage.postValue(context.getString(R.string.valid_message))
        } else {
            clickValidate.postValue(false)
            validationMessage.postValue(context.getString(R.string.invalid_message))
        }
    }


    /**
     * Fetches the JSON file to load puzzle data on the board
     */
    private fun getSudokuBoard(path: String): JSONArray {
        val inputStream = Utility.getAssetJsonData(context.assets.open(path))
        var jsonStringData = JSONObject()
        var sudokuBoardJsonData = JSONArray()

        inputStream?.apply {
            jsonStringData = JSONObject(this)
        }

        if (jsonStringData.has(Utility.JSON_SUDOKU_BOARD_KEY)) {
            sudokuBoardJsonData = jsonStringData.getJSONArray(Utility.JSON_SUDOKU_BOARD_KEY)
        }

        return sudokuBoardJsonData
    }

    private fun jsonArrayToArray(inputJSONArray: JSONArray): Array<Array<String?>> {
        val outputArray = Array<Array<String?>>(9) { arrayOfNulls(9) }

        for (index in 0 until inputJSONArray.length()) {
            val innerJSONArray = inputJSONArray.get(index) as JSONArray
            for (innerArrayIndex in 0 until innerJSONArray.length()) {
                outputArray[index][innerArrayIndex] = innerJSONArray[innerArrayIndex].toString()
            }
        }

        return outputArray
    }


    //-----------------------------
    // Main business logic
    private fun isValidSudoku(inputBoard: Array<IntArray>?): Boolean {
        if (inputBoard.isNullOrEmpty())
            return false

        val rowsData = arrayOfNulls<HashMap<Int, Int>>(9)
        val columnsData = arrayOfNulls<HashMap<Int, Int>>(9)
        val boxesData = arrayOfNulls<HashMap<Int, Int>>(9)

        for (index in 0..8) {
            rowsData[index] = HashMap()
            columnsData[index] = HashMap()
            boxesData[index] = HashMap()
        }
        var number: Int
        for (rowIndex in 0..8) {
            for (colIndex in 0..8) {
                val cellNumber = inputBoard[rowIndex][colIndex]
                if (inputBoard[rowIndex][colIndex] != 0) {
                    number = cellNumber.toInt()
                    val boxIndex = rowIndex / 3 * 3 + colIndex / 3
                    rowsData[rowIndex]?.put(number, getOrDefault(rowsData[rowIndex], number) + 1)
                    columnsData[colIndex]?.put(
                        number,
                        getOrDefault(columnsData[colIndex], number) + 1
                    )
                    boxesData[boxIndex]?.put(number, getOrDefault(boxesData[boxIndex], number) + 1)

                    if (rowsData[rowIndex]?.get(number)!! > 1 || columnsData[colIndex]?.get(number)!! > 1 || boxesData[boxIndex]?.get(
                            number
                        )!! > 1
                    )
                        return false
                }
            }
        }

        return true
    }

    private fun getOrDefault(hashMap: HashMap<Int, Int>?, key: Int): Int {
        return hashMap?.get(key) ?: 0
    }

    /** JSON Array conversion to Integer array */
    private fun toIntArray(arrayString: String): IntArray {

        try {
            assert(arrayString.toCharArray().size == 81)
            val intArray = IntArray(81)
            for (i in 0 until 81) {
                intArray[i] = arrayString[i].toString().toInt()
            }
            return intArray
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return IntArray(0)

    }

    private fun setDataInTheBoard(fileResource: Int) {
        val stringJsonArray =
            jsonArrayToArray(
                getSudokuBoard(
                    context.getString(fileResource)
                )
            )
        val jsonArray = JSONArray(stringJsonArray)
        var actualInputData = ""

        for (iteration in stringJsonArray.indices) {
            val jsonArrayInnerArray = jsonArray.get(iteration) as JSONArray
            val rowStringArray = Array(9) { String() }
            var stringData = ""

            for (jsonArrayIndex in 0 until jsonArrayInnerArray.length()) {
                rowStringArray[jsonArrayIndex] = jsonArrayInnerArray.get(jsonArrayIndex) as String
                if (rowStringArray[jsonArrayIndex] == ".") {
                    rowStringArray[jsonArrayIndex] = "0"
                }
                stringData += rowStringArray[jsonArrayIndex]
                if (jsonArrayIndex == jsonArrayInnerArray.length() - 1) {
                    actualInputData += stringData
                }
            }
        }

        boardInputData.postValue(toIntArray(actualInputData))
    }

    fun onValidLoadClick() {
        validationMessage.postValue(context.getString(R.string.validate_indicator_message))
        setDataInTheBoard(R.string.valid_sudoku_board_file_name)
    }

    fun onInvalidLoadClick() {
        validationMessage.postValue(context.getString(R.string.validate_indicator_message))
        setDataInTheBoard(R.string.invalid_sudoku_board_file_name)
    }

    fun onValidateClick() {
        if (boardInputData.value != null) {
            val boardDataToValidate: Array<IntArray> = Array(9) { IntArray(9) }

            var first: Int
            var last = 8
            val limit = 8
            var inputArrayIndex = 0
            var intArray = IntArray(9)
            val inputData = boardInputData.value as IntArray
            for (itr in inputData.indices) {
                intArray[limit - (last - itr)] = inputData[itr]

                if ((last - itr) == 0) {
                    boardDataToValidate[inputArrayIndex] = intArray
                    first = itr + 1
                    last = first + limit
                    inputArrayIndex++
                    intArray = IntArray(9)
                }
            }

            provideValidationMessage(boardDataToValidate)
        } else {
            validationMessage.postValue(context.getString(R.string.no_board_loaded_to_verify_message))
        }
    }
}