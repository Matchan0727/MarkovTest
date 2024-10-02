package jp.simplespace

fun main() {
    var generator = MarkovTextGenerator(3)
    generator.add(mutableListOf("犬はほえる。","犬は泣く。","ほっぺは赤い。","酒に酔う。","酒は友達。","全く前が見えない。"))
    generator.initAll()
    println(generator.generate())
}
