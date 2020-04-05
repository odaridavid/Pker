package com.github.odaridavid.pker;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
class ColorsAdapter : ListAdapter<Color, ColorsAdapter.ColorViewHolder>(ColorDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int): Unit =
        getItem(position).let { holder.bind(it) }

    inner class ColorViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(color: Color) {
            //TODO  Create Layout
        }
    }

    companion object {
        val ColorDiffUtil = object : DiffUtil.ItemCallback<Color>() {
            override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
                return oldItem.hex == newItem.hex
            }
        }
    }
}