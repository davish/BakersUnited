package team082;

import battlecode.common.*;

public class TankFactory extends BaseRobot{

	public TankFactory(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException {
		spawnUnit(RobotType.TANK);
		rc.yield();
	}

}
