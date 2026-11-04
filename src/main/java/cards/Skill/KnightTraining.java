package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;

public class KnightTraining extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "KnightTraining";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/KnightTraining.png";
    private static final int COST = 2;
    // 获得的力量
    private static final int BASE_STRENGTH = 2;
    // 加入抽牌堆的随机非稀有牌数量
    private static final int CARD_AMOUNT = 2;
    // 升级减费
    private static final int NEW_COST = 1;

    public KnightTraining() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_STRENGTH;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new StrengthPower(p, this.magicNumber), this.magicNumber));

        // 加入 2 张随机非稀有牌（普通 + 罕见）
        RandomCardHelper.addRandomCardsToPile(RandomCardHelper.PilePosition.DRAW_PILE,
                CARD_AMOUNT, CardTypeFilter.any(),
                CardRarityFilter.of(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON));
    }

    @Override
    public AbstractCard makeCopy() {
        return new KnightTraining();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(NEW_COST);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
