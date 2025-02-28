package com.xenon.nocturne.ui.now

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xenon.nocturne.databinding.FragmentNowBinding

class NowFragment : Fragment() {

    private var _binding: FragmentNowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nowViewModel =
            ViewModelProvider(this)[NowViewModel::class.java]

        _binding = FragmentNowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNow
        nowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}