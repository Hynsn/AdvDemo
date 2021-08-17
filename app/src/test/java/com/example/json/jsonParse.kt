package com.example.json

import com.google.gson.Gson
import org.junit.Test
import java.util.ArrayList

/**
 * Author: Hynsonhou
 * Date: 2021/8/15 16:48
 * Description: json解析测试
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/15   1.0       首次创建
 */
class jsonParse {
    var sb = StringBuilder()

    @Test
    fun parse(){
        sb.append("{\"adjustTempListInC\":[150,155,160,165,170,175\n,180,185,190,195,200,205,210,215,220,225,230,235,240,245,250,255,260,265],\"adjustTempListInF\":[300,305,310,315,320,325,330,335,340,345,350,355,360,365,370,375,380,385,390,395,400,405,410,415,420,425,430,435,440,445,450,455,460,465,470,475,480,485,490,495,500,505,510],\"computableModeList\":[{\"foodItemList\":[{\"itemKey\":\"Type\",\"itemName\":\"Type\",\"subFoodItemList\":[{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Steak_56.png\",\"subItemKey\":\"Steak\",\"subItemName\":\"Steak\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Hamburger_Patty_56.png\",\"subItemKey\":\"Hamburger Patty\",\"subItemName\":\"Hamburger Patty\"}]},{\"itemKey\":\"Tickness\",\"itemName\":\"Tickness\",\"subFoodItemList\":[{\"dependOnSubItemKey\":\"Steak\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_inch_1_56.png\",\"subItemKey\":\"1 inch\",\"subItemName\":\"1 inch\"},{\"dependOnSubItemKey\":\"Steak\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_inch_2_56.png\",\"subItemKey\":\"2 inch\",\"subItemName\":\"2 inch\"},{\"dependOnSubItemKey\":\"Hamburger Patty\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_inch_34_56.png\",\"subItemKey\":\"3/4 inch\",\"subItemName\":\"3/4 inch\"}]},{\"itemKey\":\"Doneness\",\"itemName\":\"Doneness\",\"subFoodItemList\":[{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Blue_56.png\",\"subItemKey\":\"Blue\",\"subItemName\":\"Blue\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":300,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_rare_56.png\",\"subItemKey\":\"Rare\",\"subItemName\":\"Rare\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":360,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_rare_56.png\",\"subItemKey\":\"Medium Rare\",\"subItemName\":\"Medium Rare\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":420,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_56.png\",\"subItemKey\":\"Medium\",\"subItemName\":\"Medium\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":240,\"defaultTempOnF\":460,\"defaultTimeInSecond\":540,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_well_56.png\",\"subItemKey\":\"Medium Well\",\"subItemName\":\"Medium Well\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":480,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"1 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":600,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Blue_56.png\",\"subItemKey\":\"Blue\",\"subItemName\":\"Blue\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":420,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_rare_56.png\",\"subItemKey\":\"Rare\",\"subItemName\":\"Rare\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":540,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_rare_56.png\",\"subItemKey\":\"Medium Rare\",\"subItemName\":\"Medium Rare\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":240,\"defaultTempOnF\":465,\"defaultTimeInSecond\":480,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_56.png\",\"subItemKey\":\"Medium\",\"subItemName\":\"Medium\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":240,\"defaultTempOnF\":465,\"defaultTimeInSecond\":540,\"preheatOpen\":true,\"recipeI\nd\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_well_56.png\",\"subItemKey\":\"Medium Well\",\"subItemName\":\"Medium Well\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":600,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"2 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":780,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"3/4 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_well_56.png\",\"subItemKey\":\"Medium Well\",\"subItemName\":\"Medium Well\",\"workParam\":{\"adjustTempMaxInC\":265,\"adjustTempMaxInF\":505,\"adjustTempMinInC\":260,\"adjustTempMinInF\":500,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":260,\"defaultTempOnF\":500,\"defaultTimeInSecond\":360,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":3,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"3/4 inch\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":660,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}}]}],\"modeImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/Beef.png\",\"modeKey\":\"Beef\",\"modeName\":\"Beef\"},{\"foodItemList\":[{\"itemKey\":\"Type\",\"itemName\":\"Type\",\"subFoodItemList\":[{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Tilapia_Fillet_56.png\",\"subItemKey\":\"Tilapia Filet\",\"subItemName\":\"Tilapia Filet\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Sockeye_Salmon_56.png\",\"subItemKey\":\"Sockeye Salmon\",\"subItemName\":\"Sockeye Salmon\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_Cod_Fillet_56.png\",\"subItemKey\":\"Cod Filet\",\"subItemName\":\"Cod Filet\"}]},{\"itemKey\":\"Weight\",\"itemName\":\"Weight\",\"subFoodItemList\":[{\"dependOnSubItemKey\":\"Tilapia Filet\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_5_56.png\",\"subItemKey\":\"5 Oz\",\"subItemName\":\"5 Oz\"},{\"dependOnSubItemKey\":\"Tilapia Filet\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_55_56.png\",\"subItemKey\":\"5.5 Oz\",\"subItemName\":\"5.5 Oz\"},{\"dependOnSubItemKey\":\"Sockeye Salmon\",\"subItemImageUrl\":\"https://image.vesync.c\nom/defaultImages/AG500_CS_Series/icon_weight_4_56.png\",\"subItemKey\":\"4 Oz\",\"subItemName\":\"4 Oz\"},{\"dependOnSubItemKey\":\"Cod Filet\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_4_56.png\",\"subItemKey\":\"4 Oz-cf\",\"subItemName\":\"4 Oz\"}]},{\"itemKey\":\"Doneness\",\"itemName\":\"Doneness\",\"subFoodItemList\":[{\"dependOnSubItemKey\":\"5 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":205,\"defaultTempOnF\":400,\"defaultTimeInSecond\":480,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"5.5 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":205,\"defaultTempOnF\":400,\"defaultTimeInSecond\":360,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"4 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_rare_56.png\",\"subItemKey\":\"Rare\",\"subItemName\":\"Rare\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":450,\"adjustTempMinInC\":150,\"adjustTempMinInF\":300,\"adjustTimeMaxInSecond\":3600,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":175,\"defaultTempOnF\":350,\"defaultTimeInSecond\":300,\"preheatOpen\":true,\"recipeId\":6,\"recipeName\":\"Air Fry\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":0,\"workMode\":\"AirFry\"}},{\"dependOnSubItemKey\":\"4 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_rare_56.png\",\"subItemKey\":\"Medium Rare\",\"subItemName\":\"Medium Rare\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":450,\"adjustTempMinInC\":150,\"adjustTempMinInF\":300,\"adjustTimeMaxInSecond\":3600,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":175,\"defaultTempOnF\":350,\"defaultTimeInSecond\":300,\"preheatOpen\":true,\"recipeId\":6,\"recipeName\":\"Air Fry\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":0,\"workMode\":\"AirFry\"}},{\"dependOnSubItemKey\":\"4 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_56.png\",\"subItemKey\":\"Medium\",\"subItemName\":\"Medium\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":450,\"adjustTempMinInC\":150,\"adjustTempMinInF\":300,\"adjustTimeMaxInSecond\":3600,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":180,\"defaultTempOnF\":360,\"defaultTimeInSecond\":360,\"preheatOpen\":false,\"recipeId\":6,\"recipeName\":\"Air Fry\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":0,\"workMode\":\"AirFry\"}},{\"dependOnSubItemKey\":\"4 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_well_56.png\",\"subItemKey\":\"Medium Well\",\"subItemName\":\"Medium Well\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":450,\"adjustTempMinInC\":150,\"adjustTempMinInF\":300,\"adjustTimeMaxInSecond\":3600,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":190,\"defaultTempOnF\":370,\"defaultTimeInSecond\":300,\"preheatOpen\":false,\"recipeId\":\n6,\"recipeName\":\"Air Fry\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":0,\"workMode\":\"AirFry\"}},{\"dependOnSubItemKey\":\"4 Oz\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":450,\"adjustTempMinInC\":150,\"adjustTempMinInF\":300,\"adjustTimeMaxInSecond\":3600,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":180,\"defaultTempOnF\":360,\"defaultTimeInSecond\":300,\"preheatOpen\":true,\"recipeId\":6,\"recipeName\":\"Air Fry\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":0,\"workMode\":\"AirFry\"}},{\"dependOnSubItemKey\":\"4 Oz-cf\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_rare_56.png\",\"subItemKey\":\"Rare\",\"subItemName\":\"Rare\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":205,\"defaultTempOnF\":400,\"defaultTimeInSecond\":300,\"preheatOpen\":false,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"4 Oz-cf\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_rare_56.png\",\"subItemKey\":\"Medium Rare\",\"subItemName\":\"Medium Rare\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":205,\"defaultTempOnF\":400,\"defaultTimeInSecond\":420,\"preheatOpen\":false,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"4 Oz-cf\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_56.png\",\"subItemKey\":\"Medium\",\"subItemName\":\"Medium\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":210,\"defaultTempOnF\":410,\"defaultTimeInSecond\":420,\"preheatOpen\":false,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"4 Oz-cf\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_medium_well_56.png\",\"subItemKey\":\"Medium Well\",\"subItemName\":\"Medium Well\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":210,\"defaultTempOnF\":410,\"defaultTimeInSecond\":540,\"preheatOpen\":false,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"4 Oz-cf\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_well_done_56.png\",\"subItemKey\":\"Well Done\",\"subItemName\":\"Well Done\",\"workParam\":{\"adjustTempMaxInC\":230,\"adjustTempMaxInF\":445,\"adjustTempMinInC\":205,\"adjustTempMinInF\":400,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":205,\"defaultTempOnF\":400,\"defaultTimeInSecond\":480,\"preheatOpen\":false,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":false,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":1,\"workMode\":\"AirGrill\"}}]}],\"modeImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/Fish.png\",\"modeKey\":\"Fish\",\"modeName\":\"Fish\"},{\"foodItemList\":[{\"itemKey\":\"Type\",\"itemName\":\"Type\",\"subFoodItemList\":[{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_chicken_breast_56.png\",\"subItemKey\":\"Breast Boneless\",\"subItemName\":\"Breast Boneless\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_chicken_Drumstick_56.png\",\"subItemKey\":\"Drumstick\",\"subItemName\":\"Drumstick\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_chicken_tender_56.png\",\"subItemKey\":\"Chicken Tender\",\"subItemName\":\"Chicken Tender\"},{\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_chicken_Thighs_56.png\",\"subItemKey\":\"Thighs Bone-in\",\"subItemName\":\"Thighs Bone-in\"}]},{\"itemKey\":\"Weight\",\"itemName\":\"Weight\",\"subFoodItemList\":[{\"dependOnSubItemKey\":\"Breast Boneless\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_10_56.png\",\"subItemKey\":\"10 Oz\",\"subItemName\":\"10 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":1020,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Breast Boneless\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_11_56.png\",\"subItemKey\":\"11 Oz\",\"subItemName\":\"11 Oz\",\"workParam\":{\"adjustTempMaxInC\":265,\"adjustTempMaxInF\":505,\"adjustTempMinInC\":260,\"adjustTempMinInF\":500,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":260,\"defaultTempOnF\":500,\"defaultTimeInSecond\":1020,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":3,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Breast Boneless\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_12_56.png\",\"subItemKey\":\"12 Oz\",\"subItemName\":\"12 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":1200,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Drumstick\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_5_56.png\",\"subItemKey\":\"5 Oz\",\"subItemName\":\"5 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":600,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Drumstick\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_6_56.png\",\"subItemKey\":\"6 Oz\",\"subItemName\":\"6 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":780,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\"\n,\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Chicken Tender\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_25_56.png\",\"subItemKey\":\"2.5 Oz\",\"subItemName\":\"2.5 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":230,\"defaultTempOnF\":450,\"defaultTimeInSecond\":300,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}},{\"dependOnSubItemKey\":\"Thighs Bone-in\",\"subItemImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/icon_weight_6_56.png\",\"subItemKey\":\"6 Oz\",\"subItemName\":\"6 Oz\",\"workParam\":{\"adjustTempMaxInC\":255,\"adjustTempMaxInF\":495,\"adjustTempMinInC\":230,\"adjustTempMinInF\":450,\"adjustTimeMaxInSecond\":1800,\"adjustTimeMinInSecond\":60,\"defaultTempOnC\":245,\"defaultTempOnF\":475,\"defaultTimeInSecond\":660,\"preheatOpen\":true,\"recipeId\":16,\"recipeName\":\"Air Grill\",\"recipeType\":3,\"shakeOpen\":true,\"supportPreheat\":true,\"supportShake\":true,\"workLevel\":2,\"workMode\":\"AirGrill\"}}]}],\"modeImageUrl\":\"https://image.vesync.com/defaultImages/AG500_CS_Series/Poultry.png\",\"modeKey\":\"Poultry\",\"modeName\":\"Poultry\"}]}")

//       chose(sb.toString(),"Beef","Type","Steak")
//       chose(sb.toString(),"Fish","Type","Tilapia Filet")
//        chose(sb.toString(),"Fish","Weight","Cod Filet","4 Oz-cf")
//        chose(sb.toString(),"Fish","Doneness","Well Done")

//        choose("s","2","3")


        // 接口1
//        getModeList(sb.toString()).forEach {
//            println(it)
//        }
        // 接口2
        val gson = Gson()
        val config = gson.fromJson(sb.toString(),Config::class.java)

//        getFoodItem(config,"Fish",0,null)?.forEach {
//            println(it)
//        }
        // 接口3
        getFoodList(config,"Fish")?.forEach {
            println(it)
        }
    }

    fun choose(vararg test: String){
        test.forEach {
            println("$it")
        }
    }
    fun getFoodItem(config: Config, modeKey: String, i: Int, dependKey:String?) : List<SubFoodObj>? {
        val list = ArrayList<SubFoodObj>()
        for (modeObj in config.computableModeList) {
            if (modeKey == modeObj.modeKey){
                for (SubFoodObj in modeObj.foodItemList?.get(i)?.subFoodItemList!!) {
                    if(dependKey==null){
                        list.add(SubFoodObj)
                    }
                    else if (dependKey == SubFoodObj.dependOnSubItemKey){
                        list.add(SubFoodObj)
                    }
                }
            }
        }

        return list
    }

    fun getFoodList(config: Config, modeKey: String) : List<FoodObj>? {
        val list = ArrayList<FoodObj>()
        for (modeObj in config.computableModeList) {
            if (modeKey == modeObj.modeKey){
                modeObj.foodItemList?.forEach {
                    it.subFoodItemList = null
                    list.add(it)
                }
            }
        }
        return list
    }

    fun chose(sb: String,mode: String,type:String,dependOnSubItemKey: String?,typeKey:String){
        val gson = Gson()
        val config = gson.fromJson(sb,Config::class.java)

        config.computableModeList.forEach {
            it.foodItemList?.forEach { food ->
                if(it.modeKey == mode) { // 卡片外层使用，传入到内部使用
                    //                                        println(it)
                    for (foodObj in it.foodItemList!!) {
                        if(foodObj.itemKey == type){ // 选了Type之后
                            //                            println(foodObj)
                            foodObj.subFoodItemList?.forEach { subFoodObj ->
                                if(dependOnSubItemKey==null) {
                                    if (subFoodObj.subItemKey == typeKey) {
                                        println(subFoodObj)
                                    }
                                } else{
                                    if(subFoodObj.dependOnSubItemKey==dependOnSubItemKey && subFoodObj.subItemKey==typeKey){
                                        println(subFoodObj)
                                    }
                                }
                            }
                            break
                        }
                    }
                }
                /*food.subFoodItemList.forEach { sub ->
                            println(sub)
                        }*/
            }
        }
    }

    fun getModeList(sb: String): List<ModeObj>{
        val list = ArrayList<ModeObj>()
        val gson = Gson()
        val config = gson.fromJson(sb,Config::class.java)
        config.computableModeList.forEach {
            it.foodItemList = null
            list.add(it)
        }
        return list
    }
}

data class Config(
    val adjustTempListInC:List<Int>,
    val adjustTempListInF:List<Int>,
    val computableModeList:List<ModeObj>
)
data class ModeObj(
    val modeName:String,
    val modeKey:String,
    val modeImageUrl:String,
    var foodItemList:List<FoodObj>?
) {
    constructor(modeName: String,modeKey: String,modeImageUrl:String) :this(modeName, modeKey, modeImageUrl, null)
}

data class FoodObj(
    val itemName:String,
    val itemKey:String,
    var subFoodItemList:List<SubFoodObj>?
)
data class SubFoodObj(
    val dependOnSubItemKey:String,
    val subItemName:String,
    val subItemKey:String,
    val subItemImageUrl:String,
    val workParam: workParam
)

data class workParam(
    val recipeId: Int,
    val recipeType: Int,
    val recipeName: String,
    val workMode:String,
    val defaultTimeInSecond:Int?,
    val defaultTempOnC:Int?,
    val defaultTempOnF:Int?,
    val adjustTimeMinInSecond:Int?,
    val adjustTimeMaxInSecond:Int?,
    val adjustTempMinInC:Int?,
    val adjustTempMaxInC:Int?,
    val adjustTempMinInF:Int?,
    val adjustTempMaxInF:Int?,
    val isSupportPreheat:Boolean?,
    val isPreheatOpen:Boolean?,
    val isSupportShake:Boolean?,
    val isShakeOpen:Boolean?,
)