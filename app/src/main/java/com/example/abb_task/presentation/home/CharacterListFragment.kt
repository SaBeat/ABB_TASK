package com.example.abb_task.presentation.home

import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.abb_task.R
import com.example.abb_task.R.*
import com.example.abb_task.databinding.FragmentCharacterListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : Fragment(), CharacterListAdapter.CharacterClickListener {
    var characterListBinding:FragmentCharacterListBinding? = null
    private val characterListViewModel: CharacterViewModel by viewModels()
    lateinit var characterListAdapter:CharacterListAdapter
    var genderQuery:String = ""
    var statusQuery:String = ""
    var speciesQuery:String = ""
    var _speciesQuery:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        characterListBinding = FragmentCharacterListBinding.inflate(inflater)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(string.rick_morty_title)
        setupCharacterRecyclerView()
        setUpSwipeToRefresh()
        collectFromViewModel()

        return characterListBinding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setUpSwipeToRefresh() {
        characterListBinding?.rickMortyHomeSwipeRefresh?.apply {
            setOnRefreshListener {
                characterListViewModel.onEvent(
                    CharacterListEvent.GetAllCharacterByName(
                        characterListViewModel.searchQuery.value,characterListViewModel.genderQuery.value,characterListViewModel.statusQuery.value,
                        characterListViewModel.speciesQuery.value
                    )
                )
                characterListBinding!!.rickMortyHomeSwipeRefresh.isRefreshing = false
                characterListBinding!!.rickMortyHomeRecyclerview.scrollToPosition(0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filterCharacter -> {
                showDialogOne()
                Toast.makeText(requireContext(), "Filter is clikced", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_search_menu, menu)
        val item = menu.findItem(R.id.searchCharacterMenu)

        val searchView = item?.actionView as SearchView
        searchView.queryHint = "Type a character name"
        searchView.setQuery(characterListViewModel.searchQuery.value, true)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                performSearchEvent(query)
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun showDialogOne() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        val statusRadioGroup = dialog.findViewById<RadioGroup>(R.id.statusRadioGroup)
        val genderRadioGroup = dialog.findViewById<RadioGroup>(R.id.genderRadioGroup)
        val speciesRadioGroup = dialog.findViewById<RadioGroup>(R.id.speciesRadioGroup)

        statusRadioGroup?.setOnCheckedChangeListener{_,id ->
            when(id){
                R.id.radioAlive -> {
                    statusQuery = "alive"
                    Toast.makeText(requireContext(), "Alive checked", Toast.LENGTH_SHORT).show()
                }
                R.id.radioDead -> {
                    statusQuery = "dead"
                    Toast.makeText(requireContext(), "Dead checked", Toast.LENGTH_SHORT).show()
                }
                R.id.radioUnknown -> {
                    statusQuery = "unknown"
                    Toast.makeText(requireContext(), "Unknown checked", Toast.LENGTH_SHORT).show()
                }
            }
        }

        genderRadioGroup?.setOnCheckedChangeListener{_,id ->
            when(id){
                R.id.radioMale -> {
                    genderQuery = "male"
                }
                R.id.radioFemale -> {
                    genderQuery = "female"
                }
                R.id.radioGenderless -> {
                    genderQuery = "genderless"
                }
                R.id.radioGenderUnknown -> {
                    genderQuery = "unknown"
                }
            }
        }

        speciesRadioGroup?.setOnCheckedChangeListener{_,id ->
            when(id){
                R.id.radioSpecies -> {
                    speciesQuery = _speciesQuery
                }
            }
        }

        dialog.show()
    }

    private fun performSearchEvent(word: String) {
       characterListViewModel.onEvent(CharacterListEvent.GetAllCharacterByName(word,genderQuery,statusQuery,speciesQuery))
    }

    private fun setupCharacterRecyclerView() {
        characterListAdapter = CharacterListAdapter(requireContext())
        characterListAdapter.characterClickListener = this@CharacterListFragment
        characterListBinding?.apply {
            rickMortyHomeRecyclerview.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = characterListAdapter
            }
        }
    }

    private fun collectFromViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterListViewModel.charactersFlow
                    .collectLatest {
                        characterListAdapter.submitData(it)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                characterListViewModel.speciesQuery.collectLatest { species ->
                    _speciesQuery = species
                }
            }
        }
    }

    override fun onCharacterClicked(character: com.example.abb_task.domain.model.Character?) {
        character?.let {
            findNavController().navigate(
                CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                    it
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        characterListBinding = null
    }
}