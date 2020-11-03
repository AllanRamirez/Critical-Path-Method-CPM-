package Vista;

import Modelo.*;
import Control.Control;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observer;

public class View extends javax.swing.JFrame implements Observer{

    public View() {
        initComponents();
        jMenuItemLimpiar.setActionCommand("Limpiar");
        jMenuItemRecuperar.setActionCommand("Recuperar");
        jMenuItemGuardar.setActionCommand("Guardar");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setTitle("CMP - Metodo de la Ruta Critica");
        this.setResizable(false);  
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuArchivo = new javax.swing.JMenu();
        jMenuItemRecuperar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemGuardar = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemLimpiar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenuArchivo.setText("Archivo");

        jMenuItemRecuperar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemRecuperar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Vista/Recuperar.png"))); // NOI18N
        jMenuItemRecuperar.setText("Recuperar");
        jMenuItemRecuperar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRecuperarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemRecuperar);
        jMenuArchivo.add(jSeparator1);

        jMenuItemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Vista/Guardar.png"))); // NOI18N
        jMenuItemGuardar.setText("Guardar");
        jMenuItemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGuardarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemGuardar);
        jMenuArchivo.add(jSeparator2);

        jMenuItemLimpiar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Vista/Limpiar.png"))); // NOI18N
        jMenuItemLimpiar.setText("Limpiar");
        jMenuArchivo.add(jMenuItemLimpiar);

        jMenuBar1.add(jMenuArchivo);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemRecuperarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRecuperarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemRecuperarActionPerformed

    private void jMenuItemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenu jMenuArchivo;
    public javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenuItem jMenuItemGuardar;
    public javax.swing.JMenuItem jMenuItemLimpiar;
    public javax.swing.JMenuItem jMenuItemRecuperar;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables

    Modelo model;
    Control controller;
    public boolean draw;
    
    public void setModelo(Modelo model) {
        this.model = model;
        model.addObserver(this);
        this.addMouseMotionListener(this.controller);
    }

    public void setControl(Control controller) {
        this.controller = controller;
        jMenuItemGuardar.addActionListener(controller);
        jMenuItemLimpiar.addActionListener(controller);
        jMenuItemRecuperar.addActionListener(controller);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

    @Override //metodo que hay que sobreescribir por implementar Observer
    public void update(java.util.Observable o, Object arg) {
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 15));
        for (int i = 0; i < model.getActividades().size(); i++) {
            Actividad act = model.getActividades().get(i);
            g.setColor(Color.ORANGE);
            if (act.getEsDeRuta()){
                g.setColor(Color.RED);
            }
            g.fillOval(act.getPoint().x, act.getPoint().y, 50, 50);
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(act.getId())+"("+act.getDuracion()+")", act.getPoint().x + 13, act.getPoint().y + 30);
        }
        
        for (int j = 0; j < model.getRelaciones().size(); j++) {
            Relacion rel = model.getRelaciones().get(j);
            Actividad actOrigen = rel.getOrigen(); 
            Actividad actDestino = rel.getSucesora(); 
            g.setColor(Color.blue);
            
            if (actOrigen != actDestino) {
                Point x1 = actOrigen.getPoint();
                Point x2 = actDestino.getPoint();
                if (x1.x < x2.x) {
                    g.drawString("", (x1.x + x2.x) / 2 + 20, (x1.y + x2.y) / 2 + 20);
                    g.drawLine(x1.x + 48, x1.y + 20, x2.x, x2.y + 20);
                    g.fillOval(x2.x - 5, x2.y + 15, 10, 10);
                }else {
                    g.drawString("", (x1.x + x2.x) / 2 + 20, (x1.y + x2.y) / 2 + 20);
                    g.drawLine(x1.x, x1.y + 25, x2.x + 48, x2.y + 35);
                    g.fillOval(x2.x + 45, x2.y + 30, 10, 10);
                }  
            }
           
        }
          
        
        if (draw) { //este if es para dibujar la relacion entre actividades
            g.setColor(Color.green);
            g.drawLine(controller.inicio.x, controller.inicio.y, controller.destino.x, controller.destino.y);
        }
        
        g.setColor(Color.darkGray);
        g.drawString("Autores: Allan Ramirez, Nataly Fernandez", 750, this.getBounds().height - 7);
    }

}
