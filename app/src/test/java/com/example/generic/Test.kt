package com.example.generic

fun getDataCfg(mod:String) : LinkedHashMap<String, Generic<AirFryDiff>> {
    val map:LinkedHashMap<String,Generic<AirFryDiff>> = LinkedHashMap()
    when(mod){
        ConfigMod.CS158 -> {
            val pA:Generic<AirFryDiff> = Generic(1,2, AirFryDiff("2"))
            map["A"] = pA
            return map
        }
        ConfigMod.V3PRO -> {
            val pB:Generic<AirFryDiff> = Generic(1,2, AirFryDiff(1))
            map["A"] = pB
        }
    }
    return map
}

fun main() {
    val t = Person("woman")
    val t1 = Person(4)

    println("$t , $t1")

    val p = P("kip",1)
    val p1 = P(2)
    val p2 = P("jack")

    println("$p , $p1 , $p2")

    getDataCfg(ConfigMod.CS158).forEach { u ->
        println("$u ")
    }
}
object ConfigMod {
    var CS158 = "CS158"
    var V3PRO = "V3PRO"
}
