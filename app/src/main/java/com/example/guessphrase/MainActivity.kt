package com.example.guessphrase

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
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
    lateinit var rv: RecyclerView
    var input= ArrayList<String>()
    var limitp=10
    var limitl=10
    var prag = arrayListOf("i ate apple","burgers are great","meat taste awesome","i am happy","i am strong","i am a programmer")
    var prq=prag[(0..(prag.size-1)).random()]
    var stared: String = ""
    var con:Boolean =true
    var old:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        top=findViewById(R.id.tvtop)
        bot=findViewById(R.id.tvguess)
        var add =findViewById<Button>(R.id.submit)
        inin =findViewById<EditText>(R.id.entry)
        rv= findViewById<RecyclerView>(R.id.rvMain)
        rv.adapter = RVAdapter(input)
        rv.layoutManager = LinearLayoutManager(this)
        stared =starit(prq)


        add.setOnClickListener {
            if (!human_is_idiot()) {
                when (con) {

                    true -> {
                        if (limitp > 0) {
                            if (inin.text.toString() == prq) {
                                input.add("your guess is correct")
                                showAlertDialog("congrats do you want to play again", 1)
                            } else {
                                input.add("wrong guess: ${inin.text}")
                                inin.hint = "guess a letter"
                                limitp--
                                con = !con
                            }
                            inin.text.clear()
                        }
                    }
                    false -> {
                        if (limitl > 0) {
                            if (inin.text.toString().count() == 1) {
                                if (old.contains(inin.text.toString())) showAlertDialog(
                                    "don`t guess old answer idiot",
                                    0
                                )
                                else {
                                    var i = lettercheck(prq, inin.text.toString())
                                    if (i.isNotEmpty()) {
                                        dstar(i, prq)
                                        input.add("found ${i.size} ${inin.text}(s)")
                                        limitl--
                                        input.add("$limitl guesses remaining")
                                        old += old + inin.text.toString()
                                        inin.hint = "Guess the full phrase"
                                        con = !con

                                    } else showAlertDialog("it can`t be empty", 0)
                                }
                            } else showAlertDialog("one letter only", 0)
                            inin.text.clear()

                        }
                    }
                }
                if (limitl == 0 && limitp == 0) showAlertDialog("you lost", 1)
                if (!stared.contains("*")) {
                    input.add("you win")
                }

            }
            rv.adapter = RVAdapter(input)
            rv.scrollToPosition(input.size-1)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("top",top.text.toString())
        outState.putString("bot",bot.text.toString())
        outState.putStringArrayList("input",input)
        outState.putInt("limp",limitp)
        outState.putInt("liml",limitl)
        outState.putString("prq",prq)
        outState.putString("stared",stared)
        outState.putString("old",old)
        outState.putBoolean("con",con)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        top.text= savedInstanceState.getString("top")
        bot.text=savedInstanceState.getString("bot")
        input= savedInstanceState.getStringArrayList("input")!!
        limitp=savedInstanceState.getInt("limp")
        limitl=savedInstanceState.getInt("liml")
        prq=savedInstanceState.getString("prq")!!
        stared=savedInstanceState.getString("stared")!!
        old = savedInstanceState.getString("old")!!
        con =savedInstanceState.getBoolean("con")
        rv.adapter = RVAdapter(input)
        if(con){
            inin.hint="Guess the full phrase"
        }else inin.hint="Guess a letter"

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
        for(i in 0 until prq.count()){
            if(prq[i].toString() == letter) {
                pos.add(i)
            }
        }
        
        return pos
    }
    fun dstar(dd:ArrayList<Int>,prq:String){
        var dtext=top.text.toString().toCharArray()
        for (i in dd){
            dtext[i+8]=prq[i]
        }
        top.text=dtext.joinToString("")
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
            if(a==1)dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id ->  finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);//this.recreate();input.clear();rv.adapter = RVAdapter(input);prq=prag[(0..(prag.size-1)).random()]
                })
                    else dialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener {
                    dialog, id -> null
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