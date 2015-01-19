package team082;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class MinerFactory extends BaseRobot {

	public MinerFactory(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException {
		int numMiners = rc.readBroadcast(15);
		
		if (numMiners < 15)
			spawnUnit(RobotType.MINER);
		rc.yield();
	}

}
