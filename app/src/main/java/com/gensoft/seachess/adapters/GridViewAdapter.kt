package com.gensoft.seachess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.gensoft.seachess.R

class GridViewAdapter(private val context: Context,private val listener: OnBoxClickListener) : BaseAdapter() {

    private val board = Array(3) { arrayOfNulls<String>(3) }

    override fun getCount(): Int {
        return 9 // Total number of boxes (3x3 grid)
    }

    override fun getItem(position: Int): Any? {
        return null // Not used
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Return the item ID
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val boxView = convertView ?: inflater.inflate(R.layout.grid_view_item, parent, false)

        val btnBox = boxView.findViewById<Button>(R.id.btnBox)
        val row = position / 3
        val col = position % 3

        btnBox.text = board[row][col]
        btnBox.setOnClickListener {
            listener.onBoxClicked(row, col, btnBox)
        }

        return boxView
    }


    fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = null
            }
        }
        notifyDataSetChanged()
    }
}
//Listener interface to handle the logic in the MainActivity
interface OnBoxClickListener {
    fun onBoxClicked(row: Int, col: Int,btnBox: Button)
}
