package by.godevelopment.firebaselearn.common

open class AppException : RuntimeException()

class AccountAlreadyExistsException : AppException()

class AuthException : AppException()