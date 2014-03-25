package fhfl.android.mobapps_osm;

public class StatsPoint {
	private double TimeOffset;
	private double Speed;
	
	public StatsPoint(double t, double s){
		TimeOffset = t;
		Speed = s;
	}
	
	public double getSpeed() {
		return Speed;
	}

	public double getTimeOffset() {
		return TimeOffset;
	}

}
