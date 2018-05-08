package com.ub.utils.base

import com.arellomobile.mvp.MvpPresenter

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>()  {
}