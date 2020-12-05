package com.example.basketballcount.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basketballcount.R
import kotlinx.android.synthetic.main.fragment_start_game.*
import kotlinx.android.synthetic.main.fragment_start_game.view.*

class StartGameFragment : Fragment() {
    var startScoreGame:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_start_game, container, false)
        view.choose_mode_btn{
            initialCheckedIndex=1
            initWithItems {
                listOf("점수","시간")
            }
            onSegmentChecked {segment->
                if(segment.text=="점수"){
                    startScoreGame=1
                }
                else if(segment.text=="시간"){
                    startScoreGame=2
                }
            }
            onSegmentUnchecked {
                startScoreGame=0
            }
            onSegmentRechecked {segment->
                if(segment.text=="점수"){
                    startScoreGame=1
                }
                else if(segment.text=="시간"){
                    startScoreGame=2
                }
            }

        }
        // Inflate the layout for this fragment
        return view
    }


}