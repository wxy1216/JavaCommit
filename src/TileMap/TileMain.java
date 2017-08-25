package TileMap;

public class TileMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TileBounds tileBounds=new TileBounds();
		tileBounds.minCol=1;
		tileBounds.maxCol=2;
		tileBounds.minRow=1;
		tileBounds.maxRow=1;
		tileBounds.zoomlevel=17;
		
		String outPutFileName="d:\\tilemaps.tif";// ‰≥ˆÕº∆¨µÿ÷∑
		String tilePath="D:/tiles/c";
		
		tileBounds.CombineTiles(tileBounds,tilePath,outPutFileName);
	}

}
