package pcp.com.mykotlin2021.codelabtest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pcp.com.mykotlin2021.InitialFragmentDirections
import pcp.com.mykotlin2021.R
import pcp.com.mykotlin2021.databinding.FragmentCodeLabBinding
import pcp.com.mykotlin2021.databinding.FragmentInitialBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CodeLabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodeLabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCodeLabBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_lab, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val args = CodeLabFragmentArgs.fromBundle(requireArguments())
        binding.tvMyTestText.text = args.showInfo.toString()

        binding.btnIntent.setOnClickListener { view : View ->
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.codelab_implicit_intent_text) + args.showInfo.toString())
            startActivity(shareIntent)
        }

        binding.btnOnSaveInstanceState.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_codeLabFragment_to_onSaveInstancesStateFragment)
        }
        Toast.makeText(context, "ShowInfo: ${args.showInfo}", Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CodeLabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CodeLabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}