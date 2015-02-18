
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
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
