/*
 * Copyright (c) 2018. Louis Cognault Ayeva Derman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.louiscad.splittiessample.extensions.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

@ObsoleteCoroutinesApi
suspend inline fun <E> ReceiveChannel<E>.doOnEach(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline action: suspend (E) -> Unit
) = CoroutineScope(coroutineContext).launch(context) { consumeEach { action(it) } }

@ObsoleteCoroutinesApi
suspend fun <E> ReceiveChannel<E>.consumeEachAndCancelPrevious(
        context: CoroutineContext = EmptyCoroutineContext,
        skipEquals: Boolean = false,
        action: suspend CoroutineScope.(E) -> Unit
): Unit = coroutineScope {
    var job: Job? = null
    var previousValue: E? = null
    consumeEach { newValue ->
        job?.let {
            if (skipEquals && previousValue == newValue) return@consumeEach else it.cancelAndJoin()
        }
        previousValue = newValue
        job = launch(context) {
            action(newValue)
        }
    }
}
