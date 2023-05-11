package api.rest.cookie.com.di

import api.rest.cookie.com.service.UserService
import api.rest.cookie.com.service.impl.UserServiceImpl
import org.koin.dsl.module

val myModule = module {
    single<UserService> { UserServiceImpl() }
}