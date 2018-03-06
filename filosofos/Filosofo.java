
package filosofos;

/**
 *
 * @author Oscar Eduardo López Guzmán(Sheen)
 * Clase Filósofo hereda de la clase Thread y contiene los datos de un filósofo
 */
public class Filosofo extends Thread{
	public int id;
	public String estado;
	private Mesa m;
        //Constructor de filósofos, crea un nuevo filósofo y lo asigna a una mesa recibida
	public Filosofo(int id, Mesa m){
		this.id = id;
		this.estado = new String();
		this.m = m;
	}
        //Método sobreescrito run heredado de la clase thread, controla el hilo.
	public void run(){
		try {
			m.levantarCubiertos(id);
			Thread.sleep(3000);
			m.bajarCubiertos(id);
		}catch(InterruptedException e){
			
		} finally{
			m.getL3()[id].setText("Terminó de comer");
		}
	}
}
