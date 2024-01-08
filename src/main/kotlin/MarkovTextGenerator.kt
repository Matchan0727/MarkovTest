package jp.simplespace

import com.atilika.kuromoji.ipadic.Tokenizer

class MarkovTextGenerator(val n: Int) {

    val tokenizer = Tokenizer()
    val list1 = mutableListOf<List<String>>()
    val wordsDic = mutableMapOf<List<String>,Int>()
    val mDic = mutableMapOf<List<String>,MutableMap<String,Int>>()
    val beginWordsDic = mutableMapOf<List<String>,Int>()
    val sentences = mutableListOf<String>()
    var result = ""

    fun add(str: String){
        var tokens = tokenizer.tokenize(str)
        val list = mutableListOf("__BEGIN__")
        for(token in tokens){
            list.add(token.surface)
        }
        list.add("__END__")
        if((list.size-2)<n) return
        list1.add(list)
    }
    fun add(strs: List<String>){
        for(str in strs) add(str)
    }
    fun initWordsDic(){
        val words = mutableListOf<List<String>>()
        for(list in list1){
            var i = 0
            while((list.size-i)>=n){
                var j = 0
                var l = mutableListOf<String>()
                while(j<n){
                    l.add(j,list[i+j])
                    j++
                }
                words.add(l)
                i++
            }
        }
        wordsDic.clear()
        wordsDic.putAll(words.groupingBy{it}.eachCount())
        println(wordsDic)
    }
    fun initMDic(){
        for(words in wordsDic.keys){
            var nwords = words.slice(0..n-2)
            var nextWord = words[n-1]
            if(!mDic.containsKey(nwords)) {
                mDic.put(nwords,mutableMapOf())
            }
            wordsDic[words]?.let { mDic[nwords]?.put(nextWord, it) }
        }
        println(mDic)
    }
    fun initBeginWordsDic(){
        for(words in wordsDic.keys){
            if(words[0] == "__BEGIN__"){
                wordsDic[words]?.let { beginWordsDic.put(words.slice(1..n-2),it) }
            }
        }
        println(beginWordsDic)
    }
    fun initAll(){
        initWordsDic()
        initMDic()
        initBeginWordsDic()
    }
    fun generate() : String{
        if(mDic.isEmpty()) return "None"
        sentences.add("__BEGIN__")
        sentences.addAll(randomChoiceBeginWords(beginWordsDic))
        while(true){
            var backWords = sentences.takeLast(n-1)
            var nextWord = mDic[backWords]?.let { randomChoiceMDic(it) }
            if(nextWord == "__END__"){
                break
            }
            nextWord?.let { sentences.add(it) }
        }
        result = sentences.slice(1..sentences.size-1).joinToString("")
        return result
    }
    private fun randomChoiceBeginWords(map: Map<List<String>,Int>) : List<String>{
        var choice = listOf<String>()
        var random = 0.0
        for(str in map.keys.shuffled()){
            var r = map[str]?.let{Math.random()*it}
            if (r != null) {
                if((r > random)){
                    choice = str
                    random = r
                }
            }
        }
        return choice
    }
    private fun randomChoiceMDic(map: MutableMap<String,Int>) : String{
        var choice = ""
        var random = 0.0
        for(str in map.keys.shuffled()){
            var r = map[str]?.let{Math.random()*it}
            if (r != null) {
                if((r > random)){
                    choice = str
                    random = r
                }
            }
        }
        return choice
    }
}