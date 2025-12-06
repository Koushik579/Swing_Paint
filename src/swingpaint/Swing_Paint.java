
package swingpaint;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class Swing_Paint extends javax.swing.JFrame {
    public Swing_Paint() {
        initComponents();
        drawing();
        capanel.add(canvas);
        events();
        trace();
        head.setLayout(new FlowLayout(FlowLayout.LEFT,25,10)); 
    }
    // mouse event for closing - minimising - changing size - moving frame  
    public int a = 0 ;
    boolean isMax = false;
    public int mouseX,mouseY;
    public void events()
    {
        close.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
                {
                    System.exit(0);
                }
            @Override
            public void mouseEntered(MouseEvent e)
                {
                    //close.setIcon(new ImageIcon(getClass().getResource("/img/closer.png")));
                    close.setOpaque(true);
                    close.setBackground(Color.red);
                }
            @Override
            public void mouseExited(MouseEvent e)
                {
                    //close.setIcon(new ImageIcon(getClass().getResource("/img/closew.png")));
                    close.setOpaque(false);
                    close.setBackground(new Color(102,0,102));
                }
        });  
        size.addMouseListener(new MouseAdapter()
        {  
           @Override
           public void mouseClicked(MouseEvent e)
                {
                    if(isMax)
                    {
                       Swing_Paint.this.setExtendedState(JFrame.NORMAL);
                       System.out.print(Swing_Paint.this.getExtendedState());
                       size.setIcon(new ImageIcon(getClass().getResource("/img/sboxw.png")));
                       isMax = false;
                    }
                    else
                    {

                        Swing_Paint.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        isMax = true;
                        size.setIcon(new ImageIcon(getClass().getResource("/img/dboxw.png")));
                        Swing_Paint.this.repaint();
                        canvas.repaint();
                        //canvas.setBounds(10, 10, (canvaspanel.getWidth()-30), (canvaspanel.getHeight()-30));
                    }
                }
           @Override
           public void mouseEntered(MouseEvent e)
                {
                    size.setOpaque(true);
                    size.setBackground(new Color(102,102,102));
                }
           @Override
            public void mouseExited(MouseEvent e)
                {
                    size.setOpaque(false);
                    size.setBackground(Color.black);
                }
           
        });        
        mini.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
                {
                   Swing_Paint.this.setState(JFrame.ICONIFIED);
                }
            @Override
            public void mouseEntered(MouseEvent e)
                {
                    mini.setOpaque(true);
                    mini.setBackground(new Color(102,102,102));
                }
            @Override
            public void mouseExited(MouseEvent e)
                {
                    mini.setOpaque(false);
                    mini.setBackground(Color.black);
                }
        });
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
    }   
    // tracing the canvas
    public Point start,end;
    public JPanel canvas;
    public ArrayList<Colorshape> store = new ArrayList();
    public Path2D currentpath = null;
    public int Shape_picker = 0;
    public void trace()
    {
        canvas.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                start = e.getPoint();
                end = start;
                canvas.repaint();
                
                if(ispencil)
                      {
                          currentpath = new Path2D.Double();
                          currentpath.moveTo(e.getX(),e.getY());
                      }       
            }
            @Override
            public void mouseReleased(MouseEvent e)
            {
                end = e.getPoint();
                int x = Math.min(start.x,end.x);
                int y = Math.min(start.y,end.y);
                int w = Math.abs(start.x - end.x);
                int h = Math.abs(start.y - end.y);
                
                if(isrectangle)
                {
                    store.add(new Colorshape(new Rectangle(x,y,w,h),col,fill_color));
                }
                else if(iscircle)
                {
                    store.add(new Colorshape(new Ellipse2D.Double(x,y,w,w),col,fill_color));
                }
                else if(isellipse)
                {
                    store.add(new Colorshape(new Ellipse2D.Double(x,y,w,h),col,fill_color));
                }
                else if(istriangle)
                {
                    int x1 = start.x;
                    int y1 = start.y;
                    int x2 = end.x;
                    int y2 = end.y;
                          
                    int midbase = (x1+x2)/2;
                    int top = Math.min(y1, y2) - Math.abs(x2 - x1)/2;
                          
                    int[] pointx = {x1,x2,midbase};
                    int[] pointy = {y1,y2,top};
                    
                    store.add(new Colorshape(new Polygon(pointx, pointy, 3),col,fill_color));
                }
                else if(isline)
                {
                    store.add(new Colorshape(new Line2D.Double(start.x,start.y,end.x,end.y),col,fill_color));
                }
                if(ispencil && currentpath!=null)
                {
                    store.add(new Colorshape(currentpath,col,fill_color));
                    currentpath=null;
                }
                canvas.repaint();
            } 
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                end = e.getPoint();
                if(ispencil && currentpath!=null)
                {
                    currentpath.lineTo(e.getX(), e.getY());
                    canvas.repaint();
                }                
                canvas.repaint();
            }
        });
        canvas.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
               if(ispaint)
                {
                    Point point_check = e.getPoint();
                    for(int a = (store.size()-1) ; a>=0 ; a++)
                    {
                        if(a>=0 && a<store.size())
                        {
                            Colorshape cs = store.get(a);
                            if(cs.shape.getBounds2D().contains(point_check))
                            {
                                System.out.println(cs.fillcolor);
                                cs.fillcolor = fill_color;
                                System.out.println("final"+cs.fillcolor);
                                Shape_picker = a;
                                canvas.repaint();
                                break;
                            }
                        }  
                    }      
                }
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(ispaint)
                {
                    Point p = e.getPoint();
                    for (int i = store.size() - 1; i >= 0; i--) 
                    {
                        if (store.get(i).shape.contains(p)) {
                            Toolkit toolkit = Toolkit.getDefaultToolkit();
                            Image img = toolkit.getImage(getClass().getResource("/img/paintbucket.png"));
                            Point hotspot = new Point(0,26);
                            Cursor custom = toolkit.createCustomCursor(img, hotspot, "paint");
                            canvas.setCursor(custom);
                            return;
                        }
                        else
                        {
                            canvas.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                }
                else if(isrectangle || iscircle || istriangle || isline || isellipse )
                {
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
                    Point hotspot = new Point(9,9);
                    Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
                    canvas.setCursor(custom);
                }
        
    }
});
    }   
    // drawing in the canvas
    public Color col = Color.black;
    public Color fill_color = Color.white;
    public void drawing()
    {
        canvas = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setColor(col);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setStroke(new BasicStroke(2));
                    for(Object r : store)
                    {
                       if(r instanceof Colorshape)
                       {
                           Colorshape obj = (Colorshape)r;
                           g2d.setColor(obj.fillcolor);
                           g2d.fill(obj.shape);
                           g2d.setColor(obj.color); 
                           g2d.draw(obj.shape);
                       }
                       else if(r instanceof Shape)
                       {
                           g2d.setColor(Color.black);
                           g2d.draw((Shape)r);   
                       }
                    }
                    if(start!=null || end!=null)
                    {
                      int x = Math.min(start.x,end.x);
                      int y = Math.min(start.y,end.y);
                      int w = Math.abs(start.x - end.x);
                      int h = Math.abs(start.y - end.y);
                       
                      if(isrectangle)
                      {
                          g2d.drawRect(x, y, w, h);
                      }
                      if(iscircle)
                      {
                          int d = Math.min(w, h);
                          g2d.drawOval(x, y, d, d);
                      }
                      if(isellipse)
                      {
                          Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, w, h);
                          g2d.draw(ellipse);
                      }
                      if(istriangle)
                      {
                          int x1 = start.x;
                          int y1 = start.y;
                          int x2 = end.x;
                          int y2 = end.y;
                          
                          int midbase = (x1+x2)/2;
                          int top = Math.min(y1, y2) - Math.abs(x2 - x1)/2;
                          
                          int[] pointx = {x1,x2,midbase};
                          int[] pointy = {y1,y2,top};
                          
                          g2d.drawPolygon(pointx, pointy, 3);
                      }
                      if(isline)
                      {
                          g2d.drawLine(start.x,start.y,end.x,end.y);
                      }
                      if(ispencil && currentpath != null)
                      {
                          g2d.draw(currentpath);
                      }
                    } 
                }
        };
        canvas.setOpaque(true);
        canvas.setBackground(Color.white);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        color = new javax.swing.JColorChooser();
        ok = new javax.swing.JButton();
        cancle = new javax.swing.JButton();
        select = new javax.swing.JButton();
        cancle2 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        close_mini = new javax.swing.JPanel();
        close = new javax.swing.JLabel();
        size = new javax.swing.JLabel();
        mini = new javax.swing.JLabel();
        canvaspanel = new javax.swing.JPanel();
        capanel = new javax.swing.JPanel();
        head = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        menu = new javax.swing.JPanel();
        rectan = new javax.swing.JButton();
        tri = new javax.swing.JButton();
        circ = new javax.swing.JButton();
        li = new javax.swing.JButton();
        elip = new javax.swing.JButton();
        pencil = new javax.swing.JButton();
        color_select = new javax.swing.JButton();
        erase = new javax.swing.JButton();
        undo = new javax.swing.JButton();
        redo = new javax.swing.JButton();
        colorlabel = new javax.swing.JLabel();
        paint = new javax.swing.JButton();
        fill2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle("Select_Color");
        jDialog1.setBackground(new java.awt.Color(204, 204, 204));

        ok.setBackground(new java.awt.Color(204, 204, 204));
        ok.setText("Select");
        ok.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        cancle.setBackground(new java.awt.Color(204, 204, 204));
        cancle.setText("Cancle");
        cancle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cancle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancleActionPerformed(evt);
            }
        });

        select.setBackground(new java.awt.Color(204, 204, 204));
        select.setText("Select");
        select.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });

        cancle2.setBackground(new java.awt.Color(204, 204, 204));
        cancle2.setText("Cancle");
        cancle2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cancle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancle2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(select)
                .addGap(18, 18, 18)
                .addComponent(ok)
                .addGap(54, 54, 54)
                .addComponent(cancle)
                .addGap(18, 18, 18)
                .addComponent(cancle2)
                .addContainerGap(129, Short.MAX_VALUE))
            .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok)
                    .addComponent(cancle)
                    .addComponent(select)
                    .addComponent(cancle2))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setUndecorated(true);

        close_mini.setBackground(new java.awt.Color(0, 0, 0));

        close.setBackground(new java.awt.Color(102, 0, 102));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/closew.png"))); // NOI18N

        size.setBackground(new java.awt.Color(204, 204, 204));
        size.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        size.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/sboxw.png"))); // NOI18N

        mini.setBackground(new java.awt.Color(102, 102, 102));
        mini.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/miniw.png"))); // NOI18N

        javax.swing.GroupLayout close_miniLayout = new javax.swing.GroupLayout(close_mini);
        close_mini.setLayout(close_miniLayout);
        close_miniLayout.setHorizontalGroup(
            close_miniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, close_miniLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mini, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        close_miniLayout.setVerticalGroup(
            close_miniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(size, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
            .addComponent(close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mini, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        canvaspanel.setBackground(new java.awt.Color(153, 153, 153));
        canvaspanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        capanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        capanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout canvaspanelLayout = new javax.swing.GroupLayout(canvaspanel);
        canvaspanel.setLayout(canvaspanelLayout);
        canvaspanelLayout.setHorizontalGroup(
            canvaspanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, canvaspanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(capanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        canvaspanelLayout.setVerticalGroup(
            canvaspanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvaspanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(capanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        head.setBackground(new java.awt.Color(0, 0, 0));
        head.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 10));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("File");
        head.add(jLabel2);

        menu.setBackground(new java.awt.Color(153, 153, 153));
        menu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        rectan.setBackground(new java.awt.Color(102, 102, 102));
        rectan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/sboxw.png"))); // NOI18N
        rectan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rectan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanActionPerformed(evt);
            }
        });

        tri.setBackground(new java.awt.Color(102, 102, 102));
        tri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/triangle.png"))); // NOI18N
        tri.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triActionPerformed(evt);
            }
        });

        circ.setBackground(new java.awt.Color(102, 102, 102));
        circ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/circle.png"))); // NOI18N
        circ.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        circ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                circActionPerformed(evt);
            }
        });

        li.setBackground(new java.awt.Color(102, 102, 102));
        li.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/line.png"))); // NOI18N
        li.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        li.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liActionPerformed(evt);
            }
        });

        elip.setBackground(new java.awt.Color(102, 102, 102));
        elip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/oval.png"))); // NOI18N
        elip.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        elip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elipActionPerformed(evt);
            }
        });

        pencil.setBackground(new java.awt.Color(102, 102, 102));
        pencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-pencil-26.png"))); // NOI18N
        pencil.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pencil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencilActionPerformed(evt);
            }
        });

        color_select.setBackground(new java.awt.Color(102, 102, 102));
        color_select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-palette-26.png"))); // NOI18N
        color_select.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        color_select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                color_selectActionPerformed(evt);
            }
        });

        erase.setBackground(new java.awt.Color(102, 102, 102));
        erase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eraser.png"))); // NOI18N
        erase.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        erase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eraseActionPerformed(evt);
            }
        });

        undo.setBackground(new java.awt.Color(102, 102, 102));
        undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/undo.png"))); // NOI18N
        undo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });

        redo.setBackground(new java.awt.Color(102, 102, 102));
        redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/redo.png"))); // NOI18N
        redo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        colorlabel.setBackground(new java.awt.Color(0, 0, 0));
        colorlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colorlabel.setOpaque(true);
        colorlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorlabelMouseClicked(evt);
            }
        });

        paint.setBackground(new java.awt.Color(102, 102, 102));
        paint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-paint-bucket-26.png"))); // NOI18N
        paint.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        paint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintActionPerformed(evt);
            }
        });

        fill2.setBackground(new java.awt.Color(255, 255, 255));
        fill2.setOpaque(true);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setOpaque(true);

        jLabel6.setText("Tools");

        jLabel7.setText("Color Pickes");

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(menuLayout.createSequentialGroup()
                                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(rectan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(undo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(li, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(circ, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(menuLayout.createSequentialGroup()
                                .addComponent(erase, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52))))
                    .addGroup(menuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(paint, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(menuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fill2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(redo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(elip, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pencil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(colorlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(color_select, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rectan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(elip, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(circ, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tri, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(li, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pencil, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addComponent(erase, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(20, 20, 20)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(color_select, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorlabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fill2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 325, Short.MAX_VALUE)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(redo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(undo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(head, javax.swing.GroupLayout.DEFAULT_SIZE, 1228, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(close_mini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(canvaspanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(close_mini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(canvaspanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleName("window");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    public boolean isrectangle = false,iscircle = false , istriangle = false, isstar = false, isellipse = false,isline = false,ispencil = false,iseraser = false,ispaint = false;
    private void rectanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectanActionPerformed
        isrectangle = true;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = false;
        iseraser = false;
        ispaint = false;
        rectan.setBackground(new Color(204,204,204));
        circ.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
        Point hotspot = new Point(9,9);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
        canvas.setCursor(custom);
    }//GEN-LAST:event_rectanActionPerformed

    private void circActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_circActionPerformed
        iscircle = true;
        isrectangle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = false;
        iseraser = false;
        ispaint = false;
        circ.setBackground(new Color(204,204,204));
        rectan.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
        Point hotspot = new Point(9,9);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
        canvas.setCursor(custom);
    }//GEN-LAST:event_circActionPerformed

    private void elipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elipActionPerformed
        isrectangle = false;
        iscircle = false;
        isellipse = true;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = false;
        iseraser = false;
        ispaint = false;
        elip.setBackground(new Color(204,204,204));
        circ.setBackground(new Color(102,102,102));
        rectan.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
        Point hotspot = new Point(9,9);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
        canvas.setCursor(custom);
    }//GEN-LAST:event_elipActionPerformed

    private void triActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triActionPerformed
        isrectangle = false;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = true;
        ispencil = false;
        iseraser = false;
        ispaint = false;
        tri.setBackground(new Color(204,204,204));
        circ.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        rectan.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
        Point hotspot = new Point(9,9);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
        canvas.setCursor(custom);
    }//GEN-LAST:event_triActionPerformed

    private void liActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liActionPerformed
        isrectangle = false;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = true;
        istriangle = false;
        iseraser = false;
        ispaint = false;
        li.setBackground(new Color(204,204,204));
        circ.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        rectan.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/crosshair.png"));
        Point hotspot = new Point(9,9);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "crosshair");
        canvas.setCursor(custom);
    }//GEN-LAST:event_liActionPerformed

    private void pencilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencilActionPerformed
        isrectangle = false;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = true;
        iseraser = false;
        ispaint = false;
        pencil.setBackground(new Color(204,204,204));
        circ.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        rectan.setBackground(new Color(102,102,102));
        fill_color = Color.white;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage(getClass().getResource("/img/pencilcross.png"));
        Point hotspot = new Point(0,26);
        Cursor custom = toolkit.createCustomCursor(img, hotspot, "pencil");
        canvas.setCursor(custom);
    }//GEN-LAST:event_pencilActionPerformed

    private void eraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eraseActionPerformed
        iseraser = true;
        isrectangle = false;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = false;
        ispaint = false;
        rectan.setBackground(new Color(102,102,102));
        circ.setBackground(new Color(102,102,102));
        elip.setBackground(new Color(102,102,102));
        tri.setBackground(new Color(102,102,102));
        li.setBackground(new Color(102,102,102));
        pencil.setBackground(new Color(102,102,102));
        fill_color = Color.white;
    }//GEN-LAST:event_eraseActionPerformed

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        int store_size = store.size();
        if(store_size > 0)
        {
            start = null;
            end = null;
            store.remove(store_size - 1);
            canvas.repaint();
        }
    }//GEN-LAST:event_undoActionPerformed

    private void color_selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_color_selectActionPerformed
        jDialog1.setVisible(true);
        jDialog1.pack();
        jDialog1.setLocationRelativeTo(canvaspanel);
        select.setVisible(false);
        cancle2.setVisible(false);
        ok.setVisible(true);
        cancle.setVisible(true);
    }//GEN-LAST:event_color_selectActionPerformed
    //public Color col = Color.black;
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        col = color.getColor();
        jDialog1.dispose();
        colorlabel.setBackground(col);
    }//GEN-LAST:event_okActionPerformed
    
    private void colorlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorlabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_colorlabelMouseClicked
     
    private void cancleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancleActionPerformed
        jDialog1.dispose();
        col = Color.black;
        colorlabel.setBackground(col);
    }//GEN-LAST:event_cancleActionPerformed

    private void paintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintActionPerformed
        iseraser = false;
        isrectangle = false;
        iscircle = false;
        isellipse = false;
        isstar = false;
        isline = false;
        istriangle = false;
        ispencil = false;
        ispaint = true;
        jDialog1.setVisible(true);
        jDialog1.pack();
        jDialog1.setLocationRelativeTo(canvaspanel);
        select.setVisible(true);
        cancle2.setVisible(true);
        ok.setVisible(false);
        cancle.setVisible(false); 
        
    }//GEN-LAST:event_paintActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        fill_color = color.getColor();
        jDialog1.dispose();
        
        fill2.setBackground(fill_color);
    }//GEN-LAST:event_selectActionPerformed

    private void cancle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancle2ActionPerformed
        jDialog1.dispose();
        col = Color.white;
    }//GEN-LAST:event_cancle2ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Swing_Paint().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancle;
    private javax.swing.JButton cancle2;
    private javax.swing.JPanel canvaspanel;
    private javax.swing.JPanel capanel;
    private javax.swing.JButton circ;
    private javax.swing.JLabel close;
    private javax.swing.JPanel close_mini;
    private javax.swing.JColorChooser color;
    private javax.swing.JButton color_select;
    private javax.swing.JLabel colorlabel;
    private javax.swing.JButton elip;
    private javax.swing.JButton erase;
    private javax.swing.JLabel fill2;
    private javax.swing.JPanel head;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JButton li;
    private javax.swing.JPanel menu;
    private javax.swing.JLabel mini;
    private javax.swing.JButton ok;
    private javax.swing.JButton paint;
    private javax.swing.JButton pencil;
    private javax.swing.JButton rectan;
    private javax.swing.JButton redo;
    private javax.swing.JButton select;
    private javax.swing.JLabel size;
    private javax.swing.JButton tri;
    private javax.swing.JButton undo;
    // End of variables declaration//GEN-END:variables
}
