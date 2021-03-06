import java.math.BigInteger

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

    data class LiteralPacket(override val version: Int, val data: BigInteger) : Packet(version, 4)
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

    fun bitsToBigInteger(bits: ByteArray, range: IntRange = 0 until bits.size): BigInteger {
        var result = BigInteger.ZERO
        var mult = BigInteger.ONE
        range.reversed().forEach {
            if (bits[it] != zero) {
                result += mult
            }
            mult *= BigInteger.TWO
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

            ParsedPacket(LiteralPacket(version, bitsToBigInteger(list.toByteArray())), index + 5)
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
                1 -> {
                    val subPacketsCount = bitsToInt(bits, startIndex + 7..startIndex + 17)
                    var index = startIndex + 18
                    val subPackets = mutableListOf<Packet>()
                    repeat(subPacketsCount) {
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

    fun versionsSum(packet: Packet): Int {
        var sum = 0
        sum += packet.version
        if (packet is OperatorPacket) {
            sum += packet.subPackets.sumOf { versionsSum(it) }
        }
        return sum
    }

    fun evaluate(packet: Packet): BigInteger {

        if (packet is LiteralPacket) {
            return packet.data
        }
        check(packet is OperatorPacket) { "unexpected type of packet $packet" }

        when (packet.typeId) {
            0 -> return packet.subPackets.sumOf { evaluate(it) }
            1 -> {
                var result = BigInteger.ONE
                packet.subPackets.asSequence().forEach {
                    result *= evaluate(it)
                }
                return result
            }
            2 -> return packet.subPackets.minOf { evaluate(it) }
            3 -> return packet.subPackets.maxOf { evaluate(it) }
            5 -> {
                check(packet.subPackets.size == 2) { "type 5 must have 2 subPackets" }
                val p0 = evaluate(packet.subPackets[0])
                val p1 = evaluate(packet.subPackets[1])
                return if (p0 > p1) BigInteger.ONE else BigInteger.ZERO
            }
            6 -> {
                check(packet.subPackets.size == 2) { "type 6 must have 2 subPackets" }
                val p0 = evaluate(packet.subPackets[0])
                val p1 = evaluate(packet.subPackets[1])
                return if (p0 < p1) BigInteger.ONE else BigInteger.ZERO
            }
            7 -> {
                check(packet.subPackets.size == 2) { "type 7 must have 2 subPackets" }
                val p0 = evaluate(packet.subPackets[0])
                val p1 = evaluate(packet.subPackets[1])
                return if (p0 == p1) BigInteger.ONE else BigInteger.ZERO
            }

            else -> error("unexpected typeId ${packet.typeId}")
        }
    }

    fun part1(input: List<String>): Int = versionsSum(parsePacket(str2BitArray(input[0])).packet)

    fun part2(input: List<String>): BigInteger = evaluate(parsePacket(str2BitArray(input[0])).packet)

    fun Long.toBig() : BigInteger = BigInteger.valueOf(this)

    with(str2BitArray("D2FE28")) {
        check(this.joinToString("") == "110100101111111000101000")
        val parsedPacket = parsePacket(this).packet
        check(parsedPacket is LiteralPacket)
        check(parsedPacket.version == 6) { "fail version in packet $parsedPacket" }
        check(parsedPacket.typeId == 4) { "fail typeId in packet $parsedPacket" }
        check(parsedPacket.data == 2021L.toBig()) { "fail data in packet $parsedPacket" }
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
            check(this.data == 10L.toBig())
        }
        with(parsedPacket.subPackets[1]) {
            check(this is LiteralPacket)
            check(this.data == 20L.toBig())
        }
    }

    with(str2BitArray("EE00D40C823060")) {
        check(this.joinToString("") == "11101110000000001101010000001100100000100011000001100000")
        val parsedPacket = parsePacket(this).packet
        check(parsedPacket is OperatorPacket)
        check(parsedPacket.version == 7) { "fail version in packet $parsedPacket" }
        check(parsedPacket.typeId == 3) { "fail typeId in packet $parsedPacket" }
        check(parsedPacket.lengthTypeId == 1) { "fail lengthTypeId in packet $parsedPacket" }
        check(parsedPacket.subPackets.size == 3)
        with(parsedPacket.subPackets[0]) {
            check(this is LiteralPacket)
            check(this.data == 1L.toBig())
        }
        with(parsedPacket.subPackets[1]) {
            check(this is LiteralPacket)
            check(this.data == 2L.toBig())
        }
        with(parsedPacket.subPackets[2]) {
            check(this is LiteralPacket)
            check(this.data == 3L.toBig())
        }

        with(str2BitArray("8A004A801A8002F478")) {
            val parsedPacket = parsePacket(this).packet
            check(parsedPacket is OperatorPacket)
            check(parsedPacket.version == 4) { "fail version in packet $parsedPacket" }
            check(parsedPacket.subPackets.size == 1)
            with(parsedPacket.subPackets[0]) {
                check(this is OperatorPacket)
                check(this.version == 1)
            }
            check(versionsSum(parsedPacket) == 16)
        }
        with(str2BitArray("620080001611562C8802118E34")) {
            val parsedPacket = parsePacket(this).packet
            check(versionsSum(parsedPacket) == 12)
        }
        with(str2BitArray("C0015000016115A2E0802F182340")) {
            val parsedPacket = parsePacket(this).packet
            check(versionsSum(parsedPacket) == 23)
        }
        with(str2BitArray("A0016C880162017C3686B18A3D4780")) {
            val parsedPacket = parsePacket(this).packet
            check(versionsSum(parsedPacket) == 31)
        }
    }

    check(evaluate(parsePacket(str2BitArray("C200B40A82")).packet) == 3L.toBig())
    check(evaluate(parsePacket(str2BitArray("04005AC33890")).packet) == 54L.toBig())
    check(evaluate(parsePacket(str2BitArray("880086C3E88112")).packet) == 7L.toBig())
    check(evaluate(parsePacket(str2BitArray("CE00C43D881120")).packet) == 9L.toBig())
    check(evaluate(parsePacket(str2BitArray("D8005AC2A8F0")).packet) == 1L.toBig())
    check(evaluate(parsePacket(str2BitArray("F600BC2D8F")).packet) == 0L.toBig())
    check(evaluate(parsePacket(str2BitArray("F600BC2D8F")).packet) == 0L.toBig())
    check(evaluate(parsePacket(str2BitArray("9C005AC2F8F0")).packet) == 0L.toBig())
    check(evaluate(parsePacket(str2BitArray("9C0141080250320F1802104A08")).packet) == 1L.toBig())

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 31)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))

    // 723516735 too low - for Int result
    // 13608418623 tool low for Long result
}
