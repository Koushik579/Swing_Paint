/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingpaint;

import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author chris
 */
public class Colorshape 
{
    Shape shape;
    Color color,fillcolor;
    
    public Colorshape(Shape shape,Color color,Color fillcolor)
    {
        this.shape = shape;
        this.color = color;
        this.fillcolor = fillcolor;
    }
    
}
