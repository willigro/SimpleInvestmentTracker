package com.rittmann.common.datasource.dao

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class SmallTest {

    @Test
    fun tes() {
//        val sort = arrayListOf(1, 3, 6, 4, 1, 2).sorted()
        val sort = arrayListOf(-1, -3).sorted()
//        val sort = arrayListOf(1,2,3).sorted()

        println(sort)

        val bigger = sort.last()
        var smallest = 0
        for (i in 1..bigger) {
            println(i)
            if (sort.contains(i).not()) {
                smallest = i
                break
            }
        }

        when {
            smallest > 0 -> println(smallest)
            bigger == sort.last() && bigger > 1 -> println(bigger + 1)
            else -> println(1)
        }
    }


    @Test
    fun test2() {
        doTest2(21, "To crop or not to crop", "To crop or not to")
        doTest2(
            39,
            "The quick brown fox jumps over the lazy dog",
            "The quick brown fox jumps over the lazy"
        )
        doTest2(100, "Why not", "Why not")
        doTest2(14, "Codility We test coders", "Codility We")
    }

    private fun doTest2(k: Int, sentence: String, expected: String) {
        val split = sentence.split(" ")

        println(split)

        var allowedSentence = ""

        var currentWord: String
        for (i in split.indices) {
            currentWord = split[i]

            if (allowedSentence.length + currentWord.length <= k) {
                allowedSentence += currentWord
                if (i < split.lastIndex && allowedSentence.length <= k) {
                    allowedSentence += " "
                } else
                    break
            } else {
                break
            }
        }

        assertThat(allowedSentence.trimEnd(), `is`(expected))
    }

    @Test
    fun testing3() {
        doTest3(
            arrayOf("pim", "pom"),
            arrayOf("999999999", "777888999"),
            "88999",
            "pom"
        )

        doTest3(
            arrayOf("sander", "amy", "ann", "michael"),
            arrayOf("123456789", "234567890", "789123456", "123123123"),
            "1",
            "ann"
        )

        doTest3(
            arrayOf("adam", "eva", "leo"),
            arrayOf("121212121", "111111111", "444555666"),
            "112",
            "NO CONTACT"
        )
    }

    private fun doTest3(
        names: Array<String>,
        numbers: Array<String>,
        phone: String,
        expected: String
    ) {
        val map = hashMapOf<String, String>()
        for (i in names.indices) {
            if (numbers[i].contains(phone))
                map[names[i]] = numbers[i]
        }
        if (map.size == 0) {
            assertThat("NO CONTACT", `is`(expected))
        } else {

            assertThat(map.toSortedMap().toList().first(), `is`(expected))
        }
    }

    /*
    A = ["pim", "pom"}
    B = ["999999999", "777888999"]
    P = "88999
    return "pom"

    A = ["sander", "amy", "ann", "michael"}
    B = ["123456789", "234567890", "789123456", "123123123"]
    P = "1
    return only "ann", cause it is alphabetic smaller

    A = ["adam", "eva", "leo"}
    B = ["121212121", "111111111", "444555666"]
    P = "112"
    return "NO CONTACT"
    *
     */

    @Test
    fun testing4() {
        doTest4(intArrayOf(6, 1, 4, 6, 3, 2, 7, 4), 3, 2, 24)
        doTest4(intArrayOf(10, 19, 15), 2, 2, -1)
    }

    private fun doTest4(tree: IntArray, alice: Int, bob: Int, expected: Int) {
        if (tree.size < alice + bob)
            assertThat(-1, `is`(expected))
        else {
            var biggestSum = 0
            for (i in tree.indices) {
                var sumAlice = 0

                val lastIndexAlice = i + alice - 1

                if (lastIndexAlice <= tree.lastIndex) {
                    for (a in 0 until alice) {
                        println("alice index ${i + a} value ${tree[i + a]}")
                        sumAlice += tree[i + a]
                    }
                }

                val sumBob = lookBob(tree, i, lastIndexAlice, bob)

                lookBob(tree, i, lastIndexAlice, bob)
                if (sumAlice + sumBob > biggestSum)
                    biggestSum = sumAlice + sumBob
                println("Alice $sumAlice Bob $sumBob Total ${sumAlice + sumBob}")
            }
            assertThat(biggestSum, `is`(expected))
//            val alice1Possibilities = getPossibilities(tree, alice)
//            println(alicePossibilities)
//            val bobPossibilities = getPossibilities(tree, bob)
//            println(bobPossibilities)
        }
    }

    private fun lookBob(
        tree: IntArray,
        initialIndexAlice: Int,
        lastIndexAlice: Int,
        bob: Int
    ): Int {
        var biggerSumBob = 0
        for (i in tree.indices) {
            if (i in initialIndexAlice..lastIndexAlice)
                continue

            val lastLeftIndexBob = i + bob - 1
            val lastRightIndexBob = i + bob - 1

            var sumBob = 0
            if (initialIndexAlice > lastLeftIndexBob || lastRightIndexBob <= tree.lastIndex)
                for (b in 0 until bob) {
                    println("bob index ${i + b} value ${tree[i + b]}")
                    sumBob += tree[i + b]
                }
            println("sum bob $sumBob")
            if (sumBob > biggerSumBob)
                biggerSumBob = sumBob
        }
        return biggerSumBob
    }

    private fun getPossibilities(tree: List<Int>, child: Int): List<Int> {
        val arr = arrayListOf<Int>()
        for (i in tree.indices) {
            var sum = 0
            var nextIndex = 0
            if (i + child - 1 <= tree.lastIndex) {
                for (a in 0 until child) {
                    println("index ${i + nextIndex} value ${tree[i + nextIndex]}")
                    sum += tree[i + nextIndex]
                    nextIndex++
                }
            }
            println(sum)
            arr.add(sum)
        }
        return arr
    }
}