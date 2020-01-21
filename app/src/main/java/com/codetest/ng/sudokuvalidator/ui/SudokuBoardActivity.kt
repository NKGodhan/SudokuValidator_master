package com.codetest.ng.sudokuvalidator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codetest.ng.sudokuvalidator.R
import com.codetest.ng.sudokuvalidator.databinding.SudokuBoardActivityBinding
import com.codetest.ng.sudokuvalidator.mvvm.SudokuValidatorViewModel
import kotlinx.android.synthetic.main.sudoku_board_activity.*


class SudokuBoardActivity : AppCompatActivity() {

    private lateinit var viewModel: SudokuValidatorViewModel
    private lateinit var binding: SudokuBoardActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.sudoku_board_activity)

        viewModel = ViewModelProviders.of(this).get(SudokuValidatorViewModel::class.java)

        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()

        viewModel.clickValidate.observe(this, Observer {
            if (it) {

            }
        })

        viewModel.boardInputData.observe(this, Observer {
            sudokuGridView.setGameInput(it)
        })
    }

}
