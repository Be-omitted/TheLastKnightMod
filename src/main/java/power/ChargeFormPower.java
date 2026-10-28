package power;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helper.TheLastKnightModHelper;

public class ChargeFormPower extends AbstractPower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "ChargeFormPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    public ChargeFormPower(AbstractCreature owner, int amount) {
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/ChargeFormPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("img/power/ChargeFormPower32.png"), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        // 层数同时作为弃牌数与抽牌数
        int count = Math.max(this.amount, 0);
        this.description = powerStrings.DESCRIPTIONS[0] + count
                + powerStrings.DESCRIPTIONS[1] + count
                + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card == null) {
            return;
        }
        if (this.amount <= 0) {
            return;
        }
        this.flash();
        // 先随机弃牌再抽牌，两者数量都等于当前层数
        AbstractDungeon.actionManager.addToBottom(
                new DiscardAction(this.owner, this.owner, this.amount, true));
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(this.owner, this.amount));
    }
}
