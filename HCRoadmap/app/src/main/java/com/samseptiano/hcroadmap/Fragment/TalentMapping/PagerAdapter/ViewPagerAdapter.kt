package com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter


import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.samseptiano.hcroadmap.Fragment.TalentMapping.CompareFragment
import com.samseptiano.hcroadmap.R


class ViewPagerAdapter internal constructor(
    context: Context,
    data: List<Fragment>,
    viewPager2: ViewPager2,
    cf:CompareFragment
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    private val mData: List<Fragment>
    private val context: Context
    private val cf: CompareFragment

    private val mInflater: LayoutInflater
    private val viewPager2: ViewPager2
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_viewpager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val animal = mData[position]
        val ft: FragmentTransaction =  cf.parentFragmentManager.beginTransaction()
        ft.replace(holder.frameLayout.id, mData.get(position))
        ft.commit()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var frameLayout: FrameLayout


        init {
            frameLayout = itemView.findViewById(R.id.fragmentViewPager2)
//            relativeLayout = itemView.findViewById(R.id.container)
//            button = itemView.findViewById(R.id.btnToggle)
//            button.setOnClickListener(object : DialogInterface.OnClickListener() {
//                fun onClick(v: View?) {
//                    if (viewPager2.orientation == ViewPager2.ORIENTATION_VERTICAL) viewPager2.orientation =
//                        ViewPager2.ORIENTATION_HORIZONTAL else {
//                        viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
//                    }
//                }
//            })
        }
    }

    init {
        mInflater = LayoutInflater.from(context)
        mData = data
        this.context = context
        this.cf = cf
        this.viewPager2 = viewPager2
    }
}
