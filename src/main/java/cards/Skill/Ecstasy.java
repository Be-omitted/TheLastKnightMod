package cards.Skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
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
import pathes.AbstractCardEnum;

/**
 * 狂喜
 * 丢弃手牌并加入随机牌。
 */
public class Ecstasy extends CustomCard {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Ecstasy";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Ecstasy.png";
    // 费用
    private static final int COST = 1;
    // 固定丢弃的张数
    private static final int DISCARD_AMOUNT = 4;
    // 加入手牌的随机牌张数
    private static final int BASE_ADD = 2;
    // 升级增加的随机牌张数
    private static final int UPGRADE_ADD = 1;

    public Ecstasy() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_ADD;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先选择并丢弃 5 张牌，手牌不足时由 DiscardAction 自动改为全部丢弃
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, DISCARD_AMOUNT, false));
        // 未升级只从普通/罕见牌中抽取，升级后放开稀有度限制
        CardRarityFilter rarityFilter = this.upgraded
                ? CardRarityFilter.any()
                : CardRarityFilter.of(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON);
        RandomCardHelper.addRandomCardsToPile(RandomCardHelper.PilePosition.HAND,
                this.magicNumber, CardTypeFilter.any(), rarityFilter);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ecstasy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级同时提高张数并解除稀有度限制
            this.upgradeMagicNumber(UPGRADE_ADD);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
