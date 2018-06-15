package com.merlinjobs.currencyexchange.core.use_cases.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class ObservableUseCase<Response, Params>(private val mSubscribeOnScheduler: Scheduler,
                                                   private val mObserverOnScheduler: Scheduler) :
        IObservableUseCase<Response, Params> {

    private val mCompositeDisposables = CompositeDisposable()

    abstract fun buildUseCase(params: Params): Observable<Response>

    override fun execute(params: Params, observer: DisposableObserver<Response>) {
        val observable = buildUseCase(params)
                .subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserverOnScheduler)
        mCompositeDisposables.add(observable.subscribeWith(observer))
    }

    override fun dispose() {
        mCompositeDisposables.dispose()
    }


}