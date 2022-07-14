import javax.swing.*;
import java.awt.event.*;

public class PannelloGrafico
{	private JFrame frame;
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
	// Attributi dell'applicazione
	/* FINE PARTE DA PERSONALIZZARE */

	public PannelloGrafico()
	{	/* INIZIO PARTE DA PERSONALIZZARE */
		titoloFinestra = "Titolo finestra";
		numeroRigheA = 10;
		numeroColonneA = 10;
		numeroRigheB = 10;
		numeroColonneB = 10;
		numeroBottoni = 3;
		/* FINE PARTE DA PERSONALIZZARE */

		inizializza();
	}

	private void bottonePremuto(int numeroBottone)
	{	/* INIZIO PARTE DA PERSONALIZZARE */
		setMessaggio("Premuto il bottone "+numeroBottone);
		/* FINE PARTE DA PERSONALIZZARE */
	}

	public static void main(String[] args)
	{	PannelloGrafico p = new PannelloGrafico();
		/* INIZIO PARTE DA PERSONALIZZARE */
		p.setEtichettaBottone(1, "Bottone 1");
		p.setEtichettaBottone(2, "Bottone 2");
		p.setEtichettaBottone(3, "Bottone 3");
		/* FINE PARTE DA PERSONALIZZARE */
	}

	private void setMessaggio(String m)
	{	messaggio.setText(m);
	}

	private void setEtichettaBottone(int numeroBottone, String etichetta)
	{	bottoni[numeroBottone-1].setText(etichetta);
	}

	private String[][] getMatriceA()
	{	String[][] ret = new String[numeroRigheA][numeroColonneA];
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				ret[i][j] = matriceA[i][j].getText();
		return ret;
	}

	private String[][] getMatriceB()
	{	String[][] ret = new String[numeroRigheB][numeroColonneB];
		for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				ret[i][j] = matriceB[i][j].getText();
		return ret;
	}

	private void setMatriceA(String[][] A)
	{	for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				matriceA[i][j].setText(A[i][j]);
	}

	private void setMatriceB(String[][] B)
	{	for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				matriceB[i][j].setText(B[i][j]);
	}
	
	private void bloccaMatriceA()
	{	for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				matriceA[i][j].setEditable(false);;
	}

	private void sbloccaMatriceA()
	{	for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
				matriceA[i][j].setEditable(true);;
	}
	
	private void bloccaMatriceB()
	{	for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				matriceB[i][j].setEditable(false);;
	}

	private void sbloccaMatriceB()
	{	for(int i=0;i<numeroRigheB;i++)
			for(int j=0;j<numeroColonneB;j++)
				matriceB[i][j].setEditable(true);;
	}
	
	private int[][] convertiInMatriceIntera(String[][] m)
	{	int nRighe=m.length;
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

	private String[][] convertiDaMatriceIntera(int[][] m)
	{	int nRighe=m.length;
		int nColonne=m[0].length;
		String[][] ret = new String[nRighe][nColonne];
		for(int i=0;i<nRighe;i++)
			for(int j=0;j<nColonne;j++)
				ret[i][j]=""+m[i][j];
		return ret;
	}

	private void inizializza()
	{	frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{	System.out.println(e);
			System.exit(0);
		}
		frame.setVisible(true);
		frame.setBounds(100, 100, 780, 360+30*numeroBottoni);
		frame.setTitle(titoloFinestra);

		messaggio = new JLabel("");
		frame.getContentPane().add(messaggio);
		messaggio.setBounds(240, 270, 300, 30);
		messaggio.setHorizontalAlignment(JLabel.CENTER);

		bottoni=new JButton[numeroBottoni];
		ActionListener listener = new PressioneBottoni();
		for(int i=0;i<numeroBottoni;i++)
		{	JButton bottone=new JButton();
			bottone.setBounds(240, 300+30*i, 300, 30);
			bottoni[i]=bottone;
			bottone.addActionListener(listener);
			frame.getContentPane().add(bottone);
		}

		matriceA = new JTextField[numeroRigheA][numeroColonneA];
		for(int i=0;i<numeroRigheA;i++)
			for(int j=0;j<numeroColonneA;j++)
			{	JTextField campoTesto = new JTextField("");
				frame.getContentPane().add(campoTesto);
				campoTesto.setBounds(50+30*j, 60+20*i, 30, 20);
				campoTesto.setHorizontalAlignment(JTextField.CENTER);
				matriceA[i][j] = campoTesto;
			}
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

		if(numeroRigheB != 0 || numeroColonneB != 0)
		{	matriceB = new JTextField[numeroRigheB][numeroColonneB];
			for(int i=0;i<numeroRigheB;i++)
				for(int j=0;j<numeroColonneB;j++)
				{	JTextField campoTesto = new JTextField("");
					frame.getContentPane().add(campoTesto);
					campoTesto.setBounds(430+30*j, 60+20*i, 30, 20);
					campoTesto.setHorizontalAlignment(JTextField.CENTER);
					matriceB[i][j] = campoTesto;
				}
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
		}
	}

	private class PressioneBottoni implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	int numeroBottonePremuto = -1;
			for(int i=0;i<numeroBottoni;i++)
				if(e.getSource()==bottoni[i])
					numeroBottonePremuto = i + 1;
			bottonePremuto(numeroBottonePremuto);
		}
	}

}


