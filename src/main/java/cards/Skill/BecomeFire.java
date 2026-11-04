package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class BecomeFire extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "BecomeFire";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/BecomeFire.png";
    private static final int COST = 1;
    // 一次消耗所有手牌时使用的数量上限
    private static final int EXHAUST_ALL = 10;

    public BecomeFire() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先记录手牌数，消耗后再补入等量的随机牌
        int handSize = p.hand.size();
        AbstractDungeon.actionManager.addToBottom(
                new ExhaustAction(p, p, EXHAUST_ALL, false));
        RandomCardHelper.addRandomCardsToPile(RandomCardHelper.PilePosition.HAND,
                handSize, CardTypeFilter.any(), CardRarityFilter.any());
    }

    @Override
    public AbstractCard makeCopy() {
        return new BecomeFire();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级后不再消耗自身
            this.exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
