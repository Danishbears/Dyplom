package com.example.diplomich.ViewModel

class Products {
    var name: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null
    var category: String? = null
    var pid: String? = null
    var date: String? = null
    var time: String? = null

    constructor() {}
    constructor(
        name: String?,
        description: String?,
        price: String?,
        image: String?,
        category: String?,
        pid: String?,
        date: String?,
        time: String?
    ) {
        this.name = name
        this.description = description
        this.price = price
        this.image = image
        this.category = category
        this.pid = pid
        this.date = date
        this.time = time
    }
}