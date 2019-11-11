package edu.newhaven.krikorherlopian.android.vproperty.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.newhaven.krikorherlopian.android.vproperty.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.game.view.*
import kotlinx.android.synthetic.main.question.view.*


class GameFragment : Fragment() {
    var state = 0
    var correctAnswer = 0
    var attemptsCounter = 3
    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.game, container, false)

        val questions = resources.getStringArray(R.array.questions)
        val IMAGES_ARRAY = arrayOf(
            R.drawable.abstraction,
            R.drawable.layering,
            R.drawable.domain_seperation,
            R.drawable.information_hiding,
            R.drawable.least_privilege,
            R.drawable.minimization,
            R.drawable.modularization,
            R.drawable.process_isolation,
            R.drawable.resource_encapsulation,
            R.drawable.simplicity_of_design
        )
        var count = 0
        //shuffle the arrays
        for (ima in IMAGES_ARRAY) {
            var x1 = (0..9).random()
            IMAGES_ARRAY[count] = IMAGES_ARRAY[x1]
            IMAGES_ARRAY[x1] = ima
            var value = questions[count]
            questions[count] = questions[x1]
            questions[x1] = value
            count = count + 1
        }

        correctAnswer = 0
        var options = setOptions(0, questions)
        //shuffle the arrays
        shuffle(options, questions)
        changeFragment(IMAGES_ARRAY, options)
        root!!.button_next.setOnClickListener {
            if (correctAnswer == 0 && frame_container?.option1?.isChecked!!) {
                state = state + 1
                correctAnswer = 0
                next(questions, IMAGES_ARRAY)
            } else if (correctAnswer == 1 && frame_container?.option2?.isChecked!!) {
                state = state + 1
                correctAnswer = 0
                next(questions, IMAGES_ARRAY)
            } else if (correctAnswer == 2 && frame_container?.option3?.isChecked!!) {
                state = state + 1
                correctAnswer = 0
                next(questions, IMAGES_ARRAY)
            } else if (correctAnswer == 3 && frame_container?.option4?.isChecked!!) {
                state = state + 1
                correctAnswer = 0
                next(questions, IMAGES_ARRAY)
            } else if (frame_container?.option4?.isChecked!! == false
                && frame_container?.option3?.isChecked!! == false
                && frame_container?.option2?.isChecked!! == false
                && frame_container?.option1?.isChecked!! == false
            ) {
                Toasty.success(
                    context!!,
                    R.string.choose_option,
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            } else {
                Toasty.error(
                    context!!,
                    R.string.wrong_answer,
                    Toast.LENGTH_SHORT,
                    true
                ).show()
                attemptsCounter = attemptsCounter - 1
                root?.attempts?.text = "" + attemptsCounter
                if (attemptsCounter == 0) {
                    //you lost
                    root?.bottomlayout?.visibility = View.GONE
                    root?.frame_container?.visibility = View.GONE
                    root?.resultlayout?.visibility = View.VISIBLE
                    root?.result?.text = resources.getString(R.string.you_lost)
                    val mPlayer = MediaPlayer.create(context!!, R.raw.youlose)
                    mPlayer.start()
                } else {
                    val mPlayer = MediaPlayer.create(context!!, R.raw.wronganswer)
                    mPlayer.start()
                }
            }
        }

        return root
    }

    fun next(questions: Array<String>, IMAGES_ARRAY: Array<Int>) {
        Toasty.success(
            context!!,
            R.string.correct_answer,
            Toast.LENGTH_SHORT,
            true
        ).show()
        if (state == 10) {
            //you won
            System.out.println("play you won song")
            val mPlayer = MediaPlayer.create(context!!, R.raw.winning)
            mPlayer.start()
            root?.bottomlayout?.visibility = View.GONE
            root?.frame_container?.visibility = View.GONE
            root?.resultlayout?.visibility = View.VISIBLE
            root?.result?.text = resources.getString(R.string.you_won)
        } else {
            val mPlayer = MediaPlayer.create(context!!, R.raw.rightanswer)
            mPlayer.start()
            var options = setOptions(state, questions)
            shuffle(options, questions)
            changeFragment(IMAGES_ARRAY, options)
        }
    }

    fun changeFragment(IMAGES_ARRAY: Array<Int>, options: Array<String>) {
        root?.progress_bar?.setProgress((state + 1), true)
        val fragment = QuestionFragment.newInstance(
            IMAGES_ARRAY[state],
            options[0],
            options[1],
            options[2],
            options[3],
            correctAnswer
        )
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().setCustomAnimations(
            R.animator.card_flip_right_in, R.animator.card_flip_right_out,
            R.animator.card_flip_left_in, R.animator.card_flip_left_out
        ).replace(R.id.frame_container, fragment).commit()

    }

    fun shuffle(options: Array<String>, questions: Array<String>) {
        var count = 0
        for (ima in options) {
            var x1 = (0..3).random()
            var value = options[count]
            options[count] = options[x1]
            options[x1] = value
            if (options[count].equals(questions[state])) {
                correctAnswer = count
            } else if (options[x1].equals(questions[state])) {
                correctAnswer = x1
            }
            count = count + 1
        }
    }

    fun setOptions(i: Int, questions: Array<String>): Array<String> {
        //shuffle options
        var options = arrayOf(questions[i], "1", "2", "3")
        val ques = questions.clone()
        ques[i] = ques[9]
        var x = (0..8).random()
        options[1] = ques[x]
        ques[x] = ques[8]

        x = (0..7).random()
        options[2] = ques[x]
        ques[x] = ques[7]

        x = (0..6).random()
        options[3] = ques[x]
        return options
    }
}