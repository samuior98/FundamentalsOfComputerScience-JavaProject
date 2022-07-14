import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Game2048 {

	private JFrame frame;
	private JTextField[][] matriceA;
	private JTextField[][] matriceB;
	private JButton[] bottoni;
	private JLabel messaggio;
	private String titoloFinestra;
	private int numeroRigheA;
	private int numeroColonneA;
	private int numeroRigheB;
	private int numeroColonneB;
	private int numeroBottoni;

	/* INIZIO PARTE DA PERSONALIZZARE */
	private JLabel scoreA, scoreB;
	private JLabel giocatoreUno, giocatoreDue;
	private boolean playerOne;
	private boolean vittoriaA, vittoriaB;
	private boolean sconfittaA, sconfittaB;
	private boolean nextA, nextB;
	private int scoreOne, scoreTwo;
	private int spaceA, spaceB;
	private int target;
	private Random rand;
	/* FINE PARTE DA PERSONALIZZARE */

	
	//COSTRUTTORE:imposta le caratteristiche del pannello e invoca inizializza() che inizializza gli elementi grafici
	public Game2048() {  
		/* INIZIO PARTE DA PERSONALIZZARE */
		titoloFinestra = "2048";
		numeroRigheA = 10;
		numeroColonneA = 10;
		numeroRigheB = 10;
		numeroColonneB = 10;
		numeroBottoni = 4;
		playerOne = true;
		vittoriaA = vittoriaB = sconfittaA = sconfittaB = false;
		nextA = nextB = true;
		scoreOne = scoreTwo = 0;
		target = 2048;
		rand = new Random();
		/* FINE PARTE DA PERSONALIZZARE */
		inizializza();
	}

	private void bottonePremuto(int numeroBottone) {  
		/* INIZIO PARTE DA PERSONALIZZARE */
		if(!vittoriaA && !vittoriaB && !sconfittaA && !sconfittaB) {
			boolean moved = false;
			if (playerOne) {
				moved = moveA(numeroBottone);
				scoreA.setText("Punteggio: " + scoreOne);
				if (spaceA == 0 && !nextA)
					sconfittaA = true;
			}
			
			else {
				moved = moveB(numeroBottone);
				scoreB.setText("Punteggio: " + scoreTwo);
				if (spaceB == 0 && !nextB)
					sconfittaB = true;
			}
			
			if (moved) {
				setMessaggio(playerOne ? "Turno giocatore 2" : "Turno giocatore 1");
				playerOne = !playerOne;
			}
			
			if (vittoriaA)
				setMessaggio("Ha vinto il giocatore 1!");
			else if (vittoriaB)
				setMessaggio("Ha vinto il giocatore 2!");
			else if (sconfittaA)
				setMessaggio("Il giocatore 1 ha perso!");
			else if (sconfittaB)
				setMessaggio("Il giocatore 2 ha perso!");
		}
		/* FINE PARTE DA PERSONALIZZARE */
	}
	
	private boolean moveA(int numeroBottone) { 
		boolean moved = false;
		String[][] ms = getMatriceA();
		int[][] mi = convertiInMatriceIntera(ms);
		
		if (numeroBottone == 1) { //UP
			rotateL(mi);
			moved = merge(mi, true);
			if (moved)
				spaceA = spawnTile(mi);
			rotateR(mi);
		}
		else if (numeroBottone == 2) { //RIGHT
			rotateL(mi);
			rotateL(mi);
			moved = merge(mi, true);
			if (moved)
				spaceA = spawnTile(mi);
			rotateR(mi);
			rotateR(mi);
		}
		else if (numeroBottone == 3) { //DOWN
			rotateR(mi);
			moved = merge(mi, true);
			if (moved)
				spaceA = spawnTile(mi);
			rotateL(mi);
		}
		else {                        //LEFT
			moved = merge(mi, true);
			if (moved)
				spaceA = spawnTile(mi);
		}
		
		nextA = movesAvailable(mi);
		ms = convertiDaMatriceIntera(mi);
		setMatriceA(ms);
		return moved;
	}
	
	private boolean moveB(int numeroBottone) {
		boolean moved = false;
		String[][] ms = getMatriceB();
		int[][] mi = convertiInMatriceIntera(ms);
		if (numeroBottone == 1) {
			rotateL(mi);
			moved = merge(mi, true);
			if (moved)
				spaceB = spawnTile(mi);
			rotateR(mi);
		}
		else if (numeroBottone == 2) {
			rotateL(mi);
			rotateL(mi);
			moved = merge(mi, true);
			if (moved)
				spaceB = spawnTile(mi);
			rotateR(mi);
			rotateR(mi);
		}
		else if (numeroBottone == 3) {
			rotateR(mi);
			moved = merge(mi, true);
			if (moved)
				spaceB = spawnTile(mi);
			rotateL(mi);
		}
		else {
			moved = merge(mi, true);
			if (moved)
				spaceB = spawnTile(mi);
		}
		nextB = movesAvailable(mi);
		ms = convertiDaMatriceIntera(mi);
		setMatriceB(ms);
		return moved;
	}
	
    private boolean merge(int[][] m, boolean updateScore) { 
    	boolean nullMove = true;
    	for (int i = 0; i < m.length; i++) {
			ArrayList<Integer> row = new ArrayList<>();
			for (int j = 0; j < m.length; j++)
				if (m[i][j] != 0)
					row.add(m[i][j]);
			for (int k = 0; k < row.size() - 1 ; k++)
				if (row.get(k).equals(row.get(k + 1))) {
					int newTile = 2 * row.get(k);
					row.set(k, newTile);
					if (updateScore) {
						if (playerOne) {
						    scoreOne += newTile;
						    if (newTile == target)
						    	vittoriaA = true;
						}
						else {
							scoreTwo += newTile;
							if (newTile == target)
						    	vittoriaB = true;
						}
					}
					row.set(k + 1, 0);
				}
			
			for (int k = 0; k < row.size(); k++)
				if (row.get(k) == 0)
					row.remove(k);
			
			int length = m.length - row.size();
			for (int k = 0; k < length; k++)
					row.add(0);
			
			for (int j = 0; j < row.size(); j++) {
				if (m[i][j] != row.get(j) && nullMove)
					nullMove = false;
				m[i][j] = row.get(j);
			}
    	}
    	return !nullMove;
    }
    
    private void rotateL(int[][] m) {  
    	int[][] mc = new int[m.length][m.length];
    	for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			mc[i][j] = m[i][j];
    	for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			m[m.length - j - 1][i] = mc[i][j];
    }
    
    private void rotateR(int[][] m) {  
    	int[][] mc = new int[m.length][m.length];
    	for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			mc[i][j] = m[i][j];
    	for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			m[j][m.length - i - 1] = mc[i][j];
    }
    
    
    //Tale metodo verifica se il giocatore in turno ha mosse da effettuare o è bloccato.
    private boolean movesAvailable(int[][] m) { 
    	int[][] mc = new int[m.length][m.length];
    	for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			mc[i][j] = m[i][j];
		merge(mc,false);
		for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			if (mc[i][j] != m[i][j])
    				return true;
		
		rotateL(mc);
		merge(mc, false);
		rotateR(mc);
		for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			if (mc[i][j] != m[i][j])
    				return true;
		
		rotateR(mc);
		merge(mc, false);
		rotateL(mc);
		for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			if (mc[i][j] != m[i][j])
    				return true;
		
		rotateL(mc);
		rotateL(mc);
		merge(mc, false);
		rotateR(mc);
		rotateR(mc);
		for (int i = 0; i < m.length; i++)
    		for (int j = 0; j < m.length; j++)
    			if (mc[i][j] != m[i][j])
    				return true;
		return false;
    }
    
    
    //Dopo aver effettuato la mossa tale metodo fa apparire un nuovo numero (2) in una casella vuota, scelta casualmente,
    // della matrice del giocatore in turno.
	private int spawnTile(int[][] m) {  
		ArrayList<Integer> zero_row = new ArrayList<>();
		ArrayList<Integer> zero_col = new ArrayList<>();
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m.length; j++)
				if (m[i][j] == 0) {
					zero_row.add(i);
					zero_col.add(j);
				}
		
		int freeSpace = zero_row.size();
		if (freeSpace != 0) {
		    int rand_index = rand.nextInt(zero_row.size());
		    m[zero_row.get(rand_index)][zero_col.get(rand_index)] = 2;
		    freeSpace--;
		}
		return freeSpace;
	}
	
	private Color getForeground(String n) {
		switch (n) {
		case "2": return new Color(117, 102, 83);
		case "4": return new Color(117, 102, 83);
		}
		return Color.WHITE;
	}
	
	private Color getBackground(String n) {
		switch (n) {
		case "2":    return new Color(0xeee4da);
	    case "4":    return new Color(0xede0c8);
	    case "8":    return new Color(0xf2b179);
	    case "16":   return new Color(0xf59563);
	    case "32":   return new Color(0xf67c5f);
	    case "64":   return new Color(0xf65e3b);
	    case "128":  return new Color(0xedcf72);
	    case "256":  return new Color(0xedcc61);
	    case "512":  return new Color(0xedc850);
	    case "1024": return new Color(0xedc53f);
	    case "2048": return new Color(0xedc22e);
	    }
	    return new Color(0xcdc1b4);
	}
	
	private void randomMove() {
		while (!vittoriaA && !vittoriaB && !sconfittaA && !sconfittaB) {
			int randint = rand.nextInt(4);
			bottonePremuto(randint);
		}
	}

	
	//Crea un'istanza della classe e imposta le etichette dei bottoni
	public static void main(String[] args) {  
		Game2048 p = new Game2048();
		/* INIZIO PARTE DA PERSONALIZZARE */
		p.setEtichettaBottone(1, "Su");
		p.setEtichettaBottone(2, "Destra");
		p.setEtichettaBottone(3, "Giù");
		p.setEtichettaBottone(4, "Sinistra");
		//p.randomMove();
		/* FINE PARTE DA PERSONALIZZARE */
	}


	
	private void setMessaggio(String m) {     //Imposta il messaggio da visualizzare all'utente
		messaggio.setText(m);
	}

	private void setEtichettaBottone(int numeroBottone, String etichetta) { //Imposta l'etichetta di un bottone
		bottoni[numeroBottone-1].setText(etichetta);
	}

	private String[][] getMatriceA() {  //Restituisce il contenuto dell'attuale matrice A
		String[][] ret = new String[numeroRigheA][numeroColonneA];
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				ret[i][j] = matriceA[i][j].getText();
		return ret;
	}

	private String[][] getMatriceB() {  
		String[][] ret = new String[numeroRigheB][numeroColonneB];
		for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				ret[i][j] = matriceB[i][j].getText();
		return ret;
	}

	private void setMatriceA(String[][] A) {  //Imposta il contenuto della matrice A
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++) {
				matriceA[i][j].setForeground(getForeground(A[i][j]));  // aggiunta di colori per le caselle.
				matriceA[i][j].setBackground(getBackground(A[i][j]));
				matriceA[i][j].setText(A[i][j]);
			}
	}

	private void setMatriceB(String[][] B) {  
		for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++) {
				matriceB[i][j].setForeground(getForeground(B[i][j]));  // aggiunta di colori per le caselle.
				matriceB[i][j].setBackground(getBackground(B[i][j]));
				matriceB[i][j].setText(B[i][j]);
			}
	}
	
	private void bloccaMatriceA() {  //Rende la matrice A non modificabile
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				matriceA[i][j].setEditable(false);
	}

	private void sbloccaMatriceA() { //Rende la matrice A modificabile
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				matriceA[i][j].setEditable(true);
	}
	
	private void bloccaMatriceB() {
		for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				matriceB[i][j].setEditable(false);
	}

	private void sbloccaMatriceB() {
		for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				matriceB[i][j].setEditable(true);
	}
	
	private int[][] convertiInMatriceIntera(String[][] m) { //Converte una matrice di stringhe in una matrice di interi
		int nRighe=m.length;
		int nColonne=m[0].length;
		int[][] ret = new int[nRighe][nColonne];
		for(int i=0;i<nRighe;i++)
			for(int j=0;j<nColonne;j++)
				if(m[i][j].equals(""))
					ret[i][j]=0;
				else
					ret[i][j]=Integer.parseInt(m[i][j]);
		return ret;
	}

	private String[][] convertiDaMatriceIntera(int[][] m) { //Converte una matrice di interi in una matrice di stringhe 
		int nRighe=m.length;
		int nColonne=m[0].length;
		String[][] ret = new String[nRighe][nColonne];
		for(int i=0;i<nRighe;i++)
			for(int j=0;j<nColonne;j++)
				if (m[i][j] == 0)                   // le caselle contenenti 0 sono rese invisibili.
					ret[i][j]="";
			    else
				    ret[i][j]=""+m[i][j];
		return ret;
	}

	


	private void inizializza() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		frame.setVisible(true);
		frame.setBounds(150, 40, 890, 670);
		frame.setResizable(false);
		frame.setTitle(titoloFinestra);
		frame.getContentPane().setBackground(new Color(187, 173, 160));

		messaggio = new JLabel("Turno giocatore 1");
		frame.getContentPane().add(messaggio);
		messaggio.setBounds(285, 499, 300, 30);
		messaggio.setHorizontalAlignment(JLabel.CENTER);
		messaggio.setFont(new Font("Arial", Font.BOLD, 10));
		
		giocatoreUno = new JLabel("Giocatore 1");
		frame.getContentPane().add(giocatoreUno);
		giocatoreUno.setBounds(200, 8, 100, 30);
		giocatoreUno.setFont(new Font("Arial", Font.BOLD, 10));
		
		giocatoreDue = new JLabel("Giocatore 2");
		frame.getContentPane().add(giocatoreDue);
		giocatoreDue.setBounds(620, 8, 100, 30);
		giocatoreDue.setFont(new Font("Arial", Font.BOLD, 10));

		scoreA = new JLabel("Punteggio: " + scoreOne);
		frame.getContentPane().add(scoreA);
		scoreA.setBounds(25, 398, 400, 30);
		scoreA.setHorizontalAlignment(JLabel.CENTER);
		scoreA.setFont(new Font("Arial", Font.BOLD, 10));
		
		scoreB = new JLabel("Punteggio: " + scoreTwo);
		frame.getContentPane().add(scoreB);
		scoreB.setBounds(440, 398, 400, 30);
		scoreB.setHorizontalAlignment(JLabel.CENTER);
		scoreB.setFont(new Font("Arial", Font.BOLD, 10));
		
		bottoni = new JButton[numeroBottoni];
		ActionListener listener = new PressioneBottoni();
		for(int i = 0; i < 4; i++) {
			bottoni[i] = new JButton();
			if (i % 2 == 0)
			    bottoni[i].setBounds(400, 430 + (i * 65), 70, 40);
			else
				bottoni[i].setBounds(600 - (i * 100), 494, 70, 40);
			bottoni[i].setBackground(new Color(117, 102, 83));
			bottoni[i].setFont(new Font("Arial", Font.BOLD, 10));
			bottoni[i].setForeground(Color.WHITE);
			bottoni[i].setBorderPainted(false);
			bottoni[i].addActionListener(listener);
			frame.getContentPane().add(bottoni[i]);
		}
		
		matriceA = new JTextField[numeroRigheA][numeroColonneA];
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++) {
				JTextField campoTesto = new JTextField("");
				frame.getContentPane().add(campoTesto);
				campoTesto.setBounds(50+35*j, 45+35*i, 33, 33);
				campoTesto.setHorizontalAlignment(JTextField.CENTER);
				campoTesto.setFont(new Font("Arial", Font.BOLD, 12));
				matriceA[i][j] = campoTesto;
			}
/*
		for(int i=0;i<numeroRigheA;i++)
		{	JLabel numeroRigaA = new JLabel(""+i);
			frame.getContentPane().add(numeroRigaA);
			numeroRigaA.setBounds(30, 60+20*i, 30, 20);
		}
		for(int j=0;j<numeroColonneA;j++)
		{	JLabel numeroColonnaA = new JLabel(""+j);
			frame.getContentPane().add(numeroColonnaA);
			numeroColonnaA.setBounds(60+30*j, 40, 30, 20);
		}
*/
		if(numeroRigheB != 0 || numeroColonneB != 0) {
			matriceB = new JTextField[numeroRigheB][numeroColonneB];
			for(int i=0;i<numeroRigheB;i++)
				for(int j=0;j<numeroColonneB;j++) {
					JTextField campoTesto = new JTextField("");
					frame.getContentPane().add(campoTesto);
					campoTesto.setBounds(470+35*j, 45+35*i, 33, 33);
					campoTesto.setHorizontalAlignment(JTextField.CENTER);
					campoTesto.setFont(new Font("Arial", Font.BOLD, 12));
					matriceB[i][j] = campoTesto;
				}
/*
            for(int i=0;i<numeroRigheB;i++)
			{	JLabel numeroRigaB = new JLabel(""+i);
				frame.getContentPane().add(numeroRigaB);
				numeroRigaB.setBounds(410, 60+20*i, 30, 20);
			}
			for(int j=0;j<numeroColonneB;j++)
			{	JLabel numeroColonnaB = new JLabel(""+j);
				frame.getContentPane().add(numeroColonnaB);
				numeroColonnaB.setBounds(440+30*j, 40, 30, 20);
			}
*/
		}
		String[][] msa = getMatriceA();
		String[][] msb = getMatriceB();
		int[][] mia = convertiInMatriceIntera(msa);
		int[][] mib = convertiInMatriceIntera(msb);
		spaceA = spawnTile(mia);
		//spaceA = spawnTile(mia);
		spaceB = spawnTile(mib);
		//spaceB = spawnTile(mib);
		msa = convertiDaMatriceIntera(mia);
		msb = convertiDaMatriceIntera(mib);
		setMatriceA(msa);
		setMatriceB(msb);
		bloccaMatriceA();
		bloccaMatriceB();
	}

	private class PressioneBottoni implements ActionListener{	
		public void actionPerformed(ActionEvent e){	
			int numeroBottonePremuto = -1;
			for(int i=0;i<numeroBottoni;i++)
				if(e.getSource()==bottoni[i])
					numeroBottonePremuto = i + 1;
			bottonePremuto(numeroBottonePremuto);
		}
	}

}//Game2048
