package sa.edu.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingData(var id:Int,var name:String,var quntity:String,var editpen:Boolean = false) {
}
@Composable
fun shoppinglistApplication() {

    var sItem by remember {
        mutableStateOf(listOf<ShoppingData>())
    }
    var showalertdialog by remember {
        mutableStateOf(false)
    }
    var itemname by remember {
        mutableStateOf("")
    }
    var itemquntity by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showalertdialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            items(sItem) {
            item ->
                   if (item.editpen) {
                listeditui(Item = item, oneditcomplete = { editname, editquntity ->
                    sItem = sItem.map { it.copy(editpen = false) }
                    val editeditem = sItem.find { it.id == item.id }
                    editeditem?.let {
                        it.name = editname
                        it.quntity = editquntity.toString()
                    }
                })
            } else {
                shoppingitemlook(Item = item,
                    oneditclick = {
                        sItem = sItem.map { it.copy(editpen = it.id == item.id) }
                    },
                    ondeleteclick = {
                        sItem = sItem - item
                    })
            }

        }

        }
        if(showalertdialog){
          AlertDialog(onDismissRequest = { showalertdialog = false},
              confirmButton = {
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                            horizontalArrangement = Arrangement.SpaceAround) {
                                            Button(onClick = { if(itemname.isNotBlank()){
                                                val newitem = ShoppingData(
                                                    id = sItem.size+1,
                                                    name =itemname,
                                                    quntity = itemquntity
                                                )
                                                sItem = sItem+newitem
                                                showalertdialog = false
                                                itemname = ""
                                                itemquntity = ""

                                                } }) {
                                                Text(text = "Add")

                                            }
                                            Button(onClick = {showalertdialog = false }) {
                                                Text(text = "cancel")
                                            }
                                        }
          },
              title = { Text(text = "Add Shopping Item")},
              text = {
                  Column {
                      OutlinedTextField(value = itemname, onValueChange ={itemname = it} ,
                          singleLine = true,
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(8.dp))
                      OutlinedTextField(value = itemquntity, onValueChange ={itemquntity= it} ,
                          singleLine = true,
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(8.dp))
                  }
              }
              )
        }

    }
}

@Composable
fun shoppingitemlook(
    Item :ShoppingData,
    oneditclick:()->Unit ,
    ondeleteclick:()->Unit

){
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .border(
            border = BorderStroke(2.dp, Color(0xFF00FF00)),
            shape = RoundedCornerShape(20)
        ),
     horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(text = Item.name, modifier =Modifier.padding(24.dp))
        Text(text = "qty: ${Item.quntity}", modifier =Modifier.padding(24.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick =oneditclick)
            {
                Icon(imageVector = Icons.Default.Edit, contentDescription ="edit button" )

            }
            IconButton(onClick =ondeleteclick)
            {
                Icon(imageVector = Icons.Default.Delete, contentDescription ="delete button" )

            }
        }


    }
}
@Composable
fun listeditui(Item: ShoppingData,oneditcomplete:(String,Int)->Unit){
    var editname by remember {
        mutableStateOf(Item.name)
    }
    var editquqntity by remember {
        mutableStateOf(Item.quntity.toString())
    }
    var isedit by remember {
        mutableStateOf(Item.editpen)
    }
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = editname, onValueChange = { editname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            BasicTextField(value = editquqntity, onValueChange = { editquqntity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

        }
        Button(onClick = { isedit= false
        oneditcomplete(editname,editquqntity.toIntOrNull()?:1)}) {
            Text(text = "save")
        }

    }
}