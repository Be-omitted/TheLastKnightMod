package power;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.TheLastKnightModHelper;

public class WillPower extends AbstractPower{
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID+"WillPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    public WillPower(AbstractCreature owner, int amount){
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("img/power/WillPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("img/power/WillPower32.png"), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        int healNumber = Math.max(this.amount, 0);
        String w = helper.TheLastKnightModHelper.GetWillHText(this.owner);
        this.description = powerStrings.DESCRIPTIONS[0] + healNumber + powerStrings.DESCRIPTIONS[1]
                + w;
        if (this.owner.hasPower(RedundantVestigePower.BASE_ID)){
            ((RedundantVestigePower)this.owner.getPower(RedundantVestigePower.BASE_ID)).syncWR(w);
        }
    }
    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        updateDescription();
        if (this.amount == 0){
            this.owner.powers.remove(this);
        }
    }
    @Override
    public void stackPower(int stackAmount){
        if (stackAmount < 0){
            this.reducePower(-stackAmount);
        }else{
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            updateDescription();
            if (this.amount == 0) {
                this.owner.powers.remove(this);
            }
        }
    }

    public void syncWR(String w){
        int Number = Math.max(this.amount, 0);
        this.description = powerStrings.DESCRIPTIONS[0] + Number + powerStrings.DESCRIPTIONS[1]
                + w;
    }

    // 回血实现在core

}
