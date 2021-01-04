package pcp.com.mykotlin2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import pcp.com.mykotlin2021.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    data class HorseName(var name: String = "", var nickname: String = "")
    // Data class處理1: Data class應該就是由 activity_main.xml 命名的一個 class, 在 <data></data> 裡定義

    private val myName: HorseName = HorseName("91 Horse", "Sheep") //Data class處理2: 宣告一個 HorseName 的變數,並內定預設值

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)  // Don't need if use DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.btnNavigation.setOnClickListener {
            binding.tvMyTestText.text = "Navigation click"
            binding.myHorseClass = myName   //Data class處理3: 把 activity_main.xml 內的 myHorseClass 設為 myName, 這樣 xml 的 tvMyTestText2 值就會變
            Log.v(TAG, "Horse test 001")
            binding.invalidateAll()
        }
    }
}