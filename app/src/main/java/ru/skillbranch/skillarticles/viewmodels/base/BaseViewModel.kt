package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.*
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel

abstract class BaseViewModel<T : IViewModelState>(initState: T) : ViewModel() {
    val notifications = MutableLiveData<Event<Notify>>()

    val state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    protected val currentState
        get() = state.value!!

    @UiThread
    protected inline fun updateState(update: (currentState: T) -> T) {
        val updateState: T = update(currentState)
        state.value = updateState
    }

    @UiThread
    protected fun notify(content: Notify) {
        notifications.value =
            Event(content)
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        state.observe(owner, Observer { onChanged(it!!) })
    }

    fun observeNotifications(
        owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit
    ) {
        notifications.observe(owner,
            EventObserver {
                onNotify(it)
            })
    }

    protected fun <S> subscribeOnDataSource(
        source: LiveData<S>,
        onChanged: (newValue: S, currentState: T) -> T?
    ) {
        state.addSource(source) {
            state.value = onChanged(it, currentState) ?: return@addSource
        }
    }

    fun saveState(outState: Bundle) {
        currentState.save(outState)
    }

    @Suppress("UNCHECKED_CAST")
    fun restoreState(savedState: Bundle) {
        state.value = currentState.restore(savedState) as T
    }
}

class Event<out E>(private val content: E) {
    var hasBennHandled = false

    fun getContentIfNotHandled(): E? {
        return if (hasBennHandled) null
        else {
            hasBennHandled = true
            content
        }
    }

    fun peekContent(): E = content
}

class EventObserver<E>(private val onEventUnhandleContent: (E) -> Unit) : Observer<Event<E>> {
    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandleContent(it)
        }
    }
}

sealed class Notify(val message: String) {
    data class TextMessage(val msg: String) : Notify(msg)

    data class ActionMessage(
        val msg: String,
        val actionLabel: String,
        val actionHandler: (() -> Unit)?
    ) : Notify(msg)

    data class ErrorMessage(
        val msg: String,
        val errLabel: String,
        val errHandler: (() -> Notify)?
    ) : Notify(msg)
}