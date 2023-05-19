package com.hynson.detail.chefsnote

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.hynson.detail.base.BaseViewModel

/**
 * EmptyClassBlock
 */
class ChefsNoteVM() : BaseViewModel() {
    var notes: String =
        "这个价钱不一定啊。那就买个油耗低那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车那就买个油耗低的车的车，这样就 便宜很多了哈哈！这个价钱不一定啊。那就买个油耗低的车，这样就便宜很多了哈哈！"
    val title = MutableLiveData<String>()
    val titleVisible = ObservableBoolean(false)
}