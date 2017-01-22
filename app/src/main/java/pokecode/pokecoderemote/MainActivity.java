package pokecode.pokecoderemote;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity  {
    /*Variables pour interraction avec les boutons de l'interface et la zone de texte*/
    Button haut, bas, freinB, bl, phares, essuieG, gauche, droite, aligne;
    TextView tV;
    ListView lv;

    /*Variables pour la gestion des LEDs et de l'affichage de l'application*/
    int vitesse = 1;
    boolean h = false,b = false,f = false,p = false,e = false;

    /*Variables de configuration de la communication bluetooth*/
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices; //liste des appareils appairés au téléphone
    private BtInterface btS = null; //module supplémentaire pour récupérer et envoyer les données via bluetooth
    private BluetoothDevice voiture;
    private long lastTime = 0;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String data = msg.getData().getString("receivedData");

            long t = System.currentTimeMillis();
            if(t-lastTime > 100) {// Pour éviter que les messages soient coupés

                lastTime = System.currentTimeMillis();
            }

        }
    };

    final Handler handlerStatus = new Handler() {
        //récupère l'état de l'appareil
        public void handleMessage(Message msg) {
            int co = msg.arg1;
            if(co == 1) {

            } else if(co == 2) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //définit le comportement de l'application au lancement
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Bind des boutons de la vue aux variables de ce code*/
        tV=(TextView)findViewById(R.id.VitesseDisplay);
        aligne=(Button)findViewById(R.id.BoutonAvant);
        haut=(Button)findViewById(R.id.BoutonPlus);
        bas=(Button)findViewById(R.id.BoutonBas);
        droite=(Button)findViewById(R.id.BoutonDroit);
        gauche=(Button)findViewById(R.id.BoutonGauche);
        freinB=(Button)findViewById(R.id.BoutonFrein);
        phares=(Button)findViewById(R.id.BoutonPhares);
        essuieG=(Button)findViewById(R.id.BoutonEGlaces);
        bl=(Button)findViewById(R.id.buttonList); //sert à initier la connexion à la voiture
        lv = (ListView)findViewById(R.id.listView); //affiche le nom de la voiture

        BA = BluetoothAdapter.getDefaultAdapter(); //récupération du bluetooth
        btS  = new BtInterface(handlerStatus, handler);

        haut.setOnClickListener(new View.OnClickListener() {
            /*Appui sur le bouton haut aligne les roues vers l'avant et fait avancer la voiture plus ou moins vite
            en fonction de la vitesse actuelle
             */
            public void onClick(View v) {
                btS.sendData("turn 0");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               switch (vitesse){
                /*détermination de la vitesse actuelle et envoie de la commande d'avancement de la voiture*/
                   case 1:
                       btS.sendData("stoplight 0");
                       f = false;
                       try {
                           Thread.sleep(300);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       btS.sendData("drive 1 3333");
                       vitesse++;
                       tV.setText("1");
                       break;
                   case 2 :
                       btS.sendData("drive 1 6666");
                       vitesse++;
                       tV.setText("2");
                       break;
                   case 3:
                       btS.sendData("drive 1 10000");
                       tV.setText("3");
                       break;
               }

            }
        });

        bas.setOnClickListener(new View.OnClickListener() {
            /*Appui sur le bouton bas décrémente la vitesse ou enclenche la marche arrière si la vitesse en marche avant
            était au maximum
             */
            public void onClick(View v) {
                switch (vitesse){

                    case 1:
                        btS.sendData("stoplight 0");
                        f = false;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        btS.sendData("drive 2 3333");
                        tV.setText("-1");
                        vitesse = 1;
                        break;
                    case 2 :
                        btS.sendData("drive 1 3333");
                        vitesse --;
                        tV.setText("1");
                        break;
                    case 3:
                        btS.sendData("drive 1 6666");
                        vitesse --;
                        tV.setText("2");
                        break;
                }
            }
        });

        aligne.setOnClickListener(new View.OnClickListener() {
            //met les roues vers l'avant
            public void onClick(View v) {
                btS.sendData("turn 0");
            }
        });

        gauche.setOnClickListener(new View.OnClickListener() {
            //oriente les roues à gauche
            public void onClick(View v) {
                btS.sendData("turn 1");
            }
        });
        droite.setOnClickListener(new View.OnClickListener() {
            //oriente les roues à droite
            public void onClick(View v) {
                btS.sendData("turn 2");
            }
        });
        freinB.setOnClickListener(new View.OnClickListener() {
            /*Appui sur le bouton Freiner arrête la voiture*/
            public void onClick(View v) {
                if(!f) {
                    btS.sendData("stoplight 1"); //on allume les feux arrieres
                    f = true;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tV.setText("0");
                    btS.sendData("drive 0 0"); //on s'arrete
                    vitesse = 1; //on repartira à la vitesse 1
                }else{
                    btS.sendData("stoplight 0"); //on eteint les feux arrieres
                    f = false;
                }
            }
        });

        phares.setOnClickListener(new View.OnClickListener() {
            //allume ou éteint les phares en fonction de leur état précédent
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
            //allume ou éteint les essuie-glaces en fonction de leur état précédent
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


    }



    public void list(View v){
        /*récupère la liste des appareils aparaillés au téléphone, les classe dans une list et
        affiche ceux dont le nom est celui de la voiture
         */
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