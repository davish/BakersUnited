package team082;

import battlecode.common.*;

public class RobotPlayer {
	public static void run(RobotController rc) {
		BaseRobot b;
		switch(rc.getType()) {
		case HQ:
			b = new HQ(rc);
			break;
		case BEAVER:
			b = new Beaver(rc);
			break;
		case MINER:
			b = new Miner(rc);
			break;
		case MINERFACTORY:
			b = new MinerFactory(rc);
			break;
		case BARRACKS:
			b = new Barracks(rc);
			break;
		case TOWER:
			b = new Tower(rc);
			break;
		case SOLDIER:
			b = new Soldier(rc);
			break;
		case TANKFACTORY:
			b = new TankFactory(rc);
			break;
		case TANK:
			b = new Tank(rc);
			break;
		case HELIPAD:
			b = new Helipad(rc);
			break;
		case DRONE:
			b = new Drone(rc);
			break;
		default:
			b = new BaseRobot(rc);
			break;
		}
		
		while (true) {
			try {
				b.go();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
