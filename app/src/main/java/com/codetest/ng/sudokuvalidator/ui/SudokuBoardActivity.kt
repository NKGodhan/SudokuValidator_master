package com.codetest.ng.sudokuvalidator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codetest.ng.sudokuvalidator.R
import com.codetest.ng.sudokuvalidator.mvvm.SudokuValidatorViewModel
import kotlinx.android.synthetic.main.sudoku_board_activity.*


class SudokuBoardActivity : AppCompatActivity() {

    private lateinit var boardDataToValidate: Array<CharArray>

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

            boardDataToValidate = Array(9) { CharArray(9) }

            var first: Int
            var last = 8
            val limit = 8
            var inputArrayIndex = 0
            var charArray = CharArray(9)
            for (itr in it.indices) {
                charArray[limit - (last - itr)] = it[itr].toChar()

                if ((last - itr) == 0) {
                    boardDataToValidate.set(inputArrayIndex, charArray)
                    first = itr + 1
                    last = first + limit
                    inputArrayIndex++
                    charArray = CharArray(9)
                }
            }
        })
    }

    /**
     * All views click events are define
     */
    private fun clickEvents() {
        validateButton.setOnClickListener {
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
