package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val wynikrzutu = findViewById<TextView>(R.id.textView2)
        val sumapunktow = findViewById<TextView>(R.id.textView3)
        val przyciskrzuta = findViewById<Button>(R.id.button)
        val resetuj = findViewById<Button>(R.id.button2)

        val kostka1 = findViewById<ImageView>(R.id.imageView1)
        val kostka2 = findViewById<ImageView>(R.id.imageView2)
        val kostka3 = findViewById<ImageView>(R.id.imageView3)
        val kostka4 = findViewById<ImageView>(R.id.imageView4)
        val kostka5 = findViewById<ImageView>(R.id.imageView5)

        var suma = 0;
        var sumarzutu = 0;
        przyciskrzuta.setOnClickListener {

            var rzut1 = Random.nextInt(1, 7)
            var rzut2 = Random.nextInt(1, 7)
            var rzut3 = Random.nextInt(1, 7)
            var rzut4 = Random.nextInt(1, 7)
            var rzut5 = Random.nextInt(1, 7)
        if(rzut1 == rzut2 || rzut1 == rzut3 || rzut1 == rzut4 || rzut1 == rzut5){
            suma +=rzut1
            sumarzutu +=rzut1
        }
        if(rzut2 == rzut3 || rzut2 == rzut4 || rzut2 == rzut5 || rzut2 == rzut1){
            suma +=rzut2
            sumarzutu +=rzut2
        }
        if(rzut3 == rzut1 || rzut3 == rzut2 || rzut3 == rzut4 || rzut3 == rzut5){
            suma +=rzut3
            sumarzutu +=rzut3
        }
        if(rzut4 == rzut1 || rzut4 == rzut2 || rzut4 == rzut3 || rzut4 == rzut5){
            suma +=rzut4
            sumarzutu +=rzut4
        }
        if(rzut5 == rzut1 || rzut5 == rzut2 || rzut5 == rzut3 || rzut5 == rzut4){
            suma +=rzut5
            sumarzutu +=rzut5
        }
            wynikrzutu.text =  "Wynik tego losowania: $sumarzutu"
            sumapunktow.text = "Wynik gry: $suma"
            sumarzutu = 0

            var rzutowanakosc1 = when (rzut1) {
                1 -> R.drawable.kosc1
                2 -> R.drawable.kosc2
                3 -> R.drawable.kosc3
                4 -> R.drawable.kosc4
                5 -> R.drawable.kosc5
                else -> R.drawable.kosc6
            }
            kostka1.setImageResource(rzutowanakosc1)

            var rzutowanakosc2 = when (rzut2) {
                1 -> R.drawable.kosc1
                2 -> R.drawable.kosc2
                3 -> R.drawable.kosc3
                4 -> R.drawable.kosc4
                5 -> R.drawable.kosc5
                else -> R.drawable.kosc6
            }
            kostka2.setImageResource(rzutowanakosc2)

            var rzutowanakosc3 = when (rzut3) {
                1 -> R.drawable.kosc1
                2 -> R.drawable.kosc2
                3 -> R.drawable.kosc3
                4 -> R.drawable.kosc4
                5 -> R.drawable.kosc5
                else -> R.drawable.kosc6
            }
            kostka3.setImageResource(rzutowanakosc3)

            var rzutowanakosc4 = when (rzut4) {
                1 -> R.drawable.kosc1
                2 -> R.drawable.kosc2
                3 -> R.drawable.kosc3
                4 -> R.drawable.kosc4
                5 -> R.drawable.kosc5
                else -> R.drawable.kosc6
            }
            kostka4.setImageResource(rzutowanakosc4)

            var rzutowanakosc5 = when (rzut5) {
                1 -> R.drawable.kosc1
                2 -> R.drawable.kosc2
                3 -> R.drawable.kosc3
                4 -> R.drawable.kosc4
                5 -> R.drawable.kosc5
                else -> R.drawable.kosc6
            }
            kostka5.setImageResource(rzutowanakosc5)
        }


        resetuj.setOnClickListener {
            sumarzutu = 0;
            suma = 0;
            wynikrzutu.text =  "Wynik tego losowania: 0"
            sumapunktow.text = "Wynik gry: 0"
            kostka1.setImageResource(R.drawable.koscniewiadoma)
            kostka2.setImageResource(R.drawable.koscniewiadoma)
            kostka3.setImageResource(R.drawable.koscniewiadoma)
            kostka4.setImageResource(R.drawable.koscniewiadoma)
            kostka5.setImageResource(R.drawable.koscniewiadoma)
        }
    }

}