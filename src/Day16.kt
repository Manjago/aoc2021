fun main() {

    val dict = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111",
    )

    val zero = 0.toByte()

    abstract class Packet(open val version: Int, open val typeId: Int) {
        override fun toString(): String {
            return "Packet(version=$version, typeId=$typeId)"
        }
    }

    data class LiteralPacket(override val version: Int, val data: Long) : Packet(version, 4)
    data class OperatorPacket(
        override val version: Int,
        override val typeId: Int,
        val lengthTypeId: Int,
        val subPackets: List<Packet>
    ) : Packet(version, typeId)

    fun bitsToInt(bits: ByteArray, range: IntRange): Int {
        var result = 0
        var mult = 1
        range.reversed().forEach {
            if (bits[it] != zero) {
                result += mult
            }
            mult *= 2
        }
        return result
    }

    fun bitsToLong(bits: ByteArray, range: IntRange = 0 until bits.size): Long {
        var result = 0L
        var mult = 1
        range.reversed().forEach {
            if (bits[it] != zero) {
                result += mult
            }
            mult *= 2
        }
        return result
    }

    data class ParsedPacket(val packet: Packet, val index: Int)

    fun parsePacket(bits: ByteArray, startIndex: Int = 0): ParsedPacket {
        val typeId = bitsToInt(bits, startIndex + 3..startIndex + 5)
        val version = bitsToInt(bits, startIndex + 0..startIndex + 2)
        return if (typeId == 4) {

            var index = startIndex + 6
            val list = mutableListOf<Byte>()
            while (bits[index] != zero) {
                (index + 1..index + 4).forEach {
                    list.add(bits[it])
                }
                index += 5
            }
            (index + 1..index + 4).forEach {
                list.add(bits[it])
            }

            ParsedPacket(LiteralPacket(version, bitsToLong(list.toByteArray())), index + 5)
        } else {
            val lengthTypeId = bits[startIndex + 6].toInt()

            when (lengthTypeId) {
                0 -> {
                    val subPacketsLength = bitsToInt(bits, startIndex + 7..startIndex + 21)
                    var index = startIndex + 22
                    val finishIndex = index + subPacketsLength
                    val subPackets = mutableListOf<Packet>()
                    while (index < finishIndex) {
                        val parsedPacket = parsePacket(bits, index)
                        index = parsedPacket.index
                        subPackets.add(parsedPacket.packet)
                    }
                    ParsedPacket(
                        OperatorPacket(
                            version, typeId, lengthTypeId,
                            subPackets.toList()
                        ), index
                    )
                }
                else -> error("unexpected lengthTypeId $lengthTypeId")
            }
        }
    }

    fun str2BitArray(hexInStr: String): ByteArray {
        val len = hexInStr.length * 4
        val result = ByteArray(len)
        var index = 0
        hexInStr.toCharArray().forEach {
            var local = 0
            val s = dict[it]!!
            repeat(4) {
                if (s[local++] == '1') {
                    result[index] = 1
                }
                index++
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    with(str2BitArray("D2FE28")) {
        check(this.joinToString("") == "110100101111111000101000")
        val parsedPacket = parsePacket(this).packet
        check(parsedPacket is LiteralPacket)
        check(parsedPacket.version == 6) { "fail version in packet $parsedPacket" }
        check(parsedPacket.typeId == 4) { "fail typeId in packet $parsedPacket" }
        check(parsedPacket.data == 2021L) { "fail data in packet $parsedPacket" }
    }

    with(str2BitArray("38006F45291200")) {
        check(this.joinToString("") == "00111000000000000110111101000101001010010001001000000000")
        val parsedPacket = parsePacket(this).packet
        check(parsedPacket is OperatorPacket)
        check(parsedPacket.version == 1) { "fail version in packet $parsedPacket" }
        check(parsedPacket.typeId == 6) { "fail typeId in packet $parsedPacket" }
        check(parsedPacket.lengthTypeId == 0) { "fail lengthTypeId in packet $parsedPacket" }
        check(parsedPacket.subPackets.size == 2)
        with(parsedPacket.subPackets[0]) {
            check(this is LiteralPacket)
            check(this.data == 10L)
        }
        with(parsedPacket.subPackets[1]) {
            check(this is LiteralPacket)
            check(this.data == 20L)
        }
    }


    /*
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test_0")
    check(part1(testInput) == 1)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
     */
}
