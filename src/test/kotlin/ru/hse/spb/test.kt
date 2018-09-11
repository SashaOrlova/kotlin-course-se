package ru.hse.spb

import org.junit.Test
import org.junit.Assert.*

class ProblemAKtTest {

    @Test
    fun testCheckOneWord() {
        assertTrue(checkOneWord("petr", NOUN_M))
        assertTrue(checkOneWord("nataliala", ADJECTIVE_W))
        assertTrue(checkOneWord("feinites", VERB_W))
        assertFalse(checkOneWord("etis", NOUN_W))
        assertFalse(checkOneWord("kataliala", VERB_W))
        assertFalse(checkOneWord("inites", ADJECTIVE_M))
    }

    @Test
    fun testGetGender() {
        assertEquals(getGender("petra"), Gender.WOMAN)
        assertEquals(getGender("initas"), Gender.UNKNOWN)
        assertEquals(getGender("nuiteretr"), Gender.MAN)
    }

    @Test
    fun testCheckWithGender() {
        assertTrue(checkWithGender("nataliala kataliala vetra feinites".split(' '),
                ADJECTIVE_W, NOUN_W, VERB_W))
        assertFalse(checkWithGender("etis atis animatis etis atis amatis".split(' '),
                ADJECTIVE_M, NOUN_M, VERB_M))
    }

    @Test
    fun testCheckPetersLanguage() {
        assertTrue(checkPetersLanguage(listOf("qweasbvflios")))
        assertTrue(checkPetersLanguage("liala petra inites".split(' ')))
        assertFalse(checkPetersLanguage("petra petra petra".split(' ')))
        assertFalse(checkPetersLanguage("pliala petr".split(' ')))
    }
}