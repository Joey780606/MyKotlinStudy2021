package pcp.com.mykotlin2021

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pcp.com.mykotlin2021.databinding.ActivityMainBinding
import pcp.com.mykotlin2021.databinding.FragmentInitialBinding
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InitialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InitialFragment : Fragment() {
    private val TAG: String = "InitialFragment"
    private lateinit var binding: FragmentInitialBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    data class HorseName(var name: String = "", var nickname: String = "")
    // Data class處理1: Data class應該就是由 activity_main.xml 命名的一個 class, 在 <data></data> 裡定義

    private val myName: HorseName = HorseName("91 Horse", "Sheep") //Data class處理2: 宣告一個 HorseName 的變數,並內定預設值

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Timber.i("Lifecycle check: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_initial, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_initial, container, false)

        Timber.i("Lifecycle check: onCreateView")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var mSendInfo: Int = 0

        binding.btnDatabinding.setOnClickListener {
            binding.tvMyTestText.text = "Navigation click"
            binding.myHorseClass =
                myName   //Data class處理3: 把 activity_main.xml 內的 myHorseClass 設為 myName, 這樣 xml 的 tvMyTestText2 值就會變
            Log.v(TAG, "Horse test 001")
            binding.invalidateAll()
        }

        binding.btnCodelab.setOnClickListener { view : View ->
            view.findNavController().navigate(InitialFragmentDirections.actionInitialFragmentToCodeLabFragment(mSendInfo))
        }
        Timber.i("Lifecycle check: onActivityCreated")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InitialFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InitialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}