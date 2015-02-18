/**
 * Juego
 *
 * Examen Parcial
 * una changuita puede esterminar a los fantasmas  y ganar puntos
 * si choca con los juanitos, pierde vidas
 *
 * @author Juan Carlos Guzmán
 * @version 1.0
 * @date 11 feb 2015
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;


public class Juego extends Applet implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basMalo;         // Objeto malo
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy1;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoChimpy2;   // Objeto sonido de Chimpy
    
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
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
             
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
            int iPosRandY  = (int) (Math.random() * getHeight());
            Base basFantasma = new Base(iPosRandX, iPosRandY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
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
        URL urlSonidoChimpy1 = this.getClass().getResource("monkey1.wav");
        adcSonidoChimpy1 = getAudioClip (urlSonidoChimpy1);
        
        //defino el sonido 2
        URL urlSonidoChimpy2 = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy2 = getAudioClip (urlSonidoChimpy2);
        
        addKeyListener(this);   //Hago que se active con teclado
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
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
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza() {
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
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
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
                adcSonidoChimpy1.play();
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
                int iPosRandY  = (int) (Math.random() * getHeight());
                basFantasma.setX(iPosRandX);
                basFantasma.setY(iPosRandY);
                adcSonidoChimpy2.play();
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
                int iPosRandY  = (int) (Math.random() * getHeight());
                basFantasma.setX(iPosRandX);
                basFantasma.setY(iPosRandY);
            }
        }
        
        if (iVidas == 0) {
            bFin = true;    //Hago que se termine el juego
        }
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico) {
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
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) { 
            iDireccion = 1; //Cambio direccion
        }
        if (e.getKeyCode() == KeyEvent.VK_S) { 
            iDireccion = 2; //Cambio direccion
        }
        if (e.getKeyCode() == KeyEvent.VK_A) { 
            iDireccion = 3; //Cambio direccion
        }
        if (e.getKeyCode() == KeyEvent.VK_D) { 
            iDireccion = 4; //Cambio direccion
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            iPausa = iPausa * -1;
        }
        if (e.getKeyCode() == KeyEvent. VK_ESCAPE) {
            bFin = true;    //Cambio direccion
        }
    }
}