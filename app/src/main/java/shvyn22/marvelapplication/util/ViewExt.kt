package shvyn22.marvelapplication.util

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import shvyn22.marvelapplication.R

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun FragmentActivity.recolorAppBar(colorRes: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        (this as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(resources.getColor(colorRes, this.theme))
        )
    }
}

fun FragmentActivity.showBottomBar() {
    val navBar = this.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    navBar.visibility = View.VISIBLE
}

fun FragmentActivity.hideBottomBar() {
    val navBar = this.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    navBar.visibility = View.GONE
}