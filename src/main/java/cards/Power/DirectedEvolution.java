package cards.Power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import power.ContingentMorphoComputationalKernelPower;

public class DirectedEvolution extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "DirectedEvolution";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/DirectedEvolution.png";
    private static final int COST = 2;
    // 随机模肢
    private static final int BASE_LIMB = 1;
    private static final int UPGRADE_LIMB = 1;
    // 牌数
    private static final int HAND_ADD_AMOUNT = 1;

    public DirectedEvolution() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_LIMB;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 未升级：描述为“随机普通牌”，只允许普通稀有度
        // 已升级：描述为“随机非稀有牌”，允许普通 + 罕见，即排除稀有
        CardRarityFilter rarityFilter = this.upgraded
                ? CardRarityFilter.of(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON)
                : CardRarityFilter.of(AbstractCard.CardRarity.COMMON);
        CardTypeFilter typeFilter = CardTypeFilter.any();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p,
                        new ContingentMorphoComputationalKernelPower(p, this.magicNumber,
                                RandomCardHelper.PilePosition.HAND, HAND_ADD_AMOUNT,
                                typeFilter, rarityFilter),
                        this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DirectedEvolution();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            // 改名，标记为已升级
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_LIMB);
            // 升级文案把随机牌池由“普通牌”放宽为“非稀有牌”
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }


}
