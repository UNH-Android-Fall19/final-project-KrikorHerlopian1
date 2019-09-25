package edu.newhaven.krikorherlopian.android.vproperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.newhaven.krikorherlopian.android.vproperty.viewmodel.PropertyViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class PropertyFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propertyViewModel =
            ViewModelProviders.of(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        propertyViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}