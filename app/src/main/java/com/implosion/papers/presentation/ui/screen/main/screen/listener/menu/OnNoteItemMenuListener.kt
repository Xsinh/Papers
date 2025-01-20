package com.implosion.papers.presentation.ui.screen.main.screen.listener.menu

interface OnNoteItemMenuListener {

    fun markedNote(noteId: Int, isMark: Boolean)

    fun onNoteDelete(id: Int)
}