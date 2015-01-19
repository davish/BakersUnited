package team082;

import battlecode.common.*;

public class Soldier extends BaseRobot {

	public Soldier(RobotController rc) {
		super(rc);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() throws GameActionException{
		attackLeastHealthEnemy();
		// make a swarm, then path there
		if (rc.isCoreReady()) {
            MapLocation rallyPoint = new MapLocation(rc.readBroadcast(150), rc.readBroadcast(151));

            Direction newDir = getMoveDir(rallyPoint);
            rc.setIndicatorString(0, String.valueOf(rallyPoint.x) +","+ String.valueOf(rallyPoint.y));
            if (newDir != null) {
                rc.move(newDir);
            }
        }
		rc.yield();
	}

}
