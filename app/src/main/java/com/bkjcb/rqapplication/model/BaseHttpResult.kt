package com.bkjcb.rqapplication.model

abstract class BaseHttpResult<T> : HttpResult() {
    var datas: T? = null
}