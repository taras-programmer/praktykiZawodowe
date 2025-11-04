package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

data class Slowko(
    val slowo: String,
    val tlumaczenie: String,
    val grupa: String = "Bez grupy"
)

class MainActivity : AppCompatActivity() {

    private var slowka: MutableList<Slowko> = mutableListOf()
    private var aktualneSlowko: Slowko? = null
    private var punkty = 0
    private var liczbaPytan = 0
    private var wybranaGrupa: String = "Wszystkie"
    private var slowkaDoTestu: MutableList<Slowko> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editSlowo = findViewById<EditText>(R.id.editSlowo)
        val editTlumaczenie = findViewById<EditText>(R.id.editTlumaczenie)
        val editGrupa = findViewById<EditText>(R.id.editGrupa)
        val buttonDodaj = findViewById<Button>(R.id.buttonDodaj)
        val editLista = findViewById<EditText>(R.id.editLista)
        val buttonDodajListe = findViewById<Button>(R.id.buttonDodajListe)
        val buttonStartTestu = findViewById<Button>(R.id.buttonStartTestu)
        val buttonPokaz = findViewById<Button>(R.id.buttonPokaz)
        val buttonUkryj = findViewById<Button>(R.id.buttonUkryj)
        val buttonUsunWszystkie = findViewById<Button>(R.id.buttonUsunWszystkie)
        val textViewPytanie = findViewById<TextView>(R.id.textViewPytanie)
        val editOdpowiedz = findViewById<EditText>(R.id.editOdpowiedz)
        val buttonSprawdz = findViewById<Button>(R.id.buttonSprawdz)
        val textViewWynik = findViewById<TextView>(R.id.textViewWynik)
        val spinnerGrupy = findViewById<Spinner>(R.id.spinnerGrupy)
        val containerWszystkie = findViewById<LinearLayout>(R.id.containerWszystkie)

        zaladujSlowa()
        aktualizujSpinner(spinnerGrupy)

        buttonDodaj.setOnClickListener {
            val slowo = editSlowo.text.toString().trim()
            val tlumaczenie = editTlumaczenie.text.toString().trim()
            val grupa = editGrupa.text.toString().trim().ifEmpty { "Bez grupy" }
            if (slowo.isNotEmpty() && tlumaczenie.isNotEmpty()) {
                slowka.add(Slowko(slowo, tlumaczenie, grupa))
                zapiszSlowa()
                Toast.makeText(this, "Słowo dodane", Toast.LENGTH_SHORT).show()
                editSlowo.text.clear()
                editTlumaczenie.text.clear()
                editGrupa.text.clear()
                aktualizujSpinner(spinnerGrupy)
            } else {
                Toast.makeText(this, "Wpisz słowo i tłumaczenie", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDodajListe.setOnClickListener {
            val tekst = editLista.text.toString().trim()
            if (tekst.isEmpty()) {
                Toast.makeText(this, "Wpisz listę słów", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var licznik = 0
            tekst.lines().forEach { linia ->
                val czesci = linia.split("-", "–").map { it.trim() }
                if (czesci.size >= 2) {
                    slowka.add(Slowko(czesci[0], czesci[1]))
                    licznik++
                }
            }

            if (licznik > 0) {
                zapiszSlowa()
                aktualizujSpinner(spinnerGrupy)
                Toast.makeText(this, "Dodano $licznik słów", Toast.LENGTH_SHORT).show()
                editLista.text.clear()
            } else {
                Toast.makeText(this, "Nie znaleziono poprawnych wpisów", Toast.LENGTH_SHORT).show()
            }
        }

        spinnerGrupy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                wybranaGrupa = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        buttonStartTestu.setOnClickListener {
            slowkaDoTestu = if (wybranaGrupa == "Wszystkie") {
                slowka.toMutableList()
            } else {
                slowka.filter { it.grupa == wybranaGrupa }.toMutableList()
            }

            if (slowkaDoTestu.isEmpty()) {
                Toast.makeText(this, "Brak słów w wybranej grupie", Toast.LENGTH_SHORT).show()
                textViewPytanie.text = ""
                textViewWynik.text = ""
                return@setOnClickListener
            }

            punkty = 0
            liczbaPytan = 0
            textViewWynik.text = ""
            nastepnePytanie(textViewPytanie, textViewWynik)
        }

        buttonSprawdz.setOnClickListener {
            val odpowiedz = editOdpowiedz.text.toString().trim()
            aktualneSlowko?.let {
                liczbaPytan++
                if (odpowiedz.equals(it.slowo, ignoreCase = true)) {

                    punkty++
                    Toast.makeText(this, "Poprawnie!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Niepoprawnie! Poprawna odpowiedź: ${it.slowo}", Toast.LENGTH_SHORT).show()
                }
                editOdpowiedz.text.clear()
                nastepnePytanie(textViewPytanie, textViewWynik)
            }
        }

        buttonPokaz.setOnClickListener {
            pokazListe(containerWszystkie)
        }

        buttonUkryj.setOnClickListener {
            containerWszystkie.visibility = View.GONE
        }

        buttonUsunWszystkie.setOnClickListener {
            slowka.clear()
            zapiszSlowa()
            aktualizujSpinner(spinnerGrupy)
            containerWszystkie.removeAllViews()
            textViewPytanie.text = ""
            textViewWynik.text = ""
            Toast.makeText(this, "Wszystkie słowa usunięte", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pokazListe(container: LinearLayout) {
        container.removeAllViews()
        if (slowka.isEmpty()) {
            val t = TextView(this)
            t.text = "Brak dodanych słów"
            container.addView(t)
            container.visibility = View.VISIBLE
            return
        }

        slowka.forEachIndexed { index, slowko ->
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            val text = TextView(this)
            text.text = "${slowko.slowo} → ${slowko.tlumaczenie}"
            text.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

            val button = Button(this)
            button.text = "Usuń"
            button.setOnClickListener {
                slowka.removeAt(index)
                zapiszSlowa()
                pokazListe(container)
            }

            row.addView(text)
            row.addView(button)
            container.addView(row)
        }
        container.visibility = View.VISIBLE
    }

    private fun nastepnePytanie(pytanieView: TextView, wynikView: TextView) {
        if (slowkaDoTestu.isEmpty()) {
            pytanieView.text = "Test zakończony!"
            wynikView.text = "Twój wynik: $punkty / $liczbaPytan"
            aktualneSlowko = null
            return
        }

        val index = Random.nextInt(slowkaDoTestu.size)
        aktualneSlowko = slowkaDoTestu[index]
        slowkaDoTestu.removeAt(index)
        pytanieView.text = "Przetłumacz na niemiecki: ${aktualneSlowko!!.tlumaczenie}"
    }

    private fun aktualizujSpinner(spinner: Spinner) {
        val listaGrup = slowka.map { it.grupa }.toSet().toMutableList()
        listaGrup.sort()
        listaGrup.add(0, "Wszystkie")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaGrup)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun zapiszSlowa() {
        val sharedPreferences = getSharedPreferences("words_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val dane = slowka.joinToString(";") { "${it.slowo}|${it.tlumaczenie}|${it.grupa}" }
        editor.putString("words_list", dane)
        editor.apply()
    }

    private fun zaladujSlowa() {
        val sharedPreferences = getSharedPreferences("words_pref", Context.MODE_PRIVATE)
        val dane = sharedPreferences.getString("words_list", "") ?: ""
        if (dane.isNotEmpty()) {
            slowka = dane.split(";").mapNotNull {
                val parts = it.split("|")
                if (parts.size >= 3) {
                    Slowko(parts[0], parts[1], parts[2])
                } else null
            }.toMutableList()
        }
    }
}
