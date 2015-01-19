package team082;

import battlecode.common.*;


public class Helipad extends BaseRobot {

	public Helipad(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}

	
	public void execute() throws GameActionException {
		int numDrones = rc.readBroadcast(6);
		
		if (numDrones < 30)
			spawnUnit(RobotType.DRONE);
		
		rc.yield();
	}
}
