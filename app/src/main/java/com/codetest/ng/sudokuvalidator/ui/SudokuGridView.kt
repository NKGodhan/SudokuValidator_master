package com.codetest.ng.sudokuvalidator.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.graphics.Paint.Align
import android.util.AttributeSet

/***
 * Custom view class to create Sudoku grid structure
 */
class SudokuGridView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val gridMargin = 5
    private val gridDimension = 9
    private val subBoxesDimension = 3

    private inner class GridDerivedParams(width: Int, height: Int) {
        internal val step: Float
        internal val xStart: Float
        internal val yStart: Float

        init {
            val adaptWidth = width - 2 * gridMargin
            val adaptHeight = height - 2 * gridMargin
            val colRatio = adaptWidth.toFloat() / gridDimension.toFloat()
            val rowRatio = adaptHeight.toFloat() / gridDimension.toFloat()
            step = Math.min(colRatio, rowRatio)

            val gridWidth = step * gridDimension
            val gridHeight = step * gridDimension
            xStart = (width - gridWidth) / 2
            yStart = (height - gridHeight) / 2
        }
    }

    private var params: GridDerivedParams? = null
    private var input: IntArray? = null
    private var output: IntArray? = null

    init {
        input = null
        output = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        params = GridDerivedParams(width, height)

        /* Background and grid */
        drawGrid(canvas)

        /* Draw all Sudoku cells */
        drawCells(canvas)
    }


    /**
     * Set the Array data on the Grid board
     */
    fun setGameInput(array: IntArray) {
        input = array
        invalidate()
    }


    /**
     * Method to draw the Sudoku puzzle Grid
     */
    private fun drawGrid(canvas: Canvas) {

        val step = params?.step
        val xStart = params?.xStart
        val yStart = params?.yStart

        val paintBG = Paint()
        paintBG.style = Paint.Style.FILL
        paintBG.color = Color.WHITE

        canvas.drawPaint(paintBG)

        /* Grid */
        // paint black lines
        val paintThin = Paint()
        paintThin.color = Color.BLACK

        val paintThick = Paint()
        paintThick.color = Color.BLACK
        paintThick.strokeWidth = 3.0f

        for (x in 0..gridDimension) {
            val paint = if ((x % subBoxesDimension) == 0) paintThick else paintThin
            val pos = xStart!! + x * step!!
            canvas.drawLine(pos, yStart!!, pos, yStart + step * gridDimension, paint)
        }
        for (y in 0..gridDimension) {
            val paint = if ((y % subBoxesDimension) == 0) paintThick else paintThin
            val pos = yStart!! + y * step!!
            canvas.drawLine(xStart!!, pos, xStart + step * gridDimension, pos, paint)
        }
    }


    /**
     * Method to create each cell and the color of text
     */
    private fun drawCells(canvas: Canvas) {
        if (input != null) {
            for (i in input!!.indices) {
                val row = i / gridDimension
                val col = i % gridDimension
                if (input!![i] != 0)
                    setCellText(canvas, col, row, input!![i], Color.BLACK)
            }
        }
    }

    /**
     * Method sets the text inside the Grid cells according to the given data array
     */
    private fun setCellText(canvas: Canvas, col: Int, row: Int, value: Int, color: Int) {

        val text = String.format("%d", value)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.textAlign = Align.CENTER

        paint.textSize = params?.step!! / 2
        val startX = getX(col) + params?.step!! / 2.0f
        val startY = getY(row) + params?.step!! * 0.7f

        paint.color = color
        canvas.drawText(text, startX, startY, paint)
    }

    private fun getX(col: Int): Float {
        return params?.xStart!! + params?.step!! * col
    }

    private fun getY(row: Int): Float {
        return params?.yStart!! + params?.step!! * row
    }
}