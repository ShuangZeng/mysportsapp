package com.demo.mysportsapp.features.sports.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.mysportsapp.R
import com.demo.mysportsapp.common.ui.TextPromptDialog
import com.demo.mysportsapp.features.sports.data.SportSearchRepository
import com.demo.mysportsapp.features.sports.vm.TeamSearchViewModel
import com.demo.mysportsapp.features.sports.vm.TeamSearchViewModelViewContract
import dmax.dialog.SpotsDialog

class TeamSearchHostActivity : AppCompatActivity(), TeamSearchViewModelViewContract {

    private lateinit var pg: AlertDialog
    private lateinit var viewModel: TeamSearchViewModel
    lateinit var searchContainerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_search_host_ui)
        init()
        performDefaultAction(savedInstanceState)
    }



    fun init()
    {
        viewModel = ViewModelProvider(this, TeamSearchViewModelFactory(this))[TeamSearchViewModel::class.java]
        pg=SpotsDialog.Builder().setContext(this).build()
        searchContainerView=findViewById(R.id.searchContainer)
            searchContainerView.setOnClickListener {
                val textPromptDialog = TextPromptDialog()
                textPromptDialog.setListener(object : TextPromptDialog.TextPromptChangeListener {
                    override fun onTextChanged(text: String?) {
                        viewModel.onSearchQueryReceived(text!!)
                    }
                })
                if (!textPromptDialog.isAdded) textPromptDialog.show(
                    supportFragmentManager,
                    TextPromptDialog::class.java.simpleName
                )
            }
    }


    private fun performDefaultAction(savedInstanceState: Bundle?) {

        if(savedInstanceState==null)
        {
            supportFragmentManager.beginTransaction().add(R.id.viewContainer,TeamSearchFragment()).commit()
        }
    }

    override fun showPopUpMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        runOnUiThread {
            pg.show()
        }
    }

    override fun hideProgressBar() {
        runOnUiThread {
            pg.hide()
        }
    }

    override fun showDetailScreen() {
        supportFragmentManager.beginTransaction().add(R.id.viewContainer,TeamDetailFragment()).commit()

    }
}

class TeamSearchViewModelFactory(private val view: TeamSearchViewModelViewContract) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamSearchViewModel(view, SportSearchRepository()) as T
    }
}