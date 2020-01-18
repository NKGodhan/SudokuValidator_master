package com.codetest.ng.sudokuvalidator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codetest.ng.sudokuvalidator.R
import com.codetest.ng.sudokuvalidator.mvvm.SudokuValidatorViewModel
import kotlinx.android.synthetic.main.sudoku_board_activity.*


class SudokuBoardActivity : AppCompatActivity() {

    private var boardDataToValidate: Array<IntArray> = Array(9) { IntArray(9) }

    private lateinit var viewModel: SudokuValidatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudoku_board_activity)

        viewModel = ViewModelProviders.of(this).get(SudokuValidatorViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        clickEvents()

        viewModel.clickValidate.observe(this, Observer {
            if (it) {

            }
        })

        viewModel.validationMessage.observe(this, Observer {
            message.text = it
        })

        viewModel.boardInputData.observe(this, Observer {
            sudokuGridView.setGameInput(it)

            var first: Int
            var last = 8
            val limit = 8
            var inputArrayIndex = 0
            var intArray = IntArray(9)
            for (itr in it.indices) {
                intArray[limit - (last - itr)] = it[itr]

                if ((last - itr) == 0) {
                    boardDataToValidate.set(inputArrayIndex, intArray)
                    first = itr + 1
                    last = first + limit
                    inputArrayIndex++
                    intArray = IntArray(9)
                }
            }
        })
    }

    /**
     * All views click events are define
     */
    private fun clickEvents() {
        validateButton.setOnClickListener {
            if (boardDataToValidate.isNullOrEmpty()) {
                message.text = getText(R.string.no_board_loaded_to_verify_message)
                return@setOnClickListener
            }
            viewModel.provideValidationMessage(boardDataToValidate)
        }

        loadValidBoard.setOnClickListener {
            viewModel.setDataInTheBoard(R.string.valid_sudoku_board_file_name)
        }

        loadInvalidBoard.setOnClickListener {
            viewModel.setDataInTheBoard(R.string.invalid_sudoku_board_file_name)
        }
    }

}
