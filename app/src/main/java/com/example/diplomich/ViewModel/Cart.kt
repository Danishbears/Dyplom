package com.example.diplomich.ViewModel

class Cart {
    var name: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null
    var pid: String? = null
    var date: String? = null
    var discount:String?=null
    var time: String? = null
    var count:String?=null
    var Currentprice:String?=null

    constructor() {}
    constructor(
        name: String?,
        description: String?,
        price: String?,
        image: String?,
        discount:String?,
        pid: String?,
        date: String?,
        time: String?,
        count:String?,
        Currentprice:String?
    ) {
        this.name = name
        this.description = description
        this.price = price
        this.image = image
        this.pid = pid
        this.date = date
        this.time = time
        this.discount = discount
        this.count = count
        this.Currentprice = Currentprice
    }

}