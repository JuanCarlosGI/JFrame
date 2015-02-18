
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
     
    }
        
    
    
    /**
     * Metodo usado para actualizar la posicion de los objetos
     * 
     */
    public void actualiza(){
            
            
            
    }
    
    
    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton
     * con las orillas del <code>Applet</code>.
     */
    public void checaColision(){
        
        
        
        
        
    }

    /**
     * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * @param graphics es el <code>objeto grafico</code> usado para dibujar.
     */
    
    public void paint(Graphics graphics) {
        
        
        
    }
    
   /**
     * Metodo <I>paint1</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param graphics es el <code>objeto grafico</code> usado para dibujar.
     */
    
    public void paint1 (Graphics graphics){
        
        
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
