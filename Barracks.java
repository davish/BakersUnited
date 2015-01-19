package team082;

import battlecode.common.*;

public class Barracks extends BaseRobot {

	public Barracks(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException {
//		int numSoldiers = rc.readBroadcast(4);
//		if (numSoldiers < 30)
//			spawnUnit(RobotType.SOLDIER);
		rc.yield();
	}

}
