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
package splitties.views.dsl.recyclerview

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun View.wrapInRecyclerView(
    horizontal: Boolean = false,
    @IdRes id: Int = View.NO_ID,
    initView: RecyclerView.() -> Unit = {}
): RecyclerView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return recyclerView(id) {
        val contentAdapter = SingleViewAdapter(this@wrapInRecyclerView, vertical = !horizontal)
        layoutManager = contentAdapter.layoutManager
        adapter = contentAdapter
    }.apply(initView)
}
