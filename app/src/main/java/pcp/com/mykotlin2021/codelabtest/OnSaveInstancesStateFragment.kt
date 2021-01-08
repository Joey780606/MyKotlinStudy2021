package pcp.com.mykotlin2021.codelabtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pcp.com.mykotlin2021.R
import pcp.com.mykotlin2021.databinding.FragmentCodeLabBinding
import pcp.com.mykotlin2021.databinding.FragmentOnSaveInstancesStateBinding
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OnSaveInstancesStateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val KEY_DIGIT_USER_TYPE = "digit_user_type_key"
const val KEY_VISIT_TIME = "visit_time_key"

//Horse important - 目前只有翻轉有效, crash時做的 onSaveInstanceState(),需真的有crash狀態才能運作
class OnSaveInstancesStateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var ivisit_time: Int = 0
    private var iset_value: Int = 0
    private lateinit var binding: FragmentOnSaveInstancesStateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_on_save_instances_state, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_save_instances_state, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            iset_value = savedInstanceState.getInt(KEY_DIGIT_USER_TYPE, 0)
            ivisit_time = savedInstanceState.getInt(KEY_VISIT_TIME, 0) + 1
        }
        Timber.i("onActivityCreated : " + (savedInstanceState != null) + iset_value + " , " + ivisit_time)

        //binding.etDigit.setText(iset_value.toString())  //Horse important - If we use Databinding, this command is useless.
        binding.digit = iset_value          //Horse important - binding.digit and binding.visitTime are from data area in the fragment_on_save_instances_state.xml
        binding.visitTime = ivisit_time
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Timber.i("onSaveInstanceState : " + (outState != null) + binding.etDigit.text.toString().toInt() + " , " + ivisit_time)
        outState.putInt(KEY_DIGIT_USER_TYPE, binding.etDigit.text.toString().toInt())
        outState.putInt(KEY_VISIT_TIME, ivisit_time)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnSaveInstancesStateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OnSaveInstancesStateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}