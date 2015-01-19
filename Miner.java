package team082;

import battlecode.common.*;

public class Miner extends BaseRobot {

	public Miner(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException {
		attackEnemyZero();
		mineAndMove();
		rc.yield();
	}

}
