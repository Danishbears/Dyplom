package com.example.diplomich.ViewModel

class UserOrders {
    var id:String?=null
    var totalPrice:String?=null
    var address:String?=null
    var date:String?=null
    var name:String?=null
    var phone:String?=null
    var time:String?=null
    var totalCount:Int?=null
    constructor() {}
    constructor(
        id:String?,
        totalPrice: String?,
        address: String?,
        date: String?,
        name: String?,
        phone: String?,
        time: String?,
        totalCount:Int?
    ) {
        this.id=id
        this.totalPrice = totalPrice
        this.address = address
        this.date = date
        this.name = name
        this.phone = phone
        this.time = time
        this.totalCount = totalCount
    }
}