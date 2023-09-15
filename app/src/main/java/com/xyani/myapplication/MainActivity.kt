package com.xyani.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.xyani.game.GoldMinerActivity


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button_second).setOnClickListener {
            startActivity(Intent(this,GoldMinerActivity::class.java))
        }

    }




}