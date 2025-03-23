package com.maxi.myfirstapp

import android.os.Bundle
import android.text.InputType
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maxi.myfirstapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lvTodoList: ListView
    private lateinit var fab: FloatingActionButton
    private lateinit var shoppingItems: ArrayList<String>
    private lateinit var itemAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lvTodoList = findViewById(R.id.lvTodoList)
        fab = findViewById(R.id.floatingActionButton)
        shoppingItems = ArrayList()

        // dummy data
        shoppingItems.add("Apfel")
        shoppingItems.add("Banane")

        itemAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingItems)
        lvTodoList.adapter = itemAdapter

        // register listView for context menu
        registerForContextMenu(lvTodoList)

        // Floating Action Button for adding elements
        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hinzufügen")

            val input = EditText(this)
            input.hint = "Text eingeben"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ -> // dialog, which
                val newItem = input.text.toString()
                if (newItem.isNotBlank()) {
                    shoppingItems.add(newItem)
                    Toast.makeText(applicationContext, "Element hinzugefügt", Toast.LENGTH_SHORT).show()
                    itemAdapter.notifyDataSetChanged()
                }
            }

            builder.setNegativeButton("Abbrechen") { _, _ ->
                Toast.makeText(applicationContext, "Abgebrochen", Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }
    }

    // create context menu
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)  // context_menu.xml
    }

    // context menu for delete and rename
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        return when (item.itemId) {
            R.id.menu_delete -> {
                // confirm for delete
                AlertDialog.Builder(this)
                    .setTitle("Löschen bestätigen")
                    .setMessage("Möchtest du dieses Element wirklich löschen?")
                    .setPositiveButton("Ja") { _, _ ->
                        shoppingItems.removeAt(position)
                        itemAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Element gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
                true
            }

            R.id.menu_rename -> {
                // dialog for rename
                val renameBuilder = AlertDialog.Builder(this)
                renameBuilder.setTitle("Element umbenennen")

                val input = EditText(this)
                input.setText(shoppingItems[position])  // previous text as placeholder
                renameBuilder.setView(input)

                renameBuilder.setPositiveButton("OK") { _, _ ->
                    val newName = input.text.toString()
                    if (newName.isNotBlank()) {
                        shoppingItems[position] = newName
                        itemAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Element umbenannt", Toast.LENGTH_SHORT).show()
                    }
                }

                renameBuilder.setNegativeButton("Abbrechen", null)
                renameBuilder.show()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }
}
