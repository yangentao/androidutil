package dev.entao.utilapp

import android.os.Bundle
import yet.ui.activities.PageActivity
import yet.ui.page.BaseFragment

class MainActivity : PageActivity() {

    override fun getInitPage(): BaseFragment? {
        return MainPage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
