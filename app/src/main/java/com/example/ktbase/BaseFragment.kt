package com.example.ktbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<V:ViewDataBinding,VM:BaseVM<Fragment>> : Fragment() {
    protected lateinit var binding:V
    protected var vm:VM = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var provider = ViewModelProvider(this)
        vm = getVm(provider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (binding == null) {
            binding = getBinding(inflater, container, savedInstanceState)
            try {
                val setMethod = binding.javaClass.getMethod("setActivity", javaClass)
                setMethod.invoke(binding, this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bindView()
            vm.registLifeOwner(viewLifecycleOwner)
            vm.setView(this)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedState: Bundle?) {
        super.onViewCreated(view, savedState)
        initData(viewLifecycleOwner, savedState)
    }

    override fun onDestroy() {
        val parent = binding?.root.parent as ViewGroup
        parent?.removeView(binding.root)
        super.onDestroy()
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): V

    protected abstract fun getVm(provider: ViewModelProvider?): VM

    protected abstract fun bindView()

    protected abstract fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?)
}