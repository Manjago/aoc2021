package graph

import mutableCopyOf

data class SavedPath<V>(val value: MutableList<V> = mutableListOf()) {
    fun visit(vertex: V) : SavedPath<V> {
        value += vertex
        return this
    }
    fun xcopy() : SavedPath<V> = SavedPath(value.mutableCopyOf())
}
