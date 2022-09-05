package Adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fragment.CancelledFragment
import fragment.DoneFragment
import fragment.UpcomingFragment

class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return UpcomingFragment()
            }
            1 -> {
                return CancelledFragment()
            }
            2 -> {
                return DoneFragment()
            }
            else -> return UpcomingFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}