package com.implosion.papers.presentation.ui.screen.main.screen.listener.menu

interface OnNoteItemMenuListener {

    fun markedNote(isMark: Boolean)

    fun getMarkStatus(): Boolean
}