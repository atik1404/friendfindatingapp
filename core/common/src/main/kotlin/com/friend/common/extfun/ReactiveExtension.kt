package com.friend.common.extfun

//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
//import io.reactivex.rxjava3.core.Observable
//import io.reactivex.rxjava3.core.Single
//import io.reactivex.rxjava3.schedulers.Schedulers
//
//fun <T : Any> Single<T>.observeOnMainThreadSingleObserver(): Single<T> {
//    return this.subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//}
//
//fun <T : Any> Observable<T>.observeOnMainThreadObservable(): Observable<T> {
//    return this.subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//}