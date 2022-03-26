package com.ericthecoder.shopshopshoppinglist.entities.exception

class DbQueryFailedException(message: String?, cause: Throwable?) : Throwable(message, cause) {

    constructor(message: String) : this(message, null)

    constructor(cause: Throwable) : this(cause.toString(), cause)

    constructor() : this(null, null)
}
