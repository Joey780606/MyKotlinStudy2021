package pcp.com.mykotlin2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import pcp.com.mykotlin2021.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)  // Don't need if use DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}