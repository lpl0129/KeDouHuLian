package com.stay.toolslibrary.utils.livedata

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @FileName:
 *          com.safframework.livedata.ReactiveStreams.java
 * @author: Tony Shen
 * @date: 2018-04-20 16:41
 * @version V1.0 <描述当前版本功能>
 */
fun <T> Observable<T>.toLiveData(strategy: BackpressureStrategy = BackpressureStrategy.BUFFER): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this.toFlowable(strategy))

fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)

fun <T> Completable.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this.toFlowable<T>())

fun <T> Single<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this.toFlowable())

fun <T> Maybe<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this.toFlowable())

fun <T> Observable<T>.bindLifecycle(owner: LifecycleOwner, stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): Observable<T> = LifecycleConvert.bindLifecycle(this, owner, stopEvent)

fun <T> Flowable<T>.bindLifecycle(owner: LifecycleOwner, stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): Flowable<T> = LifecycleConvert.bindLifecycle(this, owner, stopEvent)

fun Completable.bindLifecycle(owner: LifecycleOwner, stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): Completable = LifecycleConvert.bindLifecycle(this, owner,stopEvent)

fun Completable.bindLifecycleWithError(owner: LifecycleOwner): Completable = LifecycleConvert.bindLifecycleWithError(this, owner)

fun <T> Single<T>.bindLifecycle(owner: LifecycleOwner, stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): Maybe<T> = LifecycleConvert.bindLifecycle(this, owner,stopEvent)

fun <T> Single<T>.bindLifecycleWithError(owner: LifecycleOwner): Single<T> = LifecycleConvert.bindLifecycleWithError(this, owner)

fun <T> Maybe<T>.bindLifecycle(owner: LifecycleOwner, stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): Maybe<T> = LifecycleConvert.bindLifecycle(this, owner,stopEvent)


fun <T> Flowable<T>.bindScheduler(): Flowable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.bindScheduler(): Observable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())




