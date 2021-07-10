package com.example.ktbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import java.lang.reflect.Method

abstract class BaseFragment<VB:ViewDataBinding,VM:BaseVM> : Fragment() {
    lateinit var bind:VB
    lateinit var vm:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider = ViewModelProvider(this)
        vm = getVm(provider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind =  DataBindingUtil.inflate(inflater, layoutId,container,false)
//      bind = getBinding(inflater, container, savedInstanceState)
        try {
            val setMethod: Method = bind::class.java.getMethod("setFrag")
            setMethod.invoke(bind, this)
        } catch (e: Exception) {
            //e.printStackTrace()
        }
        bindView()
        bind.lifecycleOwner = this
        vm.registLifeOwner(viewLifecycleOwner)
        return bind.root
    }

    override fun onViewCreated(view: View, savedState: Bundle?) {
        super.onViewCreated(view, savedState)
        initData(viewLifecycleOwner, savedState)
    }

    override fun onDestroy() {
        val parent = bind.root.parent as? ViewGroup
        parent?.removeView(bind.root)
        super.onDestroy()
    }

    protected fun navigateUp(v:View){
        val controller = Navigation.findNavController(v)
        controller.navigateUp()
    }

    abstract val layoutId: Int

    //abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB

    protected abstract fun getVm(provider: ViewModelProvider?): VM

    protected abstract fun bindView()

    protected abstract fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?)
}