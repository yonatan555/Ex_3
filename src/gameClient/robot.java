package gameClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import utils.Point3D;

public class robot {
int src; 
int dest;
double value;
int id;
double speed;
Point3D pos;


public robot() {
	this.dest=-1;
	this.speed=0;
	this.src=0;
	this.id=0;
	this.pos=Point3D.ORIGIN;
	this.value=0;
}

public int getDest() {
	return dest;
}

public void setDest(int dest) {
	this.dest = dest;
}
public int getSrc() {
	return this.src;
}

public void setSrc(int src) {
	this.src = src;
}

public double getValue() {
	return value;
}

public void setValue(double value) {
	this.value = value;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public double getSpeed() {
	return speed;
}

public void setSpeed(double speed) {
	this.speed = speed;
}

public Point3D getPos() {
	return pos;
}

public void setPos(Point3D pos) {
	this.pos = pos;
}

public robot(String s) throws JSONException {
	
	JSONObject m = new JSONObject(s);
	
	try
	{
		JSONObject ro = m.getJSONObject("Robot");
		this.value = ro.getDouble("value");
		this.src = ro.getInt("src");
		this.pos = new Point3D(ro.getString("pos"));
		this.dest= ro.getInt("dest");
		this.speed = ro.getDouble("speed");
		this.id= ro.getInt("id");
		
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
}



}
