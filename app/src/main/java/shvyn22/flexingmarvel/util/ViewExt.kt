package shvyn22.flexingmarvel.util

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import shvyn22.flexingmarvel.R

fun FragmentActivity.recolorAppBar(colorRes: Int) {
    (this as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(resources.getColor(colorRes, this.theme))
    )
}

fun FragmentActivity.showBottomBar() {
    val navBar = this.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    navBar.visibility = View.VISIBLE
}

fun FragmentActivity.hideBottomBar() {
    val navBar = this.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    navBar.visibility = View.GONE
}