package com.udacity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity() : AppCompatActivity() {

    private var selectedRepository = ""
    private var downloadStatus = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        selectedRepository = intent.getStringExtra(getString(R.string.RepositoryKey))!!
        downloadStatus = intent.getStringExtra(getString(R.string.StatusKey))!!
        val fileName=findViewById<TextView>(R.id.filename_text)
        fileName.text=selectedRepository
        val fileStatus=findViewById<TextView>(R.id.filestatus_text)
        fileStatus.text = downloadStatus
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            finish()
        }
    }

}
