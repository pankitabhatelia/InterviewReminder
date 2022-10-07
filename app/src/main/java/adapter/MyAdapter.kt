package adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fragment.CancelledFragment
import fragment.DoneFragment
import fragment.UpcomingFragment

class MyAdapter(fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpcomingFragment()
            }
            1 -> {
                CancelledFragment()
            }
            2 -> {
                DoneFragment()
            }
            else -> UpcomingFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}