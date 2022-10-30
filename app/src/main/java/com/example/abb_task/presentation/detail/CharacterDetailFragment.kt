package com.example.abb_task.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.abb_task.R
import com.example.abb_task.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {

    private var detailBinding: FragmentCharacterDetailBinding? = null
    private val args: CharacterDetailFragmentArgs by navArgs()
    lateinit var character:com.example.abb_task.domain.model.Character

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding = FragmentCharacterDetailBinding.inflate(inflater)
        character = args.characterArgs
        setActionBarTitle()
        initView()

        return detailBinding!!.root
    }

    private fun initView(){

        detailBinding?.apply {
            txtDetailName.text = character.name
            txtDetailGender.text = character.gender
            txtDetailOrigin.text = character.origin
            txtDetailStatus.text = character.status
            txtDetailSpecies.text = character.species
            txtDetailType.text = character.type

            Glide.with(requireContext()).load(character.image).into(rickMortyDetailImage)
        }
    }

    private fun setActionBarTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = character.name
    }

    override fun onDestroy() {
        super.onDestroy()
        detailBinding = null
    }

}