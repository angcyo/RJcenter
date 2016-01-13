/**直接打印字符串*/
println "angcyo"

/**声明字符串list*/
strs = ["ab", "adf", "asdf", "123235", "asdf234"]

/**不同的声明方法*/
def strs2 = ["2222ab", "222adf", "2222a2sdf", "123235", "asdf234"]

/**枚举打印list内容,it为内部关键字,指向每一条数据*/
strs.each {
    println it
}

/**方法的不同调用方式*/
println("-----------------------------------------------------")

/**覆盖闭包中的默认变量*/
strs2.each { a ->
    println a
}

println "-----------------------------------------------------"
/**Map对象的声明*/
def hash = [name: "Andy", "VPN-#": 45]
hash.each { key, value ->
    println "${key} : ${value}"
}

hash.each {
    k, v ->
        println k
        println v
}

println "-----------------------------------------------------"

/**声明一个闭包*/
def excite = {
    word -> return "this is ${word}"
}

/**可以通过两种方法调用闭包：直接调用或者通过 call() 方法调用。*/
str1 = excite "angcyo"
println str1
str1 = excite("Java")
println str1
str1 = excite.call("Groovy")
println str1

println "-----------------------------------------------------"
/**创建一个对象*/
gCl = new GClass()
gCl.name = "hello groovy"
result = gCl.add 100,200
println result

println(gCl)
/**另一种方式*/
gCl2 = new GClass2(name: "i'm is rsen", age: 18)
println gCl2?.toString()
println gCl2?.name
println gCl2?.age

/**Groovy调用Java类中的方法*/
javamain.main("a")


result = gCl.add{ab, bc->
     ab =1
     bc = 2
}
println result

