package com.ub.utils.api.response

import java.util.HashMap

class ErrorResponse {

    var message: String? = null
    var errors: Map<String, List<String>> = HashMap()
}