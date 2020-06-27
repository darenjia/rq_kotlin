package com.bkjcb.rqapplication.emergency.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Emergency(
        @Id(assignable = true)
        var id: Long = 0,
        var uid: Long = 0,
        var tel: String? = null,
        var area: String? = null,
        var unit: String? = null,
        var remarks: String? = null

)