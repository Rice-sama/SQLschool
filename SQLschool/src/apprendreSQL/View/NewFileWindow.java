/*******************************************************************************
 * 	Java tool with a GUI to help learn SQL
 * 	
 *     Copyright (C) 2020  Bayad Nasr-eddine, Bayol Thibaud, Benazzi Naima, 
 *     Douma Fatima Ezzahra, Chaouche Sonia, Kanyamibwa Blandine
 *     (thesqlschool@hotmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package apprendreSQL.View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import apprendreSQL.Controller.EventManager;
import apprendreSQL.Controller.JsonManager;
import apprendreSQL.Model.analysisTypeMetier.semantique.Test;

public class NewFileWindow implements ActionListener, GetInformation, SimilarFunctions {

	private ArrayList<String> subjects = new ArrayList<>();
	private JsonManager jsonManager;
	private JFrame frmNouveauExercice;
	private JLabel lblNo, lblBd, lblTitre, lblSujet, lblQuestion, lblRponse,lblTest, lblNom, lblPre, lblPost;
	private JTextField textFieldNoExo, textField_sujet, textField_titre, textField_nomTest, textField_Test;
	private JComboBox<String> comboBoxBD, comboBoxSujet;
	private JComboBox<Test> comboBoxTest;
	private JTextArea textAreaQ, textAreaR, textAreaPre, textAreaPost;
	private JButton btnEnregistrer, btnAjouterQuestion, btnNouveauSujet, btnAddTest;
	private EventManager eventManager;

	public NewFileWindow(EventManager manager) {
		jsonManager = new JsonManager();
		eventManager = manager;
	}

	/**
	 * @wbp.parser.entryPoint
	 * 
	 *                        This function creates the interface for the creation
	 *                        of a new file
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}

		frmNouveauExercice = new JFrame();
		frmNouveauExercice.setBounds(100, 100, 840, 725);
		frmNouveauExercice.getContentPane().setLayout(null);

		lblNo = new JLabel("Nom du fichier :");
		lblNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNo.setBounds(104, 70, 122, 28);
		frmNouveauExercice.getContentPane().add(lblNo);

		textFieldNoExo = new JTextField();
		textFieldNoExo.setBounds(276, 73, 336, 26);
		frmNouveauExercice.getContentPane().add(textFieldNoExo);
		textFieldNoExo.setColumns(10);

		lblBd = new JLabel("BD :");
		lblBd.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBd.setBounds(104, 123, 69, 28);
		frmNouveauExercice.getContentPane().add(lblBd);

		comboBoxBD = new JComboBox<String>();
		comboBoxBD.setBounds(276, 125, 336, 26);
		frmNouveauExercice.getContentPane().add(comboBoxBD);

		for (String name : getDbFiles())
			comboBoxBD.addItem(name);

		lblTitre = new JLabel("Titre :");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTitre.setBounds(104, 178, 122, 28);
		frmNouveauExercice.getContentPane().add(lblTitre);

		textField_titre = new JTextField();
		textField_titre.setColumns(10);
		textField_titre.setBounds(276, 181, 336, 26);
		frmNouveauExercice.getContentPane().add(textField_titre);

		lblSujet = new JLabel("Sujet :");
		lblSujet.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSujet.setBounds(104, 237, 69, 28);
		frmNouveauExercice.getContentPane().add(lblSujet);

		textField_sujet = new JTextField();
		textField_sujet.setBounds(276, 239, 336, 26);
		frmNouveauExercice.getContentPane().add(textField_sujet);
		textField_sujet.setVisible(false);

		comboBoxSujet = new JComboBox<String>();
		comboBoxSujet.setBounds(276, 239, 336, 26);
		frmNouveauExercice.getContentPane().add(comboBoxSujet);

		for (String file : getJSONFiles())
			for (String subject : getSubjects(file))
				subjects.add(subject);
		
		subjects = (ArrayList<String>) subjects.stream().distinct().collect(toList());
		
		for (String name : subjects)
			comboBoxSujet.addItem(name);

		lblQuestion = new JLabel("Question :");
		lblQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblQuestion.setBounds(104, 315, 69, 28);
		frmNouveauExercice.getContentPane().add(lblQuestion);

		textAreaQ = new JTextArea();
		textAreaQ.setBounds(276, 318, 336, 75);
		frmNouveauExercice.getContentPane().add(textAreaQ);

		lblRponse = new JLabel("R\u00E9ponse :");
		lblRponse.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRponse.setBounds(104, 400, 69, 28);
		frmNouveauExercice.getContentPane().add(lblRponse);

		textAreaR = new JTextArea();
		textAreaR.setBounds(276, 407, 336, 75);
		frmNouveauExercice.getContentPane().add(textAreaR);
		
		lblTest = new JLabel("Tests :");
		lblTest.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTest.setBounds(104, 493, 69, 28);
		frmNouveauExercice.getContentPane().add(lblTest);
		
		comboBoxTest = new JComboBox<Test>();
		comboBoxTest.setBounds(276, 495, 336, 26);
		frmNouveauExercice.getContentPane().add(comboBoxTest);
		
		lblNom = new JLabel("Nom :");
		lblNom.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNom.setBounds(104, 530, 122, 28);
		frmNouveauExercice.getContentPane().add(lblNom);

		textField_nomTest = new JTextField();
		textField_nomTest.setColumns(10);
		textField_nomTest.setBounds(276, 533, 336, 26);
		frmNouveauExercice.getContentPane().add(textField_nomTest);

		
		btnEnregistrer = new JButton("Cr\u00E9er fichier JSON");
		btnEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textFieldNoExo.getText().endsWith(".json") && textFieldNoExo.getText().length() > 5) {
					// pour verifier que le nom de l'exo (fichier json ne soit pas vide)

					if (jsonManager.getListQuestion().size() > 0) {

						jsonManager.createJSON("resource/" + textFieldNoExo.getText());
						frmNouveauExercice.dispose();
					} else {
						JOptionPane.showMessageDialog(frmNouveauExercice, "Vous devez au moin ajouter une question.",
								"Erreur", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frmNouveauExercice,
							"Le fichier sp�cifi� doit finir par \".json\"et contenue au moin un caractere.", "Erreur",
							JOptionPane.WARNING_MESSAGE);
					new HighlightListener(textFieldNoExo);
				}
			}
		});
		btnEnregistrer.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEnregistrer.setBounds(455, 650, 138, 36);
		frmNouveauExercice.getContentPane().add(btnEnregistrer);

		btnAjouterQuestion = new JButton("Ajouter Question");
		btnAjouterQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveQuestion(frmNouveauExercice, textField_titre, textAreaQ, textAreaR, comboBoxBD);
			}

		});

		btnAjouterQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAjouterQuestion.setBounds(285, 650, 138, 36);
		frmNouveauExercice.getContentPane().add(btnAjouterQuestion);

		btnNouveauSujet = new JButton("Nouveau sujet");
		btnNouveauSujet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newSubject(btnNouveauSujet, comboBoxSujet, textField_sujet, subjects);
			}

		});
		btnNouveauSujet.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNouveauSujet.setBounds(276, 275, 139, 26);
		frmNouveauExercice.getContentPane().add(btnNouveauSujet);
		
		textAreaPre = new JTextArea();
		textAreaPre.setBounds(276, 570, 155, 73);
		frmNouveauExercice.getContentPane().add(textAreaPre);
		
		/*
		textAreaPre.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	textAreaPre.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		        // nothing
		    }
		});
		*/
		
		textAreaPost = new JTextArea();
		textAreaPost.setBounds(450, 570, 158, 73);
		frmNouveauExercice.getContentPane().add(textAreaPost);
		
		/*
		textAreaPost.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	textAreaPost.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		        // nothing
		    }
		});
		*/
		
		btnAddTest = new JButton("Ajout test");
		btnAddTest.addActionListener(new ActionListener(){
		    @Override
		    public void actionPerformed(ActionEvent e){
		    	if (!textField_nomTest.getText().trim().isEmpty()) {
		    		comboBoxTest.addItem(new Test(textField_nomTest.getText(), textAreaPre.getText(), textAreaPost.getText()));
		    		textField_nomTest.setText("");
		    		textAreaPre.setText("");
		    		textAreaPost.setText("");
		    	}
		    }
		});
		
		btnAddTest.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAddTest.setBounds(640, 600, 100, 40);
		frmNouveauExercice.getContentPane().add(btnAddTest);

		frmNouveauExercice.setVisible(true);
		frmNouveauExercice.setResizable(false);
		frmNouveauExercice.setLocationRelativeTo(null);
		frmNouveauExercice.setTitle("Nouveau Fichier");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		initialize();

	}

	@SuppressWarnings("rawtypes")
	private Collector toList() {
		return Collectors.toList();
	}

	/**
	 * A function that saves the new question in a list
	 * 
	 * @param frmNouveauExercice
	 * @param textField_titre
	 * @param textAreaQ
	 * @param textAreaR
	 * @param comboBoxBD
	 */
	private void saveQuestion(JFrame frmNouveauExercice, JTextField textField_titre, JTextArea textAreaQ,
			JTextArea textAreaR, JComboBox<String> comboBoxBD) {

		System.out.println(textAreaR.getText());
		if (!verifyExecution(eventManager, textAreaR.getText(), comboBoxBD.getSelectedItem().toString()))
			return;

		if (checkFields(frmNouveauExercice, textField_titre, textAreaQ, textAreaR)) {
			String sujet;
			if (comboBoxSujet.isVisible())
				sujet = comboBoxSujet.getSelectedItem().toString();
			else
				sujet = textField_sujet.getText();
			ArrayList<Test> tList = new ArrayList<>();
			for(int i = 0 ; i < comboBoxTest.getItemCount() ; i++) tList.add(comboBoxTest.getItemAt(i));
			if (jsonManager.addQuestion(comboBoxBD.getSelectedItem().toString(), sujet, textField_titre.getText(),
					textAreaQ.getText(), textAreaR.getText(), tList, true)) { //tochange
				JOptionPane.showMessageDialog(frmNouveauExercice, "La nouvelle question est ajout�e.",
						"Nouvelle Question", JOptionPane.INFORMATION_MESSAGE);
				textAreaQ.setText("");
				textAreaR.setText("");
				textField_titre.setText("");
			} else {
				JOptionPane.showMessageDialog(null, "Cette question existe d�j� (sujet,titre).", "Attention",
						JOptionPane.WARNING_MESSAGE);
			}

		}
	}

}
