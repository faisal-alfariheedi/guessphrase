package com.example.guessphrase

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Map.entry

class MainActivity : AppCompatActivity() {
    lateinit var top: TextView
    lateinit var bot: TextView
    lateinit var inin: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        top=findViewById(R.id.tvtop)
        bot=findViewById(R.id.tvguess)
        var input = ArrayList<String>()
        var add =findViewById<Button>(R.id.submit)
        inin =findViewById<EditText>(R.id.entry)
        val rv= findViewById<RecyclerView>(R.id.rvMain)
        var prag = arrayListOf("i ate apple","burgers are great","meat taste awesome","i am happy","i am strong","i am a programmer")
        var limitp=10
        var limitl=10
        var prq=prag[(0..(prag.size-1)).random()]
        var stared: String = ""
        var con:Boolean =true
        var old:String = ""
        rv.adapter = RVAdapter(input)
        rv.layoutManager = LinearLayoutManager(this)
        stared =starit(prq)

        add.setOnClickListener {
            if (!human_is_idiot()) {
                if (limitp > 0 && con) {
                    if (inin.text.toString() == prq) {
                        input.add("your guess is correct")
                        showAlertDialog("do you want to play again",1)
                    } else {
                        input.add("wrong guess: ${inin.text}")
                        inin.hint = "guess a letter"
                        limitp--
                    }
                    inin.text.clear()
                }
                if (limitl > 0 && !con) {
                    if (inin.text.count() == 1) {
                        if(!old.contains(inin.text.toString())) showAlertDialog("don`t guess old answer idiot",0)
                        else {
                            var i = lettercheck(prq, inin.text.toString())
                            if (i.isNotEmpty()) {
                                dstar(i, prq)
                                input.add("found ${i.size} ${inin.text}(s)")
                                input.add("$limitl guesses remaining")
                                old += old + inin.text
                                inin.hint="guess the phrase"
                            } else showAlertDialog("it can`t be empty", 0)
                        }
                    }else showAlertDialog("one letter only",0)
                    inin.text.clear()

                }
                if(limitl==0&&limitp==0)showAlertDialog("you lost",1)
                if (!stared.contains("*")) {
                    input.add("you win")
                }
            }
            rv.adapter = RVAdapter(input)
        }
    }
    fun starit(prq: String):String{
        var star: String =""
        for(i in prq){
            if (i.equals(' ')){
                star=star+" "
            }else{
                star=star+"*"
            }
        }
        top.text="phrase :$star"
        return star
    }
    fun lettercheck(prq: String,letter: String):ArrayList<Int> {
        var pos = ArrayList<Int>()
        for(i in 0..prq.count()){
            if(prq[i].toString() == letter) {
                pos.add(i)
            }
        }
        
        return pos
    }
    fun dstar(dd:ArrayList<Int>,prq:String){
        var dtext=top.text.toString().toCharArray()
        for (i in dd){
            dtext[i]=prq[i]
        }
        top.text=dtext.toString()
    }
    fun human_is_idiot():Boolean{
        if(inin.text.isEmpty()){
            showAlertDialog("don`t be an idiot please",0)
            return true
        }else return false
    }
    fun showAlertDialog(title: String,a:Int) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        if(a==1) {
            alert.setTitle("Game Over")
        }else{
            alert.setTitle("warning")
        }
        // show alert dialog
        alert.show()
    }

}