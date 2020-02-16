package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class InterfazPrincipal extends Container implements ActionListener {
	
	
    public InterfazPrincipal() {
        setBackground(Color.WHITE);
        setLayout(null);
        
        JButton boton1 = new JButton("Consulta de instancias");
        JButton boton2 = new JButton("Instancia con igual valor de atributo");
        JButton boton3 = new JButton("Estadisticas por atributo");
        JButton boton4 = new JButton("Relacion entre instancias");
        
        
        boton1.setBounds(225, 50, 250, 50);
        boton2.setBounds(225, 150, 250, 50);
        boton3.setBounds(225, 250, 250, 50);
        boton4.setBounds(225, 350, 250, 50);
        add(boton1);
        add(boton2);
        add(boton3);
        add(boton4);
        boton1.addActionListener(this);
        boton2.addActionListener(this);
        boton3.addActionListener(this);
        boton4.addActionListener(this);
        SwingUtilities.updateComponentTreeUI(Main.frame);
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Consulta de instancias")) {
			Main.frame.setContentPane(new InterfazInstancias());
		}else if(e.getActionCommand().equals("Instancia con igual valor de atributo")) {
			Main.frame.setContentPane(new InterfazInstanciaIgual());
		}else if(e.getActionCommand().equals("Estadisticas por atributo")) {
			Main.frame.setContentPane(new InterfazEstadisticas());
		}else {
			Main.frame.setContentPane(new InterfazRelaciones());
		}
		
	}
}
