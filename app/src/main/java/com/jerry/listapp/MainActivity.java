package com.jerry.listapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static android.R.color.holo_blue_dark;
import static android.graphics.Color.*;

public class MainActivity extends AppCompatActivity {


    EditText editAdder;
    Button buttonAdder;
    ListView myList;
    ArrayList<String> items;
    String []  itemsString;
    String itemName;
    ArrayAdapter<String> itemsAdapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    View updateview;// above oncreate method
    ArrayList<Boolean> checked = new ArrayList<> ();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editAdder = findViewById(R.id.editTextAdder);
        buttonAdder = findViewById(R.id.buttonAdder);
        myList = findViewById(R.id.MyList);
        items = new ArrayList<>();
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



        itemsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice , items);
        restoreBlood();


        myList.setAdapter(itemsAdapter);


        /*myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int colorRes = 1140303;
                int pos = position;

               // if (updateview != null)
                 //   updateview.setBackgroundColor(Color.TRANSPARENT);
                //updateview = view;

               // view.setBackgroundColor( Color.CYAN);
               // SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions ();

               // int count = myList.getCount ();
               // view.setSelected ( true );
                //for (int i = count - 1; i >= 0; i--) {
                  //  if (sparseBooleanArray.get ( i )) {


                        //if (myList.getChildAt ( i ).getBackground ()  ==  Drawable.getColor (  )     //myList.getChildAt ( i ).getBackground (RED))
                        //for (int j = 0; j < myList.getAdapter ().getCount (); j++) {
                    //    {
                         //   myList.getChildAt ( i ).setBackgroundColor ( RED );
                       // }
                        //}
                       // parent.getChildAt ( position ).setBackgroundColor ( Color.RED );


                   // }
                   // else
                   // {
                     //   parent.getChildAt ( i ).setBackgroundColor ( TRANSPARENT );


                    //}
                //}


                // sparseBooleanArray.clear ();
               // itemsAdapter.notifyDataSetChanged ();
            }
        });*/


        myList.setOnItemLongClickListener(new OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int pos = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this item ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(int i=0; i<items.size(); i++){
                                    //editor.putString("blood"+i, items.get(i));
                                    if (pos == i)
                                    {
                                        SharedPreferences preferences = getSharedPreferences("uerInputBlood", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.remove("blood"+i);
                                        editor.apply();
                                    }
                                }
                                items.remove(pos);
                                saveBlood();
                                itemsAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this,"Item deleted!",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();




                return false;


            }

        });
    }



    public void buttonAdderClick(View v) {




        itemName = editAdder.getText().toString();


        //itemsString = new String[items.size()];



        if (editAdder.getText().toString().trim().length() > 0) {

            items.add(itemName);
            saveBlood();



           // itemsString[0]=itemName;

            //itemsString = items.stream().toArray(String[]::new);

            //int i = 0;

            //for (String s  : items )
            //{
            //    itemsString[i] =  s;
            //    i++;
           // }

            itemsAdapter.notifyDataSetChanged();



        } else if ((editAdder.getText().toString().trim().length() == 0))  {
            Toast.makeText(getApplicationContext(), "Add your item firstly!", Toast.LENGTH_SHORT).show();
        }
        editAdder.setText("");



    }




    private void saveBlood(){
        SharedPreferences preferences = getSharedPreferences("uerInputBlood", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //items.clear();
        editor.putInt("count", items.size());
        for(int i=0; i<items.size(); i++){
            editor.putString("blood"+i, items.get(i));
        }
        editor.apply();
    }

    private void restoreBlood(){
        items.clear();
        SharedPreferences preferences = getSharedPreferences("uerInputBlood", MODE_PRIVATE);
        int tmpCount = preferences.getInt("count", 0);
        for(int i=0; i<tmpCount; i++){
            items.add(preferences.getString("blood"+i, ""));
        }
    }


    private void deleteBlood()
    {

        try {
            if (items.size () > 0) {
                SharedPreferences preferences = getSharedPreferences ( "uerInputBlood", MODE_PRIVATE );
                SharedPreferences.Editor editor = preferences.edit();
                int tmpCount = preferences.getInt ( "count", 0 );
                for (int i = 0; i < tmpCount; i++) {

                    items.remove ( preferences.getString ( "blood" + i, "" ) );
                    editor.apply ();
                    items.clear ();

                }
                editor.clear();
                editor.apply();
                Toast.makeText ( MainActivity.this, "All items deleted!", Toast.LENGTH_SHORT ).show ();

            } else {
                Toast.makeText ( MainActivity.this, "No items found!", Toast.LENGTH_SHORT ).show ();

            }
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString (),Toast.LENGTH_LONG).show();
        }

    }


    public void deleteAllOnClick(View view)
    {


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Are you sure you ?")
                .setMessage("Do you want to delete ALL items ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //for(int i=0; i<items.size(); i++){
                            //editor.putString("blood"+i, items.get(i));


                          deleteBlood ();

                        //}

                        //saveBlood();
                        itemsAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    items.add(result.get(0));
                    itemsAdapter.notifyDataSetChanged();
                    saveBlood();


                    //txvResult.setText(result.get(0));
                }
                break;
        }
    }



    public void sendEmailList(View view)
    {

        restoreBlood ();

        //int tmpCount = preferences.getInt("count", 0);

        String[] stringToSend = items.toArray(new String[items.size()]);
        int i=0;
        /*for (String S: items)
        {
            items.set ( i, items.get ( i ) + "\n" );
            i++;
        }*/

        try {
            for (i = 0; i < items.size (); i++) {
                stringToSend[i] = stringToSend[i] + "\n";

            }
        }
        catch (Exception ex)
        {
            Toast.makeText ( MainActivity.this,ex.toString (),Toast.LENGTH_LONG ).show();
        }


        StringBuffer sb = new StringBuffer();
        for(i = 0; i < stringToSend.length; i++) {
            sb.append(stringToSend[i]);
        }
        String str = sb.toString();


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"example@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sending List");
        intent.putExtra(Intent.EXTRA_TEXT   , "body of email");

        //for (String S: items)


            intent.putExtra(Intent.EXTRA_TEXT   , str);




        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }







    }



}












