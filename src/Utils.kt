import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun stringAsInts(string: String) = string.split(",").asSequence().map { it.toInt() }.toList()

fun <T> List<T>.mutableCopyOf(): MutableList<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
