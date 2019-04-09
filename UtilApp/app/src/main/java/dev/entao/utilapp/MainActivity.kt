package dev.entao.utilapp

import android.os.Bundle
import dev.entao.ui.activities.PageActivity
import dev.entao.ui.page.BaseFragment

class MainActivity : PageActivity() {

    override fun getInitPage(): BaseFragment? {
        return MainPage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
