package team082;
import battlecode.common.*;

import java.util.Hashtable;
import java.util.Random;


public class BaseRobot {
	protected RobotController rc;
	protected MapLocation myHQ, theirHQ;
	protected Team myTeam, theirTeam;
	protected Random rand;
	protected Direction facing;

	protected boolean tracing;
	protected MapLocation hand;
	protected Direction bugDir;

	protected String[][] mapMemory;
	public BaseRobot(RobotController rc) {
		this.rc = rc;
		this.rand = new Random(rc.getID());

		this.myHQ = rc.senseHQLocation();
		this.theirHQ = rc.senseEnemyHQLocation();

		this.myTeam = rc.getTeam();
		this.theirTeam = this.myTeam.opponent();

		if (myTeam == Team.A)
			this.facing = Direction.SOUTH;
		else
			this.facing = Direction.NORTH;

		this.tracing = false;
		
//		this.mapMemory = new String[myHQ.x+120][myHQ.y+120];
	}

	public void go() throws GameActionException {
		beforeExec();
		execute();
		afterExec();
	}

	public void beforeExec() throws GameActionException {
		transferSupplies();
	}

	public void execute() throws GameActionException {

	}

	public void afterExec() throws GameActionException {
	}


	public Direction[] getDirectionsToward(MapLocation from, MapLocation dest) {
		Direction toDest = from.directionTo(dest);
		Direction[] dirs = {toDest, toDest.rotateLeft(), toDest.rotateRight(), toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
		return dirs;
	}

	public Direction[] getDirectionsToward(MapLocation dest) {
		return getDirectionsToward(rc.getLocation(), dest);
	}

	public Direction getMoveDir(MapLocation dest) {
		Direction[] dirs = getDirectionsToward(dest);
		for (Direction d: dirs) {
			if (rc.canMove(d)) {
				return d;
			}
		}
		return null;
	}

	/*
	 * if (tracing) // if we're going along a wall
			if (robot clear of obstacle)
				tracing = false 
			else
				dir = direction to trace along obstacle
				move in dir 
		else // we're just going towards the target
			if (can move in directionTo(target)) 
				move in directionTo(target)
			else // we're going towards the target, but can't go towards the target
				tracing = true
	 * */

	public Direction[] getSquaresAround(MapLocation m, Direction start, boolean clockwise) {
		Direction dir = clockwise ? start.rotateRight() : start.rotateLeft();
		Direction[] r = new Direction[7];
		for (int i = 0; i < r.length; i++) {
			dir = clockwise ? dir.rotateRight() : dir.rotateLeft();
			r[i] = dir;
		}
		return r;
	}

	public void bugMove(MapLocation target) throws GameActionException {
		Direction tarDir = rc.getLocation().directionTo(target);
		if (tracing) {
			// if clear of obstacle (for now, if you can move towards the target)
			isPathOpen(rc.getLocation(), target, 3);
			if (rc.canMove(bugDir)) {
				bugDir = tarDir;
				tracing = false; // stop tracing
				if (rc.isCoreReady() && rc.canMove(bugDir)) {
					rc.move(tarDir);
				}
			} else {
				// use the hand variable for original direction
				Direction[] m = null;
				Direction startingDir = rc.getLocation().directionTo(hand);
				
				if (startingDir.compareTo(bugDir) > 0) {
					m  = getSquaresAround(hand, startingDir, false);
				} else {
					m = getSquaresAround(hand, startingDir, true);
				}

				if (m != null) {
					Direction nextMove = Direction.NONE;
					int i;
					for (i = 0; i < m.length; i++) {
						if (rc.canMove(m[i])) {
							nextMove = m[i];
							break;
						}
					}
					// now change the direction if you have to
					// hand needs to be perpendicular to the direction
					Direction oldDir = bugDir;
					bugDir = nextMove;
					MapLocation nextHand = hand.add(bugDir);
					
					if (rc.senseTerrainTile(nextHand) == TerrainTile.NORMAL) {
						bugDir = oldDir;
					} else {
						hand = nextHand;
					}
					if (rc.canMove(nextMove) && rc.isCoreReady()) {
					
						rc.move(nextMove);
					}
				}
			}

		} else {
			if (rc.canMove(tarDir)) { // if we can move in the target direction, but couldn't before
				if(rc.isCoreReady()&&rc.canMove(tarDir)){
					bugDir = tarDir;
					rc.move(tarDir); // bug away
				}
			} else { // if we can't move towards the target
				tracing = true;
				// if we can't move in the direction, that's where the wall's gonna be, so we'll put our hand there.
				hand = rc.getLocation().add(tarDir);
				
				if (rand.nextBoolean()) {
					bugDir = tarDir.rotateLeft();
				} else {
					bugDir = tarDir.rotateRight();
				}
				
			}
		}
		rc.setIndicatorString(0, bugDir.toString());
		rc.setIndicatorString(1, Boolean.toString(tracing));
	}
	
	boolean isPathOpen(MapLocation from, MapLocation to, int l) {
		MapLocation currentM = from.add(from.directionTo(to));
		for (int i = 0; i < l; i++) {
			if (!rc.senseTerrainTile(currentM).isTraversable()) {
				return false;
			}
			currentM = currentM.add(currentM.directionTo(to));
		}
		return true;
	}

	public void mineAndMove() throws GameActionException {
		if(rc.senseOre(rc.getLocation())>1){//there is ore, so try to mine
			if(rc.isCoreReady()&&rc.canMine()){
				rc.mine();
			}
		}else{//no ore, so look for ore, wander aimlessly
			double r = rand.nextDouble();
			if(r<0.25){
				facing = facing.rotateLeft();
			}else if (r < .5){
				facing = facing.rotateRight();
			}
			moveAround();
		}
	}

	public void moveAround() throws GameActionException {
		MapLocation tileInFront = rc.getLocation().add(facing);
		Direction dirToMove = getMoveDir(tileInFront);
		if(rc.isCoreReady()&&rc.canMove(dirToMove)){
			rc.move(dirToMove);
		}
	}

	public void moveTo() throws GameActionException {

	}

	public void transferSupplies() throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,rc.getTeam());
		double lowestSupply = rc.getSupplyLevel();
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){
			if(ri.supplyLevel<lowestSupply){
				lowestSupply = ri.supplyLevel;
				transferAmount = (rc.getSupplyLevel()-ri.supplyLevel)/2;
				suppliesToThisLocation = ri.location;
			}
		}
		if(suppliesToThisLocation!=null){
			try {
				rc.transferSupplies((int)transferAmount, suppliesToThisLocation);
			}
			catch(GameActionException e) {

			}
		}
	}

	public void buildUnit(RobotType type) throws GameActionException {
		if(rc.getTeamOre()>type.oreCost){
			Direction buildDir = getRandomDirection();
			if(rc.isCoreReady()&&rc.canBuild(buildDir, type)){
				rc.build(buildDir, type);
			}
		}
	}
	public Direction getRandomDirection() {
		return Direction.values()[(int)(rand.nextDouble()*8)];
	}

	public void spawnUnit(RobotType type) throws GameActionException {
		Direction randomDir = getRandomDirection();
		if(rc.isCoreReady()&&rc.canSpawn(randomDir, type) && rc.getTeamOre()>type.oreCost){
			rc.spawn(randomDir, type);
		}
	}

	public void attackEnemyZero() throws GameActionException {
		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getLocation(),rc.getType().attackRadiusSquared,rc.getTeam().opponent());
		if(nearbyEnemies.length>0){//there are enemies nearby
			//try to shoot at them
			//specifically, try to shoot at enemy specified by nearbyEnemies[0]
			if(rc.isWeaponReady()&&rc.canAttackLocation(nearbyEnemies[0].location)){
				rc.attackLocation(nearbyEnemies[0].location);
			}
		}
	}

	public void attackLeastHealthEnemy() throws GameActionException {
		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getLocation(),rc.getType().attackRadiusSquared,rc.getTeam().opponent());
		if(nearbyEnemies.length>0){//there are enemies nearby
			//try to shoot at them
			//specifically, try to shoot at enemy specified by nearbyEnemies[0]
			double min = 999999;
			RobotInfo target = nearbyEnemies[0];
			for (RobotInfo ri : nearbyEnemies) {
				if (ri.health < min) {
					min = ri.health;
					target = ri;
				}
			}

			if(rc.isWeaponReady()&&rc.canAttackLocation(nearbyEnemies[0].location)){
				rc.attackLocation(target.location);
			}
		}
	}


}
