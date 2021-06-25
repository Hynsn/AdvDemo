package com.example.ktbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<VB:ViewDataBinding,VM:BaseVM<Fragment>> : Fragment() {
    lateinit var bind:VB
    lateinit var vm:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider = ViewModelProvider(this)
        vm = getVm(provider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (bind == null) {
            bind = getBinding(inflater, container, savedInstanceState)
            try {
                val setMethod = bind.javaClass.getMethod("setActivity", javaClass)
                setMethod.invoke(bind, this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bindView()
            vm.registLifeOwner(viewLifecycleOwner)
            vm.setView(this)
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedState: Bundle?) {
        super.onViewCreated(view, savedState)
        initData(viewLifecycleOwner, savedState)
    }

    override fun onDestroy() {
        val parent = bind?.root.parent as ViewGroup
        parent?.removeView(bind.root)
        super.onDestroy()
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB

    abstract fun getVm(provider: ViewModelProvider?): VM

    abstract fun bindView()

    abstract fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?)
}