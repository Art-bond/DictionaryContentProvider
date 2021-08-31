package ru.bilchuk.dictionary.presentation.wordslist.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import ru.bilchuk.dictionary.R
import ru.bilchuk.dictionary.data.repositories.DictionaryRepository
import ru.bilchuk.dictionary.databinding.ActivityMainBinding
import ru.bilchuk.dictionary.domain.converter.DictionaryItemConverter
import ru.bilchuk.dictionary.domain.interactors.DictionaryInteractor
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor
import ru.bilchuk.dictionary.domain.models.DictionaryItem
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository
import ru.bilchuk.dictionary.presentation.addword.view.AddWordActivity
import ru.bilchuk.dictionary.presentation.wordslist.view.adapter.DictionaryAdapter
import ru.bilchuk.dictionary.presentation.wordslist.viewmodel.DictionaryViewModel
import ru.bilchuk.dictionary.utils.ISchedulersProvider
import ru.bilchuk.dictionary.utils.SchedulersProvider

class MainActivity : AppCompatActivity() {

    private var viewModel: DictionaryViewModel? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.fab.setOnClickListener { v: View? -> startAddWordActivity() }
        initUI()
        createViewModel()
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.loadDataAsyncRx()
    }

    private fun initUI() {
        val divider = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(this, R.drawable.divider)
        if (drawable != null) {
            divider.setDrawable(drawable)
        }
        binding!!.recyclerView.addItemDecoration(divider)
    }

    private fun createViewModel() {
        val dictionaryRepository: IDictionaryRepository = DictionaryRepository(applicationContext)
        val dictionaryInteractor: IDictionaryInteractor =
            DictionaryInteractor(dictionaryRepository, DictionaryItemConverter())
        val schedulersProvider: ISchedulersProvider = SchedulersProvider()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DictionaryViewModel(dictionaryInteractor, schedulersProvider) as T
            }
        }).get(DictionaryViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel!!.progressLiveData.observe(
            this,
            { isVisible: Boolean -> showProgress(isVisible) })
        viewModel!!.dictionaryItemsLiveData.observe(
            this,
            { dictionaryItems: List<DictionaryItem> -> showWords(dictionaryItems) })
        viewModel!!.errorLiveData.observe(this, { throwable: Throwable -> showError(throwable) })
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding!!.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showWords(dictionaryItems: List<DictionaryItem>) {
        Log.d(TAG, "showData called dictionaryItems size = " + dictionaryItems.size)
        val dictionaryAdapter = DictionaryAdapter(dictionaryItems)
        binding!!.recyclerView.adapter = dictionaryAdapter
    }

    private fun showError(throwable: Throwable) {
        Log.e(TAG, "showError called with error = $throwable")
        Snackbar.make(binding!!.root, throwable.toString(), Snackbar.LENGTH_LONG).show()
    }

    private fun startAddWordActivity() {
        startActivity(Intent(this, AddWordActivity::class.java))
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}