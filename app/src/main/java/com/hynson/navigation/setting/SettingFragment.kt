package com.hynson.navigation.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

import com.hynson.R
import com.hynson.databinding.FragDetailBinding
import com.fastdroid.ktbase.BaseFragment
import com.hynson.navigation.NavigationVM

class SettingFragment : BaseFragment<FragDetailBinding, SetVM>() {
    val TAG: String? = SettingFragment::class.simpleName
    override fun getVm(provider: ViewModelProvider): SetVM {
        return provider.get(SetVM::class.java)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragDetailBinding.inflate(inflater,container,false)

    private var naviVM: NavigationVM? = null
//    val viewmodel: NavigationVM by viewModels()

    override fun bindView() {
        naviVM = ViewModelProvider(requireActivity()).get(NavigationVM::class.java)
        // 该方法正式应用，需要配置pop behavior或者使用下属方法
        //binding.btnBack.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_detailFragment_to_navigation_home));
        bind.btnBack.setOnClickListener { view -> navigateUp(view) }
    }

    override fun initData(owner: LifecycleOwner?, savedInstanceState: Bundle?) {}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override val layoutId: Int
        get() = R.layout.frag_detail

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}