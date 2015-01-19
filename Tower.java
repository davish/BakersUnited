package team082;

import battlecode.common.*;

public class Tower extends BaseRobot {

	public Tower(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException {
		attackLeastHealthEnemy();
		rc.yield();
	}

}
