package com.example.abb_task.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abb_task.databinding.CharacterListItemBinding
import javax.inject.Inject

class CharacterListAdapter @Inject constructor(private val context: Context) :
    PagingDataAdapter<com.example.abb_task.domain.model.Character, CharacterListAdapter.CharacterListViewHolder>(DiffUtilCallback()) {
    var characterClickListener: CharacterClickListener? = null

    inner class CharacterListViewHolder(private val binding: CharacterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            characterClickListener
            itemView.setOnClickListener {
                characterClickListener?.onCharacterClicked(
                    getItem(absoluteAdapterPosition)
                )
            }
        }

        fun bindCharacter(character: com.example.abb_task.domain.model.Character) {
            binding.apply {
                txtHomeName.text = character.name
                txtHomeStatus.text = character.status
                txtHomeGender.text = character.gender
                txtHomeSpecies.text = character.species
                Glide.with(context).load(character.image).into(rickMortyHomeImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val binding =
            CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        getItem(position)?.let { holder.bindCharacter(it) }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<com.example.abb_task.domain.model.Character>() {
        override fun areItemsTheSame(oldItem: com.example.abb_task.domain.model.Character, newItem: com.example.abb_task.domain.model.Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: com.example.abb_task.domain.model.Character, newItem: com.example.abb_task.domain.model.Character) =
            oldItem == newItem
    }

    interface CharacterClickListener {
        fun onCharacterClicked(character: com.example.abb_task.domain.model.Character?)
    }
}