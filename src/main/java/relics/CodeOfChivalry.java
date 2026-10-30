package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helper.TheLastKnightModHelper;

public class CodeOfChivalry extends CustomRelic {
    public static final String ID = TheLastKnightModHelper.HEAD_ID + "CodeOfChivalry";
    private static final String IMG = "img/relics/CodeOfChivalry.png";
    private static final String IMG_OTL = "img/relics/outline/CodeOfChivalry.png";
    // 每多少金币获得 1 点战斗开始时能量
    private static final int GOLD_PER_ENERGY = 100;

    // 记录本场战斗是否已经触发过首次回合的能量
    private boolean firstTurn = true;

    public CodeOfChivalry() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.SHOP, AbstractRelic.LandingSound.CLINK);
    }

    // 当前金币可折算出的能量数，即遗物显示的层数
    private int getEnergyAmount() {
        if (AbstractDungeon.player == null) {
            return 0;
        }
        return Math.max(0, AbstractDungeon.player.gold / GOLD_PER_ENERGY);
    }

    private void updateCounter() {
        this.setCounter(getEnergyAmount());
    }

    @Override
    public void onEquip() {
        updateCounter();
    }

    @Override
    public void onGainGold() {
        updateCounter();
    }

    @Override
    public void onLoseGold() {
        updateCounter();
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
    }

    @Override
    public void atBattleStart() {
        updateCounter();
    }

    @Override
    public void atTurnStart() {
        if (!this.firstTurn) {
            return;
        }
        this.firstTurn = false;

        int energyAmount = getEnergyAmount();
        this.setCounter(energyAmount);
        if (energyAmount <= 0) {
            return;
        }
        this.flash();
        // 与原版提灯一致，在第一回合开始、能量回满之后再加入能量
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(energyAmount));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CodeOfChivalry();
    }
}
