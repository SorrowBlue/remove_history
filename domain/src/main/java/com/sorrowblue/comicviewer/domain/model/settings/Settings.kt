package com.sorrowblue.comicviewer.domain.model.settings

import com.sorrowblue.comicviewer.domain.model.BaseRequest
import com.sorrowblue.comicviewer.domain.model.SupportExtension
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val useAuth: Boolean = false,
)

class UpdateSettingsRequest(val useAuth: Boolean? = null) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
