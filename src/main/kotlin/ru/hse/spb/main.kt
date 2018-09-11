package ru.hse.spb

const val ADJECTIVE_M = "lios"
const val ADJECTIVE_W = "liala"
const val VERB_M = "initis"
const val VERB_W = "inites"
const val NOUN_M = "etr"
const val NOUN_W = "etra"

val cases = mutableListOf(ADJECTIVE_M, ADJECTIVE_W, NOUN_M, NOUN_W, VERB_M, VERB_W)
enum class Gender {
    MAN, WOMAN, UNKNOWN
}

fun checkOneWord(word: String, kind: String): Boolean {
    return word.endsWith(kind)
}

fun getGender(word: String): Gender {
    var gender = Gender.UNKNOWN
    if (checkOneWord(word, ADJECTIVE_M))
        gender = Gender.MAN
    if (checkOneWord(word, ADJECTIVE_W))
        gender = Gender.WOMAN
    if (gender == Gender.UNKNOWN) {
        if (checkOneWord(word, NOUN_M))
            gender = Gender.MAN
        if (checkOneWord(word, NOUN_W))
            gender = Gender.WOMAN
    }
    return gender
}

fun checkWithGender(sentence: List<String>, adjective: String, noun: String, verb: String): Boolean {
    var pos = 0
    while (pos < sentence.size && checkOneWord(sentence[pos], adjective))
        pos++
    if (pos == sentence.size || pos < sentence.size && !checkOneWord(sentence[pos], noun))
        return false
    pos++
    while (pos < sentence.size && checkOneWord(sentence[pos], verb))
        pos++
    if (pos == sentence.size)
        return true
    return false
}

fun checkPetersLanguage(sentence: List<String>): Boolean {
    if (sentence.size == 1) {
        var answer = false
        for (kind in cases) {
            answer = answer || checkOneWord(sentence[0], kind)
        }
        return answer
    }
    val gender = getGender(sentence[0])
    return when (gender) {
        Gender.UNKNOWN -> false
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