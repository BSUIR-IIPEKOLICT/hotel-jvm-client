package loshica.hotel

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import loshica.hotel.databinding.ActivityMainBinding
import loshica.hotel.interfaces.IMainActivity
import loshica.hotel.viewModels.RoomViewModel
import loshica.hotel.adapters.FragmentAdapter
import loshica.hotel.viewModels.ConnectionViewModel
import loshica.hotel.views.PageTransformer
import loshica.vendor.LOSActivity

class MainActivity : LOSActivity(), IMainActivity {

    private var layout: ActivityMainBinding? = null
    private val roomViewModel: RoomViewModel by viewModels()
    private val connectionViewModel: ConnectionViewModel by viewModels()

    private var hasConnectionObserver: Observer<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout = ActivityMainBinding.inflate(layoutInflater)
        val fragmentAdapter = FragmentAdapter(this)

        with(layout!!) {
            mainPager.adapter = fragmentAdapter
            mainPager.currentItem = 0
            mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    supportActionBar?.title = resources.getStringArray(R.array.main_tabs)[position]
                }
            })
            mainPager.setPageTransformer(PageTransformer())

            TabLayoutMediator(mainTab, mainPager) { tab: TabLayout.Tab, position: Int ->
                tab.text = resources.getStringArray(R.array.main_tabs)[position]
            }.attach()

            hasConnectionObserver = Observer { if (!it) finish() }

            setContentView(root)
        }
    }

    override fun onStart() {
        super.onStart()
        hasConnectionObserver?.let { connectionViewModel.hasConnection.observe(this, it) }
        connectionViewModel.checkConnection()
    }

    override fun onStop() {
        super.onStop()
        hasConnectionObserver?.let { connectionViewModel.hasConnection.removeObserver(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        roomViewModel.onDestroy()
        connectionViewModel.onDestroy()
    }

    override fun onBackPressed() {
        val pager: ViewPager2? = layout?.mainPager

        if (pager?.currentItem != null && pager.currentItem > 0)
            pager.setCurrentItem(0, true)
        else
            finish()
    }

    override fun swipe(position: Int) {
        layout?.mainPager?.setCurrentItem(position, true)
    }
}