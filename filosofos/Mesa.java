
package filosofos;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.*;
import javax.swing.*;
/**
 *
 * @author Oscar Eduardo López Guzmán(Sheen)
 * Clase Mesa, genera una mesa de n filósofos
 */
public class Mesa extends JFrame{
        //Declaración de variables
	private Filosofo [] filosofos;
	private JLabel[] l,l2,l3;
        private JLabel inicio,finalC,t,m;
	private static JButton[] btn;
	private Lock lock;
	private Condition[] con;
	static Calendar horai = new GregorianCalendar();
	static int[] hinicio = new int[3];
	int[] hfinal = new int[3];
	int acum=0;
        
        //getter de los label 3, pues se requiere modificarse desde otro lugar
        public JLabel[] getL3(){
            return l3;
        }
        
        
        
        //DISEÑO
        
	//Declarando las imágenes 
        ImageIcon h = new ImageIcon(getClass().getResource("/img/hambre.png"));
	ImageIcon hambriento = new ImageIcon(h.getImage().getScaledInstance(100, 80, Image.SCALE_DEFAULT));
        ImageIcon c = new ImageIcon(getClass().getResource("/img/comiendo.png"));
	ImageIcon comiendo = new ImageIcon(c.getImage().getScaledInstance(100, 80, Image.SCALE_DEFAULT));
        ImageIcon p = new ImageIcon(getClass().getResource("/img/clientes.png"));
	ImageIcon pensando = new ImageIcon(p.getImage().getScaledInstance(100, 80, Image.SCALE_DEFAULT));
        ImageIcon me = new ImageIcon(getClass().getResource("/img/mesa.png"));
	ImageIcon mesa = new ImageIcon(me.getImage().getScaledInstance(200, 180, Image.SCALE_DEFAULT));
        
        //Método que recibe las dimensiones del JFrame y devuelve la posición en que las etiqueras
        //deben ir acomodadas para que formen un círculo
        public JLabel crearLabel(int x, int y){
		JLabel label = new JLabel();
		label.setSize(120,100);
		label.setLocation(x - (label.getWidth()/2), y - (label.getHeight()/2));
		label.setText("PENSANDO");
		return label;
	}
        //Método que devuelve la posición en que los botones deben ir para formar un círculo
	public JButton crearButton(int i, int x, int y){
		JButton button = new JButton();
		button.setSize(120,20);
		button.setText("["+i+"]"+"Comer");
		button.setLocation(x - (button.getWidth()/2), y + 60);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				accionBtn(e);
			}
		});
		return button;
	}
        //Método que recibe un evento del botón e inicializa un hijo correspondiente a un filósofo
	private void accionBtn(ActionEvent e){
		int a = e.getActionCommand().indexOf('[');
		int b = e.getActionCommand().indexOf(']');
		int i = Integer.parseInt(e.getActionCommand().substring(a + 1,b).trim());
		this.btn[i].setVisible(false);
		this.filosofos[i].start();
                
                //Aquí se toma la hora de la maquina y se imprime
                hinicio[0] = horai.get(Calendar.HOUR_OF_DAY);
		hinicio[1] = horai.get(Calendar.MINUTE);
		hinicio[2] = horai.get(Calendar.SECOND);
		inicio.setText("Comenzaron a comer a las:  " +hinicio[0] +":"+hinicio[1]+":"+hinicio[2]);
                //Aquí se inicializan los demás hilos de los filósofos
                for (int p=0;p<this.filosofos.length;p++){
                    if(p!=i){
                        this.btn[p].setVisible(false);
                        this.filosofos[p].start();
                    }
                }
                
	}
        //Método que devuelve un array bidimensional con las proporciones para formar un círculo centrado
	public int[][] setPosition(int x, int y, int r, int n){
		int [][] resultado = new int [n][2];
		for(int i=0;i<n;i++){
			resultado[i][0] = (int) (x - (r*Math.sin(((2*Math.PI) / n ) * i))) +20;
			resultado[i][1] = (int) (y + (r*Math.cos(((2*Math.PI) / n ) * i))) +70;
		}
		return resultado;
	}
        
        //Método constructor de la mesa, recibe un entero correspondiente a los folósofos
        //sentados en la mesa
        
	public Mesa(int n){
		inicializar(n);
		this.lock = new ReentrantLock();
		this.filosofos = new Filosofo[n];
		this.con = new Condition[n];
		for(int i=0;i<n; i++){
			this.filosofos[i] = new Filosofo(i, this);
			this.filosofos[i].estado = "PENSANDO";
			this.con[i] = this.lock.newCondition();
		}
	}     
        
        //Inicializa todos los componentes gráficos
	public void inicializar(int n){
                //Propiedades de la ventana
		this.setTitle("Filósofos");
		this.setSize(800,700);
                this.getContentPane().setBackground(new Color(255,228,181));
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
                
                
		this.l = new JLabel[n];
		this.l2 = new JLabel[n];
		this.l3 = new JLabel[n];
		this.btn = new JButton[n];

                int [][] positions = setPosition((this.getWidth() / 2),(this.getHeight() /3),190,n);
		for(int i=0;i<n;i++){
			this.l[i] = crearLabel(positions[i][0],positions[i][1]+50);
			this.l2[i] = crearLabel(positions[i][0]+20,positions[i][1]);
			this.l3[i] = crearLabel(positions[i][0],positions[i][1]+110);
			this.btn[i] = crearButton(i,positions[i][0]-10, positions[i][1]+40);
			this.l[i].setText("");
                        this.l[i].setIcon(hambriento);
			this.l3[i].setText("");
			l3[i].setForeground(Color.RED);
			add(this.l[i]);
			add(this.l2[i]);
			add(this.l3[i]);
			add(this.btn[i]);
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JLabel titulo = new JLabel("<html><body><h1>CENA DE LOS FILÓSOFOS<h1></body></html>");
                titulo.setSize(500,20);
                JLabel desc = new JLabel("<html><body align=center>" + n + " filósofos están sentados en la mesa, ante ellos un plato de spaguetti tan resbaloso que necesitan"
                                           + " <br>2 tenedores para comerlo, sin embargo, solo hay un tenedor entre cada plato. Presione 'Comer'"
                                           + " <br>bajo alguno de los filósofos para que comiencen a comer todos.</body></html>");
                desc.setSize(800,100);
                titulo.setLocation((this.getWidth()/2-120), 20);
                desc.setLocation((this.getWidth()/2-250), 30);
                add(titulo);
                add(desc);
                inicio = new JLabel("");
                inicio.setSize(500,40);
                inicio.setLocation(10,580);
                finalC = new JLabel("");
                finalC.setSize(500,40);
                finalC.setLocation(10,600);
                t = new JLabel("");
                t.setSize(500,40);
                t.setLocation(10,620);
                m = new JLabel("");
                m.setSize(220,200);
                m.setLocation(300,270);
                m.setIcon(mesa);
                add(m);
                add(inicio);
                add(finalC);
                add(t);
	}
	
        
        
        
        
        
        //FUNCIONALIDAD
        
        //Método que cambia el estado de un filósofo así como el ícono que represente dicho estado
	public void cambioEstado(int i, String state){
		this.filosofos[i].estado = state;
                switch(state){
                    case "HAMBRIENTO":
                        this.l[i].setIcon(hambriento);
                        break;
                    case "COMIENDO" : 
                        this.l[i].setIcon(comiendo);
                        break;
                    case "PENSANDO": 
                        this.l[i].setIcon(pensando);
                        break;
                }
		this.l2[i].setText(state);
	}
        
        //Método que comprueba el estado de un filósofo
	public void test(int i){
		if(!this.filosofos[(i+1)% filosofos.length].estado.equals("COMIENDO")
				&& this.filosofos[i].estado.equals("HAMBRIENTO")
				&& !this.filosofos[(i+(filosofos.length - 1))%filosofos.length].estado.equals("COMIENDO")) {
			cambioEstado(i, "COMIENDO");
			con[i].signal();
		}
	}
        
        //Método para indicar que el proceso de comer correpondiente a un filósofo comenzará
        //es decir, va a comenzar a comer
	public void levantarCubiertos(int i) throws InterruptedException{
		lock.lock();
		try{
			cambioEstado(i,"HAMBRIENTO");
			test(i);
			if(!this.filosofos[i].estado.equals("COMIENDO")){
				this.con[i].await();
			}
		} finally {
			lock.unlock();
		}
	}
        
        //Método para indicar que el proceso de comer correpondiente a un filósofo terminó
        //es decir, terminó de comer
	public void bajarCubiertos(int i){
		lock.lock();
		try{
			cambioEstado(i,"PENSANDO");
			test((i+(filosofos.length - 1)) %filosofos.length);
			test((i+1) %filosofos.length);
			acum++;
		} finally{
			lock.unlock();
			Calendar horaf = new GregorianCalendar();
                        //Se comprueba que todos los filósofos hayan terminado de comer y entonces
                        //se captura la hora del sistema y se imprime
			if(acum == (filosofos.length)){
				hfinal[0] = horaf.get(Calendar.HOUR_OF_DAY);
				hfinal[1] = horaf.get(Calendar.MINUTE);
				hfinal[2] = horaf.get(Calendar.SECOND);
				finalC.setText("Terminaron de comer a las:  " +hfinal[0] +":"+hfinal[1]+":"+hfinal[2]);
                                t.setText(calculart(hfinal));
			}
		}
	}
        //Método que calcula la diferencia en segundos desde que se inició hasta que terminaron de
        //comer. Se considera que los filósofos podrían tardar horas.
        public String calculart(int[] hf){
            String t = "";
            if (hf[0]==hinicio[0]){
                if (hf[1]==hinicio[1]){
                    t = "Se tardaron " + (hf[2]-hinicio[2]) + " segundos en terminar.";
                } else if (hf[1]>hinicio[1]){
                    t = "Se tardaron " + (hf[2]+(60-hinicio[2]))*(60*(hf[1]-hinicio[1])) + " segundos en terminar.";
                }
            } else if (hf[1]>hinicio[1]){
                    t = "Se tardaron " + ((hf[2]+(60-hinicio[2]))*(60*(hf[1]-hinicio[1]))) * (60*(hf[2]-hinicio[2])) + " segundos en terminar.";
            }
            return t;
        }

	
}

