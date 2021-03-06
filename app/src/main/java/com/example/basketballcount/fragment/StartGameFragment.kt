package com.example.basketballcount.fragment


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.basketballcount.MainActivity.Companion.database
import com.example.basketballcount.MainActivity.Companion.loseGame
import com.example.basketballcount.MainActivity.Companion.overviewList
import com.example.basketballcount.MainActivity.Companion.result
import com.example.basketballcount.MainActivity.Companion.winGame
import com.example.basketballcount.R
import com.example.basketballcount.ScoreGameActivity
import com.example.basketballcount.viewmodel.WinGameViewModel
import com.example.basketballcount.adaptor.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_start_game.view.*
import java.lang.Integer.parseInt
import kotlin.properties.Delegates

class StartGameFragment : Fragment() {
    var goalScore by Delegates.notNull<Int>()
    var goalTime by Delegates.notNull<Int>()
    var userName by Delegates.notNull<String>()
    var startScoreGame = false

    val ourTeamList= mutableListOf<String>()
    val awayTeamList= mutableListOf<String>()

    var resultWinGame = true
    var resultMyScore = 0
    var resultAwayScore = 0
    var resultAwayName = ""
    var resultGameTime = 0
    var resultGameDate = ""

    var getAway = false
    var getMin = false
    var getSec = false
    var getScore = false
    var readyToStart = false
    private lateinit var startButton: Button
    private lateinit var model: WinGameViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_game, container, false)
        view.add_away_email_btn.setOnClickListener {
            if(view.get_away_email_et.text.toString().isNotEmpty()){
                awayTeamList.add(view.get_away_email_et.text.toString())
                view.away_email_tv.text=view.away_email_tv.text.toString()+"\n"+view.get_away_email_et.text.toString()
                view.get_away_email_et.text=null
            }
        }
        view.add_our_email_btn.setOnClickListener {
            if(view.get_ourteam_email_et.text.toString().isNotEmpty()){
                ourTeamList.add(view.get_ourteam_email_et.text.toString())
                view.our_email_tv.text=view.our_email_tv.text.toString()+"\n"+view.get_ourteam_email_et.text.toString()
                view.get_ourteam_email_et.text=null
            }
        }
        startButton = view.findViewById(R.id.start_game_btn)
        startButton.setOnClickListener {
            if(view.get_ourteam_email_et.text.toString().isNotEmpty()){
                ourTeamList.add(view.get_ourteam_email_et.text.toString())
                view.our_email_tv.text=view.our_email_tv.text.toString()+"\n"+view.get_ourteam_email_et.text.toString()
                view.get_ourteam_email_et.text=null
            }
            if(view.get_away_email_et.text.toString().isNotEmpty()){
                awayTeamList.add(view.get_away_email_et.text.toString())
                view.away_email_tv.text=view.away_email_tv.text.toString()+"\n"+view.get_away_email_et.text.toString()
                view.get_away_email_et.text=null
            }
            if (readyToStart) {
                userName = view.get_away_et.text.toString()
                if (startScoreGame) {
                    goalScore = parseInt(view.decide_score_et.text.toString())
                    goalTime = 0
                } else {
                    goalScore = 0
                    goalTime =
                        parseInt(view.decide_min_et.text.toString()) * 60 + parseInt(view.decide_sec_et.text.toString())
                }

                activity?.let {
                    val intent = Intent(context, ScoreGameActivity::class.java)
                    val makeGson = GsonBuilder().create()
                    val listType: TypeToken<MutableList<String>> = object : TypeToken<MutableList<String>>() {}
                    val strContact = makeGson.toJson(ourTeamList, listType.type)
                    val awayContact=makeGson.toJson(awayTeamList,listType.type)
                    intent.putExtra("our_team_list",strContact)
                    intent.putExtra("away_team_list",awayContact)
                    intent.putExtra("our_team_size",ourTeamList.size)
                    intent.putExtra("away_team_size",awayTeamList.size)
                    intent.putExtra("goal_score", goalScore)
                    intent.putExtra("goal_time", goalTime)
                    intent.putExtra("game_type", startScoreGame)
                    intent.putExtra("away_name", userName)
                    startActivityForResult(intent, 3)
                }

            } else {
                Toast.makeText(context, "게임세팅을 해주세요", Toast.LENGTH_SHORT).show()
            }

        }
        view.choose_mode_btn {
            initialCheckedIndex = 1
            initWithItems {
                listOf("점수", "시간")
            }
            onSegmentChecked { segment ->
                if (segment.text == "점수") {
                    startScoreGame = true
                    view.decide_min_et.visibility = View.INVISIBLE
                    view.decide_sec_et.visibility = View.INVISIBLE
                    view.decide_score_et.visibility = View.VISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                } else if (segment.text == "시간") {
                    startScoreGame = false
                    view.decide_min_et.visibility = View.VISIBLE
                    view.decide_sec_et.visibility = View.VISIBLE
                    view.decide_score_et.visibility = View.INVISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                }
            }
            onSegmentUnchecked {
                startScoreGame = true
            }
            onSegmentRechecked { segment ->
                if (segment.text == "점수") {
                    startScoreGame = true
                    view.decide_min_et.visibility = View.INVISIBLE
                    view.decide_sec_et.visibility = View.INVISIBLE
                    view.decide_score_et.visibility = View.VISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                } else if (segment.text == "시간") {
                    startScoreGame = false
                    view.decide_min_et.visibility = View.VISIBLE
                    view.decide_sec_et.visibility = View.VISIBLE
                    view.decide_score_et.visibility = View.INVISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                }
            }

        }
        view.get_away_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getAway = view.get_away_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getAway = count > 0
                readyToStart()
            }
        })
        view.decide_score_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getScore = view.decide_score_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getScore = count > 0
                readyToStart()
            }
        })
        view.decide_min_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getMin = view.decide_min_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getMin = count > 0
                readyToStart()
            }
        })
        view.decide_sec_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getSec = view.decide_sec_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getSec = count > 0
                readyToStart()
            }
        })
        return view
    }

    private fun readyToStart() {
        readyToStart = if (getAway && ((getMin && getSec) || getScore)) {
            startButton.setBackgroundColor(Color.GREEN)
            true
        } else {
            startButton.setBackgroundColor(Color.GRAY)
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var oursize=0
        var awaysize=0
        if (requestCode == 3) {
            resultAwayName = data!!.getStringExtra("away_name").toString()
            resultMyScore = data.getIntExtra("my_score", 0)
            resultAwayScore = data.getIntExtra("away_score", 0)
            resultGameTime = data.getIntExtra("game_time", 0)
            resultWinGame = data.getBooleanExtra("game_result", true)
            resultGameDate = data.getStringExtra("game_date").toString()
            oursize=data.getIntExtra("our_team_size",0)
            awaysize=data.getIntExtra("away_team_size",0)
            val setRecyclerView=Result(resultWinGame,resultGameTime,resultMyScore,resultAwayScore,resultAwayName,resultGameDate)//livedata로 만들기
            overviewList.add(result.itemCount,setRecyclerView)
            model.setResult(overviewList)
        }
        if (resultWinGame) {
            winGame++
            model.setWinGame(winGame.toString())
        } else {
            loseGame++
            model.setLoseGame(loseGame.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model=ViewModelProvider(requireActivity()).get(WinGameViewModel::class.java)
    }




}