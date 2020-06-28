package com.bkjcb.rqapplication.actionregister.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Created by DengShuai on 2020/6/24.
 * Description :
 */
@Entity
data class ActionRegisterItem(@Id(assignable = true) var id: Long = 0,
                              var uuid: String? = null,
                              var wid: String? = null,
                              var lid: String?= null,
                              var userId: String?= null,
                              var case_source: String?= null,
                              var zi: String?= null,
                              var di: String?= null,
                              var hao: String?= null,
                              var crime_time: String?= null,
                              var crime_address: String?= null,
                              var party: String?= null,
                              var party_address: String?= null,
                              var party_phone: String?= null,
                              var reporter: String?= null,
                              var reporter_address: String?= null,
                              var reporter_phone: String?= null,
                              var case_introduction: String?= null,
                              var undertaker: String?= null,
                              var undertaker_time: String?= null,
                              var undertaker_opinion: String?= null,
                              var systime: Long = 0,
                              var status: Int = 0,
                              var phoneftp: String?= null
) : Serializable