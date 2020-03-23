package zelix.cc.client.utils.Potion;

import net.minecraft.potion.Potion;

public class PotionTime {
    public Potion po;
    public int maxtime;

    public PotionTime(Potion potion,int maxtime) {
        this.po = potion;
        this.maxtime = maxtime;
    }
}
