package com.implosion.papers.presentation.ui.screen.main.screen.listener

interface OnNoteClickListener {

    fun onNoteClick(id: Int)

    fun onNoteLongClick(id: Int)

    fun onNoteLongClickDismiss()

    fun onNoteDelete(id: Int)
}