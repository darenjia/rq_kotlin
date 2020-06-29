package com.bkjcb.rqapplication.base.model

abstract class BaseHttpResult<T> : HttpResult() {
    var datas: T? = null
}