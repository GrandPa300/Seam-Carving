import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver 
{
    private int width, height;
    private Color[][] pixelColor;
    private double[][] pixelEnergy;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        this.width = picture.width();
        this.height = picture.height();
        pixelColor = new Color[height][width];
        pixelEnergy = new double[height][width];
        
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                pixelColor[row][col] = picture.get(col,row); 
        
        for (int row = 0; row < height; row++)
             for (int col = 0; col < width; col++)
             {
                 if (row == 0 || row == height - 1 || 
                     col == 0 || col == width - 1)
                         pixelEnergy[row][col] = 1000;
                 else pixelEnergy[row][col] = getEnergy(row,col);
            }
    }
    
    // current picture
    public Picture picture()
    {
        Picture picToReturn = new Picture(width, height);
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                picToReturn.set(col, row, pixelColor[row][col]);
        return picToReturn;   
    }
    
    // width of current picture
    public int width() {return width;}
    
    // height of current picture
    public int height() {return height;}
    
    private double getEnergy(int row, int col)
    {
        Color left = pixelColor[row][col - 1];
        Color rght = pixelColor[row][col + 1];
        Color up = pixelColor[row - 1][col];
        Color dn = pixelColor[row + 1][col];
        
        double Rx = rght.getRed() - left.getRed();
        double Gx = rght.getGreen() - left.getGreen();
        double Bx = rght.getBlue() - left.getBlue();
        double Ry = dn.getRed() - up.getRed(); 
        double Gy = dn.getGreen() - up.getGreen(); 
        double By = dn.getBlue() - up.getBlue();
        
        return Math.sqrt(Rx*Rx + Gx*Gx + Bx*Bx + 
                         Ry*Ry + Gy*Gy + By*By);
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {return pixelEnergy[y][x];}
    
    private void transpose()
    {
        double tempEnergy[][] = new double[width][height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tempEnergy[j][i] = pixelEnergy[i][j];
                
        pixelEnergy = null;
        pixelEnergy = new double[width][height];
        
        for (int i = 0; i < width; i++)
            System.arraycopy(tempEnergy[i], 0, pixelEnergy[i], 0, height);
            
        Color[][] tempColor = new Color[width][height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tempColor[j][i] = pixelColor[i][j];
                
        pixelColor = null;
        pixelColor = new Color[width][height];
        
        for (int i = 0; i < width; i++)
            System.arraycopy(tempColor[i], 0, pixelColor[i], 0, height);
        
        int swap = 0;
        swap = width;
        width = height;
        height = swap;
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() 
    {
        /*
        transpose();
        return findVerticalSeam();
        */
        
        int[] pixelTo = new int[width * height];
        double[] energyTo = new double[width * height];
        for (int row = 1; row < height - 1; row++)
            for (int col = 1; col < width - 1; col++)
                energyTo[row * width + col] = Double.POSITIVE_INFINITY;
        for (int col = 1; col < width - 1; col++)      
            for (int row = 1; row < height - 1; row++)
            {
                int prevPix = row * width + col - 1;
                int leftPix = (row - 1) * width + col;
                int midPix = row * width + col;
                int rghtPix = (row + 1) * width + col;
                
                double energyLeft = pixelEnergy[row - 1][col] + energyTo[prevPix];
                double energyMid = pixelEnergy[row][col] + energyTo[prevPix];
                double energyRght = pixelEnergy[row + 1][col] + energyTo[prevPix];
                
                // check left
                if (leftPix != 0 && energyTo[leftPix] > energyLeft)
                {
                    energyTo[leftPix] = energyLeft;
                    pixelTo[leftPix] = row;
                }
                
                // check mid
                if (energyTo[midPix] > energyMid)
                {
                    energyTo[midPix] = energyMid;
                    pixelTo[midPix] = row;
                }
                
                // check right
                if (rghtPix != width - 1 && energyTo[rghtPix] > energyRght)
                {
                    energyTo[rghtPix] = energyRght;
                    pixelTo[rghtPix] = row;
                }
            }
            
            int[] seam = new int[width];
            double lowestEnergy = Double.POSITIVE_INFINITY;
            for (int row = 1; row < height - 1; row++)
            {
                int curPix = row * width + width - 2; // note
                if (energyTo[curPix] < lowestEnergy)
                {
                    lowestEnergy = energyTo[curPix];
                    seam[width - 2] = row;
                    seam[width - 1] = row;
                }
            }
            
            for (int col = width - 3; col >= 0; col--)
                seam[col] = pixelTo[seam[col + 1] * width + col + 1]; // note
            return seam;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() 
    {
        int[] pixelTo = new int[width * height];
        double[] energyTo = new double[width * height];
        for (int row = 1; row < height - 1; row++)
            for (int col = 1; col < width - 1; col++)
                energyTo[row * width + col] = Double.POSITIVE_INFINITY;
        
        for (int row = 1; row < height - 1; row++)
            for (int col = 1; col < width - 1; col++)
            {
                int prevPix = (row - 1) * width + col;
                int leftPix = row * width + col - 1;
                int midPix = row * width + col;
                int rghtPix = row * width + col + 1;
                
                double energyLeft = pixelEnergy[row][col - 1] + energyTo[prevPix];
                double energyMid = pixelEnergy[row][col] + energyTo[prevPix];
                double energyRght = pixelEnergy[row][col + 1] + energyTo[prevPix];
                
                // check left
                if (leftPix != 0 && energyTo[leftPix] > energyLeft)
                {
                    energyTo[leftPix] = energyLeft;
                    pixelTo[leftPix] = col;
                }
                
                // check mid
                if (energyTo[midPix] > energyMid)
                {
                    energyTo[midPix] = energyMid;
                    pixelTo[midPix] = col;
                }
                
                // check right
                if (rghtPix != width - 1 && energyTo[rghtPix] > energyRght)
                {
                    energyTo[rghtPix] = energyRght;
                    pixelTo[rghtPix] = col;
                }
            }
            
            int[] seam = new int[height];
            double lowestEnergy = Double.POSITIVE_INFINITY;
            for (int col = 1; col < width - 1; col++)
            {
                int curPix = (height - 2) * width + col;
                if (energyTo[curPix] < lowestEnergy)
                {
                    lowestEnergy = energyTo[curPix];
                    seam[height - 2] = col;
                    seam[height - 1] = col;
                }
            }
            
            
            for (int row = height - 3; row >= 0; row--)
                seam[row] = pixelTo[(row + 1) * width + seam[row + 1]];
            return seam;       
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        /*
        removeVerticalSeam(seam);
        transpose();
        */
        for (int col = 0; col < width - 1; col++)
            for (int row = seam[col]; row < height - 1; row++)
            {
                pixelColor[row][col] = pixelColor[row + 1][col];
                pixelEnergy[row][col] = pixelEnergy[row + 1][col];
            }
        height--;
        
        for (int col = 1; col < width - 1; col++)
        {
            int cutLoc = seam[col];
            if (cutLoc > 1) pixelEnergy[cutLoc - 1][col] = getEnergy(cutLoc - 1, col);
            if (cutLoc < height - 1) pixelEnergy[cutLoc][col] = getEnergy(cutLoc, col);
        }
        
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        for (int row = 0; row < height; row++)
        {
            int cutLoc = seam[row];
            System.arraycopy(pixelColor[row], cutLoc + 1, pixelColor[row], cutLoc, width - 1 - cutLoc);
            System.arraycopy(pixelEnergy[row], cutLoc + 1, pixelEnergy[row], cutLoc, width - 1 - cutLoc);
         }
        width--;
        
        for (int row = 1; row < height - 1; row++)
        {
            int cutLoc = seam[row];
            if (cutLoc > 1) pixelEnergy[row][cutLoc - 1] = getEnergy(row, cutLoc - 1);
            if (cutLoc < width - 1) pixelEnergy[row][cutLoc] = getEnergy(row, cutLoc);
        }
    }
}