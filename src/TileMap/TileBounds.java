package TileMap;

import java.io.File;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset; 
import org.gdal.gdal.Driver; 
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants; 

public class TileBounds {

	int minCol;
	int maxCol;
	int minRow;
	int maxRow;
	int zoomlevel;
	
	double[]Quantify (double[] dArray,Band band,int width, int height){
		double [] minmax=new double[2];
		band.ComputeRasterMinMax(minmax);// 最小值minmax[0]，最大值minmax[1]
		double temp;
		double dif=minmax[1]-minmax[0];
		
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				temp=(double) dArray[j*width+i]-minmax[0];
				dArray[j*width+i]=temp/dif*255;
			}
		}
		return dArray;
	}

	void SaveBitmapBuffered(Dataset src,Dataset dst,int x,int y){
		
		//get the gdal band objects from the Dataset
		Band redBand=src.GetRasterBand(1);
		Band greenBand=src.GetRasterBand(2);
		Band blueBand=src.GetRasterBand(3);
		
		//get the width and height of the raster
		int width=redBand.getXSize();
		int height=redBand.getYSize();
//		int iYSize=hDataset.getRasterYSize();
		
		double[] r=new double[width*height];
		double[] g=new double[width*height];
		double[] b=new double[width*height];
					
		redBand.ReadRaster(0, 0, width, height,r);
		greenBand.ReadRaster(0, 0, width, height,g);
		blueBand.ReadRaster(0, 0, width, height,b);
		
		r=Quantify(r,redBand,width, height);
		g=Quantify(g,greenBand,width, height);
		b=Quantify(b,blueBand,width, height);
		for(int i=10;i<20;i++){
			System.out.println(r[i]);
		}
		
		Band wrb=dst.GetRasterBand(1);
		wrb.WriteRaster(x*width, y*height, width, height, r);
		Band wgb=dst.GetRasterBand(2);
		wgb.WriteRaster(x*width, y*height, width, height, g);
		Band wbb=dst.GetRasterBand(3);
		wbb.WriteRaster(x*width, y*height, width, height, b);
	}
	
	
	/*拼接瓦片*/
	public void CombineTiles(TileBounds tileBounds,String tilePath,String outPutFileName){
		
		File file=new File(outPutFileName);
		if(file.exists()){
			file.delete();
		}
		int imageWidth=1039*(tileBounds.maxCol-tileBounds.minCol+1);//
		int imageHeight=1039*(tileBounds.maxRow-tileBounds.minRow+1);
		
		//Register driver(s)
		gdal.AllRegister();
		Driver driver=gdal.GetDriverByName("GTiff");
		Dataset destDataset=driver.Create(outPutFileName, imageWidth, imageHeight, 3);
		
		
		//col列x，row行y
		for(int col=tileBounds.minCol;col<=tileBounds.maxCol;col++){
			for(int row=tileBounds.minRow;row<=tileBounds.maxRow;row++){
				try{
					String sourceFileName=tilePath+Integer.toString(col)+".tif";//按行列分的文件夹
					File sourceFile=new File(sourceFileName);
					System.out.println(sourceFile.getName());
					if(sourceFile.exists()){
						Dataset sourceDataset=gdal.Open(sourceFileName,gdalconstConstants.GA_ReadOnly);
						
					
						if(sourceDataset !=null){
							SaveBitmapBuffered(sourceDataset, destDataset, col - tileBounds.minCol, row - tileBounds.minRow);
						}
					}
					}
				
				catch(Exception ex){
					System.out.println(ex.toString());
				}
			}
		}
		
		destDataset.delete();
		
	}
	
	
	
	public int getMinCol() {
		return minCol;
	}
	public void setMinCol(int minCol) {
		this.minCol = minCol;
	}
	public int getMaxCol() {
		return maxCol;
	}
	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}
	public int getMinRow() {
		return minRow;
	}
	public void setMinRow(int minRow) {
		this.minRow = minRow;
	}
	public int getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	public int getZoomlevel() {
		return zoomlevel;
	}
	public void setZoomlevel(int zoomlevel) {
		this.zoomlevel = zoomlevel;
	}
	
	
	
}
