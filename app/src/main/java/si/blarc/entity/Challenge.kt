package si.blarc.entity

import java.io.Serializable

data class Challenge (
    val title: String,
    val description: String,
    val reward: Int,
    val assignedTo: String?,
    val assignedFrom: String?,
    val colorId: Int?,
    val avatarId: Int?,
    var completed: Boolean?,
    val dateToDo: String?,
    var id: String?

    ) : Serializable {
    constructor() : this("", "", 0, "", "", null, null, false, "", "")
}
