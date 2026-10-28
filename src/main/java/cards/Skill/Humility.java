package cards.Skill;

import cards.Virtue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.TheLastKnightModHelper;
import pathes.AbstractCardEnum;
import power.WillPower;

public class Humility extends Virtue {
    private static final String ID = TheLastKnightModHelper.HEAD_ID + "Humility";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG = "img/card/Humility.png";
    private static final int COST = -2;
    // 从手牌被丢弃时抽取的牌数
    private static final int DRAW_AMOUNT = 3;
    private static final int BASE_WILL = 2;

    public Humility() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.TheLastKnight_COLOR, CardRarity.UNCOMMON, CardTarget.NONE);
        // 尝试打出时显示的原因
        this.cantUseMessage = "不能被打出";

        this.baseMagicNumber = BASE_WILL;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 仅设置 -2 费用不足以阻止打出，需要显式返回 false
        return false;
    }

    @Override
    public void triggerOnManualDiscard() {
        // 升级后先把本牌移出弃牌堆，再抽牌，避免抽牌触发洗牌时把自己抽回
        if (this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new MoveToDrawPileBottomAction(this));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(AbstractDungeon.player, DRAW_AMOUNT));
        // 被丢弃时获得意志
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new WillPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));

    }

    @Override
    public AbstractCard makeCopy() {
        return new Humility();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    // 把刚被丢弃的这张牌从弃牌堆移到抽牌堆底
    private static class MoveToDrawPileBottomAction extends AbstractGameAction {
        private final AbstractCard card;

        private MoveToDrawPileBottomAction(AbstractCard card) {
            this.card = card;
            this.actionType = ActionType.CARD_MANIPULATION;
        }

        @Override
        public void update() {
            // 只在牌确实位于弃牌堆时移动，避免重复加入抽牌堆
            if (AbstractDungeon.player.discardPile.contains(this.card)) {
                AbstractDungeon.player.discardPile.moveToBottomOfDeck(this.card);
            }
            this.isDone = true;
        }
    }
}
