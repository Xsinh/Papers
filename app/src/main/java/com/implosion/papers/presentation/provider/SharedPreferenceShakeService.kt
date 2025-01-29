package com.implosion.papers.presentation.provider

class SharedPreferenceShakeService(
    private val prefsProvider: SharedPreferenceProvider
) {
    companion object {

        private const val NOTE_LIST_SHAKE_STATE = "note_list_shake_state"
    }

    fun setNoteListShake(isShake: Boolean) {
        prefsProvider.putBoolean(NOTE_LIST_SHAKE_STATE, isShake)
    }

    val isNoteListShake: Boolean
        get() = prefsProvider.getBoolean(NOTE_LIST_SHAKE_STATE)
}