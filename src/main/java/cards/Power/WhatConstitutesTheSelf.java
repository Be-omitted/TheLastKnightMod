package cards.Power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import power.WillPower;

public class WhatConstitutesTheSelf extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "WhatConstitutesTheSelf";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/WhatConstitutesTheSelf.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    // 获得的力量/敏捷或随机模肢数
    private static final int BASE_GAIN = 4;

    public WhatConstitutesTheSelf() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BASE_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int will = p.hasPower(WillPower.BASE_ID)
                ? p.getPower(WillPower.BASE_ID).amount
                : 0;
        int limbCount = TheLastKnightModHelper.GetModULimbBasePowerNumber(p);

        if (will > limbCount) {
            // 意志大于模肢数：获得力量与敏捷
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p,
                            new StrengthPower(p, this.magicNumber), this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p,
                            new DexterityPower(p, this.magicNumber), this.magicNumber));
        } else {
            // 否则获得随机模肢
            for (int i = 0; i < this.magicNumber; i++) {
                TheLastKnightModHelper.ModULimbBasePowerPool(p,
                        TheLastKnightModHelper.ModULimbBasePowerPoolIsAll(p));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhatConstitutesTheSelf();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级只降低费用
            this.upgradeBaseCost(UPGRADE_COST);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
