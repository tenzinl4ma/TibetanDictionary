package com.example.data.database

import com.example.data.entity.EnToTb
import com.example.data.entity.TbToEn
import com.example.data.entity.TbToTb

object DictionarySeedData {
    fun entries(): List<EnToTb> = listOf(
        EnToTb(word = "Compassion", definition = "སྙིང་རྗེ།"),
        EnToTb(word = "Altruism", definition = "གཞན་ཕན།"),
        EnToTb(word = "Love", definition = "བྱམས་པ།"),
        EnToTb(word = "Wisdom", definition = "ཤེས་རབ།")
    )

    fun tbToEnEntries(): List<TbToEn> = listOf(
        TbToEn(word = "སྙིང་རྗེ།", definition = "Compassion"),
        TbToEn(word = "གཞན་ཕན།", definition = "Altruism"),
        TbToEn(word = "བྱམས་པ།", definition = "Love"),
        TbToEn(word = "ཤེས་རབ།", definition = "Wisdom")
    )

    fun tbToTbEntries(): List<TbToTb> = listOf(
        TbToTb(word = "སྙིང་རྗེ།", definition = "གནད་དོན་གྱི་དགོས་པ།"),
        TbToTb(word = "བྱམས་པ།", definition = "དགའ་བ་དང་བརྩེ་བ།")
    )
}
