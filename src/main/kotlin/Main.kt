package jp.simplespace

fun main() {
    var generator = MarkovTextGenerator(3)
    generator.add(mutableListOf("酒を１本飲む。","２本も飲むと楽しい。","５本も飲むと悲しい。","酒は楽しい。","酒は悲しい。","悲しい酒は美空ひばり。"))
    generator.initAll()
    println(generator.generate())
}