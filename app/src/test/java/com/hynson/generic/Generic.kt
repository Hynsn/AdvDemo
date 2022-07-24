package com.hynson.generic

data class Generic<T>(
    var a:Int,
    var b:Int,
    var diff:T?
)

data class AirFryDiff(
    // 无法解决同类型参数问题，如果是这种情况可以考虑将两个参数复用为同一个，如x,y -> xy
    var a:Int?,
    var b: String?
){
    constructor(b:Int?) : this(0,null)
    constructor(a:String?) : this(0,a)
}

// 主要函数
data class P(
    var name: String?,
    var age:Int
){
    //次要构造函数
    constructor(int: Int) : this(null,int)
    constructor(s: String?) : this(s,0)
}

class Person {
    var name:String? = null
    var age:Int = 0
    var sex:String? = null

    constructor(name: String) {
        this.name = name
    }
    constructor(sex: String,name: String):this(name){
        this.sex = sex
        this.name = name
    }
    constructor(age: Int){
        this.age = age
    }

    override fun toString(): String {
        return "$name , $age , $sex"
    }
}