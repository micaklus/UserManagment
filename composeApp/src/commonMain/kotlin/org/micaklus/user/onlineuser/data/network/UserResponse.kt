package org.micaklus.user.onlineuser.data.network

import kotlinx.serialization.Serializable
import org.micaklus.user.onlineuser.domain.model.Address
import org.micaklus.user.onlineuser.domain.model.Company
import org.micaklus.user.onlineuser.domain.model.Geo
import org.micaklus.user.onlineuser.domain.model.OnlineUser


@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressResponse,
    val phone: String,
    val website: String,
    val company: CompanyResponse
)

@Serializable
data class AddressResponse(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoResponse
)

@Serializable
data class GeoResponse(
    val lat: String,
    val lng: String
)

@Serializable
data class CompanyResponse(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

fun CompanyResponse.toCompany(): Company = Company(
    name = name,
    catchPhrase = catchPhrase,
    bs = bs
)

fun Company.toCompanyResponse(): CompanyResponse = CompanyResponse(
    name = name,
    catchPhrase = catchPhrase,
    bs = bs
)

fun GeoResponse.toGeo(): Geo = Geo(
    lat = lat,
    lng = lng
)

fun Geo.toGeoResponse(): GeoResponse = GeoResponse(
    lat = lat,
    lng = lng
)

fun AddressResponse.toAddress(): Address = Address(
    street = street,
    suite = suite,
    city = city,
    zipcode = zipcode,
    geo = geo.toGeo()
)

fun Address.toAddressResponse(): AddressResponse = AddressResponse(
    street = street,
    suite = suite,
    city = city,
    zipcode = zipcode,
    geo = geo.toGeoResponse()
)

fun UserResponse.toOnlineUser(): OnlineUser = OnlineUser(
    id = id,
    name = name,
    username = username,
    email = email,
    address = address.toAddress(),
    phone = phone,
    website = website,
    company = company.toCompany()
)

fun OnlineUser.toUserResponse(): UserResponse = UserResponse(
    id = id,
    name = name,
    username = username,
    email = email,
    address = address.toAddressResponse(),
    phone = phone,
    website = website,
    company = company.toCompanyResponse()
)