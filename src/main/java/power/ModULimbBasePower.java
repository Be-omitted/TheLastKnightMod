package power;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class ModULimbBasePower extends AbstractPower {
    public final String BASE_ID_MUL;

    protected ModULimbBasePower(AbstractCreature owner, int amount, String base_id) {
        this.BASE_ID_MUL = base_id;
        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/" + BASE_ID_MUL + "84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/" + BASE_ID_MUL + "32.png"), 0, 0, 32, 32);
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        this.updateDescription();
        if (this.amount <= 0) {
            this.owner.powers.remove(this);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount < 0) {
            this.reducePower(-stackAmount);
        } else {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            this.updateDescription();
            if (this.amount <= 0) {
                this.owner.powers.remove(this);
            }
        }
    }
    public abstract int getMULNumber();
}
