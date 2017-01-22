package pokecode.pokecoderemote;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity  {
    Button haut, bas, freinB, warnB, bl, phares, essuieG, stopB, gauche, droite, clignG, clignD;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    ListView lv;
    private BluetoothDevice voiture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        haut=(Button)findViewById(R.id.BoutonHaut);
        bas=(Button)findViewById(R.id.BoutonBas);
        droite=(Button)findViewById(R.id.BoutonDroit);
        gauche=(Button)findViewById(R.id.BoutonGauche);
        freinB=(Button)findViewById(R.id.BoutonFrein);
        warnB=(Button)findViewById(R.id.BoutonWarning);
        phares=(Button)findViewById(R.id.BoutonPhares);
        essuieG=(Button)findViewById(R.id.BoutonEGlaces);
        stopB=(Button)findViewById(R.id.BoutonArretUrg);
        clignG=(Button)findViewById(R.id.BoutonClignGauche);
        clignD=(Button)findViewById(R.id.BoutonClignDroit);
        bl=(Button)findViewById(R.id.buttonList);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView); //ligne à réécrire pour otus les boutons
    }

    public void vitessePlus(View v){
     //TODO
    }
    public void vitesseMoins(View v){
        //TODO
    }
    public void frein(View v){
        //TODO
    }
    public void warning(View v){
        //TODO
    }
    public void phares(View v){
        //TODO
    }
    public void essuieGlaces(View v){
        //TODO
    }
    public void freinUrgence(View v){
        //TODO
    }
    public void tourneDroit(View v){
        //TODO
    }
    public void tourneGauche(View v){
        //TODO
    }
    public void clignGauche(View v){
        //TODO
    }
    public void clignDroit(View v){
        //TODO
    }

    public void list(View v){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices){
            Toast.makeText(getApplicationContext(), "Recuperation des appareils appaires",Toast.LENGTH_SHORT).show();
            if(bt.getName().equals("STCAR-000007")){ //si l'un des appareils connectés est la voiture
                voiture = bt;
                list.add(bt.getName());

                final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);
            }

        }

    }
}