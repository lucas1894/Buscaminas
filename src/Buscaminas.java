import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Buscaminas {
    public static void main(String[] args) {
        //Ventana que muestra el menu principal.
        VentanaMenu ventanaMenu = new VentanaMenu();
        PanelMenu menu = new PanelMenu();
        ventanaMenu.setVisible(true);
        ventanaMenu.add(menu);
        ventanaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class VentanaMenu extends JFrame{
    Toolkit pantalla=Toolkit.getDefaultToolkit();
    Dimension tamanioPantalla=pantalla.getScreenSize();
    int alturaPantalla=tamanioPantalla.height;
    int anchoPantalla=tamanioPantalla.width;
        
    public VentanaMenu() {
        setBounds(anchoPantalla/4, alturaPantalla/4, 400, 200);
        setTitle("Buscaminas");
        Image icono=pantalla.getImage("img/buscaminas.png");
        setIconImage(icono);
    }
}

class VentanaFacil extends JFrame implements ActionListener{
    Toolkit pantalla=Toolkit.getDefaultToolkit();
    Dimension tamanioPantalla=pantalla.getScreenSize();
    int alturaPantalla=tamanioPantalla.height;
    int anchoPantalla=tamanioPantalla.width;
    PanelBuscaminas tablero=new PanelBuscaminas(10,10);
        
    public VentanaFacil() {
        setBounds(anchoPantalla/4, alturaPantalla/4, alturaPantalla/2, alturaPantalla/2);
        setTitle("Buscaminas");
        Image icono=pantalla.getImage("img/buscaminas.png");
        setIconImage(icono);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(true);
        add(tablero);
    }
}

class VentanaMedio extends JFrame implements ActionListener{
    Toolkit pantalla=Toolkit.getDefaultToolkit();
    Dimension tamanioPantalla=pantalla.getScreenSize();
    int alturaPantalla=tamanioPantalla.height;
    int anchoPantalla=tamanioPantalla.width;
    PanelBuscaminas tablero=new PanelBuscaminas(16,16);
        
    public VentanaMedio() {
        setBounds(anchoPantalla/4, alturaPantalla/4, alturaPantalla/2, alturaPantalla/2);
        setTitle("Buscaminas");
        Image icono=pantalla.getImage("img/buscaminas.png");
        setIconImage(icono);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(true);
        add(tablero);
    }
}

class VentanaDificil extends JFrame implements ActionListener{
    Toolkit pantalla=Toolkit.getDefaultToolkit();
    Dimension tamanioPantalla=pantalla.getScreenSize();
    int alturaPantalla=tamanioPantalla.height;
    int anchoPantalla=tamanioPantalla.width;
    PanelBuscaminas tablero=new PanelBuscaminas(16,32);
        
    public VentanaDificil() {
        setBounds(anchoPantalla/4, alturaPantalla/4, anchoPantalla/2, alturaPantalla/2);
        setTitle("Buscaminas");
        Image icono=pantalla.getImage("img/buscaminas.png");
        setIconImage(icono);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(true);
        add(tablero);
    }
}

class PanelMenu extends JPanel{
    
    VentanaFacil ventana1 = new VentanaFacil();
    VentanaMedio ventana2 = new VentanaMedio();
    VentanaDificil ventana3 = new VentanaDificil();
    JButton facil = new JButton("Fácil");
    JButton medio = new JButton("Medio");
    JButton dificil = new JButton("Difícil");
    
    public PanelMenu(){
        add(facil);
        facil.addActionListener(ventana1);
        add(medio);
        medio.addActionListener(ventana2);
        add(dificil);
        dificil.addActionListener(ventana3);
    }
}

class PanelBuscaminas extends Panel{
    Casilla tablero[][];
    boolean minas[][];
    int maxMinas;
    int filas;
    int columnas;
    PanelBuscaminas(int filas, int columnas){
        this.filas=filas;
        this.columnas=columnas;
        setLayout(new GridLayout(filas,columnas));
        tablero = new Casilla[filas][columnas];
        minas = new boolean[filas][columnas];
        maxMinas = (int) (filas*columnas)/6;
        // creamos los botones y vaciamos la matriz donde
        // colocaremos las minas
        for(int i=0; i<filas; i++){
            for(int j=0; j<columnas; j++){
                minas[i][j] = false;
                tablero[i][j] = new Casilla("");
                add(tablero[i][j]);
                tablero[i][j].addActionListener(new AccionBoton(this,i,j));
            }
        }
        
        // colocamos minas
        for(int i=0; i<maxMinas; i++){
            int f;
            int c;
            do {
                f = (int)(Math.random()*filas);
                c = (int)(Math.random()*columnas);
            }while(minas[f][c]);
            minas[f][c] = true;
        }
    }
}

class Casilla extends Button {
    boolean descubierta;
    Casilla(String t){
        super.setLabel(t);
        setBackground(new Color(200,200,200));
        setFont(new Font("Arial", Font.BOLD, 15));
        descubierta = false;
    }
}

class AccionBoton implements ActionListener {
    PanelBuscaminas pb;
    int f, c;
    AccionBoton(PanelBuscaminas pb,int f, int c){
        this.pb = pb;
        this.f = f;
        this.c = c;
    }
    public void actionPerformed(ActionEvent ae) {
        if(!pb.tablero[f][c].descubierta){
            pb.tablero[f][c].descubierta = true;
            pb.tablero[f][c].setBackground(Color.CYAN);
            
            if(pb.minas[f][c]){
                pb.tablero[f][c].setLabel("*");
                for(int i=0; i<pb.filas; i++){
                    for(int j=0; j<pb.columnas; j++){
                        if(pb.minas[i][j]){
                            pb.tablero[i][j].setBackground(new Color(255,50,100));
                            pb.tablero[i][j].setLabel("*");
                        }
                    }
                }
                JOptionPane.showMessageDialog(pb.getParent(), "¡PERDISTE!");
                System.exit(0);
            }
            else{
                int numMinas = CuentaMinas(f,c);
                switch(numMinas){
                    case 0:
                        AutoDescubrir(f,c);
                        break;
                    default:
                        pb.tablero[f][c].setLabel(Integer.toString(numMinas));
                }
            }
            int num_desc = numDescubiertas();
            if((pb.filas*pb.columnas)-num_desc == pb.maxMinas)
            {
                JOptionPane.showMessageDialog(pb.getParent(), "¡GANASTE!");
                System.exit(0);
            }
        }
    }
    int CuentaMinas(int f, int c){
        int num_minas = 0;
        for(int i=f-1; i<=f+1; i++){
            for(int j=c-1; j<=c+1; j++){
                if(!(i==f && j==c) && i>=0 && i<pb.filas && j>=0 && j<pb.columnas){
                    if(pb.minas[i][j]){
                        num_minas++;
                    }
                }
            }
        }
        return num_minas;
    }
    void AutoDescubrir(int f, int c){
        pb.tablero[f][c].descubierta = true;
        for(int i=f-1; i<=f+1; i++){
            for(int j=c-1; j<=c+1; j++){
                if(i>=0 && i<30 && j>=0 && j<50 && !(i==f && j==c) && !pb.tablero[i][j].descubierta){
                    int numMinas = CuentaMinas(i,j);
                    pb.tablero[i][j].setBackground(Color.CYAN);
                    switch(numMinas){
                        case 0:
                            AutoDescubrir(i,j);
                            break;
                        default:
                            pb.tablero[i][j].descubierta = true;
                            pb.tablero[i][j].setLabel(Integer.toString(numMinas));
                            pb.tablero[i][j].setBackground(Color.CYAN);
                    }
                }
            }
        }
    }
    int numDescubiertas(){
        int nd=0;
        for(int i=0; i<pb.filas; i++){
            for(int j=0; j<pb.columnas; j++){
                if(pb.tablero[i][j].descubierta){
                    nd++;
                }
            }
        }
        return nd;
    }
}