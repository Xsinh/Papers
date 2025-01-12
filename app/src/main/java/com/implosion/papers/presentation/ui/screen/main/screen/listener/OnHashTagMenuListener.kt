package com.implosion.papers.presentation.ui.screen.main.screen.listener

import com.implosion.domain.model.TagModel

interface OnHashTagMenuListener {

    fun findNote(tagModel: TagModel)

    fun deleteHashTag(hashTagId: Int, noteId: Int)

    fun dismissMenu()
}