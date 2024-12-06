package org.micaklus.user.user.domain.model

data class User(
    val id: Long? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: Int? = null,
    val gender: Int? = null
) {

    fun getGenderEnum(): Gender {
        when (gender) {
            0 -> return Gender.MALE
            1 -> return Gender.FEMALE
            else -> return Gender.OTHER
        }
    }

    companion object {
        val empty = User()
    }
}