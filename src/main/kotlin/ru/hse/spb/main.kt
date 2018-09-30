const val ADJECTIVE_M = "lios"
const val ADJECTIVE_W = "liala"
const val VERB_M = "initis"
const val VERB_W = "inites"
const val NOUN_M = "etr"
const val NOUN_W = "etra"

enum class Gender {
    MAN, WOMAN
}

fun checkOneWord(word: String, kind: String): Boolean {
    return word.endsWith(kind)
}

fun getGender(word: String): Gender? {
    var gender: Gender? = null
    if (checkOneWord(word, ADJECTIVE_M))
        gender = Gender.MAN
    if (checkOneWord(word, ADJECTIVE_W))
        gender = Gender.WOMAN
    if (gender == null) {
        if (checkOneWord(word, NOUN_M))
            gender = Gender.MAN
        if (checkOneWord(word, NOUN_W))
            gender = Gender.WOMAN
    }
    return gender
}

fun checkWithGender(sentence: List<String>, adjective: String, noun: String, verb: String): Boolean {
    var position = 0
    while (position < sentence.size && checkOneWord(sentence[position], adjective))
        position++
    if (position == sentence.size || position < sentence.size && !checkOneWord(sentence[position], noun))
        return false
    position++
    while (position < sentence.size && checkOneWord(sentence[position], verb))
        position++
    if (position == sentence.size)
        return true
    return false
}

fun checkPetersLanguage(sentence: List<String>): Boolean {
    val cases = listOf(ADJECTIVE_M, ADJECTIVE_W, NOUN_M, NOUN_W, VERB_M, VERB_W)
    if (sentence.size == 1) {
        return cases.any { checkOneWord(sentence[0], it) }
    }
    val gender = getGender(sentence[0])
    return when (gender) {
        null -> false
        Gender.MAN -> checkWithGender(sentence, ADJECTIVE_M, NOUN_M, VERB_M)
        Gender.WOMAN -> checkWithGender(sentence, ADJECTIVE_W, NOUN_W, VERB_W)
    }
}

fun main(args: Array<String>) {
    val words = readLine()!!.split(' ')
    val result = checkPetersLanguage(words)
    if (result)
        println("YES")
    else
        println("NO")
}