package com.example.diplomich.ViewModel

class UserOrders {
    var totalPrice:String?=null
    var address:String?=null
    var date:String?=null
    var name:String?=null
    var phone:String?=null
    var time:String?=null


    constructor() {}
    constructor(
        totalPrice: String?,
        address: String?,
        date: String?,
        name: String?,
        phone: String?,
        time: String?
    ) {
        this.totalPrice = totalPrice
        this.address = address
        this.date = date
        this.name = name
        this.phone = phone
        this.time = time
    }
}