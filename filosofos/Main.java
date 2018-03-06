
package filosofos;

import javax.swing.JOptionPane;

/**
 *
 * @author Oscar Eduardo López Guzmán (Sheen)
 * Clase Main, inicializa la aplicación.
 */
public class Main {
    
    public static void main(String[] args){
		Integer a;
		a = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántos filósofos van a comer?: "));
		new Mesa(a).setVisible(true);
	}
    
}
