package gameClient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

public class auto {

int numbergame;
play p;
KML_Logger kmlLog;



public void setgamenumber(int sen) {
	this.numbergame=  sen;
}

Thread t;
public void threadForKML(game_service game)
{
	t = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(game.isRunning())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String time = java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
				LocalTime end = java.time.LocalTime.now(); 
				end = end.plusNanos(100*1000000);
				String endTime = java.time.LocalDate.now()+"T"+end;
				kmlLog.setFruits(time , endTime);
				kmlLog.setBots(time , endTime);
				
			}
		}
	});
	t.start();
}

public int getrobs(game_service game) throws JSONException {
	int i = 0;

	JSONObject m = new JSONObject(game.toString());
	try {
		JSONObject ro = m.getJSONObject("GameServer");
		i = ro.getInt("robots");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return i;
}


public  void StartAuto(game_service game) {
	try {
		p = new play(game);
		int count = getrobs(game);
		MyGameGUI gui = new MyGameGUI(p.grp);
		gui.setplay(game);
		gui.initGUI();
		gui.p.locatefruit();
		for (int i = 0; i < count; i++)
		{
			game.addRobot(gui.p.fru.get(i).getSrc());
		}
		
		gui.p.moverob(game);
		Graph_Algo g = new Graph_Algo(gui.p.grp);
		game.startGame();
		kmlLog = new KML_Logger(p.grp);
		kmlLog.BuildGraph();
		kmlLog.setGame(game);
		threadForKML(game);
		long time0 = game.timeToEnd();	
		while (game.isRunning()) {
			if(time0 - game.timeToEnd() > 35) game.move();
			MyGameGUI.time = game.timeToEnd() / 1000;
			List<node_data> l = new ArrayList<node_data>();
			for (int i = 0, j = 0; i < count && j < gui.p.fru.size(); i++, j++) {
				if (gui.p.rob.get(i).getSrc() != gui.p.fru.get(j).getdest()) {
					l = g.shortestPath(gui.p.rob.get(i).getSrc(), gui.p.fru.get(j).getdest());
					if (l != null) {
						for (int k = l.size() - 2; k >= 0; k--) {
							game.chooseNextEdge(i, l.get(k).getKey());
							gui.p.locatefruit();
							gui.p.moverob(game);
							gui.paint();
						}
					} else {
						break;
					}
				} else {
					game.chooseNextEdge(i, gui.p.fru.get(j).getSrc());
				}
			}
			
			gui.p.movefrut(game);
			gui.p.moverob(game);
			MyGameGUI.score = showScore(game);
			gui.p.locatefruit();
			gui.paint();
		}
		kmlLog.save("data/"+numbergame+".kml");
	}

	catch (Exception e) {
		e.printStackTrace();
	}

}
public String showScore(game_service game) throws JSONException {

	JSONObject m = new JSONObject(game.toString());
	try {

		JSONObject ro = m.getJSONObject("GameServer");
		int grade = ro.getInt("grade");
		int moves = ro.getInt("moves");
	MyGameGUI.score = "grade: " + grade + "moves :" + moves;
		return MyGameGUI.score;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "";

}
}
