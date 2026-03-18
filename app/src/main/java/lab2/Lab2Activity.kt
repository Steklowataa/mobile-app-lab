package lab2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lab3.Lab03Activity
import pl.wsei.pam.lab01.R
import android.content.Intent

class Lab2Activity : AppCompatActivity() {

    fun onClickBoardSize(v: View) {
        val tag: String? = v.tag as String?
        val tokens: List<String>? = tag?.split(" ")

        val rows = tokens?.get(0)?.toIntOrNull() ?: 0
        val columns = tokens?.get(1)?.toIntOrNull() ?: 0

        val intent = Intent(this, Lab03Activity::class.java)

        intent.putExtra("rows", rows)
        intent.putExtra("columns", columns)

//        Toast.makeText(this, "rows: $rows, columns: $columns", Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab2)
    }
}