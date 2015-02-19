
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
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
    private final int iHeight=500; // alto del JFrame
    private final int iWidth=800; // ancho del JFrame
    
    private Base basMalo;         // Objeto malo
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaDBImage;   // Imagen a proyectar en Applet	
    private Graphics graGrafica;  // Objeto grafico de la Imagen
    
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
    private int iTotalFantasmitas; // total de fantasmas que hay
    private int iTotalJuanitos; // total de juanitos que hay
    private String nombreArchivo;    //Nombre del archivo.
    
    private Image imgChimpy;
    private Image imgFantasmita;
    private Image imgJuanito;
    
 
    
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
                
        nombreArchivo= "UltimaPartida.txt";    //Nombre del archivo.
       
        imgJuanito = Toolkit.getDefaultToolkit().getImage(this.getClass()
                .getResource("Images/juanito1.png"));
        
		
		
        
        // se crean a los Juanitos
        iTotalJuanitos = (int) (Math.random() * 2) + 4; //Cantidad de Juanitos
        
        for(int iI = 0; iI < iTotalJuanitos ; iI++) {
            int iPosRandX  = (int) (Math.random() * iMAXANCHO);
            int iPosRandY  = (int) (Math.random() * iHeight) * -1;
            
            Base basJuanito = new Base(iPosRandX, iPosRandY,
                     iWidth / iMAXANCHO, iHeight / iMAXALTO,imgJuanito);
            iPosRandX = (int) (Math.random() * (iWidth - 
                        basJuanito.getAncho()));
            
            
            basJuanito.setX(iPosRandX * (iWidth / iMAXANCHO)  );
            lklJuanitos.add(basJuanito);
        }
        
         //Se cargan las imágenes(cuadros) para la animación de fantasmita
        imgFantasmita = Toolkit.getDefaultToolkit().getImage(this.getClass()
                .getResource("Images/fantasmita1.png"));
        
        
        
        //Se crean los fantasmas
        iTotalFantasmitas = (int) (Math.random() * 3) + 5; //Cantidad de fantasmas
        for(int iI = 0; iI < iTotalFantasmitas ; iI++) {
            int iPosRandX  = (int) (Math.random() * iWidth) * -1;
            int iPosRandY  = (int) (Math.random() * (iHeight));
            Base basFantasma = new Base(iPosRandX, iPosRandY,
                     iWidth / iMAXANCHO, iHeight / iMAXALTO,
                    imgFantasmita);
            iPosRandY = (int) (Math.random() * (iHeight - 
                    basFantasma.getAlto()));
            basFantasma.setY(iPosRandY);
            lklFantasmas.add(basFantasma);
        }
        
        //Se cargan las imágenes(cuadros) para la animación de chimpy
        imgChimpy = Toolkit.getDefaultToolkit().getImage(this.getClass()
                .getResource("Images/Chimpy1.png"));
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * iWidth / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * iHeight / iMAXALTO;        
	basMalo = new Base(iWidth /2,iHeight / 2,
                 iWidth / iMAXANCHO, iHeight / iMAXALTO,imgChimpy);
        
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
        if (basMalo.getY() + basMalo.getAlto() > iHeight) {
            iDireccion = 0;
            basMalo.setY(iHeight - basMalo.getAlto());
        }
        //Si se sale hacia la izquierda
        if (basMalo.getX() < 0) {
            iDireccion = 0;
            basMalo.setX(0);
        }
        //Si se sale hacia la derecha
        if (basMalo.getX() + basMalo.getAncho() > iWidth) { 
            iDireccion = 0;
            basMalo.setX(iWidth - basMalo.getAncho());
        }
        
        //Checo colision con los juanitos
        for(Base basJuanito:lklJuanitos) {
            if (basJuanito.intersecta(basMalo)) {
                iJuanitosChocaron++;
                int iPosRandX  = (int) (Math.random() * (iWidth - 
                        basJuanito.getAncho()));
                int iPosRandY  = (int) (Math.random() * iHeight) * -1;
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
                int iPosRandX  = (int) (Math.random() * iWidth) * -1;
                int iPosRandY  = (int) (Math.random() * (iHeight -
                        basFantasma.getAlto()));
                basFantasma.setX(iPosRandX);
                basFantasma.setY(iPosRandY);
                scSonidoChimpy2.play();
                iScore++;
            }
        }
        
        //Checo clision de Juanitos con piso
        for(Base basJuanito:lklJuanitos) {
            if (basJuanito.getY() + basJuanito.getAlto() > iHeight) {
                int iPosRandX  = (int) (Math.random() * (iWidth - 
                        basJuanito.getAncho()));
                int iPosRandY  = (int) (Math.random() * iHeight) * -1;
                basJuanito.setX(iPosRandX);
                basJuanito.setY(iPosRandY);
            }
        }
        
        //Checo colision de fantasmas con pared
        for(Base basFantasma:lklFantasmas) {
            if (basFantasma.getX() + basFantasma.getAncho() > iWidth){
                int iPosRandX  = (int) (Math.random() * iWidth) * -1;
                int iPosRandY  = (int) (Math.random() * (iHeight -
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
        
        // Inicializan el DoubleBuffer
        if (imaDBImage == null){
            imaDBImage = createImage (this.getSize().width, this.getSize().height);
            graGrafica = imaDBImage.getGraphics ();
	}
        
        Image imgFondo = Toolkit.getDefaultToolkit().getImage(this.getClass().
                getResource("Ciudad.png"));
        
        graGrafica.drawImage(imgFondo, 0,0, this);
        
        // Actualiza el Foreground.
        graGrafica.setColor (getForeground());
        paint1(graGrafica);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaDBImage, 0, 0, this);
        
        
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
                
                
                basFantasma.paint(graDibujo, this);
            }
            //Dibuja la imagen de malo en el Applet
            
           
            basMalo.paint(graDibujo,this);
            graDibujo.setColor(Color.RED); //Escribo en color rojo
            graDibujo.drawString("Vidas: " + iVidas, 10, 40);   //Escribo vidas
            graDibujo.drawString("Puntos: " + iScore, 10, 60);  // escribo score
            
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
        
        if (bFin){  //Dibujo el fin del juego
            URL urlImagenFin = this.getClass().getResource("game Over.png");
            graDibujo.drawImage(Toolkit.getDefaultToolkit().getImage(urlImagenFin)
                    , iWidth /2, iHeight / 2, this);
        }
        
    }
    
    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
     * @param keyEvent es el <code>evento</code> generado al presionar las teclas.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        
        if (keyEvent.getKeyCode() == KeyEvent.VK_W) { 
            iDireccion = 1; //Cambio direccion
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_S) { 
            iDireccion = 2; //Cambio direccion
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_A) { 
            iDireccion = 3; //Cambio direccion
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_D) { 
            iDireccion = 4; //Cambio direccion
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
            iPausa = iPausa * -1;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_G) {
            try{
		// si se preisona la G se graba el archivo con todos los datos
		grabaArchivo();
                
            }catch(IOException expExeption){
                    
            }
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_C) {
            try{
		// si se preisona la C se carga el archivo con todos los datos
		leeArchivo();
                
            }catch(IOException expExeption){
                    
            }
         }
        if (keyEvent.getKeyCode() == KeyEvent. VK_ESCAPE) {
            bFin = true;    //Cambio direccion
        }
        //repaint();
        
    }
    
    
    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que no es de accion.
     * @param keyEvent es el <code>evento</code> que se genera en al presionar las teclas.
     */
    
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        
    }
    
    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla presionada.
     * @param keyEvent es el <code>evento</code> que se genera en al soltar las teclas.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        
    }
    /**
     * Metodo que lee a informacion de un archivo.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException{
    	BufferedReader fileIn;
        try{
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	} catch (FileNotFoundException e){
    		File filArchivo = new File(nombreArchivo);
    		PrintWriter fileOut = new PrintWriter(filArchivo);
    		fileOut.println("");
    		fileOut.close();
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	}
    	String sDato = fileIn.readLine();
        int iDato;
    	if (!sDato.equals("")){
    		
            // establece el score y las vidas
            iDato=Integer.parseInt(sDato);
            iScore=iDato;
            iDato=Integer.parseInt(fileIn.readLine());  
            iVidas=iDato;
            iDato=Integer.parseInt(fileIn.readLine());  
            iDireccion=iDato;
            iDato=Integer.parseInt(fileIn.readLine());  
            iPausa=iDato;
            // establece x y y de basMalo
            iDato=Integer.parseInt(fileIn.readLine()); 
            basMalo.setX(iDato);
            iDato=Integer.parseInt(fileIn.readLine()); 
            basMalo.setY(iDato);
               
            // los fantasmas
            iDato=Integer.parseInt(fileIn.readLine());
            iTotalFantasmitas=iDato;
                
            lklFantasmas.clear();
            for(int iI = 0; iI < iTotalFantasmitas ; iI++) {
                
                int iPosX=Integer.parseInt(fileIn.readLine());
                int iPosY=Integer.parseInt(fileIn.readLine());
                Base basFantasma = new Base(iPosX, iPosY,
                    iWidth / iMAXANCHO, iHeight / iMAXALTO,imgFantasmita);
                
                lklFantasmas.add(basFantasma);
            }
            // los fantasmas
            iDato=Integer.parseInt(fileIn.readLine());
            iTotalJuanitos=iDato;
                
            lklJuanitos.clear();
            for(int iI = 0; iI < iTotalJuanitos ; iI++) {
                
                int iPosX=Integer.parseInt(fileIn.readLine());
                int iPosY=Integer.parseInt(fileIn.readLine());
            
                Base basJuanito = new Base(iPosX, iPosY,
                        iWidth / iMAXANCHO, iHeight / iMAXALTO,imgJuanito);

                lklJuanitos.add(basJuanito);
            }
                
            fileIn.close();
                
    		
    	}
        
        
    }
	
    /**
     * Metodo que escribe en el archivo
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException{
        
    	PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
        fileOut.println("" + iScore);
        fileOut.println("" + iVidas);
        fileOut.println("" + iDireccion);
        fileOut.println("" + iPausa);
        fileOut.println("" + basMalo.getX());
        fileOut.println("" + basMalo.getY());
        fileOut.println("" + iTotalFantasmitas);
        
        for(Base basFantasmita:lklFantasmas) {
            
            fileOut.println("" + basFantasmita.getX());
            fileOut.println("" + basFantasmita.getY());
        }
        fileOut.println("" + iTotalJuanitos);
        for(Base basJuanito:lklJuanitos) {
            
            fileOut.println("" + basJuanito.getX());
            fileOut.println("" + basJuanito.getY());
        }
        
       
    	fileOut.close();	
        
        
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
