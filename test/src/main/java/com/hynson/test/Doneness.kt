package com.hynson.test

/**
 * Author: Hynsonhou
 * Date: 2021/8/16 14:06
 * Description: 熟度需求实体
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/16   1.0       首次创建
 */


data class ChosedItem(
    val name: String,
    val key: String,
    val chosedName: String,
    val chosedkey: String,
    val iconUrl: String
)

data class Config(
    val adjustTempListInC: List<Int>,
    val adjustTempListInF: List<Int>,
    val computableModeList: List<ModeObj>
)

data class ModeObj(
    val modeName: String,
    val modeKey: String,
    val modeImageUrl: String,
    var foodItemList: List<FoodObj>?
) {
    constructor(modeName: String, modeKey: String, modeImageUrl: String) : this(modeName, modeKey, modeImageUrl, null)
}

data class FoodObj(
    val itemName: String,
    val itemKey: String,
    var subFoodItemList: List<SubFoodObj>?
){
    constructor(itemName: String,itemKey: String) : this(itemName,itemKey,null)
}

data class SubFoodObj(
    val dependOnSubItemKey: String,
    val subItemName: String,
    val subItemKey: String,
    val subItemImageUrl: String,
    val workParam: workParam
)

data class workParam(
    val recipeId: Int,
    val recipeType: Int,
    val recipeName: String,
    val workLevel: Int,
    val workMode: String,
    val defaultTimeInSecond: Int,
    val defaultTempOnC: Int,
    val defaultTempOnF: Int,
    val adjustTimeMinInSecond: Int,
    val adjustTimeMaxInSecond: Int,
    val adjustTempMinInC: Int,
    val adjustTempMaxInC: Int,
    val adjustTempMinInF: Int,
    val adjustTempMaxInF: Int,

    val preheatOpen:Boolean,
    val shakeOpen:Boolean,
    val supportPreheat: Boolean,
    val supportShake: Boolean
)