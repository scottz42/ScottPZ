package xyz.scottz.scottpz;

/**
 * Created by lei on 2017/6/13.
 */

// simple class for generating zombies
public class ZombieInfo {
    protected Zombie zombie ;
    protected int row ;
    protected long time ;   // time in ms from start

    public ZombieInfo(Zombie zombie, int row)
    {
        this.zombie = zombie;
        this.row = row;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public void setZombie(Zombie zombie) {
        this.zombie = zombie;

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
