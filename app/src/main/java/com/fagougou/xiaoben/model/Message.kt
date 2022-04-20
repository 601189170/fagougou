package com.fagougou.xiaoben.model

enum class Speaker { ROBOT, USER }
data class Message(val speaker: Speaker, val content:String)
