
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JoseFernando
 */
public class JuegoJFrame extends JFrame implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basMalo;         // Objeto malo
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    
    private SoundClip scSonidoChimpy1;  // Objeto sonido de Chimpy 
    private SoundClip scSonidoChimpy2;  //Objeto sonido de Chimpy
    
    //private AudioClip adcSonidoChimpy1;   // Objeto sonido de Chimpy
    //private AudioClip adcSonidoChimpy2;   // Objeto sonido de Chimpy
    
    //Variables que se usan en el juego
    private int iVidas; //Vidas
    private int iScore; //Puntuación
    private int iDireccion;     //Dirección de Nena
    private LinkedList<Base> lklFantasmas; //Lista de fantasmas
    private LinkedList<Base> lklJuanitos; //Lista de Juuanitos
    private int iJuanitosChocaron;  //Cuantos Juanitos han chocado
    private int iPausa; //Para ver si está en pausa
    private boolean bFin; //Checa si se terminó el juego
    
    
    
    /**
     * Constructor de la clase <code>JuegoJFrame</code>.
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>JFrame</code> y se definen funcionalidades.
     */
	public JuegoJFrame() {
        
            
        URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");
        URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        
        iDireccion = 0; //Nena no se mueve
        
        iPausa = -1;    //Inicio sin estar en pausa
        bFin = false;   //No se ha terminado el juego
        
        lklFantasmas = new LinkedList<Base>();  //Creo la lista de fantasmas
        lklJuanitos = new LinkedList<Base>();   //Creo la lista de Juanitos
        
        iJuanitosChocaron = 0; //Ha chocado con 0 Juanitos
        
        iVidas = (int) (Math.random() * 2) + 3; //Inicio las vidas entre 3 y 5
        iScore = 0; //Inicio el score
                
        // se crean a los Juanitos
        int iRandom = (int) (Math.random() * 6) + 10; //Cantidad de Juanitos
        for(int iI = 0; iI < iRandom ; iI++) {
            int iPosRandX  = (int) (Math.random() * getWidth());
            int iPosRandY  = (int) (Math.random() * getHeight()) * -1;
            Base basJuanito = new Base(iPosRandX, iPosRandY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));
            iPosRandX = (int) (Math.random() * (getWidth() - 
                        basJuanito.getAncho()));
            basJuanito.setX(iPosRandX);
            lklJuanitos.add(basJuanito);
        }
        
        //Se crean los fantasmas
        iRandom = (int) (Math.random() * 3) + 8; //Cantidad de fantasmas
        for(int iI = 0; iI < iRandom ; iI++) {
            int iPosRandX  = (int) (Math.random() * getWidth()) * -1;
            int iPosRandY  = (int) (Math.random() * (getHeight()));
            Base basFantasma = new Base(iPosRandX, iPosRandY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            iPosRandY = (int) (Math.random() * (getHeight() - 
                    basFantasma.getAlto()));
            basFantasma.setY(iPosRandY);
            lklFantasmas.add(basFantasma);
        }
        
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("chimpy.gif");
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	basMalo = new Base(getWidth() /2,getHeight() / 2, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
        
        //defino el sonido 1
        scSonidoChimpy1 = new SoundClip("monkey1.wav");
        
        //defino el sonido 2
        scSonidoChimpy2 = new SoundClip("monkey2.wav");
                
                
        //Hago que se active con teclado
        addKeyListener(this);  
        
        // Declaras un hilo
        Thread t = new Thread (this);
	// Empieza el hilo
	t.start ();
            
        }
    
    /** 
     * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>JFrame</code> y luego manda a dormir el hilo.
     * 
     */
    @Override
    public void run() {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (!bFin) {
            actualiza();
            checaColision();
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
        
    
    
    /**
     * Metodo usado para actualizar la posicion de los objetos
     * 
     */
    public void actualiza(){
        if (iPausa == -1 && !bFin) {
            switch(iDireccion) {
                case 1: { //se mueve hacia arriba
                    basMalo.setY(basMalo.getY() - 2);
                    break;    
                }
                case 2: { //se mueve hacia abajo
                    basMalo.setY(basMalo.getY() + 2);
                    break;    
                }
                case 3: { //se mueve hacia izquierda
                    basMalo.setX(basMalo.getX() - 2);
                    break;    
                }
                case 4: { //se mueve hacia derecha
                    basMalo.setX(basMalo.getX() + 2);
                    break;    	
                }
            }
            
            //muevo a los Juanitos
            for (Base basJuanito:lklJuanitos) {
                basJuanito.setY(basJuanito.getY() + (6 - iVidas));
            }
            
            //Muevo a los fantasmas
            for (Base basFantasma:lklFantasmas) {
                int iRandom = (int) (Math.random() * 4) + 3;
                basFantasma.setX(basFantasma.getX() + iRandom);
            }
        }    
            
            
    }
    
    
    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton
     * con las orillas del <code>Applet</code>.
     */
    public void checaColision(){
        
        //Si se sale hacia arriba
        if (basMalo.getY() < 0) {
            iDireccion = 0;
            basMalo.setY(0);
        }	
        //Si se sale hacia abajo
        if (basMalo.getY() + basMalo.getAlto() > getHeight()) {
            iDireccion = 0;
            basMalo.setY(getHeight() - basMalo.getAlto());
        }
        //Si se sale hacia la izquierda
        if (basMalo.getX() < 0) {
            iDireccion = 0;
            basMalo.setX(0);
        }
        //Si se sale hacia la derecha
        if (basMalo.getX() + basMalo.getAncho() > getWidth()) { 
            iDireccion = 0;
            basMalo.setX(getWidth() - basMalo.getAncho());
        }
        
        //Checo colision con los juanitos
        for(Base basJuanito:lklJuanitos) {
            if (basJuanito.intersecta(basMalo)) {
                iJuanitosChocaron++;
                int iPosRandX  = (int) (Math.random() * (getWidth() - 
                        basJuanito.getAncho()));
                int iPosRandY  = (int) (Math.random() * getHeight()) * -1;
                basJuanito.setX(iPosRandX);
                basJuanito.setY(iPosRandY);
                scSonidoChimpy1.play();
                if (iJuanitosChocaron == 5) {
                    iVidas--;
                    iJuanitosChocaron = 0;
                }
            }
        }
        
        //Checo cliosion con fantasmas
        for(Base basFantasma:lklFantasmas) {
            if (basFantasma.intersecta(basMalo)) {
                int iPosRandX  = (int) (Math.random() * getWidth()) * -1;
                int iPosRandY  = (int) (Math.random() * (getHeight() -
                        basFantasma.getAlto()));
                basFantasma.setX(iPosRandX);
                basFantasma.setY(iPosRandY);
                scSonidoChimpy2.play();
                iScore++;
            }
        }
        
        //Checo clision de Juanitos con piso
        for(Base basJuanito:lklJuanitos) {
            if (basJuanito.getY() + basJuanito.getAlto() > getHeight()) {
                int iPosRandX  = (int) (Math.random() * (getWidth() - 
                        basJuanito.getAncho()));
                int iPosRandY  = (int) (Math.random() * getHeight()) * -1;
                basJuanito.setX(iPosRandX);
                basJuanito.setY(iPosRandY);
            }
        }
        
        //Checo colision de fantasmas con pared
        for(Base basFantasma:lklFantasmas) {
            if (basFantasma.getX() + basFantasma.getAncho() > getWidth()){
                int iPosRandX  = (int) (Math.random() * getWidth()) * -1;
                int iPosRandY  = (int) (Math.random() * (getHeight() -
                        basFantasma.getAlto()));
                basFantasma.setX(iPosRandX);
                basFantasma.setY(iPosRandY);
            }
        }
        
        if (iVidas == 0) {
            bFin = true;    //Hago que se termine el juego
        }
        
        
        
    }

    /**
     * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * @param graphics es el <code>objeto grafico</code> usado para dibujar.
     */
    
    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null) {
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
        
        
    }
    
   /**
     * Metodo <I>paint1</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param graphics es el <code>objeto grafico</code> usado para dibujar.
     */
    
    public void paint1 (Graphics graDibujo){
        // si la imagen ya se cargo
        if (basMalo != null) {
            //Dibuja la imagen de los Juanitos
            for (Base basJuanito:lklJuanitos) {
                basJuanito.paint(graDibujo, this);
            }
            //dibujo a los fantasmas
            for (Base basFantasma:lklFantasmas) {
                basFantasma.paint(graDibujo,this);
            }
            //Dibuja la imagen de malo en el Applet
            basMalo.paint(graDibujo, this);
            graDibujo.setColor(Color.RED); //Escribo en color rojo
            graDibujo.drawString("Vidas: " + iVidas, 10, 10);   //Escribo vidas
            graDibujo.drawString("Puntos: " + iScore, 10, 30);  // escribo score
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
        
        if (bFin){  //Dibujo el fin del juego
            URL urlImagenFin = this.getClass().getResource("game Over.png");
            graDibujo.drawImage(Toolkit.getDefaultToolkit().getImage(urlImagenFin)
                    , getWidth() /2, getHeight() / 2, this);
        }
        
    }
    
    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
     * @param keyevent es el <code>evento</code> generado al presionar las teclas.
     */
    @Override
    public void keyPressed(KeyEvent keyevent) {
        
        
        repaint();
        
    }
    
    
    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que no es de accion.
     * @param keyevent es el <code>evento</code> que se genera en al presionar las teclas.
     */
    
    @Override
    public void keyTyped(KeyEvent keyevent) {
        
    }
    
    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla presionada.
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    @Override
    public void keyReleased(KeyEvent keyevent) {
        
    }
    /**
     * Metodo que lee a informacion de un archivo.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException{
    	
    }
	
    /**
     * Metodo que escribe en el archivo
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException{
    	
        
        
    }

    
    /**
     * Metodo principal
     *
     * @param args es un arreglo de tipo <code>String</code> de linea de comandos
     */
    public static void main(String[] args) {
        
    	// TODO code application logic here
    	
        JuegoJFrame jjfJuego = new JuegoJFrame();
    	jjfJuego.setSize(800, 500); // crea la ventana de 800x500
    	jjfJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	jjfJuego.setVisible(true);
    }
    
    
    
}
