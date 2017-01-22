package pokecode.pokecoderemote;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity  {
    Button haut, bas, freinB, warnB, bl, phares, essuieG, stopB, gauche, droite, clignG, clignD;
    int vitesse = 1;
    boolean h = false,b = false,f = false,w = false,p = false,e = false,s = false;
    int i = 0;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    private BtInterface btS = null;
    ListView lv;
    private BluetoothDevice voiture;
    private long lastTime = 0;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String data = msg.getData().getString("receivedData");

            long t = System.currentTimeMillis();
            if(t-lastTime > 100) {// Pour éviter que les messages soit coupés

                lastTime = System.currentTimeMillis();
            }

        }
    };

    final Handler handlerStatus = new Handler() {
        public void handleMessage(Message msg) {
            int co = msg.arg1;
            if(co == 1) {

            } else if(co == 2) {

            }
        }
    };

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
        btS  = new BtInterface(handlerStatus, handler);

        haut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btS.sendData("turn 0");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               switch (vitesse){

                   case 1:
                       btS.sendData("drive 1 3333");
                       vitesse++;
                       break;
                   case 2 :
                       btS.sendData("drive 1 6666");
                       vitesse++;
                       break;
                   case 3:
                       btS.sendData("drive 1 10000");
                       break;
               }


            }
        });

        bas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (vitesse){

                    case 1:
                        btS.sendData("drive 2 3333");
                        break;
                    case 2 :
                        btS.sendData("drive 1 3333");
                        vitesse--;
                        break;
                    case 3:
                        btS.sendData("drive 1 6666");
                        vitesse--;
                        break;
                }
            }
        });
        droite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btS.sendData("turn 1");
            }
        });
        gauche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btS.sendData("turn 2");
            }
        });
        freinB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btS.sendData("drive 0 0");
                vitesse = 1; //on repartira à la vitesse 1
            }
        });
        warnB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //A remplir
            }
        });
        phares.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(p) {
                    btS.sendData("light 0");
                    p = false;
                }else {
                    btS.sendData("light 1");
                    p = true;
                }
            }
        });
        essuieG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if(e) {
                   btS.sendData("essuieglace 0");
                   e = false;
               }else{
                   btS.sendData("essuieglace 1");
                   e = true;
               }
            }
        });
        stopB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btS.sendData("stoplight 1");
            }
        });
        clignG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //A remplir
            }
        });
        clignD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //A remplir
            }
        });






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
        btS.connect();


    }
}