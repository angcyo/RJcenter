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
excite = {
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
result = gCl.add 100, 200
println result

println(gCl)
/**另一种方式*/
gCl2 = new GClass2(name: "i'm is rsen", age: 18)
println gCl2?.toString()
println gCl2?.name
println gCl2?.age

/**Ta就是java*/
println gCl2.class

println "-----------------------------------------------------"
/**函数的声明*/
def fun1(alv) {
    for (v in 1..<5) {
        println v
    }
}
/**调用*/
fun1 2

/**函数的闭包声明*/
def fun2() {
    println "----fun2------"
    for (str in "A..z") {
        println str
    }
}
/**请注意,,这里不能缺少()括号,因为fun2是函数,而不是闭包,只有闭包才可以,直接调用或者 call() 调用*/
fun2()

/**集合*/
def range = 0..4
println range.class
assert range instanceof List
/**集合*/
def range2 = "A..z"
println range2.class
assert range2 instanceof String

println excite.class
println excite


println "-----------------------------------------------------"
bFun = {
    a, b -> return a + b
}
println bFun.call(4, 5)
println bFun(40, 50)
println "-----------------------------------------------------"
println bFun(400, 500).each {
    a -> println a
}

println "-----------------------------------------------------"
/**?问号的使用,可以防止null空指针异常*/
gCls3 = new GClass2(age: "adf")
println gCls3?.class
println gCls3?.name
println gCls3?.age
/**任何集合都可以用each闭包输出*/
gCls3.each {
    v -> println v
}
println gCls3.all().class
gCls3.all().each {
    value -> println value
}


println "-----------------------------------------------------"
/**函数默认最后一行,就是返回值,可以省略return*/
String fun_s1(){
    "fun_s1"
}
/**也会返回*/
def fun_s2(){
    "fun_s2"
    1
}

println fun_s1()
println fun_s2()

println "-----------------------------------------------------"
System.getProperties().list System.out



println "-----------------------------------------------------"
