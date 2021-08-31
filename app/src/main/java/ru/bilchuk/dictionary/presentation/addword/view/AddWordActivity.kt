package ru.bilchuk.dictionary.presentation.addword.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.bilchuk.dictionary.R
import ru.bilchuk.dictionary.data.models.MediaStoreImage
import ru.bilchuk.dictionary.data.repositories.DictionaryRepository
import ru.bilchuk.dictionary.databinding.ActivityAddWordBinding
import ru.bilchuk.dictionary.domain.converter.DictionaryItemConverter
import ru.bilchuk.dictionary.domain.interactors.DictionaryInteractor
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository
import ru.bilchuk.dictionary.presentation.EXTRA_IMAGE
import ru.bilchuk.dictionary.presentation.addword.viewmodel.AddWordViewModel
import ru.bilchuk.dictionary.presentation.gallery.GalleryActivity
import ru.bilchuk.dictionary.presentation.wordslist.view.MainActivity
import ru.bilchuk.dictionary.utils.ISchedulersProvider
import ru.bilchuk.dictionary.utils.SchedulersProvider


class AddWordActivity : AppCompatActivity() {
    private var binding: ActivityAddWordBinding? = null
    private var viewModel: AddWordViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initUI()
        createViewModel()
        observeLiveData()
        val image: MediaStoreImage? = intent.getParcelableExtra(EXTRA_IMAGE)
        settingImage(image)

        binding?.buttonAdd?.setOnClickListener { v: View? ->
            image?.contentUri?.let { imageUri ->
                viewModel?.addWord(
                    binding?.keywordEditText?.text.toString(),
                    binding?.translationEditText?.text.toString(),
                    imageUri
                )
            }
        }
    }

    private fun settingImage(image: MediaStoreImage?) {


        if (image != null) {
            Glide.with(binding!!.imageAdd)
                .load(image.contentUri.toString().toUri())
                .thumbnail(0.33f)
                .centerCrop()
                .into(binding!!.imageAdd)
        }

        binding?.imageAdd?.setOnClickListener {
            addPicture()
        }
    }

    private fun addPicture() {
        val intent = Intent(this, GalleryActivity::class.java).apply {
            //putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun createViewModel() {
        val dictionaryRepository: IDictionaryRepository = DictionaryRepository(applicationContext)
        val dictionaryInteractor: IDictionaryInteractor =
            DictionaryInteractor(dictionaryRepository, DictionaryItemConverter())
        val schedulersProvider: ISchedulersProvider = SchedulersProvider()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddWordViewModel(dictionaryInteractor, schedulersProvider) as T
            }
        }).get(AddWordViewModel::class.java)
    }


    private fun observeLiveData() {
        viewModel?.textAddedLiveData
            ?.observe(this, { result: Boolean -> processLiveData(result) })
    }

    private fun processLiveData(result: Boolean) {
        if (result) {
            val intent = Intent(this, MainActivity::class.java).apply {
                //putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        } else {
            Snackbar.make(binding!!.root, getString(R.string.input_error), Snackbar.LENGTH_SHORT)
                .show()
        }
    }


}