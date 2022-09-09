package shvyn22.flexingmarvel.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexingmarvel.R
import shvyn22.flexingmarvel.databinding.ActivityMainBinding
import shvyn22.flexingmarvel.presentation.main.MainViewModel
import shvyn22.flexingmarvel.util.collectOnLifecycle

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.charactersFragment,
                R.id.seriesFragment, R.id.eventsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        configureMenu()
    }

    private fun configureMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                viewModel.isDarkTheme.collectOnLifecycle(this@MainActivity) { isDarkTheme ->
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_NO
                        else AppCompatDelegate.MODE_NIGHT_YES
                    )
                    menu.findItem(R.id.action_dark_theme).setIcon(
                        if (isDarkTheme) R.drawable.ic_night_mode
                        else R.drawable.ic_light_mode
                    )
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_dark_theme -> {
                        viewModel.editThemePreferences()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}