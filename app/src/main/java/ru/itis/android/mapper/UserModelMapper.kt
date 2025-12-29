package ru.itis.android.mapper

import ru.itis.android.db.entity.UserEntity
import ru.itis.android.model.UserDataModel

class UserModelMapper {

    fun map(input: UserDataModel): UserEntity {
        return UserEntity(
            username = input.username,
            password = input.password
        )
    }

    fun map(input: UserEntity): UserDataModel {
        return UserDataModel(
            username = input.username,
            password = input.password
        )
    }
}