package fr.cnrs.liris.tagomatic.imagefetch;

public class PanoParam {
	
	
	//Differece of value X on both lat and longt has following distance values:
	//+/- 0.001 -> 130 m
	//+/- 0.002 -> 260 m
	//+/- 0.003 -> 390 m
	//+/- 0.004 -> 521 m
	//+/- 0.005 ->  651 m
	//+/- 0.006 -> 780 m
	//+/- 0.01 -> 1300 m
	
	
	private String set  = "public";
	private int from ;
	private int to ;
	private float minx;
	private float maxx ;
	private float miny ;
	private float maxy ;
	private String size = "small";
	private boolean mapFilter = false;
	private float range;
	
	
	
	public PanoParam() {
		
	}


	

	public PanoParam(String set, int from, int to, float minx, float maxx,
			float miny, float maxy, String size, boolean mapFilter) {
		super();
		this.set = set;
		this.from = from;
		this.to = to;
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		this.size = size;
		this.mapFilter = mapFilter;
	}

	public PanoParam(String set, int from, int to, float ln, 
			float lt) {
		super();
		this.set = set;
		this.from = from;
		this.to = to;
		this.minx = ln - range;
		this.maxx = ln + range;
		this.miny = lt - range;
		this.maxy = lt + range;
		this.size = "medium";
		this.mapFilter = true;
	}
	
	public PanoParam(float lt, float ln, float searchRange) {
		super();
		this.set = "full";
		this.from = 1;
		this.to = 100;
		this.range = searchRange;
		this.minx = ln - range;
		this.maxx = ln + range;
		this.miny = lt - range;
		this.maxy = lt + range;
		this.size = "medium";
		this.mapFilter = false;
	}



	public String getSet() {
		return set;
	}



	public void setSet(String set) {
		this.set = set;
	}



	public int getFrom() {
		return from;
	}



	public float getRange() {
		return range;
	}




	public void setRange(float range) {
		this.range = range;
	}




	public void setFrom(int from) {
		this.from = from;
	}



	public int getTo() {
		return to;
	}



	public void setTo(int to) {
		this.to = to;
	}



	public float getMinx() {
		return minx;
	}



	public void setMinx(float minx) {
		this.minx = minx;
	}



	public float getMaxx() {
		return maxx;
	}



	public void setMaxx(float maxx) {
		this.maxx = maxx;
	}



	public float getMiny() {
		return miny;
	}



	public void setMiny(float miny) {
		this.miny = miny;
	}



	public float getMaxy() {
		return maxy;
	}



	public void setMaxy(float maxy) {
		this.maxy = maxy;
	}



	public String getSize() {
		return size;
	}



	public void setSize(String size) {
		this.size = size;
	}



	public boolean getMapFilter() {
		return mapFilter;
	}



	public void setMapFilter(boolean mapFilter) {
		this.mapFilter = mapFilter;
	}
	
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer() ;
		
		res.append("Set=").append(set).append(",").append("From=").append(from).
		append(",").append("To=").append(to).
		append(",").append("minx=").append(minx).
		append(",").append("maxx=").append(maxx).
		append(",").append("miny=").append(miny).
		append(",").append("maxy=").append(maxy).
		append(",").append("size=").append(size).
		append(",").append("mapfilter=").append(mapFilter);

		return res.toString();
	}
	
	
	
}
