package com.ub.utils.api.response

class BaseResponse<T : Any> {
    lateinit var result: T
}